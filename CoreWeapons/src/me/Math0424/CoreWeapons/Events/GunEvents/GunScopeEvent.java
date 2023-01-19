package me.Math0424.CoreWeapons.Events.GunEvents;

import me.Math0424.CoreWeapons.Events.MyEvent;
import org.bukkit.entity.Player;

public class GunScopeEvent extends MyEvent {

    private boolean isCancelled;

    private final Player shooter;

    public GunScopeEvent(Player shooter) {
        this.shooter = shooter;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Player getShooter() {
        return shooter;
    }

}
