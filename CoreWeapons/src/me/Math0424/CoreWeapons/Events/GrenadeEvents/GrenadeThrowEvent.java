package me.Math0424.CoreWeapons.Events.GrenadeEvents;

import me.Math0424.CoreWeapons.Events.MyEvent;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import org.bukkit.entity.Player;

public class GrenadeThrowEvent extends MyEvent {

    private boolean isCancelled;

    private final Grenade grenade;
    private final Player thrower;

    public GrenadeThrowEvent(Grenade grenade, Player thrower) {
        this.grenade = grenade;
        this.thrower = thrower;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Grenade getGrenade() {
        return grenade;
    }

    public Player getThrower() {
        return thrower;
    }

}
