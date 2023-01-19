package me.Math0424.CoreWeapons.Guns.Bullets.Entity;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.MyBullet;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public class FlameBullet extends MyBullet {


    public FlameBullet(LivingEntity shooter, Container<Gun> cont, Gun gun, double accuracyMultiplier) {
        super(shooter, cont, gun, accuracyMultiplier);
        this.getBukkitEntity().setFireTicks(10000);
    }

    @Override
    public boolean blockHit(Location hit, Block block) {
        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 3);
        return true;
    }

    @Override
    public boolean entityHit(LivingEntity entity) {
        DamageUtil.setFireDamage(gun.getBulletDamage(), entity, shooter, container);
        this.getBukkitEntity().getLocation().getWorld().spawnParticle(Particle.BLOCK_CRACK, this.getBukkitEntity().getLocation(), 25, Material.REDSTONE_BLOCK.createBlockData());
        return true;
    }

}
