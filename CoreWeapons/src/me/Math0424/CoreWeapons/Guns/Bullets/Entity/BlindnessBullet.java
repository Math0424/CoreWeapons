package me.Math0424.CoreWeapons.Guns.Bullets.Entity;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BlindnessBullet extends RegularBullet {

    public BlindnessBullet(LivingEntity shooter, Container<Gun> cont, Gun gun, double accuracyMultiplier) {
        super(shooter, cont, gun, accuracyMultiplier);
    }

    public boolean entityHit(LivingEntity entity) {
        super.entityHit(entity);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 2, false, false, false), true);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 1, false, false, false), true);
        return true;
    }

}
