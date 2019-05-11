package mpeciakk.claimchunk.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mpeciakk.claimchunk.Constants;
import mpeciakk.claimchunk.models.ClaimData;
import mpeciakk.claimchunk.models.Member;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableTextComponent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public class ClaimManager {

    private static List<ClaimData> data = new ArrayList<>();

    public static ClaimData get(int x, int z, int dimension) {
        for (ClaimData claimData : data) {
            if (claimData.getX() == x && claimData.getZ() == z && claimData.getDimension() == dimension) {
                return claimData;
            }
        }

        return new ClaimData();
    }

    public static String claim(PlayerEntity player) {
        if (getClaimsCount(player) >= Config.MAX_CLAIMS) return new TranslatableTextComponent(Constants.Messages.TOO_MANY_CLAIMS).getText();

        ClaimData cd = get((int) player.x >> 4, (int) player.z >> 4, player.dimension.getRawId());

        if (cd.getOwner() != null) {
            if (cd.isOwned()) return new TranslatableTextComponent(Constants.Messages.ALREADY_CLAIMED).getText();
            if (!cd.isOwner(player.getUuid())) return new TranslatableTextComponent(Constants.Messages.NOT_OWNER).getText();
        }

        cd.setX((int) player.x >> 4);
        cd.setZ((int) player.z >> 4);
        cd.setOwner(new Member(player.getDisplayName().getText(), player.getUuid()));
        cd.setDimension(player.dimension.getRawId());

        data.add(cd);

        save();

        return new TranslatableTextComponent(Constants.Messages.CLAIMED).getText();
    }

    public static String unclaim(PlayerEntity player) {
        ClaimData cd = get((int) player.x >> 4, (int) player.z >> 4, player.dimension.getRawId());

        if (!cd.isOwner(player.getUuid())) return new TranslatableTextComponent(Constants.Messages.NOT_OWNER).getText();

        data.remove(cd);

        save();

        return new TranslatableTextComponent(Constants.Messages.UNCLAIMED).getText();
    }

    public static String unclaimAll(PlayerEntity sender) {
        data.removeIf(cd -> cd.isOwner(sender.getUuid()));

        save();

        return new TranslatableTextComponent(Constants.Messages.UNCLAIMED).getText();
    }

    private static int getClaimsCount(PlayerEntity player) {
        int n = 0;

        for (ClaimData cd : data) {
            System.out.println(cd.getOwner().getUuid());

            if (cd.isOwner(player.getUuid())) n += 1;
        }

        return n;
    }

    public static void load() {
        File f = new File("claimsdata/" + Constants.MOD_ID + ".json");

        if (f.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
                Gson gson = new Gson();
                Type listType = new TypeToken<Collection<ClaimData>>() {}.getType();
                data = gson.fromJson(bufferedReader, listType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();

        File f = new File("claimsdata/");
        if (!f.exists()) f.mkdir();

        f = new File("claimsdata/" + Constants.MOD_ID + ".json");
        if (f.exists()) f.delete();

        try {
            FileWriter writer = new FileWriter("claimsdata/" + Constants.MOD_ID + ".json");
            writer.write(gson.toJson(data));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String addMember(PlayerEntity sender, Member member) {
        for (ClaimData cd : data) {
            if (cd.isOwner(sender.getUuid())) {
                cd.addMember(member);
            }
        }

        save();
        return new TranslatableTextComponent(Constants.Messages.PLAYER_ADDED).getText();
    }

    public static String removeMember(PlayerEntity sender, UUID uuid) {
        for (ClaimData cd : data) {
            if (cd.isOwner(sender.getUuid())) {
                cd.removeMember(uuid);
            }
        }

        save();
        return new TranslatableTextComponent(Constants.Messages.MEMBER_REMOVED).getText();
    }
}
