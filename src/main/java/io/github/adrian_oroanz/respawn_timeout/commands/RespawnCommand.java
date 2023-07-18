package io.github.adrian_oroanz.respawn_timeout.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import io.github.adrian_oroanz.respawn_timeout.RespawnTimeoutMod;
import io.github.adrian_oroanz.respawn_timeout.state.PlayerState;
import io.github.adrian_oroanz.respawn_timeout.state.ServerState;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;


public final class RespawnCommand {
	
	public static void register (CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("respawntimeout")
			.then(literal("respawn")
				.executes((context) -> respawn(context.getSource()))
			));
	}

	public static int respawn (ServerCommandSource source) {
		ServerPlayerEntity playerEntity = source.getPlayer();
		PlayerState playerState = ServerState.getPlayerState(playerEntity);

		if (playerState.deathTimestamp == 0) {
			source.sendFeedback(() -> Text.translatable("txt.respawn-timeout.player_na"), false);

			return 1;
		}
		
		RespawnTimeoutMod.tryRespawnPlayer(playerEntity);

		return Command.SINGLE_SUCCESS;
	}
	
}
