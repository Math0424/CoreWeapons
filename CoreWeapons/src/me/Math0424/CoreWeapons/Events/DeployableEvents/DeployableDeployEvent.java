package me.Math0424.CoreWeapons.Events.DeployableEvents;

import me.Math0424.CoreWeapons.Deployables.Deployable;
import me.Math0424.CoreWeapons.Events.MyEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DeployableDeployEvent extends MyEvent {

    private boolean isCancelled;

    private final Deployable deployable;
    private final Player player;
    private final Location location;

    public DeployableDeployEvent(Deployable deployable, Player player, Location location) {
        this.deployable = deployable;
        this.player = player;
        this.location = location;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Deployable getDeployable() {
        return deployable;
    }

    public Location getLocation() {
        return location;
    }

    public Player getPlayer() {
        return player;
    }

}
