package me.Math0424.CoreWeapons.Events.ArmorEvents;


import me.Math0424.CoreWeapons.Armor.Type.PowerLegs;
import org.bukkit.entity.Player;

public class PowerLegsUseEvent extends ArmorUseEvent {

    private final PowerLegs legs;

    public PowerLegsUseEvent(PowerLegs legs, Player player) {
        super(legs, player);
        this.legs = legs;
    }

    public PowerLegs getPowerLegs() {
        return legs;
    }

}
