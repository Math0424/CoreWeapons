package me.Math0424.CoreWeapons.Events.ArmorEvents;

import me.Math0424.CoreWeapons.Armor.Type.BaseArmor;
import me.Math0424.CoreWeapons.Events.MyEvent;
import org.bukkit.entity.Player;

public class ArmorUseEvent extends MyEvent {

    private boolean isCancelled;

    private final BaseArmor armor;
    private final Player player;

    public ArmorUseEvent(BaseArmor armor, Player player) {
        this.armor = armor;
        this.player = player;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public BaseArmor getArmor() {
        return armor;
    }

    public Player getPlayer() {
        return player;
    }

}
