package me.Math0424.CoreWeapons.Data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerData implements Serializable {

    private static final Map<UUID, PlayerData> loaded = new HashMap<>();

    private UUID playerUUID;
    private int lifetimeXP = 0;
    private int currentXP = 0;
    private Map<String, Object> storage = new HashMap<>();

    protected PlayerData() {}
    protected PlayerData(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public int getPlayerLevel() {
        return (lifetimeXP / 200) + 1;
    }

    public int getLifetimeXP() {
        return lifetimeXP;
    }

    public int getCurrentXP() {
        return currentXP;
    }

    public void addToXp(int i) {
        lifetimeXP += i;
        currentXP += i;
    }

    public void set(String value, Object obj) {
        storage.put(value, obj);
    }

    public Object getOrDefault(String value, Object obj) {
        return storage.getOrDefault(value, obj);
    }

    public void addTo(String value, int amount) {
        Object val = storage.getOrDefault(value, 0);
        if (val instanceof Double d) {
            storage.put(value, d + amount);
        } else if(val instanceof Integer i) {
            storage.put(value, i + amount);
        }
    }

    private Map<String, Object> serialize() {
        storage.put("playerUUID", playerUUID.toString());
        storage.put("lifetimeXP", lifetimeXP);
        storage.put("currentXP", currentXP);
        return storage;
    }

    private void deSerialize(Map<String, Object> map) {
        storage = map;
        playerUUID = UUID.fromString((String) storage.get("playerUUID"));
        currentXP = ((Double)storage.get("currentXP")).intValue();
        lifetimeXP = ((Double)storage.get("lifetimeXP")).intValue();
    }

    /*

        Static Methods

     */

    public static PlayerData getPlayerData(UUID playerUUID) {
        if (!loaded.containsKey(playerUUID)) {
            PlayerData data = loadPlayerData(playerUUID);
            if (data == null) {
                MyUtil.log(Level.INFO, "Made new playerData for " + playerUUID);
                loaded.put(playerUUID, new PlayerData(playerUUID));
            } else {
                MyUtil.log(Level.INFO, "Loaded playerData for " + playerUUID);
                loaded.put(playerUUID, data);
            }
        }
        return loaded.get(playerUUID);
    }

    public static void savePlayerData(Player p) {
        savePlayerData(getPlayerData(p.getUniqueId()));
    }

    public static void saveAllPlayerData() {
        for (PlayerData d : loaded.values()) {
            savePlayerData(d);
        }
    }

    public static void unloadPlayerData(Player p) {
        MyUtil.log(Level.INFO, "Unloaded playerData for " + p.getName());
        savePlayerData(p);
        loaded.remove(p.getUniqueId());
    }

    public static boolean isPlayerDataLoaded(Player p) {
        return loaded.containsKey(p.getUniqueId());
    }

    private static void savePlayerData(PlayerData data) {
        File dir = new File(CoreWeaponsAPI.getPlugin().getDataFolder() + "/PlayerData/");
        File dataFile = new File(CoreWeaponsAPI.getPlugin().getDataFolder() + "/PlayerData/" + data.getPlayerUUID() + ".yml");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        JSONObject mainData = new JSONObject(data.serialize());

        try {
            if (!dataFile.exists())
                dataFile.createNewFile();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            PrintWriter fos = new PrintWriter(dataFile);
            fos.write(gson.toJson(mainData));

            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PlayerData loadPlayerData(UUID playerUUID) {
        String path = CoreWeaponsAPI.getPlugin().getDataFolder() + "/PlayerData/";
        File dataFile = new File(path + playerUUID + ".yml");
        if (dataFile.exists()) {
            try {
                FileReader reader = new FileReader(dataFile);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JSONObject mainData = gson.fromJson(reader, JSONObject.class);

                PlayerData data = new PlayerData();
                data.deSerialize(mainData);

                reader.close();
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }



}
