package me.Math0424.CoreWeapons.Grenades.Types;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeExplodeEvent;
import me.Math0424.CoreWeapons.Grenades.Grenade.BaseGrenade;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ImpactGrenade extends BaseGrenade {

    public ImpactGrenade(Player p, Container<Grenade> cont, Double strength) {
        super(p, cont, strength);
    }

    @Override
    public void thrown(Item item) {
        new BukkitRunnable() {
            int timesRun = 0;

            @Override
            public void run() {
                timesRun++;
                int x = item.getLocation().getBlockX();
                int z = item.getLocation().getBlockZ();
                int y = item.getLocation().getBlockY();

                Block b1 = new Location(item.getWorld(), x + 1, y, z).getBlock();
                Block b2 = new Location(item.getWorld(), x - 1, y, z).getBlock();
                Block b3 = new Location(item.getWorld(), x, y, z + 1).getBlock();
                Block b4 = new Location(item.getWorld(), x, y, z - 1).getBlock();
                Block b5 = new Location(item.getWorld(), x, y - 1, z).getBlock();

                if (b1.getType() != Material.AIR || b2.getType() != Material.AIR || b3.getType() != Material.AIR
                        || b4.getType() != Material.AIR || b5.getType() != Material.AIR) {
                    if (!(b1.isLiquid() || b2.isLiquid() || b3.isLiquid() || b4.isLiquid() || b5.isLiquid())) {

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

                if (timesRun == 300 || item.isDead()) {
                    cancel();
                    item.remove();
                }
            }
        }.runTaskTimer(CoreWeaponsAPI.getPlugin(), 5, 1);
    }
}
