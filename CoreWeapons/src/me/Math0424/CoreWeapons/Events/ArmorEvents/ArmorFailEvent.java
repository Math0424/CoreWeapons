package me.Math0424.CoreWeapons.Events.ArmorEvents;

import me.Math0424.CoreWeapons.Armor.Armor;
import me.Math0424.CoreWeapons.Events.MyEvent;
import org.bukkit.entity.Player;

public class ArmorFailEvent extends MyEvent {

    private final Armor armor;
    private final ArmorFailReason reason;
    private final Player player;

    public ArmorFailEvent(Armor armor, Player player, ArmorFailReason reason) {
        this.armor = armor;
        this.player = player;
        this.reason = reason;
    }

    public Armor getArmor() {
        return armor;
    }

    public ArmorFailReason getReason() {
        return reason;
    }

    public Player getPlayer() {
        return player;
    }

}
