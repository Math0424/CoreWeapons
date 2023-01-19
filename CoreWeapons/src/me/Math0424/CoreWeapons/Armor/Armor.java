package me.Math0424.CoreWeapons.Armor;

import me.Math0424.CoreWeapons.Armor.Type.ArmorType;
import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Core.SerializableItem;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class Armor extends SerializableItem<Armor> {

    private String name;

    private UUID uuid;
    private ArmorType armorType;
    private String ammoID;

    private int maxHeight;
    private double maxSpeed;
    private double acceleration;

    private int maxUses;
    private int currentUses;

    private int fixTime;
    private int usesFixedPerReload;

    public Armor() {}

    public Armor(String name, ArmorType armorType, String ammoID, int maxHeight, double maxSpeed, double acceleration, int maxUses, int fixTime, int usesFixedPerReload) {
        this.uuid = UUID.randomUUID();
        this.currentUses = maxUses;

        this.name = name;
        this.armorType = armorType;
        this.ammoID = ammoID;
        this.maxHeight = maxHeight;
        this.maxSpeed = maxSpeed;
        this.acceleration = acceleration;
        this.maxUses = maxUses;
        this.fixTime = fixTime;
        this.usesFixedPerReload = usesFixedPerReload;
    }

    @Override
    public void serialize(Map<String, Object> map) {
        map.put("name", name);
        map.put("uuid", uuid);
        map.put("armorType", armorType);
        map.put("ammoID", ammoID);

        map.put("maxHeight", maxHeight);
        map.put("maxSpeed", maxSpeed);
        map.put("acceleration", acceleration);

        map.put("maxUses", maxUses);
        map.put("currentUses", currentUses);

        map.put("fixTime", fixTime);
        map.put("usesFixedPerReload", usesFixedPerReload);
    }

    @Override
    public void deSerialize(Map<String, Object> map) {
        name = (String) map.get("name");

        uuid = (UUID) map.get("uuid");
        armorType = (ArmorType) map.get("armorType");
        ammoID = (String) map.get("ammoID");

        maxHeight = (int) map.get("maxHeight");
        maxSpeed = (double) map.get("maxSpeed");
        acceleration = (double) map.get("acceleration");

        maxUses = (int) map.get("maxUses");
        currentUses = (int) map.get("currentUses");

        fixTime = (int) map.get("fixTime");
        usesFixedPerReload = (int) map.get("usesFixedPerReload");
    }

    @Override
    public String friendlyName() {
        return "Armor";
    }

    public boolean equals(ItemStack item) {
        Container<Armor> cont = Container.getContainerItem(Armor.class, item);
        if (cont != null) {
            return uuid.equals(cont.getObject().uuid);
        }
        return false;
    }

    public void setCurrentUses(int currentUses) {
        this.currentUses = currentUses;
    }


    public String getName() {
        return name;
    }

    public ArmorType getArmorType() {
        return armorType;
    }

    public String getAmmoID() {
        return ammoID;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public int getCurrentUses() {
        return currentUses;
    }

    public int getFixTime() {
        return fixTime;
    }

    public int getUsesFixedPerReload() {
        return usesFixedPerReload;
    }
}
