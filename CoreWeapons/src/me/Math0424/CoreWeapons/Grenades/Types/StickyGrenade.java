package me.Math0424.CoreWeapons.Grenades.Types;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeExplodeEvent;
import me.Math0424.CoreWeapons.Grenades.Grenade.BaseGrenade;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class StickyGrenade extends BaseGrenade {

    private Vector offset;
    private Block stuckToBlock;
    private Entity stuckToEntity;

    public StickyGrenade(Player p, Container<Grenade> cont, Double strength) {
        super(p, cont, strength);
    }

    @Override
    public void thrown(Item item) {
        new BukkitRunnable() {
            int timesRun = 0;

            @Override
            public void run() {
                timesRun++;

                if (stuckToBlock != null || stuckToEntity != null) {
                    if (stuckToBlock != null) {
                        item.teleport(stuckToBlock.getLocation().add(offset));
                    } else {
                        item.teleport(stuckToEntity.getLocation().add(offset));
                        item.setVelocity(stuckToEntity.getVelocity());
                    }
                } else if (item.getVelocity().lengthSquared() != 0) {
                    RayTraceResult res = item.getWorld().rayTrace(item.getLocation(), item.getVelocity().normalize(), .3, FluidCollisionMode.NEVER, true, 2, entity -> entity != item);
                    if (res != null) {
                        if (res.getHitEntity() != null) {
                            stuckToEntity = res.getHitEntity();
                            item.setGravity(false);
                            offset = item.getLocation().toVector().subtract(stuckToEntity.getLocation().toVector());
                        } else if (res.getHitBlock() != null) {
                            stuckToBlock = res.getHitBlock();
                            item.setGravity(false);
                            offset = item.getLocation().toVector().subtract(stuckToBlock.getLocation().toVector());
                        }
                    }
                }

                if (timesRun == grenade.getExplodeTime() || item.isDead()) {
                    GrenadeExplodeEvent event = new GrenadeExplodeEvent(player, grenade, item);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        DamageUtil.setExplosionDamage(item.getLocation(), 4, player, null, DamageExplainer.IMPACTGRENADE);
                        item.getWorld().createExplosion(item.getLocation().getX(), item.getLocation().getY() + 1, item.getLocation().getZ(), grenade.getExplosiveYield(), false, true);
                    }
                    item.remove();
                    cancel();
                }
            }
        }.runTaskTimer(CoreWeaponsAPI.getPlugin(), 5, 1);
    }


}
