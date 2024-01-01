package io.github.adrian_oroanz.respawn_timeout.util;

import io.github.adrian_oroanz.respawn_timeout.state.PlayerState;
import io.github.adrian_oroanz.respawn_timeout.state.ServerState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;


/**
 * Contains all the possible respawn conditions that can be used to check if a player can respawn.
 */
public final class RespawnConditions {

	/**
	 * Represents the base respawn condition. A player can respawn if they are on spectator mode and have a registered death time.
	 * @param playerEntity The player to check.
	 * @return True if the player can respawn, false otherwise.
	 */
	public static boolean base (ServerPlayerEntity playerEntity) {
		MinecraftServer server = playerEntity.getServer();

		if (server == null)
			return false;

		ServerState serverState = ServerState.getServerState(server);
		PlayerState playerState = ServerState.getPlayerState(playerEntity);

		// The player must be on spectator mode (timed out) and have a registered death time.
		if (!playerEntity.isSpectator() || playerState.deathTimestamp == 0)
			return false;

		long respawnTimeoutInSeconds = serverState.timeUnit.toSeconds(serverState.respawnTimeout);
		long timeSinceDeathInSeconds = (System.currentTimeMillis() - playerState.deathTimestamp) / 1000;

		// Time elapsed since death should be greater or equal than the defined timeout.
		if (timeSinceDeathInSeconds < respawnTimeoutInSeconds) {
			String remainingTime = TimeUtils.secondsToHHmmss((int)(respawnTimeoutInSeconds - timeSinceDeathInSeconds));

			playerEntity.sendMessage(Text.translatable("txt.respawn-timeout.player_status", remainingTime), false);

			return false;
		}

		// Under normal circumstances, the player should be able to respawn.
		return true;
	}

}
