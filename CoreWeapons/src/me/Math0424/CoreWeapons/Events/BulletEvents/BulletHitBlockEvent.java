package me.Math0424.CoreWeapons.Events.BulletEvents;

import me.Math0424.CoreWeapons.Events.MyEvent;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public class BulletHitBlockEvent extends MyEvent {

    private boolean isCancelled;

    private final Gun gun;
    private final LivingEntity shooter;
    private final Block hitBlock;

    public BulletHitBlockEvent(Block hitBlock, Gun gun, LivingEntity shooter) {
        this.gun = gun;
        this.shooter = shooter;
        this.hitBlock = hitBlock;
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

    public Block getHitBlock() {
        return hitBlock;
    }
}
