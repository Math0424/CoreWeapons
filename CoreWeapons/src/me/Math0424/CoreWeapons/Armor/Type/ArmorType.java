package me.Math0424.CoreWeapons.Armor.Type;

import me.Math0424.CoreWeapons.Armor.Armor;
import me.Math0424.CoreWeapons.Core.Container;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public enum ArmorType {

    SPEEDBOOTS(SpeedBoots.class),
    POWERLEGS(PowerLegs.class),
    JETPACK(Jetpack.class);

    Class<? extends BaseArmor> clazz;

    ArmorType(Class<? extends BaseArmor> clazz) {
        this.clazz = clazz;
    }

    public void useArmor(Container<Armor> a, PlayerMoveEvent e) {
        try {
            a.getObject().getArmorType().getClazz().newInstance().useArmor(a, e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void toggledFlight(Container<Armor> a, PlayerToggleFlightEvent e) {
        try {
            a.getObject().getArmorType().getClazz().newInstance().toggleFlight(a, e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Class<? extends BaseArmor> getClazz() {
        return this.clazz;
    }


}
