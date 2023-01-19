package me.Math0424.CoreWeapons.Util;

import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

public class MyUtil {

    static Random rand = new Random();

    public static String getIp() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static float random() {
        return (float) rand.nextDouble();
    }

    public static int random(int i) {
        if (i < 0) {
            return 0;
        }
        return rand.nextInt(i);
    }

    public static int randomPosNeg(int num) {
        return random(num + 1 + num) - num;
    }

    public static boolean isSameBlockLocation(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockY() == loc2.getBlockY() && loc1.getBlockZ() == loc2.getBlockZ();
    }

    public static boolean hasAvaliableSlot(Player p, ItemStack item) {
        Inventory inv = p.getInventory();

        for (int i = 0; i <= 35; i++) {
            if (inv.getItem(i) == null || ItemStackUtil.isSimilarNameType(item, inv.getItem(i))) {
                return true;
            }
        }
        return false;
    }

    public static void log(Level l, String message) {
        Bukkit.getServer().getLogger().log(l, ChatColor.YELLOW + "[CoreWeapons] " + ChatColor.RESET + message);
    }

    public static void log(Level l, String sender, String message) {
        Bukkit.getServer().getLogger().log(l, ChatColor.YELLOW + "[" + sender + "] " + ChatColor.RESET + message);
    }

    public static void sendActionBarMessage(Player p, String text) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
    }

    public static List<Block> generateBlockSphere(Location center, int radius, boolean hollow) {
        List<Block> blocks = new ArrayList<Block>();
        int bx = center.getBlockX();
        int by = center.getBlockY();
        int bz = center.getBlockZ();
        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        Block b = center.getWorld().getBlockAt(x, y, z);
                        if (!b.isEmpty()) {
                            if (b.isLiquid()) {
                                b.setBlockData(Material.AIR.createBlockData());
                                continue;
                            }
                            if (!CoreWeaponsAPI.getPlugin().getUnBreakables().contains(b.getType())) {
                                blocks.add(b);
                            }
                        }
                    }
                }
            }
        }
        return blocks;
    }

    public static boolean isChunkLoaded(Location loc) {
        return loc.getWorld().isChunkLoaded((int) loc.getX() / 16, (int) loc.getZ() / 16);
    }

    public static boolean isNearbyChunkUnLoaded(Location loc) {
        return !loc.getWorld().isChunkLoaded((int) loc.getX() / 16, (int) loc.getZ() / 16) ||
                !loc.getWorld().isChunkLoaded((int) (loc.getX() + 32) / 16, (int) (loc.getZ() + 32) / 16) ||
                !loc.getWorld().isChunkLoaded((int) (loc.getX() - 32) / 16, (int) (loc.getZ() - 32) / 16) ||
                !loc.getWorld().isChunkLoaded((int) (loc.getX() - 32) / 16, (int) (loc.getZ() + 32) / 16) ||
                !loc.getWorld().isChunkLoaded((int) (loc.getX() + 32) / 16, (int) (loc.getZ() - 32) / 16);
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return (str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase()).replace("_", "-");
    }

    public static Map<String, Object> deserializeMap(String mapStr) {
        Map<String, Object> returned = new HashMap<>();
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(FileUtil.decompress(Base64Coder.decodeLines(mapStr.replace(":", System.getProperty("line.separator")))));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            returned = (Map<String, Object>) dataInput.readObject();
            inputStream.close();
            dataInput.close();
        } catch (Exception ignored) {}
        return returned;
    }

    public static String serializeMap(Map<String, Object> map) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(map);
            dataOutput.close();
            outputStream.close();
            return Base64Coder.encodeLines(FileUtil.compress(outputStream.toByteArray())).replace(System.getProperty("line.separator"), ":");
        } catch (Exception ignored) {}
        return null;
    }


}
