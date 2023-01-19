package me.Math0424.CoreWeapons.Grenades.Types;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.EntityInSingularityEvent;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeExplodeEvent;
import me.Math0424.CoreWeapons.Grenades.Grenade.BaseGrenade;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class SingularityGrenade extends BaseGrenade {

    private static final HashMap<Player, Integer> inSingularity = new HashMap<Player, Integer>();

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    for (Player p : inSingularity.keySet()) {
                        inSingularity.put(p, inSingularity.get(p) - 1);
                        if (inSingularity.get(p) == 0) {
                            inSingularity.remove(p);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }.runTaskTimer(CoreWeaponsAPI.getPlugin(), 1, 1);
    }

    public SingularityGrenade(Player p, Container<Grenade> cont, Double strength) {
        super(p, cont, strength);
    }


    @Override
    public void thrown(Item item) {
        GrenadeExplodeEvent event = new GrenadeExplodeEvent(player, grenade, item);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        new BukkitRunnable() {
            int timesRun = 0;
            final ArrayList<FallingBlock> blocks = new ArrayList<FallingBlock>();

            @Override
            public void run() {

                if (timesRun < 95) {
                    for (Entity e : item.getNearbyEntities(8, 8, 8)) {
                        if (!e.equals(item) && !blocks.contains(e)) {
                            EntityInSingularityEvent event = new EntityInSingularityEvent(e);
                            Bukkit.getPluginManager().callEvent(event);
                            e.setVelocity(new Vector(0, .04f, 0));
                            if (e instanceof Player) {
                                inSingularity.put((Player) e, 5);
                            }
                        }
                    }
                }

                if (timesRun == 0) {
                    item.getWorld().playSound(item.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 3, .8f);
                    item.setVelocity(new Vector().zero());
                    item.setGravity(false);

                    //sphere 1
                    for (Block block : MyUtil.generateBlockSphere(item.getLocation(), 3, false)) {
                        FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation(), block.getType(), block.getData());
                        block.setType(Material.AIR);
                        fallingBlock.setDropItem(false);
                        fallingBlock.setGravity(false);
                        fallingBlock.setVelocity(new Vector(0, .09f, 0));
                        blocks.add(fallingBlock);
                    }
                    //sphere 2
                    for (Block block : MyUtil.generateBlockSphere(item.getLocation(), 4, false)) {
                        FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation(), block.getType(), block.getData());
                        block.setType(Material.AIR);
                        fallingBlock.setDropItem(false);
                        fallingBlock.setGravity(false);
                        fallingBlock.setVelocity(new Vector(0, .07f, 0));
                        blocks.add(fallingBlock);
                    }
                    //sphere 3
                    for (Block block : MyUtil.generateBlockSphere(item.getLocation(), 5, false)) {
                        FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation(), block.getType(), block.getData());
                        block.setType(Material.AIR);
                        fallingBlock.setDropItem(false);
                        fallingBlock.setGravity(false);
                        fallingBlock.setVelocity(new Vector(0, .05f, 0));
                        blocks.add(fallingBlock);
                    }
                } else if (timesRun == 95) {
                    //prep the entities for movement
                    for (FallingBlock b : blocks) {
                        b.setGravity(true);
                    }
                    for (Entity e : item.getWorld().getNearbyEntities(item.getLocation(), 8, 8, 8)) {
                        if (!e.equals(item)) {
                            Vector direction = item.getLocation().toVector().subtract(e.getLocation().toVector()).normalize();
                            e.setVelocity(direction);
                            EntityInSingularityEvent event = new EntityInSingularityEvent(e);
                            Bukkit.getPluginManager().callEvent(event);
                            if (e instanceof Player) {
                                inSingularity.put((Player) e, 5);
                            }
                        }
                    }
                } else if (timesRun == 100) {
                    GrenadeExplodeEvent event = new GrenadeExplodeEvent(player, grenade, item);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        item.getWorld().playSound(item.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 4, 1);
                        item.getWorld().playSound(item.getLocation(), Sound.ITEM_TOTEM_USE, 4, 2);
                        DamageUtil.setExplosionDamage(item.getLocation(), 4, player, null, DamageExplainer.SINGULARITYGRENADE);
                        item.getWorld().createExplosion(item.getLocation().getX(), item.getLocation().getY() + 1, item.getLocation().getZ(), grenade.getExplosiveYield(), false, true);
                    }
                    item.remove();
                    cancel();
                    return;
                } else if (item.isDead()) {
                    for (FallingBlock b : blocks) {
                        b.setGravity(true);
                    }
                    cancel();
                    return;
                }

                timesRun++;
            }
        }.runTaskTimer(CoreWeaponsAPI.getPlugin(), grenade.getExplodeTime(), 1);
    }

    public static HashMap<Player, Integer> getInSingularity() {
        return inSingularity;
    }

}
