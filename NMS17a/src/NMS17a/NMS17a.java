package NMS17a;

import NMS17a.Entity.EntityBullet;
import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.MyBullet;
import me.Math0424.CoreWeapons.NMS.INMS;
import net.minecraft.core.BlockPosition;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NMS17a implements INMS {

    private final List<Material> modified = new ArrayList<>();

    @Override
    public String GetServerResourcePackUrl() {
        return ((CraftServer)Bukkit.getServer()).getServer().getResourcePack();
    }

    @Override
    public void SetServerResourcePackUrl(String url, String hash) {
        ((CraftServer)Bukkit.getServer()).getServer().setResourcePack(url, hash);
    }

    public static WorldServer GetWorld(org.bukkit.entity.Entity ent) {
        return ((CraftWorld) ent.getWorld()).getHandle();
    }

    public static ItemStack NMSItem(org.bukkit.inventory.ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }


    @Override
    public List<Material> GetModifiedStacks() {
        return modified;
    }

    @Override
    public void SetMaxStackSize(Material material, int size) {
        modified.add(material);
        if (size == 0)
            return;
        Item item = NMSItem(new org.bukkit.inventory.ItemStack(material)).getItem();
        try {
            Field f = Item.class.getDeclaredField("maxStackSize");
            f.setAccessible(true);
            f.setInt(item, size);
            modified.add(material);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void CreateBulletEntity(MyBullet bullet) {
        new EntityBullet(bullet);
    }

    @Override
    public Object ToBlockPosition(Location location) {
        return new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public String[] GetKeyDetails(MinecraftKey key) {
        return new String[] {key.getNamespace(), key.getKey()};
    }

    @Override
    public void DeleteDir(File s) {
        try {
            FileUtils.deleteDirectory(s);
        } catch (Exception ignored) {}
    }

    @Override
    public void CopyFile(File a, File b) {
        try {
            FileUtils.copyFile(a, b);
        } catch (Exception ignored) {}
    }

    @Override
    public void CopyDirFromURL(String url, File f) {
        try {
            FileUtils.copyURLToFile(new URL(url), f);
        } catch (Exception ignored) {}
    }


}
