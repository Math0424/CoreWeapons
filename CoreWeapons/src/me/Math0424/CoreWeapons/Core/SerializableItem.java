package me.Math0424.CoreWeapons.Core;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.Map;

public abstract class SerializableItem<T extends SerializableItem<T>> implements Serializable, Cloneable {

    public abstract void serialize(Map<String, Object> map);

    public abstract void deSerialize(Map<String, Object> map);

    public abstract String friendlyName();

    public ItemStack applyToItem(ItemStack item) {
        return Container.applyToItem(item, this);
    }

    @Override
    protected T clone() {
        try {
            return (T) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
