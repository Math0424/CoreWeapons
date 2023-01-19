package me.Math0424.CoreWeapons.Grenades.Types;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.Deployables.Types.BaseDeployable;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeExplodeEvent;
import me.Math0424.CoreWeapons.Grenades.Grenade.BaseGrenade;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import me.Math0424.CoreWeapons.Sound.SoundSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EmpGrenade extends BaseGrenade {


    public EmpGrenade(Player p, Container<Grenade> cont, Double strength) {
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
                    SoundSystem.playSound(grenade.getSoundID(), item.getLocation(), grenade.getThrowPitch(), grenade.getThrowVolume());
                    for (BaseDeployable dep : BaseDeployable.getDeployed()) {
                        if (dep != null && dep.getDeployLocation().distance(item.getLocation()) < grenade.getExplosiveYield()) {
                            dep.setDisable(30);
                        }
                    }
                }
                item.remove();
            }
        }.runTaskLater(CoreWeaponsAPI.getPlugin(), grenade.getExplodeTime());
    }
}