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

import java.util.concurrent.TimeUnit;

import static net.minecraft.server.command.CommandManager.ADMIN_PERMISSION_LEVEL;


public final class SetCommand {
	
	public static void register (CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("respawntimeout")
			.then(literal("set")
				.requires((source) -> source.hasPermissionLevel(ADMIN_PERMISSION_LEVEL))
				.then(argument("timeout", longArg(0, 604800))
					.then(literal("seconds")
						.executes((context) -> set(context.getSource(), getLong(context, "timeout"), TimeUnit.SECONDS))
					))));

		dispatcher.register(literal("respawntimeout")
			.then(literal("set")
				.requires((source) -> source.hasPermissionLevel(ADMIN_PERMISSION_LEVEL))
				.then(argument("timeout", longArg(0, 604800))
					.then(literal("minutes")
						.executes((context) -> set(context.getSource(), getLong(context, "timeout"), TimeUnit.MINUTES))
					))));

		dispatcher.register(literal("respawntimeout")
			.then(literal("set")
				.requires((source) -> source.hasPermissionLevel(ADMIN_PERMISSION_LEVEL))
				.then(argument("timeout", longArg(0, 604800))
					.then(literal("hours")
						.executes((context) -> set(context.getSource(), getLong(context, "timeout"), TimeUnit.HOURS))
					))));

		dispatcher.register(literal("respawntimeout")
			.then(literal("set")
				.requires((source) -> source.hasPermissionLevel(ADMIN_PERMISSION_LEVEL))
				.then(argument("timeout", longArg(0, 604800))
					.then(literal("days")
						.executes((context) -> set(context.getSource(), getLong(context, "timeout"), TimeUnit.DAYS))
					))));
	}

	public static int set (ServerCommandSource source, long timeout, TimeUnit timeUnit) throws CommandSyntaxException {
		if (timeout < 0)
			throw new SimpleCommandExceptionType(Text.translatable("cmd.respawn-timeout.set.err")).create();

		ServerState serverState = ServerState.getServerState(source.getServer());

		serverState.respawnTimeout = timeout;
		serverState.timeUnit = timeUnit;
		serverState.markDirty();

		source.sendFeedback(() -> Text.translatable("cmd.respawn-timeout.set.res", timeout, timeUnit.toString().toLowerCase().charAt(0)), true);

		return Command.SINGLE_SUCCESS;
	}

}
