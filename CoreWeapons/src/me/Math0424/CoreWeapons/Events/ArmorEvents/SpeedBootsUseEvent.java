package me.Math0424.CoreWeapons.Events.ArmorEvents;

import me.Math0424.CoreWeapons.Armor.Type.SpeedBoots;
import org.bukkit.entity.Player;

public class SpeedBootsUseEvent extends ArmorUseEvent {

    private final SpeedBoots boots;

    public SpeedBootsUseEvent(SpeedBoots boots, Player player) {
        super(boots, player);
        this.boots = boots;
    }

    public SpeedBoots getSpeedBoots() {
        return boots;
    }

}
