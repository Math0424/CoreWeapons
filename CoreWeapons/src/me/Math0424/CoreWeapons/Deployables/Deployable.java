package me.Math0424.CoreWeapons.Deployables;

import me.Math0424.CoreWeapons.Deployables.Types.DeployableType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Deployable {

    private static final ArrayList<Deployable> deployables = new ArrayList<>();

    private final String name;
    private final Double maxHealth;
    private final Integer modelId;
    private final Double range;
    private final DeployableType type;

    private final Sound deploySound;
    private final Float deployPitch;
    private final Integer deployVolume;

    private ItemStack deployableItemStack;

    public Deployable(String name, double maxHealth, double range, DeployableType type, int modelId, Sound deploySound, float deployPitch, int deployVolume) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.range = range;
        this.type = type;
        this.modelId = modelId;
        this.deploySound = deploySound;
        this.deployVolume = deployVolume;
        this.deployPitch = deployPitch;

        deployables.add(this);
        createItemStack();
    }

    private void createItemStack() {
        ItemStack item = new ItemStack(Material.ARMOR_STAND);
        //ItemMeta
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setCustomModelData(modelId);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        deployableItemStack = item;
    }

    public static ArrayList<Deployable> getDeployables() {
        return deployables;
    }

    public String getName() {
        return name;
    }

    public Double getMaxHealth() {
        return maxHealth;
    }

    public Integer getModelId() {
        return modelId;
    }

    public Double getRange() {
        return range;
    }

    public DeployableType getType() {
        return type;
    }

    public Sound getDeploySound() {
        return deploySound;
    }

    public Float getDeployPitch() {
        return deployPitch;
    }

    public ItemStack getDeployableItemstack() {
        return deployableItemStack.clone();
    }

    public Integer getDeployVolume() {
        return deployVolume;
    }

    public static Deployable getByName(String s) {
        String name = ChatColor.stripColor(s);
        for (Deployable item : deployables) {
            if (name.equals(ChatColor.stripColor(item.getName()))) {
                return item;
            }
        }
        return null;
    }

}
