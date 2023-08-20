package io.github.adrian_oroanz.respawn_timeout.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import io.github.adrian_oroanz.respawn_timeout.RespawnTimeoutMod;
import io.github.adrian_oroanz.respawn_timeout.state.PlayerState;
import io.github.adrian_oroanz.respawn_timeout.state.ServerState;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.MODERATOR_PERMISSION_LEVEL;


public final class RespawnCommand {
	
	public static void register (CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("respawntimeout")
			.then(literal("respawn")
				.executes((context) -> respawn(context.getSource(), context.getSource().getPlayer(), false))
			));

		dispatcher.register(literal("respawntimeout")
			.then(literal("respawn")
				.then(argument("player", EntityArgumentType.player())
					.requires((source) -> source.hasPermissionLevel(MODERATOR_PERMISSION_LEVEL))
					.executes((context) -> respawn(context.getSource(), EntityArgumentType.getPlayer(context, "player"), true)))
			));
	}

	public static int respawn (ServerCommandSource source, ServerPlayerEntity playerEntity, boolean forceRespawn) {
		// TODO: Implement forceRespawn.
		PlayerState playerState = ServerState.getPlayerState(playerEntity);

		if (playerState.deathTimestamp == 0) {
			source.sendFeedback(() -> Text.translatable("txt.respawn-timeout.player_na"), false);

			return Command.SINGLE_SUCCESS;
		}
		
		RespawnTimeoutMod.tryRespawnPlayer(playerEntity);

		return Command.SINGLE_SUCCESS;
	}
	
}
