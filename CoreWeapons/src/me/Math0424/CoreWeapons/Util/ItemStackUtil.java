package me.Math0424.CoreWeapons.Util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemStackUtil {

    public static ItemStack createItemStack(Material mat) {
        return createItemStack(mat, null, 1, null, 0);
    }

    public static ItemStack createItemStack(Material mat, String name) {
        return createItemStack(mat, name, 1, null, 0);
    }

    public static ItemStack createItemStack(Material mat, int id) {
        return createItemStack(mat, null, 1, null, id);
    }

    public static ItemStack createItemStack(Material mat, String name, List<String> lore) {
        return createItemStack(mat, name, 1, lore, 0);
    }

    public static ItemStack createItemStack(Material mat, String name, List<String> lore, int count, int id) {
        return createItemStack(mat, name, count, lore, id);
    }

    public static ItemStack createItemStack(Material mat, String name, int count) {
        return createItemStack(mat, name, count, null, 0);
    }

    public static ItemStack createItemStack(Material mat, String name, int count, int id) {
        return createItemStack(mat, name, count, null, id);
    }

    public static ItemStack createItemStack(Material mat, String name, List<String> lore, Integer count) {
        return createItemStack(mat, name, count, lore, 0);
    }

    public static ItemStack createItemStack(Material mat, String name, Integer count, List<String> lore, int id) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        item.setAmount(Math.max(1, count));
        if (name != null) {
            meta.setDisplayName(name);
        }
        if (lore != null) {
            List<String> finalLore = new ArrayList<>();
            for (String s : lore) {
                finalLore.add(ChatColor.translateAlternateColorCodes('&', s));
            }
            meta.setLore(finalLore);
        }
        if (id != 0) {
            meta.setCustomModelData(id);
        }
        if (!mat.isAir()) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack changeNameRemoveLore(ItemStack item, String name) {
        return createItemStack(item.getType(), name, item.getAmount(), null, item.getItemMeta().hasCustomModelData() ? item.getItemMeta().getCustomModelData() : 0);
    }

    public static void setLore(ItemStack item, String... lore) {
        setLore(item, Arrays.stream(lore).toList());
    }

    public static void setLore(ItemStack item, List<String> lore) {
        ItemMeta itemMeta = item.getItemMeta();
        List<String> chatColorLore = new ArrayList<>();
        for (String s : lore) {
            chatColorLore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        itemMeta.setLore(chatColorLore);
        item.setItemMeta(itemMeta);
    }
    /**
     * Will compare DisplayName, Type
     */
    public static boolean isSimilarNameType(ItemStack item1, ItemStack item2) {
        if (item1 == item2) {
            return true;
        }
        if ((item1 == null || item2 == null) || (item1.getType() != item2.getType())) {
            return false;
        }
        if (!item1.hasItemMeta() && !item2.hasItemMeta()) {
            return true;
        }
        if (item1.hasItemMeta() != item2.hasItemMeta()) {
            return false;
        }
        if (item1.hasItemMeta() && item2.hasItemMeta()) {
            ItemMeta meta1 = item1.getItemMeta();
            ItemMeta meta2 = item2.getItemMeta();
            return meta1.getDisplayName().equals(meta2.getDisplayName());
        }
        return false;
    }

    /**
     * Will compare ModelID, Name, Type
     */
    public static boolean isSomewhatSimilar(ItemStack item1, ItemStack item2) {
        if (!isSimilarNameType(item1, item2)) {
            return true;
        }
        if (item1.hasItemMeta() && item2.hasItemMeta()) {
            ItemMeta meta1 = item1.getItemMeta();
            ItemMeta meta2 = item2.getItemMeta();
            if (meta1.hasCustomModelData() && meta2.hasCustomModelData()) {
                return meta1.getCustomModelData() == meta2.getCustomModelData();
            } else return meta1.hasCustomModelData() == meta2.hasCustomModelData();
        }
        return false;
    }

    public static void setItemDurability(ItemStack item, int max, int current) {
        if (item.hasItemMeta()) {
            short maxDur = item.getType().getMaxDurability();
            Damageable meta = (Damageable) item.getItemMeta();
            if (meta != null) {
                meta.setDamage((short) (maxDur - (maxDur * (current / (max + 0.0)))));
                item.setItemMeta(meta);
            }
        }
    }

    public static ItemStack visualNameClone(ItemStack item) {
        ItemStack clone = createItemStack(item.getType(), item.getAmount());
        if (item.hasItemMeta()) {
            ItemMeta cMeta = clone.getItemMeta();
            ItemMeta meta = item.getItemMeta();
            if (meta.hasCustomModelData()) {
                cMeta.setCustomModelData(meta.getCustomModelData());
            }
            if (meta.hasDisplayName()) {
                cMeta.setDisplayName(meta.getDisplayName());
            }
            clone.setItemMeta(cMeta);
        }
        return clone;
    }

}
