package me.Math0424.CoreWeapons.Guns.Bullets.Abstract;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.NMS.NMSUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public abstract class MyBullet {

    public final Gun gun;
    public final LivingEntity shooter;
    public final Container<Gun> container;
    public final double accuracy;

    private Entity entity;
    private double damage;
    private boolean nextTick = false;

    protected MyBullet(LivingEntity shooter, Container<Gun> container, Gun gun, double accuracy) {
        this.gun = gun;
        this.container = container;
        this.shooter = shooter;
        this.accuracy = accuracy;
        this.damage = gun.getBulletDamage();
        init();
    }

    protected void cancelOneTick() {
        nextTick = false;
    }

    public double getDamage() {
        return damage;
    }

    public Entity getBukkitEntity() {
        return entity;
    }

    public boolean nextTick() {
        return nextTick;
    }

    public void nextTick(boolean nextTick) {
        this.nextTick = nextTick;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public Vector getShootDir() {
        double spread = (gun.getBulletSpread() / 150.0) * accuracy;
        Vector vec = new org.bukkit.util.Vector(spread * random(), spread * random(), spread * random());
        Vector pvec = shooter.getLocation().getDirection().normalize();
        return pvec.multiply(gun.getBulletSpeed()).add(vec);
    }

    protected double random() {
        return MyUtil.random() - MyUtil.random();
    }





    protected void init() {
        NMSUtil.NMS().CreateBulletEntity(this);
    }

    public void ticked() {}

    public void genericHit(Location hit) {}

    public boolean entityHit(LivingEntity entity) {
        return true;
    }

    public boolean blockHit(Location hit, Block block) {
        return true;
    }

}
