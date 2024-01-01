package io.github.adrian_oroanz.respawn_timeout.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import io.github.adrian_oroanz.respawn_timeout.state.PlayerState;
import io.github.adrian_oroanz.respawn_timeout.state.ServerState;
import io.github.adrian_oroanz.respawn_timeout.util.RespawnConditions;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static io.github.adrian_oroanz.respawn_timeout.RespawnTimeoutMod.respawnPlayer;
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
					.executes((context) -> respawn(context.getSource(), EntityArgumentType.getPlayer(context, "player"), true))
				)));
	}

	public static int respawn (ServerCommandSource source, ServerPlayerEntity playerEntity, boolean forceRespawn) {
		PlayerState playerState = ServerState.getPlayerState(playerEntity);

		// No death timestamp means the player is not timed out.
		if (playerState.deathTimestamp == 0) {
			String translationKey = (playerEntity == source.getPlayer())
				? "txt.respawn-timeout.player_na"
				: "txt.respawn-timeout.player_ext_na";

			source.sendFeedback(Text.translatable(translationKey, playerEntity.getName().getString()), false);

			return Command.SINGLE_SUCCESS;
		}

		// If true, the player is respawned regardless of the conditions.
		if (forceRespawn) {
			respawnPlayer(playerEntity, null);

			source.sendFeedback(Text.translatable("txt.respawn-timeout.player_ext_respawn", playerEntity.getName().getString()), false);

			return Command.SINGLE_SUCCESS;
		}

		respawnPlayer(playerEntity, RespawnConditions::base);

		return Command.SINGLE_SUCCESS;
	}

}
