package me.Math0424.CoreWeapons.Core;

import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.Events.CoreEvents.ContainerUpdateEvent;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.logging.Level;

public class Container<T extends SerializableItem<T>> {

    private final ItemStack itemStack;
    private final AutoTag<T> tag;
    private final T generic;

    private Container(AutoTag<T> tag, T generic, ItemStack item) {
        this.tag = tag;
        this.generic = generic;
        this.itemStack = item;
    }

    public void updateItemMapping() {
        if (itemStack == null) return;
        try {
            NamespacedKey key = new NamespacedKey(CoreWeaponsAPI.getPlugin(), generic.friendlyName() + "-data");

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.getPersistentDataContainer().set(key, (PersistentDataType) tag, generic);

            itemStack.setItemMeta(itemMeta);

            ContainerUpdateEvent update = new ContainerUpdateEvent(this);
            Bukkit.getPluginManager().callEvent(update);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public T getObject() {
        return generic;
    }

    //REGION STATIC
    public static <T extends SerializableItem<T>> Container<T> getContainerItem(Class<T> tag, ItemStack item) {
        if (item == null || item.getType() == Material.AIR || item.getItemMeta() == null) {
            return null;
        }
        try {
            T clazz = tag.getDeclaredConstructor().newInstance();

            ItemMeta itemMeta = item.getItemMeta();
            PersistentDataContainer tagContainer = itemMeta.getPersistentDataContainer();

            NamespacedKey key = new NamespacedKey(CoreWeaponsAPI.getPlugin(), clazz.friendlyName() + "-daTa");

            AutoTag<T> autoTag = new AutoTag<>(tag);
            if (tagContainer.has(key, autoTag)) {
                try {
                    clazz = tagContainer.get(key, autoTag);
                    return new Container<>(autoTag, clazz, item);
                } catch (Exception e) {
                    tagContainer.remove(key);
                    Bukkit.getLogger().log(Level.SEVERE, "Warning: Item is corrupted! removing...");
                    ItemStackUtil.changeNameRemoveLore(item, ChatColor.RED + "Outdated " + clazz.friendlyName());
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public static <T extends SerializableItem<T>> ItemStack applyToItem(ItemStack item, SerializableItem<T> value) {
        if (item == null || item.getItemMeta() == null) {
            return item;
        }
        new Container(new AutoTag<>(value.getClass()), value, item).updateItemMapping();
        return item;
    }


}
