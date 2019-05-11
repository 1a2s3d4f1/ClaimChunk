package mpeciakk.claimchunk.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mpeciakk.claimchunk.config.ClaimManager;
import mpeciakk.claimchunk.models.Member;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.StringTextComponent;

public class MemberCommand {

    public static void execute(CommandContext<ServerCommandSource> c) throws CommandSyntaxException {
        PlayerEntity sender = c.getSource().getPlayer();

        if (sender.world.isClient) return;

        if (StringArgumentType.getString(c, "type").equals("add")) {
            PlayerEntity playerEntity = EntityArgumentType.getPlayer(c, "player");

            String result = ClaimManager.addMember(sender, new Member(playerEntity.getDisplayName().getText(), playerEntity.getUuid()));

            c.getSource().sendFeedback(new StringTextComponent(result), false);
        }

        if (StringArgumentType.getString(c, "type").equals("remove")) {
            PlayerEntity playerEntity = EntityArgumentType.getPlayer(c, "player");

            String result = ClaimManager.removeMember(sender, playerEntity.getUuid());

            c.getSource().sendFeedback(new StringTextComponent(result), false);
        }
    }
}
