package mpeciakk.claimchunk.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mpeciakk.claimchunk.config.ClaimManager;
import mpeciakk.claimchunk.Constants;
import mpeciakk.claimchunk.models.ClaimData;
import mpeciakk.claimchunk.models.Member;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TranslatableTextComponent;

public class InfoCommand {

    public static void execute(CommandContext<ServerCommandSource> c) throws CommandSyntaxException {
        PlayerEntity sender = c.getSource().getPlayer();

        if (sender.world.isClient) return;

        ClaimData cd = ClaimManager.get((int) sender.x >> 4, (int) sender.z >> 4, sender.dimension.getRawId());

        System.out.println(cd.isOwned());

        if (!cd.isOwned()) {
            c.getSource().sendFeedback(new TranslatableTextComponent(Constants.Messages.CHUNK_NOT_CLAIMED), false);

            return;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(new TranslatableTextComponent(Constants.Messages.OWNER).getText()).append(cd.getOwner().getName()).append("\n").append(new TranslatableTextComponent(Constants.Messages.MEMBERS).getText()).append("\n");

        for (Member member : cd.getMembers()) {
            stringBuilder.append(member.getName());
        }

        c.getSource().sendFeedback(new StringTextComponent(stringBuilder.toString()), false);
    }
}
