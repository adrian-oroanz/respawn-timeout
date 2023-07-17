package io.github.adrian_oroanz.respawn_timeout;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.networking.api.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.adrian_oroanz.respawn_timeout.commands.ClearCommand;
import io.github.adrian_oroanz.respawn_timeout.commands.GetCommand;
import io.github.adrian_oroanz.respawn_timeout.commands.RespawnCommand;
import io.github.adrian_oroanz.respawn_timeout.commands.SetCommand;
import io.github.adrian_oroanz.respawn_timeout.state.PlayerState;
import io.github.adrian_oroanz.respawn_timeout.state.ServerState;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;


public class RespawnTimeoutMod implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("Respawn Timeout");


	@Override
	public void onInitialize (ModContainer mod) {

		// Registers the commands for the mod.
		CommandRegistrationCallback.EVENT.register((dispatcher, registry, environment) -> {
			if (!environment.isDedicated())
				return;
				
			GetCommand.register(dispatcher);
			SetCommand.register(dispatcher);
			ClearCommand.register(dispatcher);
			RespawnCommand.register(dispatcher);
		});

		// Checks and tries to respawn the player in case they had been timed out.
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			tryRespawnPlayer(handler.player);
		});

		// Changes the player's game mode to spectator when they die and saves the timestamp of their death.
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
			if (!(entity instanceof ServerPlayerEntity))
				return;

			ServerPlayerEntity playerEntity = (ServerPlayerEntity) entity;

			ServerState serverState = ServerState.getServerState(playerEntity.getServer());
			PlayerState playerState = ServerState.getPlayerState(playerEntity);

			// Set the timeout as persistent data.
			playerState.deathTimestamp = System.currentTimeMillis();
			serverState.players.put(entity.getUuid(), playerState);
			serverState.markDirty();

			playerEntity.changeGameMode(GameMode.SPECTATOR);
		});

		// Tries to revive the player on respawn if possible.
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			PlayerState playerState = ServerState.getPlayerState(oldPlayer);

			if ((playerState.deathTimestamp == 0) || !(newPlayer.isSpectator()))
				return;

			tryRespawnPlayer(newPlayer);
		});
	}


	/**
	 * Respawns the player if the timeout is over.
	 * @param playerEntity The player to verify their timeout status.
	 */
	public static void tryRespawnPlayer (ServerPlayerEntity playerEntity) {
		MinecraftServer server = playerEntity.server;
		ServerState serverState = ServerState.getServerState(server);
		PlayerState playerState = ServerState.getPlayerState(playerEntity);

		// The player must be on spectator mode (timed out) and have a registered death time.
		if (playerEntity.isSpectator() && (playerState.deathTimestamp != 0)) {
			long timeSinceDeath = (System.currentTimeMillis() - playerState.deathTimestamp) / 1000;

			// Time elapsed since death should be greater than the defined timeout.
			if (timeSinceDeath >= serverState.respawnTimeout) {
				BlockPos spawnPos = playerEntity.getSpawnPointPosition();
				ServerWorld spawnWorld = server.getWorld(playerEntity.getSpawnPointDimension());

				// If the player has a valid spawn point it will use it, otherwise it will use world's spawn.
				if (spawnPos == null) {
					spawnPos = server.getOverworld().getSpawnPos();
					spawnWorld = server.getOverworld();
				}

				int x = spawnPos.getX();
				int y = spawnPos.getY();
				int z = spawnPos.getZ();

				playerEntity.teleport(spawnWorld, x, y, z, playerEntity.getYaw(), playerEntity.getPitch());
				playerEntity.changeGameMode(GameMode.SURVIVAL);

				// Reset the timeout status.
				playerState.deathTimestamp = 0;
				serverState.players.put(playerEntity.getUuid(), playerState);
				serverState.markDirty();

				playerEntity.sendMessage(Text.of("You have respawned!"), false);
			}
			else 
				playerEntity.sendMessage(Text.of("You are on timeout! You have to wait " + (serverState.respawnTimeout - timeSinceDeath) + " seconds before respawning!"), false);
		}
	}
	
}
