package me.Math0424.CoreWeapons.Events.CoreEvents;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Events.MyEvent;

public class ContainerUpdateEvent extends MyEvent {

    private boolean isCancelled;

    private final Container container;

    public ContainerUpdateEvent(Container container) {
        this.container = container;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Container getContainer() {
        return container;
    }
}
