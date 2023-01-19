package me.Math0424.CoreWeapons.Guns.Bullets.Particle;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitBlockEvent;
import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.ParticleBullet;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Util.DrawUtil;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TeleportBullet extends ParticleBullet {

    Location start;
    Location exit;

    private static final List<Material> valid = new ArrayList<>();

    static {
        valid.addAll(Tag.CROPS.getValues());
        valid.addAll(Tag.IMPERMEABLE.getValues());
        valid.add(Material.WATER);
        valid.addAll(Tag.RAILS.getValues());
    }

    public TeleportBullet(LivingEntity shooter, Container<Gun> cont, Gun gun, double accuracyMultiplier) {
        super(shooter, cont, gun, accuracyMultiplier);

        Vector dir = getShootDir();
        Predicate<Entity> pred = e -> e.getEntityId() != shooter.getEntityId();
        RayTraceResult r = shooter.getWorld().rayTrace(shooter.getEyeLocation(), dir, gun.getBulletSpeed(), FluidCollisionMode.ALWAYS, true, 0, pred);

        start = shooter.getLocation();
        if (r != null) {
            Location hit = r.getHitPosition().toLocation(shooter.getWorld());
            DrawUtil.drawColoredLine(shooter.getEyeLocation().subtract(0, .25, 0), hit, Color.RED);
            hit.getWorld().playSound(hit, Sound.BLOCK_LAVA_EXTINGUISH, 1, 1);

            Location loc = new Location(shooter.getWorld(), hit.getBlockX(), hit.getBlockY(), hit.getBlockZ(), shooter.getLocation().getYaw(), shooter.getLocation().getPitch());

            BulletHitBlockEvent hitEvent = new BulletHitBlockEvent(r.getHitPosition().toLocation(shooter.getWorld()).getBlock(), gun, shooter);
            Bukkit.getPluginManager().callEvent(hitEvent);
            if (!hitEvent.isCancelled()) {
                if (valid.contains(loc.getBlock().getType())) {
                    loc = loc.add(0.5, 0.1, 0.5);
                } else if (valid.contains(loc.getBlock().getLocation().add(1, 0, 0).getBlock().getType())) {
                    loc = new Location(loc.getWorld(), loc.getX() + 1.5, loc.getY() + .1, loc.getZ() + .5, shooter.getLocation().getYaw(), shooter.getLocation().getPitch());
                } else if (valid.contains(loc.getBlock().getLocation().add(0, 0, 1).getBlock().getType())) {
                    loc = new Location(loc.getWorld(), loc.getX() + .5, loc.getY() + .1, loc.getZ() + 1.5, shooter.getLocation().getYaw(), shooter.getLocation().getPitch());
                } else if (valid.contains(loc.getBlock().getLocation().add(-1, 0, 0).getBlock().getType())) {
                    loc = new Location(loc.getWorld(), loc.getX() - .5, loc.getY() + .1, loc.getZ() + .5, shooter.getLocation().getYaw(), shooter.getLocation().getPitch());
                } else if (valid.contains(loc.getBlock().getLocation().add(0, 0, -1).getBlock().getType())) {
                    loc = new Location(loc.getWorld(), loc.getX() + .5, loc.getY() + .1, loc.getZ() - .5, shooter.getLocation().getYaw(), shooter.getLocation().getPitch());
                } else if (valid.contains(loc.getBlock().getLocation().add(0, 1, 0).getBlock().getType())) {
                    loc = new Location(loc.getWorld(), loc.getX() + .5, loc.getY() + 1.1, loc.getZ() - .5, shooter.getLocation().getYaw(), shooter.getLocation().getPitch());
                } else if (valid.contains(loc.getBlock().getLocation().add(0, -1, 0).getBlock().getType())) {
                    loc = new Location(loc.getWorld(), loc.getX() + .5, loc.getY() - 1.1, loc.getZ() - .5, shooter.getLocation().getYaw(), shooter.getLocation().getPitch());
                } else {
                    loc = loc.add(0.5, 0.1, 0.5);
                }

                teleportWithVelocity(shooter, loc);
                exit = shooter.getLocation();
                createPortal();
            }

        } else {
            DrawUtil.drawColoredLine(shooter.getEyeLocation().subtract(0, .25, 0), dir, gun.getBulletSpeed(), Color.PURPLE);
        }

    }

    private void teleportWithVelocity(LivingEntity ent, Location loc) {

        DrawUtil.drawColoredLine(ent.getEyeLocation(), loc, Color.BLUE);
        Vector v = ent.getVelocity();
        ent.teleport(loc);
        ent.setVelocity(v);
        shooter.getWorld().playSound(shooter.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3, 1);
        shooter.getWorld().spawnParticle(Particle.PORTAL, shooter.getLocation(), 100, 0, 1, 0);

    }

    private void createPortal() {
        new BukkitRunnable() {
            int timesRun = 0;

            @Override
            public void run() {
                timesRun++;
                if (timesRun == 1) {
                    start.getWorld().playSound(start, Sound.BLOCK_PORTAL_TRIGGER, 1, 1);
                }

                start.getWorld().spawnParticle(Particle.PORTAL, start, 100, 0, 1, 0);

                for (Entity e : start.getWorld().getNearbyEntities(start, 1, 3, 1)) {
                    if (e instanceof LivingEntity ent)
                        teleportWithVelocity(ent, exit);
                }

                if (timesRun == 20) {
                    cancel();
                }
            }
        }.runTaskTimer(CoreWeaponsAPI.getPlugin(), 0, 10);
    }

}
