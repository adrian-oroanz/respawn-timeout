package io.github.adrian_oroanz.respawn_timeout.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import io.github.adrian_oroanz.respawn_timeout.state.ServerState;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.ADMIN_PERMISSION_LEVEL;;


public final class GetCommand {
	
	public static void register (CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("respawntimeout")
			.then(literal("get")
				.requires((source) -> source.hasPermissionLevel(ADMIN_PERMISSION_LEVEL))
				.executes((context) -> get(context.getSource()))
			));
	}

	public static int get (ServerCommandSource source) {
		ServerState serverState = ServerState.getServerState(source.getServer());

		source.sendFeedback(() -> Text.translatable("cmd.respawn-timeout.get.res", serverState.respawnTimeout, serverState.timeUnit.toString().toLowerCase().charAt(0)), false);

		return Command.SINGLE_SUCCESS;
	}

}
