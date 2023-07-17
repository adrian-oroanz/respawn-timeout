package io.github.adrian_oroanz.respawn_timeout.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import io.github.adrian_oroanz.respawn_timeout.state.ServerState;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;


public final class ClearCommand {

	public static void register (CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("respawntimeout")
			.then(literal("clear")
				.requires((source) -> source.hasPermissionLevel(4))
				.executes((context) -> clear(context.getSource()))
			));
	}

	public static int clear (ServerCommandSource source) {
		ServerState serverState = ServerState.getServerState(source.getServer());

		serverState.respawnTimeout = 0;
		serverState.markDirty();

		source.sendFeedback(() -> Text.literal("Respawn timeout has been reset!"), false);

		return Command.SINGLE_SUCCESS;
	}
	
}
