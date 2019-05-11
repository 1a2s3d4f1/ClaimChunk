package mpeciakk.claimchunk.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mpeciakk.claimchunk.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;

public class Config {

    public static int MAX_CLAIMS;
    public static boolean PREVENT_BUILDING;
    public static boolean PREVENT_BREAKING_BLOCKS;
    public static boolean PREVENT_USING_BLOCKS;
    public static boolean PREVENT_USING_ITEMS;
    public static boolean PREVENT_ATTACKING_ENTITIES;

    private static HashMap<String, Object> data = new HashMap<>();
    private static HashMap<String, Object> configTemplate = new HashMap<>();

    static {
        configTemplate.put("max_claims", 10);
        configTemplate.put("prevent_building", true);
        configTemplate.put("prevent_breaking_blocks", true);
        configTemplate.put("prevent_using_blocks", true);
        configTemplate.put("prevent_using_items", true);
        configTemplate.put("prevent_attacking_entities", true);
    }

    public static void load() {
        File f = new File("config/" + Constants.MOD_ID + ".json");

        if (f.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
                Gson gson = new Gson();
                Type listType = new TypeToken<HashMap<String, Object>>() {}.getType();
                data = gson.fromJson(bufferedReader, listType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            save();

            load();
        }

        MAX_CLAIMS = ((Double) data.get("max_claims")).intValue();
        PREVENT_BUILDING = (boolean) data.get("prevent_building");
        PREVENT_BREAKING_BLOCKS = (boolean) data.get("prevent_breaking_blocks");
        PREVENT_USING_BLOCKS = (boolean) data.get("prevent_using_blocks");
        PREVENT_USING_ITEMS = (boolean) data.get("prevent_using_items");
        PREVENT_ATTACKING_ENTITIES = (boolean) data.get("prevent_attacking_entities");
    }

    private static void save() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();

        File f = new File("config/");
        if (!f.exists()) f.mkdir();

        f = new File("config/" + Constants.MOD_ID + ".json");
        if (f.exists()) f.delete();

        try {
            FileWriter writer = new FileWriter("config/" + Constants.MOD_ID + ".json");
            writer.write(gson.toJson(configTemplate));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
