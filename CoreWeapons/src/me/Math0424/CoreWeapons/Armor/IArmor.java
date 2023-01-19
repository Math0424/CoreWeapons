package me.Math0424.CoreWeapons.Armor;

import me.Math0424.CoreWeapons.Armor.Type.ArmorType;
import org.bukkit.Material;

public interface IArmor {

    String name();

    Double maxSpeed();

    Double acceleration();

    Material material();

    Integer maxHeight();

    String ammoId();

    Integer modelId();

    Integer uses();

    Integer fixTime();

    Integer usesFixedPerReload();

    ArmorType armorType();

    /*static Armor register(IArmor armor) {
        return new Armor(
                armor.name(),
                armor.armorType(),
                armor.material(),
                armor.modelId(),
                armor.maxSpeed(),
                armor.acceleration(),
                armor.maxHeight(),
                armor.ammoId(),
                armor.uses(),
                armor.fixTime(),
                armor.usesFixedPerReload()
        );
    }*/


}
