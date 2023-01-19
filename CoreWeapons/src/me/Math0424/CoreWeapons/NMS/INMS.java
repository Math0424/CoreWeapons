package me.Math0424.CoreWeapons.NMS;

import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.MyBullet;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.util.List;

public interface INMS {

    String GetServerResourcePackUrl();
    void SetServerResourcePackUrl(String url, String hash);

    List<Material> GetModifiedStacks();

    void SetMaxStackSize(Material material, int size);

    void CreateBulletEntity(MyBullet bullet);

    Object ToBlockPosition(Location location);

    String[] GetKeyDetails(MinecraftKey key);

    void DeleteDir(File s);
    void CopyFile(File a, File b);
    void CopyDirFromURL(String url, File f);

}
