package me.Math0424.CoreWeapons.Deployables.Types;

import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Deployables.Deployable;
import me.Math0424.CoreWeapons.Events.DeployableEvents.DeployablePlayerKickedOutOfShieldEvent;
import me.Math0424.CoreWeapons.Util.DrawUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.*;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class PlayerShield extends BaseDeployable {

    PlayerShield shield = this;

    public PlayerShield(Deployable deployable, Location deployLocation, String owner) {
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
                    if (getDeployable().getMaxHealth() * .75 < getCurrentHealth()) {
                        for (Vector loc : sphere) {
                            spawnParticle(loc, Color.BLUE);
                        }
                    } else if (getDeployable().getMaxHealth() * .50 < getCurrentHealth()) {
                        for (Vector loc : sphere) {
                            spawnParticle(loc, Color.BLUE);
                        }
                    } else if (getDeployable().getMaxHealth() * .25 < getCurrentHealth()) {
                        for (Vector loc : sphere) {
                            spawnParticle(loc, Color.BLUE);
                        }
                    } else {
                        for (Vector loc : sphere) {
                            spawnParticle(loc, Color.BLUE);
                        }
                    }
                }

                if (ticksAlive > getDeployable().getRange() * getDeployable().getRange()) {
                    Collection<Entity> entities = getDeployLocation().getWorld().getNearbyEntities(getDeployLocation(), getDeployable().getRange(), getDeployable().getRange(), getDeployable().getRange());
                    ArrayList<String> playersNames = new ArrayList<String>();
                    for (Entity e : entities) {
                        if (e instanceof Player) {
                            Player p = (Player) e;
                            if (p.getLocation().distance(getDeployLocation()) <= getDeployable().getRange()) {
                                playersNames.add(p.getName());
                            }
                        }
                    }

                    for (Entity e : entities) {
                        if (e != getBase() && e.getLocation().distance(getDeployLocation()) <= getDeployable().getRange() + 1 && e instanceof Projectile) {
                            if (e.getCustomName() != null && !e.isDead()) {
                                String shooter = ((Entity) ((Projectile) e).getShooter()).getName();
                                if (!playersNames.contains(shooter)) {
                                    setCurrentHealth(getCurrentHealth() - Double.parseDouble(e.getCustomName()));
                                    DrawUtil.drawColoredLine(e.getLocation(), getDeployLocation().clone().add(0, 1.5, 0), Color.BLACK);
                                    getDeployLocation().getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 1, new DustOptions(Color.BLACK, 2f));
                                    getDeployLocation().getWorld().playSound(e.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1, 1);
                                    e.setVelocity(e.getLocation().toVector().subtract(getDeployLocation().toVector()).normalize().add(e.getVelocity().multiply(-1)));
                                    setAttacked(20);
                                    updateBase();
                                }
                            }
                        } else if (e.getLocation().distance(getDeployLocation()) <= getDeployable().getRange() && e instanceof Player) {
                            if (((Player) e).getGameMode() != GameMode.CREATIVE) {

                                DeployablePlayerKickedOutOfShieldEvent event = new DeployablePlayerKickedOutOfShieldEvent(shield, (Player) e);
                                Bukkit.getPluginManager().callEvent(event);
                                if (!event.isCancelled()) {
                                    DrawUtil.drawColoredLine(e.getLocation(), getDeployLocation().clone().add(0, 1.5, 0), Color.BLACK);
                                    getDeployLocation().getWorld().playSound(e.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1, 1);
                                    if (e.isInsideVehicle()) {
                                        e.getVehicle().setVelocity(e.getLocation().toVector().subtract(getDeployLocation().toVector()).normalize().add(e.getVelocity().multiply(-1)));
                                    } else {
                                        e.setVelocity(e.getLocation().toVector().subtract(getDeployLocation().toVector()).normalize().add(e.getVelocity().multiply(-1)));
                                    }
                                    if (Bukkit.getPlayer(getOwner()) != null) {
                                        DamageUtil.setDamage(2.0, (LivingEntity) e, Bukkit.getPlayer(getOwner()), DamageExplainer.SHIELD, false);
                                    }
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
