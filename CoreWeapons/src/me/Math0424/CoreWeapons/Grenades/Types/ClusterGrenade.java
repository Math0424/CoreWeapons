package me.Math0424.CoreWeapons.Grenades.Types;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeExplodeEvent;
import me.Math0424.CoreWeapons.Grenades.Grenade.BaseGrenade;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import me.Math0424.CoreWeapons.Sound.SoundSystem;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class ClusterGrenade extends BaseGrenade {

    private final ArrayList<Item> items = new ArrayList<>();

    public ClusterGrenade(Player p, Container<Grenade> cont, Double strength) {
        super(p, cont, strength);
    }

    @Override
    public void thrown(Item item) {
        new BukkitRunnable() {
            int time = grenade.getExplodeTime();

            @Override
            public void run() {
                if (time == grenade.getExplodeTime() * (3.0 / 4.0)) {
                    SoundSystem.playSound(grenade.getSoundID(), item.getLocation(), grenade.getThrowPitch(), grenade.getThrowVolume());
                } else if (time == grenade.getExplodeTime() * (2.0 / 4.0)) {
                    SoundSystem.playSound(grenade.getSoundID(), item.getLocation(), grenade.getThrowPitch(), grenade.getThrowVolume());
                } else if (time == grenade.getExplodeTime() * (1.0 / 4.0)) {
                    SoundSystem.playSound(grenade.getSoundID(), item.getLocation(), grenade.getThrowPitch(), grenade.getThrowVolume());
                } else if (time == 0) {
                    GrenadeExplodeEvent event = new GrenadeExplodeEvent(player, grenade, item);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        SoundSystem.playSound(grenade.getSoundID(), item.getLocation(), grenade.getThrowPitch(), grenade.getThrowVolume());
                        for (int i = 0; i <= 4; i++) {
                            Item spawn = player.getWorld().dropItem(item.getLocation(), ItemStackUtil.createItemStack(Material.TNT, String.valueOf(random())));
                            spawn.setVelocity(new Vector(random(), .5, random()));
                            spawn.setPickupDelay(10000);
                            items.add(spawn);
                        }
                    }
                    item.remove();
                } else if (time == -40) {
                    for (Item item : items) {
                        if (!item.isDead()) {
                            DamageUtil.setExplosionDamage(item.getLocation(), 4, player, null, DamageExplainer.CLUSTERGRENADE);
                            item.getWorld().createExplosion(item.getLocation().getX(), item.getLocation().getY() + 1, item.getLocation().getZ(), grenade.getExplosiveYield(), false, true, item);
                            item.remove();
                        }
                    }
                    cancel();
                    return;
                }
                time--;
            }
        }.runTaskTimer(CoreWeaponsAPI.getPlugin(), 0, 1);
    }

}
