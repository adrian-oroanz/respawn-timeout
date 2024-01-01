package io.github.adrian_oroanz.respawn_timeout.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import io.github.adrian_oroanz.respawn_timeout.state.ServerState;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.ADMIN_PERMISSION_LEVEL;;


public final class ClearCommand {

	public static void register (CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("respawntimeout")
			.then(literal("clear")
				.requires((source) -> source.hasPermissionLevel(ADMIN_PERMISSION_LEVEL))
				.executes((context) -> clear(context.getSource()))
			));
	}

	public static int clear (ServerCommandSource source) {
		ServerState serverState = ServerState.getServerState(source.getServer());

		serverState.respawnTimeout = 0;
		serverState.markDirty();

		source.sendFeedback(Text.translatable("cmd.respawn-timeout.clear.res"), false);

		return Command.SINGLE_SUCCESS;
	}

}
