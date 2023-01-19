package me.Math0424.CoreWeapons.Grenades.Grenade;

import org.bukkit.Material;
import org.bukkit.Sound;

public interface IGrenade {

    String name();

    Integer modelId();

    Material material();

    Double throwMultiplier();

    Integer explodeTime();

    Integer maxStackSize();

    GrenadeType grenadeType();

    Sound throwSound();

    float throwPitch();

    Integer throwVolume();

    float explosiveYield();

	/*static Grenade register(IGrenade grenade) {
		return new Grenade(
				grenade.name(),
				grenade.grenadeType(),
				grenade.modelId(),
				grenade.material(),
				grenade.throwMultiplier(),
				grenade.explodeTime(),
				grenade.maxStackSize(),
				grenade.throwSound(),
				grenade.throwPitch(),
				grenade.throwVolume(),
				grenade.explosiveYield()
		);
	}*/


}
