package me.Math0424.CoreWeapons.Util;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Ammo.Ammo;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;

public class InventoryUtil {

    public static boolean canHaveGun(ItemStack item, Player p, int maxPrimary, int maxSecondary) {
        int totalPrimary = 0;
        int totalSecondary = 0;
        if (item != null) {
            Container<Gun> mainGun = Container.getContainerItem(Gun.class, item);
            if (mainGun != null) {
                ArrayList<ItemStack> inv = getInventoryContents(p);
                for (ItemStack i : inv) {
                    Container<Gun> otherGun = Container.getContainerItem(Gun.class, i);
                    if (otherGun != null) {
                        if (otherGun.getObject().isPrimaryGun()) {
                            totalPrimary++;
                        } else {
                            totalSecondary++;
                        }
                        if (mainGun.getObject().isPrimaryGun() && maxPrimary <= totalPrimary) {
                            return false;
                        } else if (!mainGun.getObject().isPrimaryGun() && maxSecondary <= totalSecondary) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static boolean hasAmmoItem(Player p, String id) {
        for (ItemStack item : p.getInventory()) {
            Container<Ammo> a = Container.getContainerItem(Ammo.class, item);
            if (a != null && a.getObject().getCleanAmmoId().equalsIgnoreCase(ChatColor.stripColor(id))) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasAtLeastAmmo(Player p, String id, int requested) {
        int possible = 0;
        for (ItemStack item : p.getInventory()) {
            Container<Ammo> a = Container.getContainerItem(Ammo.class, item);
            if (a != null && a.getObject().getCleanAmmoId().equalsIgnoreCase(ChatColor.stripColor(id))) {
                possible += Math.min(a.getObject().getBulletCount(), requested);
                if (possible >= requested)
                    return true;
            }
        }
        return false;
    }

    public static boolean drawAmmo(Player p, String id, int requested) {
        if (id.isEmpty() || id.equalsIgnoreCase("null"))
            return true;
        if (!hasAtLeastAmmo(p, id, requested))
            return false;

        for (ItemStack item : p.getInventory()) {
            Container<Ammo> a = Container.getContainerItem(Ammo.class, item);
            if (a != null && a.getObject().getCleanAmmoId().equalsIgnoreCase(ChatColor.stripColor(id))) {
                Ammo am = a.getObject();
                int drawn = Math.min(am.getBulletCount(), requested);

                am.setBulletCount(am.getBulletCount() - drawn);
                am.applyToItem(item);

                ItemStackUtil.setItemDurability(item, am.getMaxBulletCount(), am.getBulletCount());
                if (am.getBulletCount() == 0)
                    a.getItemStack().setAmount(0);

                requested -= drawn;
                if (requested <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void setInventory(Inventory source, Inventory target) {
        for (int i = 0; i <= source.getSize(); ++i) {
            if (i <= target.getSize()) {
                target.setItem(i, source.getItem(i));
            }
        }
    }

    public static boolean isEmpty(Inventory inv) {
        for (ItemStack i : inv.getContents()) {
            if (i != null && i.getType() != Material.AIR) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasAvailableSlot(Inventory inv, ItemStack item) {
        for (int i = 0; i <= inv.getSize(); i++) {
            ItemStack otherItem = inv.getItem(i);
            if (otherItem != null) {
                if (item.hashCode() == otherItem.hashCode()) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<ItemStack> getInventoryContents(Player p) {
        ArrayList<ItemStack> total = new ArrayList<>();
        for (ItemStack i : p.getInventory()) {
            if (i != null) {
                total.add(i);
            }
        }
        total.add(p.getItemOnCursor());
        return total;
    }

    public static Inventory fromString(String stringInv) {
        Inventory inventory = Bukkit.getServer().createInventory(null, 9);
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(FileUtil.decompress(Base64Coder.decodeLines(stringInv)));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            inputStream.close();
            dataInput.close();

        } catch (Exception e) {
            e.printStackTrace();
            MyUtil.log(Level.SEVERE, "Failed to de-serialize Inventory from string!");
        }
        return inventory;
    }

    public static String toString(Inventory inv) {
        try {
            int invSize = inv.getSize() % 9 == 0 ? inv.getSize() : ((inv.getSize() % 9) * 9);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(invSize);
            for (int i = 0; i < invSize; i++) {
                dataOutput.writeObject(inv.getItem(i));
            }

            dataOutput.close();
            outputStream.close();
            return Base64Coder.encodeLines(FileUtil.compress(outputStream.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
            MyUtil.log(Level.SEVERE, "Failed to serialize Inventory to string!");
        }
        return null;
    }


}
