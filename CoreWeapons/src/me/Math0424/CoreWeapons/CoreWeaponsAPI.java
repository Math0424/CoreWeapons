package me.Math0424.CoreWeapons;

import me.Math0424.CoreWeapons.Data.PlayerData;
import me.Math0424.CoreWeapons.Managers.CoreListeners;
import me.Math0424.CoreWeapons.NMS.NMSUtil;
import me.Math0424.CoreWeapons.Resourcepack.ResourcepackManager;
import me.Math0424.CoreWeapons.Resourcepack.ResourcepackSocket;
import me.Math0424.CoreWeapons.Util.Metrics;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class CoreWeaponsAPI extends JavaPlugin {

    private static CoreWeaponsAPI plugin;
    private static FileConfiguration config;
    private static boolean isInitialized;

    public static boolean IsInitialized() {
        return isInitialized;
    }

    public static CoreWeaponsAPI getPlugin() {
        return plugin;
    }

    private static final Map<String, Plugin> checkUpdateMap = new HashMap<>();

    public static void AddPluginToUpdateChecker(String id, Plugin name) {
        checkUpdateMap.put(id, name);
    }

    private static final ArrayList<Material> unBreakables = new ArrayList<>();

    public void onLoad() {
        plugin = this;
        isInitialized = systemsValid();

        saveConfig();
        config = loadConfiguration("config.yml","");
        Config.load();
    }

    @Override
    public FileConfiguration getConfig() { return config; }

    public void onEnable() {
        if (!isInitialized) {
            return;
        }
        new CoreListeners(this);

        AddPluginToUpdateChecker("70143", this);
        checkupdates.start();

        metrics();
        ResourcepackManager.close();
    }

    public void onDisable() {
        ResourcepackSocket.Close();
        System.setProperty("CoreWeapons-Reloaded", "true");
        if (!isInitialized) {
            return;
        }
        PlayerData.saveAllPlayerData();
    }

    private void metrics() {
        Metrics metrics = new Metrics(this, 3335);
        for (Plugin plugins : checkUpdateMap.values()) {
            metrics.addCustomChart(new Metrics.SimplePie("version", plugins::getName));
        }
    }

    private boolean systemsValid() {
        MyUtil.log(Level.INFO, "Starting server validation...");
        MyUtil.log(Level.INFO, "Java version ID = " + System.getProperty("java.version"));
        MyUtil.log(Level.INFO, "NMS version     = " + NMSUtil.getNmsID());
        MyUtil.log(Level.INFO, "Server name     = " + Bukkit.getName());
        MyUtil.log(Level.INFO, "Server version  = " + Bukkit.getVersion());
        MyUtil.log(Level.INFO, "Bukkit version  = " + Bukkit.getBukkitVersion());
        MyUtil.log(Level.INFO, "Online mode     = " + Bukkit.getOnlineMode());
        MyUtil.log(Level.INFO, "Server reloaded = " + (System.getProperty("CoreWeapons-Reloaded") != null));

        StringBuilder plugins = new StringBuilder("[");
        for (Plugin p : Bukkit.getPluginManager().getPlugins())
            plugins.append(p.getName()).append(", ");
        plugins.delete(plugins.length() - 2, plugins.length());
        plugins.append(']');

        MyUtil.log(Level.INFO, "Plugins         = (" + Bukkit.getPluginManager().getPlugins().length + ") " + plugins);

        if (!NMSUtil.isSupported()) {
            MyUtil.log(Level.SEVERE, "CoreWeapons is not made for this server version. shutting down. THIS IS NOT A BUG...");
            MyUtil.log(Level.SEVERE, "Finished server validation... failed!");
            return false;
        }
        MyUtil.log(Level.INFO, "Finished server validation... success!");
        return true;
    }

    private final Thread checkupdates = new Thread() {
        public void run() {
            MyUtil.log(Level.INFO, "UpdateChecker", ChatColor.LIGHT_PURPLE + "Running update checks...");
            for (String id : checkUpdateMap.keySet()) {
                Plugin p = checkUpdateMap.get(id);

                try {
                    URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + id);
                    URLConnection conn = url.openConnection();
                    String version = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
                    String pluginVersion = p.getDescription().getVersion();

                    int versionNumber = Integer.parseInt(version.replace(".", ""));
                    int pluginVersionNumber = Integer.parseInt(pluginVersion.replace(".", ""));

                    if (versionNumber < pluginVersionNumber) {
                        MyUtil.log(Level.INFO, p.getName(), ChatColor.LIGHT_PURPLE + "You are running on a snapshot version " + pluginVersion);
                    } else if(versionNumber != pluginVersionNumber) {
                        MyUtil.log(Level.SEVERE, p.getName(), "Update available! you are " + (versionNumber - pluginVersionNumber) + " version/s behind!");
                        MyUtil.log(Level.SEVERE, p.getName(), "Download it at https://www.spigotmc.org/resources/" + p.getName() + "." + id);
                    }
                } catch (Exception e) {
                    MyUtil.log(Level.SEVERE, p.getName(), "Error while checking for updates.");
                    e.printStackTrace();
                }

            }
            MyUtil.log(Level.INFO, "UpdateChecker", ChatColor.LIGHT_PURPLE + "Finished checking for updates...");
        }
    };

    public void addMaterialToUnBreakables(Material... mList) {
        for (Material m : mList) {
            if (!unBreakables.contains(m)) {
                unBreakables.add(m);
            }
        }
    }

    public ArrayList<Material> getUnBreakables() {
        return unBreakables;
    }

    public static FileConfiguration loadConfiguration(String fileName, String dataFolder) {
        File file = new File(getPlugin().getDataFolder(), dataFolder + fileName);

        if (!file.exists()) {
            getPlugin().saveResource(dataFolder + fileName, false);
        }
        FileConfiguration fileToReplace = new YamlConfiguration();
        try {
            fileToReplace.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return fileToReplace;
    }

}
