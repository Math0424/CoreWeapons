package me.Math0424.CoreWeapons.Armor.Type;

import me.Math0424.CoreWeapons.Armor.Armor;
import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Events.ArmorEvents.PowerLegsUseEvent;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class PowerLegs extends BaseArmor {

    @Override
    public void useArmor(Container<Armor> cont, PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Armor armor = cont.getObject();

        Block below = player.getLocation().subtract(0, 1.5, 0).getBlock();

        if (canUseArmor(player, armor)) {
            player.setAllowFlight(!player.isOnGround() && (below.getType() != Material.AIR && !below.isLiquid()));
        } else if (player.getGameMode() != GameMode.CREATIVE) {
            player.setAllowFlight(false);
        }
    }

    @Override
    public void toggleFlight(Container<Armor> cont, PlayerToggleFlightEvent e) {
        Player player = e.getPlayer();
        Armor armor = cont.getObject();

        e.setCancelled(true);
        if (player.getGameMode() != GameMode.CREATIVE)
            player.setAllowFlight(false);
        player.setFlying(false);

        if (canUseArmor(player, armor)) {
            PowerLegsUseEvent hitEvent = new PowerLegsUseEvent(this, player);
            Bukkit.getPluginManager().callEvent(hitEvent);
            if (!hitEvent.isCancelled()) {

                player.setFallDistance(-500);
                player.setVelocity(player.getLocation().getDirection().multiply(armor.getAcceleration()).setY(armor.getMaxSpeed()));
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 2f, 1f);
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 2f, 1f);

                armor.setCurrentUses(armor.getCurrentUses() - 1);
                cont.updateItemMapping();
                ItemStackUtil.setItemDurability(cont.getItemStack(), armor.getMaxUses(), armor.getCurrentUses());
            }
        }

    }

}