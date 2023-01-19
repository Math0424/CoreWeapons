package me.Math0424.CoreWeapons.Guns.Bullets.Entity;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletDestroyBlockEvent;
import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.MyBullet;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Sound.SoundCache;
import me.Math0424.CoreWeapons.Sound.SoundSystem;
import me.Math0424.CoreWeapons.Util.DrawUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NukeBullet extends MyBullet {

    public NukeBullet(LivingEntity shooter, Container<Gun> cont, Gun gun, double accuracyMultiplier) {
        super(shooter, cont, gun, accuracyMultiplier);
    }

    public void genericHit(Location hit) {
        createNuke(hit);
    }

    private void createNuke(Location loc) {

        SoundSystem.playSound(SoundCache.BULLET_NUKE_HIT, loc, 1, 200);
        for (Entity ent : loc.getWorld().getNearbyEntities(loc, gun.getBulletPower() * 4, gun.getBulletPower() * 4, gun.getBulletPower() * 4)) {
            if (ent.getLocation().distance(loc) <= gun.getBulletPower()) {
                if (ent instanceof LivingEntity) {
                    DamageUtil.setDamage(500.0, (LivingEntity) ent, shooter, container, DamageExplainer.NUKE, true);
                } else {
                    ent.remove();
                }
            } else {
                if (ent instanceof LivingEntity living) {
                    ent.setVelocity(ent.getLocation().toVector().subtract(loc.toVector()).normalize());
                    living.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 10, 10, false, false, false), true);
                    living.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 30, 10, false, false, false), true);
                    living.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 10, false, false, false), true);
                }
            }
        }

        BulletDestroyBlockEvent hitEvent = new BulletDestroyBlockEvent(gun, this, shooter, loc.getBlock());
        Bukkit.getPluginManager().callEvent(hitEvent);
        if (!hitEvent.isCancelled()) {
            for (Block nukeBlock : MyUtil.generateBlockSphere(loc, (int) gun.getBulletPower(), false)) {
                nukeBlock.setType(Material.AIR);
            }

            for (Block friedBlock : MyUtil.generateBlockSphere(loc, (int) gun.getBulletPower() + 1, true)) {
                friedBlock.setType(Material.COAL_BLOCK);
            }
        }

        if (gun.getBulletPower() <= 10)
            DrawUtil.drawParticleSphere(loc, gun.getBulletPower() * 4, 35, Color.BLACK, 2f);
    }

}
