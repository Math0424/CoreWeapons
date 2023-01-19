package me.Math0424.CoreWeapons.Events.BulletEvents;

import me.Math0424.CoreWeapons.Events.MyEvent;
import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.MyBullet;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import org.bukkit.entity.LivingEntity;

public class BulletTickEvent extends MyEvent {

    private boolean isCancelled;

    private final Gun gun;
    private final MyBullet bullet;
    private final LivingEntity shooter;

    public BulletTickEvent(Gun gun, LivingEntity shooter, MyBullet bullet) {
        this.gun = gun;
        this.shooter = shooter;
        this.bullet = bullet;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Gun getGun() {
        return gun;
    }

    public LivingEntity getShooter() {
        return shooter;
    }

    public MyBullet getBullet() {
        return bullet;
    }
}
