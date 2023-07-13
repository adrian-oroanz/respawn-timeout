package io.github.adrian_oroanz.respawn_timeout.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import io.github.adrian_oroanz.respawn_timeout.state.ServerState;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;


public final class GetCommand {
	
	public static void register (CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("respawntimeout")
			.then(literal("get")
				.requires((source) -> source.hasPermissionLevel(4))
				.executes((context) -> get(context.getSource()))
			));
	}

	public static int get (ServerCommandSource source) {
		ServerState serverState = ServerState.getServerState(source.getServer());

		source.sendFeedback(() -> Text.literal("Respawn timeout is " + serverState.respawnTimeout + " seconds"), false);

		return Command.SINGLE_SUCCESS;
	}

}
