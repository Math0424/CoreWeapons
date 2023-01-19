package me.Math0424.CoreWeapons.Events.DeployableEvents;

import me.Math0424.CoreWeapons.Deployables.Deployable;
import me.Math0424.CoreWeapons.Events.MyEvent;
import org.bukkit.entity.Player;

public class DeployableDeployFailedEvent extends MyEvent {

    private final Deployable deployable;
    private final Player player;
    private final DeployableFailReason reason;

    public DeployableDeployFailedEvent(Deployable deployable, Player player, DeployableFailReason reason) {
        this.deployable = deployable;
        this.player = player;
        this.reason = reason;
    }

    public Deployable getDeployable() {
        return deployable;
    }

    public Player getPlayer() {
        return player;
    }

    public DeployableFailReason getFailReason() {
        return reason;
    }

}
