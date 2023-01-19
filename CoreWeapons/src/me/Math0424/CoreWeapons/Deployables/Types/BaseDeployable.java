package me.Math0424.CoreWeapons.Deployables.Types;


import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.Deployables.Deployable;
import me.Math0424.CoreWeapons.Events.DeployableEvents.DeployableFailReason;
import me.Math0424.CoreWeapons.Events.DeployableEvents.DeployableUnDeployFailedEvent;
import me.Math0424.CoreWeapons.Util.DrawUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BaseDeployable {

    private static final ArrayList<BaseDeployable> deployed = new ArrayList<>();

    private ArmorStand base;
    private String uuid;

    private final Deployable deployable;
    private final Location deployLocation;
    private String owner;
    private double currentHealth = -1;

    private boolean disabled = false;
    private int disabledTime = 0;
    private boolean attacked = false;
    private int attackedTime = 0;

    private boolean undeployed = false;

    public BaseDeployable(Deployable deployable, Location deployLocation, String owner) {
        this.deployLocation = deployLocation;
        this.owner = owner;
        this.deployable = deployable;

        if (currentHealth == -1) {
            currentHealth = deployable.getMaxHealth();
        }

        deployLocation.getWorld().playSound(deployLocation, deployable.getDeploySound(), deployable.getDeployVolume(), deployable.getDeployPitch());
        deployed.add(this);
        spawnArmorStand();
        deploy();

    }

    public void spawnArmorStand() {
        deployLocation.getChunk().load();
        for (Entity e : deployLocation.getWorld().getNearbyEntities(deployLocation, 1, 1, 1)) {
            if (e instanceof ArmorStand) {
                e.remove();
            }
        }
        base = deployLocation.getWorld().spawn(deployLocation, ArmorStand.class);
        base.setGravity(false);
        base.setVisible(false);
        base.setBasePlate(false);
        base.setInvulnerable(true);
        base.setCustomName(deployable.getName() + "-=-" + currentHealth + "-=-" + owner);
        base.setHelmet(getDeployable().getDeployableItemstack());
        uuid = base.getUniqueId().toString();
        deployLocation.getWorld().playSound(deployLocation, Sound.BLOCK_BEACON_ACTIVATE, 3, 1);
    }

    public void updateBase() {
        base.setCustomName(deployable.getName() + "-=-" + currentHealth + "-=-" + owner);
    }

    public void deploy() {
        //override
    }

    public void unDeploy(Player p) {
        if (!p.getName().equals(owner)) {
            DeployableUnDeployFailedEvent event = new DeployableUnDeployFailedEvent(this, p, DeployableFailReason.NOTOWNER);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                return;
            }
        } else if (isAttacked()) {
            DeployableUnDeployFailedEvent event = new DeployableUnDeployFailedEvent(this, p, DeployableFailReason.ATTACKED);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                return;
            }
        } else if (isDisabled()) {
            DeployableUnDeployFailedEvent event = new DeployableUnDeployFailedEvent(this, p, DeployableFailReason.DISABLED);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                return;
            }
        } else if (!MyUtil.hasAvaliableSlot(p, deployable.getDeployableItemstack())) {
            DeployableUnDeployFailedEvent event = new DeployableUnDeployFailedEvent(this, p, DeployableFailReason.FULLINV);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                return;
            }
        }

        remove();
        p.getWorld().playSound(deployLocation, Sound.BLOCK_BEACON_DEACTIVATE, 3, 1);
        p.getInventory().addItem(deployable.getDeployableItemstack());

    }

    public void setDisable(int seconds) {
        if (disabled) {
            disabledTime = seconds * 20;
            return;
        }
        disabled = true;
        disabledTime = seconds * 20;
        new BukkitRunnable() {
            final List<Vector> sphere = DrawUtil.generateParticleSphere(deployLocation, deployable.getRange().intValue() * 2, deployable.getRange());

            @Override
            public void run() {
                if (disabledTime % 20 == 0) {
                    for (Vector loc : sphere) {
                        spawnParticle(loc, Color.WHITE);
                    }
                }
                if (disabledTime == 0 || base.isDead()) {
                    disabled = false;
                    cancel();
                    return;
                }
                disabledTime--;
            }
        }.runTaskTimer(CoreWeaponsAPI.getPlugin(), 0, 1);
    }

    public void setAttacked(int seconds) {
        if (attacked) {
            attackedTime = seconds * 20;
            return;
        }
        attacked = true;
        attackedTime = seconds * 20;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (attackedTime == 0 || base.isDead()) {
                    attacked = false;
                    cancel();
                    return;
                }
                attackedTime--;
            }
        }.runTaskTimer(CoreWeaponsAPI.getPlugin(), 0, 1);
    }

    public void remove() {
        undeployed = true;
        for (Entity e : deployLocation.getWorld().getNearbyEntities(deployLocation, 1, 1, 1)) {
            if (e instanceof ArmorStand) {
                e.remove();
            }
        }
        deployed.remove(this);
        base.remove();
    }

    public void spawnParticle(Vector loc, Color c) {
        base.getWorld().spawnParticle(Particle.REDSTONE, loc.toLocation(base.getWorld()), 1, 0, 0, 0, 0, new Particle.DustOptions(c, 1f), true);
    }

    //getters
    public static ArrayList<BaseDeployable> getDeployed() {
        return deployed;
    }

    public ArmorStand getBase() {
        return base;
    }

    public Deployable getDeployable() {
        return deployable;
    }

    public Location getDeployLocation() {
        return deployLocation;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isUndeployed() {
        return undeployed;
    }

    public void setOwner(String s) {
        owner = s;
        updateBase();
    }

    public String getUuid() {
        return uuid;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(Double d) {
        currentHealth = d;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isAttacked() {
        return attacked;
    }
}
