package me.Math0424.CoreWeapons.Events.DeployableEvents;

import me.Math0424.CoreWeapons.Deployables.Types.BaseDeployable;
import me.Math0424.CoreWeapons.Events.MyEvent;
import org.bukkit.entity.Player;

public class DeployableUnDeployFailedEvent extends MyEvent {

    private final BaseDeployable deployable;
    private final Player player;
    private final DeployableFailReason reason;
    private boolean isCancelled;

    public DeployableUnDeployFailedEvent(BaseDeployable deployable, Player player, DeployableFailReason reason) {
        this.deployable = deployable;
        this.player = player;
        this.reason = reason;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public BaseDeployable getDeployable() {
        return deployable;
    }

    public Player getPlayer() {
        return player;
    }

    public DeployableFailReason getFailReason() {
        return reason;
    }

}
