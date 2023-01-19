package me.Math0424.CoreWeapons.Events.DeployableEvents;

import me.Math0424.CoreWeapons.Deployables.Types.BaseDeployable;
import me.Math0424.CoreWeapons.Events.MyEvent;
import org.bukkit.entity.Player;

public class DeployableUnDeployEvent extends MyEvent {

    private boolean isCancelled;

    private final BaseDeployable deployable;
    private final Player player;

    public DeployableUnDeployEvent(BaseDeployable deployable, Player player) {
        this.deployable = deployable;
        this.player = player;
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

}
