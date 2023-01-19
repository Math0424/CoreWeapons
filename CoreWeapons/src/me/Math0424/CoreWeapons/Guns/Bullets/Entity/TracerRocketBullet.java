package me.Math0424.CoreWeapons.Guns.Bullets.Entity;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.MyBullet;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Util.DrawUtil;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class TracerRocketBullet extends MyBullet {

    int ticksAlive = 0;

    public TracerRocketBullet(LivingEntity shooter, Container<Gun> cont, Gun gun, double accuracyMultiplier) {
        super(shooter, cont, gun, accuracyMultiplier);
        this.getBukkitEntity().setFireTicks(10000);
    }

    public void genericHit(Location hit) {
        DamageUtil.setExplosionDamage(hit, (int) gun.getBulletPower(), shooter, container, DamageExplainer.ROCKET);
        this.getBukkitEntity().getWorld().createExplosion(hit, gun.getBulletPower(), false, true, this.getBukkitEntity());
    }

    public void ticked() {
        ticksAlive++;
        cancelOneTick();

        if (ticksAlive % 4 == 0) {
            if (shooter instanceof Player p) {
                if (gun.equals(p.getItemInHand())) {
                    RayTraceResult r = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getLocation().getDirection(), 150, FluidCollisionMode.NEVER, true);
                    if (r != null) {
                        DrawUtil.drawColoredLine(p.getEyeLocation().subtract(0, 1, 0), r.getHitPosition().toLocation(p.getWorld()), Color.RED);
                        this.getBukkitEntity().setVelocity(r.getHitBlock().getLocation().toVector().subtract(this.getBukkitEntity().getLocation().toVector()).normalize().multiply(1.5));
                    } else {
                        this.getBukkitEntity().setVelocity(p.getLocation().add(p.getLocation().getDirection().normalize().multiply(500)).toVector().subtract(this.getBukkitEntity().getLocation().toVector()).normalize().multiply(1.5));
                    }
                }
            }

        }

        if (ticksAlive > 200) {
            if (!this.getBukkitEntity().isDead()) {
                genericHit(this.getBukkitEntity().getLocation());
                this.getBukkitEntity().remove();
            }
        }

    }

}
