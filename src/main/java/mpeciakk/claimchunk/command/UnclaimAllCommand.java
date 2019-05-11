package mpeciakk.claimchunk.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mpeciakk.claimchunk.config.ClaimManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.StringTextComponent;

public class UnclaimAllCommand {

    public static void execute(CommandContext<ServerCommandSource> c) throws CommandSyntaxException {
        PlayerEntity sender = c.getSource().getPlayer();

        if (sender.world.isClient) return;

        String result = ClaimManager.unclaimAll(sender);

        c.getSource().sendFeedback(new StringTextComponent(result), false);
    }
}
