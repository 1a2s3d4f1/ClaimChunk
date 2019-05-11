package mpeciakk.claimchunk;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import mpeciakk.claimchunk.command.*;
import mpeciakk.claimchunk.config.ClaimManager;
import mpeciakk.claimchunk.config.Config;
import mpeciakk.claimchunk.event.Events;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.CommandManager;

public class ClaimChunk implements ModInitializer {

	@Override
	public void onInitialize() {
		CommandRegistry.INSTANCE.register(false, (dispatcher) -> dispatcher.register(
				CommandManager.literal("claim")
						.executes((c) -> {
							ClaimCommand.execute(c);

							return Command.SINGLE_SUCCESS;
						})
		));

		CommandRegistry.INSTANCE.register(false, (dispatcher) -> dispatcher.register(
				CommandManager.literal("info")
						.executes((c) -> {
							InfoCommand.execute(c);

							return Command.SINGLE_SUCCESS;
						})
		));

		CommandRegistry.INSTANCE.register(false, (dispatcher) -> dispatcher.register(
				CommandManager.literal("member")
						.then(CommandManager.argument("type", StringArgumentType.string())
								.then(CommandManager.argument("player", EntityArgumentType.player())
									.executes((c) -> {
										MemberCommand.execute(c);

										return Command.SINGLE_SUCCESS;
									})
		))));

		CommandRegistry.INSTANCE.register(false, (dispatcher) -> dispatcher.register(
				CommandManager.literal("unclaim")
						.executes((c) -> {
							UnclaimCommand.execute(c);

							return Command.SINGLE_SUCCESS;
						})
		));

		CommandRegistry.INSTANCE.register(false, (dispatcher) -> dispatcher.register(
				CommandManager.literal("unclaimall")
						.executes((c) -> {
							UnclaimAllCommand.execute(c);

							return Command.SINGLE_SUCCESS;
						})
		));

		CommandRegistry.INSTANCE.register(false, (dispatcher) -> dispatcher.register(
				CommandManager.literal("claims")
						.executes((c) -> {
							ClaimsCommand.execute(c);

							return Command.SINGLE_SUCCESS;
						})
		));

		Events.init();

		ServerStartCallback.EVENT.register(minecraftServer -> {
			System.out.println("Loading ClaimChunk's registry...");
			ClaimManager.load();
			Config.load();
		});

		ServerStopCallback.EVENT.register(minecraftServer -> {
			ClaimManager.save();
		});
	}
}
