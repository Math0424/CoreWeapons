package me.Math0424.CoreWeapons.Armor.Type;

import me.Math0424.CoreWeapons.Armor.Armor;
import me.Math0424.CoreWeapons.Core.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public abstract class BaseArmor {

    public abstract void useArmor(Container<Armor> a, PlayerMoveEvent e);

    public void toggleFlight(Container<Armor> a, PlayerToggleFlightEvent e) {
        //override
    }

    protected boolean canUseArmor(Player p, Armor a) {
        return a.getCurrentUses() > 0 && p.getLocation().getY() <= a.getMaxHeight();
    }
}
