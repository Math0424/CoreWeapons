package me.Math0424.CoreWeapons;

import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.logging.Level;

public enum Config {

    RESOURCEPACK_MESHING("ResourcepackMeshing", false),
    RESOURCEPACK_PORT("ResourcepackPort", 8080);

    private final String location;
    private final Object value;

    Config(String location, Object val) {
        this.location = location;
        this.value = val;
    }

    public String getStrVal() {
        return ChatColor.translateAlternateColorCodes('&', CoreWeaponsAPI.getPlugin().getConfig().getObject(this.getLocation(), String.class));
    }

    public Integer getIntVal() {
        return CoreWeaponsAPI.getPlugin().getConfig().getObject(this.getLocation(), Integer.class);
    }

    public Boolean getBoolVal() {
        return CoreWeaponsAPI.getPlugin().getConfig().getObject(this.getLocation(), Boolean.class);
    }

    public List<String> getStringArrayVal() {
        return CoreWeaponsAPI.getPlugin().getConfig().getObject(this.getLocation(), List.class);
    }

    public Object getValue() {
        return value;
    }

    public String getLocation() {
        return this.location;
    }

    public static void load() {
        for (Config lang : Config.values()) {
            if (CoreWeaponsAPI.getPlugin().getConfig().getString(lang.getLocation()) == null) {
                CoreWeaponsAPI.getPlugin().getConfig().set(lang.getLocation(), lang.getValue());
                MyUtil.log(Level.SEVERE, "Missing config option '" + lang.getLocation() + "' setting to default option: " + lang.getValue());
            }
        }
    }

}
