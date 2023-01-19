package me.Math0424.CoreWeapons.Armor.Type;

import me.Math0424.CoreWeapons.Armor.Armor;
import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Events.ArmorEvents.ArmorFailEvent;
import me.Math0424.CoreWeapons.Events.ArmorEvents.ArmorFailReason;
import me.Math0424.CoreWeapons.Events.ArmorEvents.JetpackUseEvent;
import me.Math0424.CoreWeapons.Grenades.Types.SingularityGrenade;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class Jetpack extends BaseArmor {

    @Override
    public void useArmor(Container<Armor> cont, PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Armor armor = cont.getObject();

        if (!SingularityGrenade.getInSingularity().containsKey(player) &&
                !player.isOnGround() && player.isSneaking() && canUseArmor(player, armor)) {
            if (e.getTo().getY() > e.getFrom().getY() || e.getTo().getY() < e.getFrom().getY()) {

                double y = player.getVelocity().getY();
                double x = player.getLocation().getDirection().getX();
                double z = player.getLocation().getDirection().getZ();

                double y1;
                double x1;
                double z1;

                if (y > armor.getMaxSpeed()) {
                    y1 = y;
                } else {
                    y1 = y + armor.getAcceleration();
                }

                x1 = x + x;
                z1 = z + z;

                JetpackUseEvent hitEvent = new JetpackUseEvent(this, player);
                Bukkit.getPluginManager().callEvent(hitEvent);
                if (!hitEvent.isCancelled()) {

                    player.setVelocity(new Vector(x1, y1, z1));
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 3f, 2f);
                    player.setFallDistance(0);

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
}