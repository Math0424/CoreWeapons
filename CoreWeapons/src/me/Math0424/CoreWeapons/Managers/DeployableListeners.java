package me.Math0424.CoreWeapons.Managers;

import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.Deployables.Deployable;
import me.Math0424.CoreWeapons.Deployables.Types.BaseDeployable;
import me.Math0424.CoreWeapons.Events.DeployableEvents.DeployableDeployEvent;
import me.Math0424.CoreWeapons.Events.DeployableEvents.DeployableDeployFailedEvent;
import me.Math0424.CoreWeapons.Events.DeployableEvents.DeployableFailReason;
import me.Math0424.CoreWeapons.Events.DeployableEvents.DeployableUnDeployEvent;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

class DeployableListeners implements Listener {

    public DeployableListeners() {
        for (World w : Bukkit.getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (e instanceof ArmorStand) {
                    DeployableListeners.armorStandDeployable((ArmorStand) e);
                }
            }
        }
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e) {

        if (e.isBlockInHand()) return;

        Player p = e.getPlayer();
        Action a = e.getAction();
        ItemStack item = null;

        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND) && p.getInventory().getItemInMainHand() != null) {
            item = p.getInventory().getItemInMainHand();
        } else if (e.getHand() != null && e.getHand().equals(EquipmentSlot.OFF_HAND) && p.getInventory().getItemInOffHand() != null) {
            item = p.getInventory().getItemInOffHand();
        }

        if (item != null && !e.isCancelled()) {
            if (a == Action.RIGHT_CLICK_BLOCK) {

                if (e.getClickedBlock() != null && (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST)) {
                    return;
                }

                Location loc = e.getClickedBlock().getLocation();
                for (Deployable d : Deployable.getDeployables()) {
                    if (ItemStackUtil.isSimilarNameType(item, d.getDeployableItemstack())) {
                        e.setCancelled(true);

                        if (loc.add(0, 1, 0).getBlock().getType() != Material.AIR) {
                            return;
                        }

                        if (BaseDeployable.getDeployed().size() > 0) {
                            for (BaseDeployable others : BaseDeployable.getDeployed()) {
                                if (others != null && others.getDeployLocation().distance(loc) <= d.getRange() * 2) {
                                    DeployableDeployFailedEvent event = new DeployableDeployFailedEvent(d, e.getPlayer(), DeployableFailReason.TOOCLOSE);
                                    Bukkit.getPluginManager().callEvent(event);
                                    return;
                                }
                            }
                        }

                        DeployableDeployEvent event = new DeployableDeployEvent(d, e.getPlayer(), loc);
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {

                            if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND) && p.getInventory().getItemInMainHand() != null) {
                                p.getInventory().getItemInMainHand().setAmount(p.getItemInHand().getAmount() - 1);
                            } else if (e.getHand() != null && e.getHand().equals(EquipmentSlot.OFF_HAND) && p.getInventory().getItemInOffHand() != null) {
                                p.getInventory().getItemInOffHand().setAmount(p.getItemInHand().getAmount() - 1);
                            }

                            d.getType().deployStructure(d, loc.subtract(0, 1.4, 0).add(.5, 0, .5), p.getName());
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void playerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent e) {
        for (BaseDeployable deployed : BaseDeployable.getDeployed()) {
            if (deployed != null && deployed.getUuid().equals(e.getRightClicked().getUniqueId().toString())) {
                e.setCancelled(true);
                DeployableUnDeployEvent event = new DeployableUnDeployEvent(deployed, e.getPlayer());
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    deployed.unDeploy(e.getPlayer());
                }
                return;
            }
        }
    }

    @EventHandler
    public void chunkLoadEvent(ChunkLoadEvent e) {
        for (Entity ent : e.getChunk().getEntities()) {
            if (ent instanceof ArmorStand) {
                armorStandDeployable((ArmorStand) ent);
            }
        }
    }

    public static void armorStandDeployable(ArmorStand stand) {
        if (stand.getCustomName() != null) {
            String[] name = stand.getCustomName().split("-=-");
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (name.length == 3 && Deployable.getByName(name[0]) != null) {

                        if (!BaseDeployable.getDeployed().isEmpty()) {
                            for (BaseDeployable deployed : BaseDeployable.getDeployed()) {
                                if (MyUtil.isSameBlockLocation(deployed.getDeployLocation(), stand.getLocation())) {
                                    return;
                                }
                            }
                        }

                        Location standLoc = stand.getLocation();
                        Deployable.getByName(name[0]).getType().deployStructure(Deployable.getByName(name[0]), standLoc, name[2]);
                        for (BaseDeployable deployed : BaseDeployable.getDeployed()) {
                            if (MyUtil.isSameBlockLocation(deployed.getDeployLocation(), standLoc)) {
                                deployed.setCurrentHealth(Double.parseDouble(name[1]));
                            }
                        }

                    }
                }
            }.runTaskLater(CoreWeaponsAPI.getPlugin(), 1);
        }
    }

}
