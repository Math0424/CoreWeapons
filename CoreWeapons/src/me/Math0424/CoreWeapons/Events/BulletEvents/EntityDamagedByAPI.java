package me.Math0424.CoreWeapons.Events.BulletEvents;

import me.Math0424.CoreWeapons.DamageHandler.CoreDamage;
import me.Math0424.CoreWeapons.Events.MyEvent;

public class EntityDamagedByAPI extends MyEvent {

    private boolean isCancelled;

    private final CoreDamage damage;

    public EntityDamagedByAPI(CoreDamage damage) {
        this.damage = damage;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public CoreDamage getWitheredDamage() {
        return damage;
    }
}
