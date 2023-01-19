package me.Math0424.CoreWeapons.Managers;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.DamageHandler.CoreDamage;
import me.Math0424.CoreWeapons.Data.PlayerData;
import me.Math0424.CoreWeapons.Events.CoreEvents.ContainerUpdateEvent;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import me.Math0424.CoreWeapons.Guns.Ammo.Ammo;
import me.Math0424.CoreWeapons.Guns.Attachments.Attachment;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Guns.QualityEnum;
import me.Math0424.CoreWeapons.NMS.NMSUtil;
import me.Math0424.CoreWeapons.Resourcepack.ResourcepackManager;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class CoreListeners implements Listener, CommandExecutor {

    private static boolean isInit;

    private ArmorListeners arml;
    private DeployableListeners depl;
    private GrenadeListener grel;
    private GunManager gunl;

    public CoreListeners(Plugin p) {
        if (!isInit && p instanceof CoreWeaponsAPI) {
            isInit = true;

            gunl = new GunManager();
            Bukkit.getServer().getPluginManager().registerEvents(gunl, p);

            grel = new GrenadeListener();
            Bukkit.getServer().getPluginManager().registerEvents(grel, p);

            depl = new DeployableListeners();
            Bukkit.getServer().getPluginManager().registerEvents(depl, p);

            arml = new ArmorListeners();
            Bukkit.getServer().getPluginManager().registerEvents(arml, p);

            Bukkit.getServer().getPluginManager().registerEvents(this, p);
            CoreWeaponsAPI.getPlugin().getCommand("coreweapons").setExecutor(this);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void containerUpdateEvent(ContainerUpdateEvent e) {
        if (!e.isCancelled()) {
            Container cont = e.getContainer();
            Object clazz = cont.getObject();

            ItemMeta meta = cont.getItemStack().getItemMeta();
            if (clazz instanceof Gun gun) {
                ItemStackUtil.setItemDurability(cont.getItemStack(), gun.getMaxShotCount(), gun.getCurrentShotCount());

                meta = cont.getItemStack().getItemMeta();
                meta.setDisplayName(ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', gun.getName()));

                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.LIGHT_PURPLE + "Quality: " + MyUtil.capitalize(QualityEnum.closest(gun.getQuality()).toString()));
                lore.add(ChatColor.LIGHT_PURPLE + "Ammo: " + ChatColor.YELLOW + gun.getAmmoID());
                lore.add(ChatColor.YELLOW + (gun.isPrimaryGun() ? "Primary" : "Secondary"));
                lore.add( ChatColor.YELLOW + MyUtil.capitalize(gun.getGunType().toString()));

                if (gun.hasAttachments()) {
                    lore.add(ChatColor.AQUA + "Attachments:");
                    for (Attachment a : gun.getAttachments()) {
                        lore.add(ChatColor.AQUA + " - " + ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', a.getName())));
                    }
                }
                meta.setLore(lore);

            } else if (clazz instanceof Grenade grenade) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', grenade.getName()));
            } else if (clazz instanceof Ammo ammo) {
                ItemStackUtil.setItemDurability(cont.getItemStack(), ammo.getMaxBulletCount(), ammo.getBulletCount());

                meta = cont.getItemStack().getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ammo.getAmmoId()));
                if (ammo.getMaxBulletCount() != 0) {
                    meta.setLore(Collections.singletonList(ChatColor.LIGHT_PURPLE + "Rounds: " + ammo.getBulletCount() + "/" + ammo.getMaxBulletCount()));
                }
            }
            cont.getItemStack().setItemMeta(meta);

        }

    }

    @EventHandler
    public void entityDeathEvent(EntityDeathEvent e) {
        CoreDamage damage = CoreDamage.getLastDamage(e.getEntity());
        if (damage != null && damage.getGun() != null) {
            damage.getGun().getObject().addToEntityKills();
            if (e.getEntity() instanceof Player)
                damage.getGun().getObject().addToPlayerKills();
            damage.getGun().updateItemMapping();
        }

    }

    @EventHandler
    public void playerLoginEvent(PlayerLoginEvent e) {
        if (!ResourcepackManager.isReady()) {
            e.setKickMessage(ChatColor.RED + "Please wait while the ResourcePack is assembled!");
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player p && strings != null && strings.length == 1) {
            if (strings[0].equalsIgnoreCase("resourcepack")) {
                p.setResourcePack(NMSUtil.NMS().GetServerResourcePackUrl());
            }
        }
        return false;
    }

    @EventHandler
    public void playerResourcePackStatusEvent(PlayerResourcePackStatusEvent e) {
        if (ResourcepackManager.isReady()) {
            switch (e.getStatus()) {
                case FAILED_DOWNLOAD, DECLINED -> {
                    e.getPlayer().sendMessage(ChatColor.RED + "Enable server ResourcePack option in your client server settings!");
                    e.getPlayer().sendMessage(ChatColor.RED + "Failure to load ResourcePack will severely diminish your gameplay experience!");

                    TextComponent message = new TextComponent("Click me to apply ResourcePack");
                    message.setUnderlined(true);
                    message.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/coreweapons resourcepack"));

                    e.getPlayer().spigot().sendMessage(message);
                    e.getPlayer().sendTitle(ChatColor.RED + "No resourcepack!", ChatColor.RED + "You do not have the resourcepack!", 10, 100, 10);
                }
            }
        }
    }

    @EventHandler
    private void onPlayerQuitEvent(PlayerQuitEvent e) {
        if (PlayerData.isPlayerDataLoaded(e.getPlayer())) {
            PlayerData.unloadPlayerData(e.getPlayer());
        }
    }

}
