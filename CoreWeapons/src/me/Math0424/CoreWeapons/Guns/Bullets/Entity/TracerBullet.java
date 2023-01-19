package me.Math0424.CoreWeapons.Guns.Bullets.Entity;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.NMS.NMSUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TracerBullet extends RegularBullet {

    public TracerBullet(LivingEntity shooter, Container<Gun> cont, Gun gun, double accuracyMultiplier) {
        super(shooter, cont, gun, accuracyMultiplier);
    }

    public boolean blockHit(Location hit, Block block) {
        hit.getWorld().playSound(hit, Sound.ENTITY_ARROW_HIT, 1, 3);
        return true;
    }

    public boolean entityHit(LivingEntity entity) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 120, 10, false, false, false), true);
        return true;
    }

    public void ticked() {
        this.getBukkitEntity().getWorld().spawnParticle(Particle.REDSTONE, this.getBukkitEntity().getLocation(), 1, new DustOptions(Color.WHITE, 1f));
    }

}
