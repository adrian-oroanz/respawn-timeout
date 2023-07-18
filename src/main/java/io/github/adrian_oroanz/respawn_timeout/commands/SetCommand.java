package io.github.adrian_oroanz.respawn_timeout.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import io.github.adrian_oroanz.respawn_timeout.state.ServerState;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.LongArgumentType.longArg;
import static com.mojang.brigadier.arguments.LongArgumentType.getLong;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public final class SetCommand {
	
	public static void register (CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("respawntimeout")
			.then(literal("set")
				.requires((source) -> source.hasPermissionLevel(4))
				.then(argument("timeout", longArg(0, 604800))
					.executes((context) -> set(context.getSource(), getLong(context, "timeout")))
				)
			));
	}

	public static int set (ServerCommandSource source, long timeout) throws CommandSyntaxException {
		if (timeout < 0)
			throw new SimpleCommandExceptionType(Text.literal("Timeout must be a positive number!")).create();

		ServerState serverState = ServerState.getServerState(source.getServer());

		serverState.respawnTimeout = timeout;
		serverState.markDirty();

		source.sendFeedback(() -> Text.literal("Respawn timeout set to " + timeout + " seconds"), true);

		return Command.SINGLE_SUCCESS;
	}

}
