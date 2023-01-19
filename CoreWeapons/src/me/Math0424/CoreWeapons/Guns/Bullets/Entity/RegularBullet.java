package me.Math0424.CoreWeapons.Guns.Bullets.Entity;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Guns.Attachments.AttachmentModifier;
import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.MyBullet;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Sound.SoundCache;
import me.Math0424.CoreWeapons.Sound.SoundSystem;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class RegularBullet extends MyBullet {

    public RegularBullet(LivingEntity shooter, Container<Gun> cont, Gun gun, double accuracyMultiplier) {
        super(shooter, cont, gun, accuracyMultiplier);
    }

    @Override
    public boolean blockHit(Location hit, Block block) {
        if (block.getType() == Material.BELL) {
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_BELL_USE, 1, 1);
        }

        SoundSystem.playSound(SoundCache.BULLET_IMPACT, hit, 1f + MyUtil.randomPosNeg(3) / 10f, 10);
        return true;
    }

    @Override
    public boolean entityHit(LivingEntity entity) {
        if (entity.getEyeHeight() + entity.getLocation().getY() - .1 <= entity.getLocation().getY()) {
            DamageUtil.setDamage(getDamage() * gun.getHeadshotMultiplier(), entity, shooter, container, DamageExplainer.HEADSHOT, true);
        } else {
            DamageUtil.setDamage(getDamage(), entity, shooter, container, DamageExplainer.BULLET, true);
        }

        if (shooter instanceof Player p && gun.getAttachmentValue(AttachmentModifier.ATTACHMENT_HIT_SOUND).isPresent()) {
            p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
        }

        //blood
        entity.getLocation().getWorld().spawnParticle(Particle.BLOCK_CRACK, entity.getLocation(), 25, Material.REDSTONE_BLOCK.createBlockData());
        return true;
    }

}
