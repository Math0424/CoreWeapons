package me.Math0424.CoreWeapons.Grenades.Types;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeExplodeEvent;
import me.Math0424.CoreWeapons.Grenades.Grenade.BaseGrenade;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import me.Math0424.CoreWeapons.Sound.SoundSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BasicGrenade extends BaseGrenade {


    public BasicGrenade(Player p, Container<Grenade> cont, Double strength) {
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
                        DamageUtil.setExplosionDamage(item.getLocation(), 4, player, null, DamageExplainer.THROWABLEGRENADE);
                        item.getWorld().createExplosion(item.getLocation().getX(), item.getLocation().getY() + 1, item.getLocation().getZ(), grenade.getExplosiveYield(), false, true, item);
                    }
                    item.remove();
                    cancel();
                    return;
                }
                time--;
                if (item.isDead()) {
                    cancel();
                }
            }
        }.runTaskTimer(CoreWeaponsAPI.getPlugin(), 0, 1);
    }
}
