package me.Math0424.CoreWeapons.Grenades.Types;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeExplodeEvent;
import me.Math0424.CoreWeapons.Grenades.Grenade.BaseGrenade;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class IncinerationGrenade extends BaseGrenade {


    public IncinerationGrenade(Player p, Container<Grenade> cont, Double strength) {
        super(p, cont, strength);
    }

    @Override
    public void thrown(Item item) {
        new BukkitRunnable() {
            int timesRun = 0;
            int size = 0;

            @Override
            public void run() {
                if (timesRun == 0) {
                    item.setVelocity(new Vector().zero());
                    item.setGravity(false);

                    //incineration field
                    GrenadeExplodeEvent event = new GrenadeExplodeEvent(player, grenade, item);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        cancel();
                        item.remove();
                        return;
                    }
                }

                if (timesRun % 2 == 0) {
                    for (Entity e : item.getLocation().getWorld().getNearbyEntities(item.getLocation(), size, size, size)) {
                        if (e instanceof LivingEntity && e.getLocation().distance(item.getLocation()) <= size) {
                            e.setFireTicks(200);
                            DamageUtil.setDamage(5.0, (LivingEntity) e, player, DamageExplainer.INCINERATIONGRENADE, true);
                            Vector direction = e.getLocation().toVector().subtract(item.getLocation().toVector()).normalize().add(new Vector(0, 1.5, 0));
                            e.setVelocity(direction);
                        }
                    }
                    for (Block block : MyUtil.generateBlockSphere(item.getLocation(), size, true)) {
                        Vector direction = block.getLocation().toVector().subtract(item.getLocation().toVector()).normalize().add(new Vector(0, 1.7, 0));
                        FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation(), block.getType(), block.getData());
                        block.breakNaturally();
                        fallingBlock.setDropItem(false);
                        fallingBlock.setVelocity(direction);
                    }
                    item.getWorld().playSound(item.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 2, 3);
                    size++;
                }

                if (item.isDead() || size > grenade.getExplosiveYield()) {
                    cancel();
                    item.remove();
                    return;
                }

                timesRun++;
            }
        }.runTaskTimer(CoreWeaponsAPI.getPlugin(), grenade.getExplodeTime(), 1);
    }


}
