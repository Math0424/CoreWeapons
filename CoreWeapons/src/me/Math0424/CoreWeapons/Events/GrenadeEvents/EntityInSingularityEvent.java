package me.Math0424.CoreWeapons.Events.GrenadeEvents;

import me.Math0424.CoreWeapons.Events.MyEvent;
import org.bukkit.entity.Entity;

public class EntityInSingularityEvent extends MyEvent {

    private final Entity entity;

    public EntityInSingularityEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
