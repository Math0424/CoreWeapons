package me.Math0424.CoreWeapons.DamageHandler;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.Events.BulletEvents.EntityDamagedByAPI;
import me.Math0424.CoreWeapons.Guns.Attachments.AttachmentModifier;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class DamageUtil {

    public static void setDamage(Double dam, LivingEntity damaged, Entity damager, DamageExplainer cause, boolean instant) {
        setDamage(dam, damaged, damager, null, cause, instant);
    }

    public static void setDamage(Double dam, LivingEntity damaged, Entity damager, Container<Gun> g, DamageExplainer cause, boolean instant) {
        CoreDamage w = new CoreDamage(damaged, damager, g, dam, cause);
        EntityDamagedByAPI event = new EntityDamagedByAPI(w);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {

            if (instant)
                damaged.setNoDamageTicks(0);

            if (g != null) {
                Optional<Double> armorPiercing = g.getObject().getAttachmentValue(AttachmentModifier.ATTACHMENT_ARMOR_PIERCING);
                if (!armorPiercing.isPresent()) {
                    damaged.damage(w.getDamageAmount(), damager);
                } else {
                    damaged.damage(w.getDamageAmount() * armorPiercing.get());
                }
            } else {
                damaged.damage(w.getDamageAmount(), damager);
            }

            damaged.setNoDamageTicks(20);
            clearDamage(damaged);
        }
    }

    public static void setFireDamage(Double dam, LivingEntity damaged, Entity damager, Container<Gun> g) {
        CoreDamage w = new CoreDamage(damaged, damager, g, dam, DamageExplainer.FIRE);
        EntityDamagedByAPI event = new EntityDamagedByAPI(w);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            damaged.setFireTicks(dam.intValue() * 20);
            damaged.damage(w.getDamageAmount(), damager);
            clearDamage(damaged);
        }
    }

    public static void setExplosionDamage(Location loc, int r, Entity damager, Container<Gun> g, DamageExplainer cause) {
        for (Entity e : loc.getWorld().getNearbyEntities(loc, r, r, r)) {
            if (e instanceof LivingEntity) {
                new CoreDamage((LivingEntity) e, damager, g, 0.0, cause);
                clearDamage((LivingEntity) e);
            }
        }
    }

    private static void clearDamage(LivingEntity damaged) {
        new BukkitRunnable() {
            @Override
            public void run() {
                CoreDamage.clearLastDamage(damaged);
            }
        }.runTaskLater(CoreWeaponsAPI.getPlugin(), 20);
    }

}
