package me.Math0424.CoreWeapons.Guns.Gun;

import me.Math0424.CoreWeapons.Guns.Attachments.Attachment;
import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;

import java.util.List;

public interface IGun {

    String name();
    boolean isPrimaryGun();
    String ammoID();
    String fireSoundID();
    float firePitch();
    int fireSoundRange();
    GunType gunType();
    BulletType bulletType();
    List<String> incompatibleAttachments();
    List<Attachment> attachments();
    float quality();
    int maxBurstCount();
    int maxAmmoCount();
    int shotsAddedPerReloadCycle();
    int bulletsDrawnPerReloadCycle();
    int reloadSpeed();
    int fireSpeed();
    double bulletDrop();
    double bulletSpeed();
    double bulletDamage();
    double headshotMultiplier();
    double bulletFalloff();
    double bulletSpread();
    float bulletPower();
    int bulletCountPerShot();

}
