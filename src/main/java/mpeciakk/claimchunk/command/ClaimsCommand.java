package mpeciakk.claimchunk.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mpeciakk.claimchunk.Constants;
import mpeciakk.claimchunk.config.ClaimManager;
import mpeciakk.claimchunk.models.ClaimData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.world.dimension.DimensionType;

import java.util.List;

public class ClaimsCommand {

    public static void execute(CommandContext<ServerCommandSource> c) throws CommandSyntaxException {
        PlayerEntity sender = c.getSource().getPlayer();

        if (sender.world.isClient) return;

        List<ClaimData> claimDataList = ClaimManager.getClaims(sender);

        if (claimDataList.size() == 0) c.getSource().sendFeedback(new TranslatableTextComponent(Constants.Messages.NO_CLAIMS), false);

        StringBuilder s = new StringBuilder();

        for (ClaimData cd : claimDataList) {
            s.append("x: ").append(cd.getX() * Math.pow(2, 4)).append(", z: ").append(cd.getZ() * Math.pow(2, 4)).append(", dimension: ").append(DimensionType.byRawId(cd.getDimension()).toString().split(":")[1]).append(claimDataList.indexOf(cd) == claimDataList.size() - 1 ? "" : "\n");
        }

        c.getSource().sendFeedback(new StringTextComponent(s.toString()), false);
    }
}
