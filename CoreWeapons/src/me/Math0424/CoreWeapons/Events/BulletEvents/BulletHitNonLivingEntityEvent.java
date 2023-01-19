package me.Math0424.CoreWeapons.Events.BulletEvents;

import me.Math0424.CoreWeapons.Events.MyEvent;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class BulletHitNonLivingEntityEvent extends MyEvent {

    private boolean isCancelled;

    private final Gun gun;
    private final LivingEntity shooter;
    private final Entity hitEntity;

    public BulletHitNonLivingEntityEvent(Entity hitEntity, Gun gun, LivingEntity shooter) {
        this.gun = gun;
        this.shooter = shooter;
        this.hitEntity = hitEntity;
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

    public Entity getHitEntity() {
        return hitEntity;
    }
}
