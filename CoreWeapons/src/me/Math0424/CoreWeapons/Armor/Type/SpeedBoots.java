package me.Math0424.CoreWeapons.Armor.Type;

import me.Math0424.CoreWeapons.Armor.Armor;
import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Events.ArmorEvents.ArmorFailEvent;
import me.Math0424.CoreWeapons.Events.ArmorEvents.ArmorFailReason;
import me.Math0424.CoreWeapons.Events.ArmorEvents.SpeedBootsUseEvent;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class SpeedBoots extends BaseArmor {

    @Override
    public void useArmor(Container<Armor> cont, PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Armor armor = cont.getObject();

        if (player.isOnGround() && player.isSprinting() && canUseArmor(player, armor)) {

            double x = Math.min(armor.getMaxSpeed(), player.getVelocity().getX() * armor.getAcceleration());
            double z = Math.min(armor.getMaxSpeed(), player.getVelocity().getZ() * armor.getAcceleration());

            SpeedBootsUseEvent hitEvent = new SpeedBootsUseEvent(this, player);
            Bukkit.getPluginManager().callEvent(hitEvent);
            if (!hitEvent.isCancelled()) {

                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_COW_STEP, 2f, 1f);
                player.setVelocity(new Vector(x, 0, z));

                armor.setCurrentUses(armor.getCurrentUses() - 1);
                cont.updateItemMapping();
                ItemStackUtil.setItemDurability(cont.getItemStack(), armor.getMaxUses(), armor.getCurrentUses());

                if (armor.getCurrentUses() == 120) {
                    ArmorFailEvent event = new ArmorFailEvent(armor, player, ArmorFailReason.ALMOSTOUT);
                    Bukkit.getPluginManager().callEvent(event);
                }

            }
        }
    }
}