package me.Math0424.CoreWeapons.Guns.Bullets.Entity;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletDestroyBlockEvent;
import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.MyBullet;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AcidBullet extends MyBullet {

    private int iteration = 1;

    public AcidBullet(LivingEntity shooter, Container<Gun> cont, Gun gun, double accuracyMultiplier) {
        super(shooter, cont, gun, accuracyMultiplier);
    }

    @Override
    public boolean blockHit(Location hit, Block block) {
        BulletDestroyBlockEvent hitEvent = new BulletDestroyBlockEvent(gun, this, shooter, block);
        Bukkit.getPluginManager().callEvent(hitEvent);
        if (!hitEvent.isCancelled()) {
            block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
            block.breakNaturally();
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_SLIME_BLOCK_BREAK, 1, 1);
            iteration++;
            return false;
        }
        return true;
    }

    @Override
    public boolean entityHit(LivingEntity entity) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 5, 1, false, false, false), true);
        DamageUtil.setDamage(getDamage(), entity, shooter, container, DamageExplainer.ACID, true);
        return true;
    }

    public int getIteration() {
        return iteration;
    }

}
