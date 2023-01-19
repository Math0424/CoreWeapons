package me.Math0424.CoreWeapons.Guns.Ammo;

import me.Math0424.CoreWeapons.Core.SerializableItem;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.ChatColor;

import java.util.Map;

public class Ammo extends SerializableItem<Ammo> {

    private String ammoId;
    private int bulletCount;
    private int maxBulletCount;

    public Ammo() { }

    public Ammo(String ammoId, int bulletCount, int maxBulletCount) {
        this.ammoId = ammoId;
        this.bulletCount = bulletCount;
        this.maxBulletCount = maxBulletCount;
    }

    @Override
    public void serialize(Map<String, Object> map) {
        map.put("ammoId", ammoId);
        map.put("bulletCount", bulletCount);
        map.put("maxBulletCount", maxBulletCount);

        map.put("noStack", MyUtil.random());
    }

    @Override
    public void deSerialize(Map<String, Object> map) {
        ammoId = (String) map.get("ammoId");
        bulletCount = (int) map.get("bulletCount");
        maxBulletCount = (int) map.get("maxBulletCount");
    }

    @Override
    public String friendlyName() {
        return "Ammo";
    }

    public String getAmmoId() {
        return ammoId;
    }

    public String getCleanAmmoId() {
        return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', ammoId));
    }

    public int getBulletCount() {
        return bulletCount;
    }

    public void setBulletCount(int bulletCount) {
        this.bulletCount = bulletCount;
    }

    public int getMaxBulletCount() {
        return maxBulletCount;
    }
}
