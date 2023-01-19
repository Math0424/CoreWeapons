package me.Math0424.CoreWeapons.Events.GunEvents;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Events.MyEvent;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import org.bukkit.entity.Player;

public class GunFireEvent extends MyEvent {

    private boolean isCancelled;

    private final Container<Gun> gun;
    private final Player shooter;

    public GunFireEvent(Container<Gun> gun, Player shooter) {
        this.gun = gun;
        this.shooter = shooter;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Container<Gun> getGun() {
        return gun;
    }

    public Player getShooter() {
        return shooter;
    }

}
