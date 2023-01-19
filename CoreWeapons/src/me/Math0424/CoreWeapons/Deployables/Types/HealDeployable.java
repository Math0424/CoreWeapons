package me.Math0424.CoreWeapons.Deployables.Types;

import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.Deployables.Deployable;
import me.Math0424.CoreWeapons.Util.DrawUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;


class HealDeployable extends BaseDeployable {

    public HealDeployable(Deployable deployable, Location deployLocation, String owner) {
        super(deployable, deployLocation, owner);
    }

    @Override
    public void deploy() {
        final List<Vector> sphere = DrawUtil.generateParticleSphere(getDeployLocation(), getDeployable().getRange().intValue() * 2, getDeployable().getRange());
        new BukkitRunnable() {
            int ticksAlive = 0;

            @Override
            public void run() {

                if (isDisabled() || MyUtil.isNearbyChunkUnLoaded(getDeployLocation())) {
                    return;
                }

                if (ticksAlive % 2 == 0 && ticksAlive < getDeployable().getRange() * getDeployable().getRange()) {
                    for (Vector loc : DrawUtil.generateParticleSphere(getDeployLocation(), getDeployable().getRange().intValue(), ticksAlive / getDeployable().getRange())) {
                        spawnParticle(loc, Color.WHITE);
                    }
                } else if (ticksAlive % 10 == 0) {
                    //color
                    if (getDeployable().getMaxHealth() * .25 < getCurrentHealth()) {
                        for (Vector loc : sphere) {
                            spawnParticle(loc, Color.MAROON);
                        }
                    } else {
                        for (Vector loc : sphere) {
                            spawnParticle(loc, Color.RED);
                        }
                    }

                    if (ticksAlive > getDeployable().getRange() * getDeployable().getRange() && ticksAlive % 10 == 0) {
                        for (Player p : getDeployLocation().getWorld().getPlayers()) {
                            if (p.getLocation().distance(getDeployLocation()) <= getDeployable().getRange()) {
                                if (p.getHealth() < 15 && !p.isDead()) {
                                    DrawUtil.drawLine(p.getLocation().add(0, .5, 0), getDeployLocation().clone().add(0, 1.5, 0), Particle.HEART);
                                    p.setHealth(p.getHealth() + 5);
                                    setCurrentHealth(getCurrentHealth() - 5);
                                    setAttacked(20);
                                    updateBase();
                                }
                            }
                        }
                    }
                }

                if (isUndeployed()) {
                    cancel();
                    return;
                } else if (getCurrentHealth() <= 0 || getBase().isDead()) {
                    remove();
                    getDeployLocation().getWorld().playSound(getDeployLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
                    cancel();
                    return;
                }
                ticksAlive++;
            }
        }.runTaskTimer(CoreWeaponsAPI.getPlugin(), 0, 1);
    }
}
