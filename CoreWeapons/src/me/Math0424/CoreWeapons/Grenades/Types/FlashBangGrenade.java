package me.Math0424.CoreWeapons.Grenades.Types;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeExplodeEvent;
import me.Math0424.CoreWeapons.Grenades.Grenade.BaseGrenade;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class FlashBangGrenade extends BaseGrenade {

    public FlashBangGrenade(Player p, Container<Grenade> cont, Double strength) {
        super(p, cont, strength);
    }

    @Override
    public void thrown(Item item) {
        new BukkitRunnable() {
            @Override
            public void run() {
                GrenadeExplodeEvent event = new GrenadeExplodeEvent(player, grenade, item);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    item.getWorld().playSound(item.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, (float)grenade.getThrowVolume(), grenade.getThrowPitch());
                    item.getWorld().spawnParticle(Particle.FLASH, item.getLocation(), 1);
                    for (Player nearby : item.getWorld().getPlayers()) {
                        if (nearby.getLocation().distance(item.getLocation()) < 40) {
                            if (nearby.hasLineOfSight(item)) {
                                double x = item.getLocation().getX() - nearby.getLocation().getX();
                                double z = item.getLocation().getZ() - nearby.getLocation().getZ();
                                double tan = Math.toDegrees(Math.atan2(x, z));

                                double yaw = nearby.getLocation().getYaw();

                                if (tan > 0) {
                                    tan = Math.abs(tan - 360);
                                } else {
                                    tan = Math.abs(tan);
                                }

                                if (yaw < 0) {
                                    yaw += 360;
                                }

                                if (tan + 60 >= yaw && tan - 60 <= yaw) {
                                    nearby.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 1, false, false, false), true);
                                }
                            }
                        }
                    }
                }
                item.remove();
            }
        }.runTaskLater(CoreWeaponsAPI.getPlugin(), grenade.getExplodeTime());
    }
}