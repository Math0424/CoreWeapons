package me.Math0424.CoreWeapons.Grenades.Types;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeExplodeEvent;
import me.Math0424.CoreWeapons.Grenades.Grenade.BaseGrenade;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class SmokeGrenade extends BaseGrenade {


    public SmokeGrenade(Player p, Container<Grenade> cont, Double strength) {
        super(p, cont, strength);
    }

    @Override
    public void thrown(Item item) {
        new BukkitRunnable() {
            int time = 0;

            @Override
            public void run() {
                GrenadeExplodeEvent event = new GrenadeExplodeEvent(player, grenade, item);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    for (Entity e : item.getNearbyEntities(grenade.getExplosiveYield(), grenade.getExplosiveYield(), grenade.getExplosiveYield())) {
                        if (e instanceof Player) {
                            ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1, false, false, false), true);
                        }
                    }
                    item.getWorld().spawnParticle(Particle.REDSTONE, item.getLocation(), 20, grenade.getExplosiveYield() - 2, grenade.getExplosiveYield() - 2, grenade.getExplosiveYield() - 2, 0, new DustOptions(Color.BLACK, 10f), true);
                    item.getWorld().playSound(item.getLocation(), Sound.UI_TOAST_OUT, 2, 2);
                }

                time++;
                if (time > 20 * 15 || item.isDead()) {
                    item.remove();
                    cancel();
                    return;
                }
            }
        }.runTaskTimer(CoreWeaponsAPI.getPlugin(), grenade.getExplodeTime(), 1);

    }

}
