package me.Math0424.CoreWeapons.Guns.Ammo;

import org.bukkit.Material;

public interface IAmmo {

    String name();

    Material material();

    int modelId();

    int maxStackSize();

    /*static Ammo register(IAmmo ammo) {
        return new Ammo(
                ammo.name(),
                ammo.material(),
                ammo.modelId(),
                ammo.maxStackSize()
        );
    }*/

}
