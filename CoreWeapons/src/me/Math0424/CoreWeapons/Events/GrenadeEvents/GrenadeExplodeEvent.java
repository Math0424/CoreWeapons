package me.Math0424.CoreWeapons.Events.GrenadeEvents;

import me.Math0424.CoreWeapons.Events.MyEvent;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class GrenadeExplodeEvent extends MyEvent {

    private boolean isCancelled;

    private final Grenade grenade;
    private final Player thrower;
    private final Item item;

    public GrenadeExplodeEvent(Player thrower, Grenade grenade, Item item) {
        this.thrower = thrower;
        this.grenade = grenade;
        this.item = item;
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

    public Item getItem() {
        return item;
    }

}
