package io.github.adrian_oroanz.respawn_timeout.state;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;


public class ServerState extends PersistentState {

	public long respawnTimeout = 0;
	public TimeUnit timeUnit = TimeUnit.SECONDS;
	public HashMap<UUID, PlayerState> players = new HashMap<>();


	@Override
	public NbtCompound writeNbt (NbtCompound nbt) {
		NbtCompound playersNbtCompound = new NbtCompound();

		players.forEach((uuid, playerState) -> {
			NbtCompound playerStateNbt = new NbtCompound();

			playerStateNbt.putLong("deathTimestamp", playerState.deathTimestamp);

			playersNbtCompound.put(uuid.toString(), playerStateNbt);
		});

		nbt.put("players", playersNbtCompound);
		nbt.putLong("respawnTimeout", respawnTimeout);
		nbt.putString("timeUnit", timeUnit.toString());

		return nbt;
	}

	public static ServerState createFromNbt (NbtCompound tag) {
		ServerState serverState = new ServerState();
		NbtCompound playersTag = tag.getCompound("players");

		playersTag.getKeys().forEach((key) -> {
			PlayerState playerState = new PlayerState();

			playerState.deathTimestamp = playersTag.getCompound(key).getLong("deathTimestamp");

			UUID uuid = UUID.fromString(key);

			serverState.players.put(uuid, playerState);
		});

		serverState.respawnTimeout = tag.getLong("respawnTimeout");

		try {
			serverState.timeUnit = TimeUnit.valueOf(tag.getString("timeUnit"));
		}
		catch (Exception e) {
			serverState.timeUnit = TimeUnit.SECONDS;
		}

		return serverState;
	}


	public static ServerState getServerState (MinecraftServer server) {
		ServerWorld overworld = server.getWorld(World.OVERWORLD);

		if (overworld == null)
			throw new NullPointerException("Overworld is null");

		PersistentStateManager persistentStateManager = overworld.getPersistentStateManager();

		return persistentStateManager.getOrCreate(
			ServerState::createFromNbt,
			ServerState::new,
			"respawn_timeout"
		);
	}

	public static PlayerState getPlayerState (LivingEntity playerEntity) {
		MinecraftServer server = playerEntity.getServer();

		if (server == null)
			throw new NullPointerException("Server is null");

		ServerState serverState = getServerState(server);

        return serverState.players.computeIfAbsent(playerEntity.getUuid(), (uuid) -> new PlayerState());
	}

}
