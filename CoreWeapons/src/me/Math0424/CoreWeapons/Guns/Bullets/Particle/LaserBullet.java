package me.Math0424.CoreWeapons.Guns.Bullets.Particle;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitBlockEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitEntityEvent;
import me.Math0424.CoreWeapons.Guns.Attachments.AttachmentModifier;
import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.ParticleBullet;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Util.DrawUtil;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.function.Predicate;

public class LaserBullet extends ParticleBullet {

    public LaserBullet(LivingEntity shooter, Container<Gun> cont, Gun gun, double accuracyMultiplier) {
        super(shooter, cont, gun, accuracyMultiplier);

        Vector dir = getShootDir();
        Predicate<Entity> pred = e -> e.getEntityId() != shooter.getEntityId();
        RayTraceResult r = shooter.getWorld().rayTrace(shooter.getEyeLocation(), dir, gun.getBulletSpeed(), FluidCollisionMode.ALWAYS, true, 0, pred);

        if (r != null && r.getHitBlock() != null) {
            BulletHitBlockEvent hitEvent = new BulletHitBlockEvent(r.getHitBlock(), gun, shooter);
            Bukkit.getPluginManager().callEvent(hitEvent);
            if (!hitEvent.isCancelled()) {
                Location hit = r.getHitPosition().toLocation(shooter.getWorld());
                DrawUtil.drawColoredLine(shooter.getEyeLocation().subtract(0, .25, 0), hit, Color.RED);
            }
        } else {
            DrawUtil.drawColoredLine(shooter.getEyeLocation().subtract(0, .25, 0), dir, gun.getBulletSpeed(), Color.RED);
        }

        if (r != null && r.getHitEntity() != null && r.getHitEntity() instanceof LivingEntity living) {
            BulletHitEntityEvent hitEvent = new BulletHitEntityEvent(living, gun, shooter);
            Bukkit.getPluginManager().callEvent(hitEvent);
            if (!hitEvent.isCancelled()) {

                Location hit = r.getHitPosition().toLocation(shooter.getWorld());
                DrawUtil.drawColoredLine(shooter.getEyeLocation().subtract(0, .25, 0), hit, Color.RED);

                DamageUtil.setDamage(gun.getBulletDamage(), living, shooter, container, DamageExplainer.LASER, true);
                if (shooter instanceof Player p && gun.getAttachmentValue(AttachmentModifier.ATTACHMENT_HIT_SOUND).isPresent()) {
                    p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                }
            }
        }
    }

}
