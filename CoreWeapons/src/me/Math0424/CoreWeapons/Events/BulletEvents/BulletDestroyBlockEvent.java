package me.Math0424.CoreWeapons.Events.BulletEvents;

import me.Math0424.CoreWeapons.Events.MyEvent;
import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.MyBullet;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public class BulletDestroyBlockEvent extends MyEvent {

    private boolean isCancelled;

    private final Gun gun;
    private final Block block;
    private final LivingEntity shooter;
    private final MyBullet bullet;

    public BulletDestroyBlockEvent(Gun gun, MyBullet bullet, LivingEntity shooter, Block block) {
        this.gun = gun;
        this.shooter = shooter;
        this.block = block;
        this.bullet = bullet;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public MyBullet getBullet() {
        return bullet;
    }

    public Gun getGun() {
        return gun;
    }

    public LivingEntity getShooter() {
        return shooter;
    }

    public Block getBlock() {
        return block;
    }
}
