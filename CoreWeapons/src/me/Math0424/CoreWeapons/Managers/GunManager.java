package me.Math0424.CoreWeapons.Managers;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.Data.PlayerData;
import me.Math0424.CoreWeapons.Events.GunEvents.GunFireEvent;
import me.Math0424.CoreWeapons.Events.GunEvents.GunReloadEvent;
import me.Math0424.CoreWeapons.Events.GunEvents.GunScopeEvent;
import me.Math0424.CoreWeapons.Guns.Attachments.Attachment;
import me.Math0424.CoreWeapons.Guns.Attachments.AttachmentModifier;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.NMS.NMSUtil;
import me.Math0424.CoreWeapons.Sound.SoundCache;
import me.Math0424.CoreWeapons.Sound.SoundSystem;
import me.Math0424.CoreWeapons.Util.DrawUtil;
import me.Math0424.CoreWeapons.Util.InventoryUtil;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class GunManager implements Listener {

    private static final HashMap<Player, Integer> holdingRightClick = new HashMap<>();
    private static final HashMap<Player, Gun> scoped = new HashMap<>();

    private static final ArrayList<Player> leftClicked = new ArrayList<>();
    private static final ArrayList<Player> reloading = new ArrayList<>();
    private static final ArrayList<Player> firing = new ArrayList<>();

    private static final ArrayList<Player> stopFire = new ArrayList<>();
    private static final HashMap<Player, Integer> disableFire = new HashMap<>();

    static {
        new BukkitRunnable() {
            Integer tick = 0;

            @Override
            public void run() {
                tick++;
                try {

                    List<Player> toRemove = new ArrayList<>();
                    for (Player p : holdingRightClick.keySet()) {
                        holdingRightClick.put(p, holdingRightClick.get(p) - 1);
                        if (holdingRightClick.get(p) <= 0)
                            toRemove.add(p);
                    }
                    toRemove.forEach(holdingRightClick.keySet()::remove);
                    toRemove.clear();

                    for (Player p : disableFire.keySet()) {
                        disableFire.put(p, disableFire.get(p) - 1);
                        if (disableFire.get(p) <= 0)
                            toRemove.add(p);
                    }
                    toRemove.forEach(disableFire.keySet()::remove);

                    for (Player p : scoped.keySet()) {
                        Gun gun = scoped.get(p);
                        if (gun.hasAttachments()) {
                            for (Attachment a : gun.getAttachments()) {
                                for(AttachmentModifier m : a.getModifiers().keySet()) {
                                    switch (m) {
                                        case ATTACHMENT_SCOPE -> p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3, a.getModifiers().get(m).intValue(), false, false, false), true);
                                        case ATTACHMENT_NIGHT_VISION -> p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 3, 1, false, false, false), true);
                                        case ATTACHMENT_LASER_SIGHT -> DrawUtil.drawColoredLine(p.getEyeLocation().subtract(0, .5, 0), p.getLocation().getDirection(), a.getModifiers().get(m), Color.RED, 1);
                                    }
                                }
                            }
                        } else {
                            scoped.remove(p);
                        }
                    }

                    for (Player p : reloading) {
                        MyUtil.sendActionBarMessage(p, ChatColor.RED + "" + ChatColor.BOLD + "Reloading...");
                    }

                } catch (Exception ignored) {
                }
            }
        }.runTaskTimer(CoreWeaponsAPI.getPlugin(), 0, 1);
    }

    @EventHandler
    private void playerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();

        if (e.getClickedBlock() != null && (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST)) {
            return;
        }

        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
            Container<Gun> cont = Container.getContainerItem(Gun.class, player.getItemInHand());
            if (cont == null)
                return;
            Gun gun = cont.getObject();

            if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
                leftClicked.remove(player);

                if (canGunFire(gun, player)) {
                    e.setCancelled(true);

                    if (gun.getOwner() == null)
                        gun.setOwner(player.getUniqueId());

                    switch (gun.getGunType()) {
                        case AUTOMATIC -> {
                            holdingRightClick.put(player, 5);
                            if (!firing.contains(player)) {
                                firing.add(player);
                                new BukkitRunnable() {
                                    public void run() {
                                        if (canRapidFire(gun, player)) {
                                            GunFireEvent fire = new GunFireEvent(cont, player);
                                            Bukkit.getPluginManager().callEvent(fire);
                                            if (!fire.isCancelled()) {
                                                shootGun(cont, player);
                                            }
                                        } else {
                                            firing.remove(player);
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(CoreWeaponsAPI.getPlugin(), 0, gun.getFireSpeed());
                            }
                        }
                        case SEMI_AUTOMATIC -> {
                            if (!holdingRightClick.containsKey(player)) {
                                GunFireEvent fire = new GunFireEvent(cont, player);
                                Bukkit.getPluginManager().callEvent(fire);
                                if (!fire.isCancelled()) {
                                    shootGun(cont, player);
                                }
                            }
                            if (holdingRightClick.get(player) == null || holdingRightClick.get(player) >= gun.getFireSpeed() - 5) {
                                holdingRightClick.put(player, gun.getFireSpeed());
                            }
                        }
                        case BURST_FIRE -> {
                            if (!holdingRightClick.containsKey(player)) {
                                gun.resetBurstRoundCount();
                            }
                            holdingRightClick.put(player, 5);
                            if (!firing.contains(player)) {
                                firing.add(player);
                                leftClicked.remove(player);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (gun.getMaxBurstCount() == 0)
                                            player.sendMessage("Gun has action 0 for burst count, it will not fire. set 'burstRoundCount' to something greater than 0");
                                        if (canRapidFire(gun, player) && gun.getMaxBurstCount() >= gun.getCurrentBurstCount()) {
                                            GunFireEvent fire = new GunFireEvent(cont, player);
                                            Bukkit.getPluginManager().callEvent(fire);
                                            if (!fire.isCancelled()) {
                                                shootGun(cont, player);
                                            }
                                        } else {
                                            firing.remove(player);
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(CoreWeaponsAPI.getPlugin(), 0, gun.getFireSpeed());
                            }
                        }
                    }
                }

            } else if (action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
                holdingRightClick.remove(player);

                if (canGunReload(gun, player)) {
                    e.setCancelled(true);
                    if (!leftClicked.contains(player)) {
                        leftClicked.add(player);
                    }
                    if (!reloading.contains(player)) {
                        reloading.add(player);
                        disableFire.put(player, 40);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (gun.equals(player.getItemInHand()) && leftClicked.contains(player) && canGunReload(gun, player)) {
                                    GunReloadEvent reload = new GunReloadEvent(gun, player);
                                    Bukkit.getPluginManager().callEvent(reload);
                                    if (!reload.isCancelled()) {
                                        int requestedRounds = Math.min(gun.getMaxShotCount() - gun.getCurrentShotCount(), gun.getBulletsDrawnPerReloadCycle());

                                        if (gun.getBulletsDrawnPerReloadCycle() == 0 || InventoryUtil.drawAmmo(player, gun.getAmmoID(), requestedRounds)) {
                                            gun.setCurrentShotCount(gun.getCurrentShotCount() + gun.getShotsAddedPerReloadCycle());

                                            ItemStackUtil.setItemDurability(cont.getItemStack(), gun.getMaxShotCount(), gun.getCurrentShotCount());
                                            cont.updateItemMapping();
                                        }
                                    }
                                } else {
                                    leftClicked.remove(player);
                                    reloading.remove(player);
                                    cancel();
                                }
                                if (gun.getCurrentShotCount() >= gun.getMaxShotCount()) {
                                    leftClicked.remove(player);
                                    reloading.remove(player);
                                    cancel();
                                }
                            }
                        }.runTaskTimer(CoreWeaponsAPI.getPlugin(), gun.getReloadSpeed(), gun.getReloadSpeed());
                    }
                }
            }
        }
    }

    private boolean canRapidFire(Gun g, Player p) {
        return g.equals(p.getItemInHand()) && canGunFire(g, p) && firing.contains(p) && holdingRightClick.containsKey(p);
    }

    private boolean canGunFire(Gun g, Player p) {
        return g != null && !reloading.contains(p) && !leftClicked.contains(p) &&
                !stopFire.contains(p) && !disableFire.containsKey(p) &&
                p.getGameMode() != GameMode.SPECTATOR && g.getCurrentShotCount() > 0;
    }

    private boolean canGunReload(Gun g, Player p) {
        return g != null && !firing.contains(p) && !holdingRightClick.containsKey(p) &&
                p.getGameMode() != GameMode.SPECTATOR && g.getCurrentShotCount() < g.getMaxShotCount() &&
                (g.getBulletsDrawnPerReloadCycle() == 0 || InventoryUtil.hasAmmoItem(p, g.getAmmoID()));
    }

    private void shootGun(Container<Gun> cont, Player player) {
        Gun gun = cont.getObject();

        gun.getBulletType().fire(player, cont);
        gun.fireOnce();

        PlayerData.getPlayerData(player.getUniqueId()).addTo("shots-fired", 1);

        Optional<Double> silencer = gun.getAttachmentValue(AttachmentModifier.ATTACHMENT_SILENCER);
        if (silencer.isPresent()) {
            SoundSystem.playSound(SoundCache.SILENCER, player.getLocation(), gun.getFirePitch() + (MyUtil.randomPosNeg(1) / 10f), silencer.get().intValue());
        } else {
            SoundSystem.playSound(gun.getFireSoundID(), player.getLocation(), gun.getFirePitch() + (MyUtil.randomPosNeg(1) / 10f), gun.getFireSoundRange());
        }

        cont.updateItemMapping();
    }

    @EventHandler
    private void playerToggleSneakEvent(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getItemInHand();
        if (!p.isSneaking()) {
            Container<Gun> gun = Container.getContainerItem(Gun.class, item);
            if (gun != null && !scoped.containsKey(p) && p.isOnGround()) {
                GunScopeEvent scope = new GunScopeEvent(p);
                Bukkit.getPluginManager().callEvent(scope);
                if (!scope.isCancelled()) {
                    scoped.put(p, gun.getObject());
                }
            }
        } else {
            scoped.remove(p);
        }
    }

    @EventHandler
    private void playerItemHeldEvent(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        leftClicked.remove(p);
        holdingRightClick.remove(p);
        reloading.remove(p);
        firing.remove(p);
        scoped.remove(p);
        disableFire.put(p, 20);
        ItemStack item = p.getInventory().getItem(e.getNewSlot());
        if (item != null && p.isSneaking()) {
            Container<Gun> gun = Container.getContainerItem(Gun.class, item);
            if (gun != null) {
                GunScopeEvent scope = new GunScopeEvent(p);
                Bukkit.getPluginManager().callEvent(scope);
                if (!scope.isCancelled()) {
                    scoped.put(p, gun.getObject());
                }
            }
        }
    }

    @EventHandler
    private void blockBreakEvent(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode() != GameMode.CREATIVE) {
            if (Container.getContainerItem(Gun.class, p.getItemInHand()) != null) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void playerItemDamageEvent(PlayerItemDamageEvent e) {
        ItemStack item = e.getItem();
        if (item.hasItemMeta() && Container.getContainerItem(Gun.class, item) != null) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    private void inventoryClickEvent(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (e.getCurrentItem() != null && e.getCursor() != null) {
                if (NMSUtil.NMS().GetModifiedStacks().contains(e.getCurrentItem().getType()) &&
                        (e.getCursor().getType() == e.getCurrentItem().getType() ||
                                e.getClick() == ClickType.SHIFT_RIGHT || e.getClick() == ClickType.SHIFT_LEFT)) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            p.updateInventory();
                        }
                    }.runTaskLater(CoreWeaponsAPI.getPlugin(), 1);
                }
            }
        }
    }


    public static void addToStopFire(Player p) {
        if (!stopFire.contains(p)) {
            stopFire.add(p);
        }
    }

    public static void removeFromStopFire(Player p) {
        stopFire.remove(p);
    }

    public static void addToDisableFire(Player p, int ticks) {
        disableFire.put(p, ticks);
    }

}