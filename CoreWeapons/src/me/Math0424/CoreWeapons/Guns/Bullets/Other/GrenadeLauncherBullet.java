package me.Math0424.CoreWeapons.Guns.Bullets.Other;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.ParticleBullet;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class GrenadeLauncherBullet extends ParticleBullet {

    public GrenadeLauncherBullet(LivingEntity shooter, Container<Gun> cont, Gun gun, double accuracyMultiplier) {
        super(shooter, cont, gun, accuracyMultiplier);

        if (shooter instanceof Player p) {
            PlayerInventory inv = p.getInventory();

            if (inv.getItemInOffHand().getType() != Material.AIR) {
                ItemStack item = inv.getItemInOffHand();
                Container<Grenade> gcont = Container.getContainerItem(Grenade.class, item);
                if (gcont != null) {
                    Grenade g = gcont.getObject();
                    g.getGrenadeType().throwGrenade(p, gcont, gun.getBulletSpeed());
                    item.setAmount(item.getAmount() - 1);
                    return;
                }
            }

            for (int i = 0; i < inv.getSize(); i++) {
                if (inv.getItem(i) != null) {
                    ItemStack item = inv.getItem(i);
                    Container<Grenade> gcont = Container.getContainerItem(Grenade.class, item);
                    if (gcont != null) {
                        Grenade g = gcont.getObject();
                        g.getGrenadeType().throwGrenade(p, gcont, gun.getBulletSpeed());
                        item.setAmount(item.getAmount() - 1);
                        return;
                    }
                }
            }
        }
    }

}
