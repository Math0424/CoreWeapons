package me.Math0424.CoreWeapons.Managers;

import me.Math0424.CoreWeapons.Armor.Armor;
import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.Util.InventoryUtil;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

class ArmorListeners implements Listener {

    ArrayList<Player> reloading = new ArrayList<>();

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e) {
        for (ItemStack i : e.getPlayer().getEquipment().getArmorContents()) {
            if (i != null && i.hasItemMeta()) {
                Container<Armor> cont = Container.getContainerItem(Armor.class, i);
                if (cont != null) {
                    cont.getObject().getArmorType().useArmor(cont, e);
                }
            }
        }
    }

    @EventHandler
    public void playerToggleFlightEvent(PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();

        for (ItemStack i : e.getPlayer().getEquipment().getArmorContents()) {
            if (i != null && i.hasItemMeta()) {
                Container<Armor> cont = Container.getContainerItem(Armor.class, i);
                if (cont != null) {
                    cont.getObject().getArmorType().toggledFlight(cont, e);
                }
            }
        }

        if (p.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
            e.getPlayer().setAllowFlight(false);
        }
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action a = e.getAction();

        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
            if (a == Action.LEFT_CLICK_BLOCK || a == Action.LEFT_CLICK_AIR) {

                ItemStack item = player.getItemInHand();
                Container<Armor> cont = Container.getContainerItem(Armor.class, item);
                if (cont == null)
                    return;
                Armor armor = cont.getObject();

                if (canArmorReload(player, cont.getObject())) {
                    e.setCancelled(true);

                    if (!reloading.contains(player)) {
                        reloading.add(player);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (reloading.contains(player) && canArmorReload(player, armor)) {

                                    if (InventoryUtil.drawAmmo(player, armor.getAmmoID(), 1)) {
                                        armor.setCurrentUses(armor.getCurrentUses() + armor.getUsesFixedPerReload());

                                        cont.updateItemMapping();
                                        ItemStackUtil.setItemDurability(cont.getItemStack(), armor.getMaxUses(), armor.getCurrentUses());
                                    }

                                } else {
                                    reloading.remove(player);
                                    cancel();
                                }
                                if (armor.getCurrentUses() == armor.getMaxUses()) {
                                    reloading.remove(player);
                                    cancel();
                                }
                            }
                        }.runTaskTimer(CoreWeaponsAPI.getPlugin(), armor.getFixTime(), armor.getFixTime());
                    }

                }
            }
        }
    }

    public boolean canArmorReload(Player p, Armor armor) {
        return armor.equals(p.getItemInHand()) && armor.getMaxUses() > armor.getCurrentUses() && InventoryUtil.hasAmmoItem(p, armor.getAmmoID());
    }

}
