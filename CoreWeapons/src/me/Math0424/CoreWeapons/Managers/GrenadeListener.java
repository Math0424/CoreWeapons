package me.Math0424.CoreWeapons.Managers;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeThrowEvent;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

class GrenadeListener implements Listener {

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e) {
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {

            Player p = e.getPlayer();
            Action a = e.getAction();
            if (a == Action.RIGHT_CLICK_BLOCK || a == Action.RIGHT_CLICK_AIR) {
                if (e.getClickedBlock() != null && (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST)) {
                    return;
                }

                ItemStack item = p.getItemInHand();
                Container<Grenade> cont = Container.getContainerItem(Grenade.class, item);
                if (cont != null) {
                    e.setCancelled(true);
                    GrenadeThrowEvent event = new GrenadeThrowEvent(cont.getObject(), p);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        cont.getObject().getGrenadeType().throwGrenade(p, cont);
                        item.setAmount(item.getAmount() - 1);
                    }
                }
            }

        }
    }

}
