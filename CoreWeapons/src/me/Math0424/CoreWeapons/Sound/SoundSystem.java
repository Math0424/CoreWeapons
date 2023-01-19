package me.Math0424.CoreWeapons.Sound;

import me.Math0424.CoreWeapons.Sound.Types.AdvancedSound;
import me.Math0424.CoreWeapons.Sound.Types.EasyRangedSound;
import me.Math0424.CoreWeapons.Sound.Types.SimpleSound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SoundSystem {

    private static final Map<String, AdvancedSound> sounds = new HashMap<>();

    private static final ArrayList<Material> softBlocks = new ArrayList<>();

    static {
        softBlocks.addAll(Tag.SAND.getValues());
        softBlocks.addAll(Tag.CARPETS.getValues());
        softBlocks.addAll(Tag.WOOL.getValues());
        softBlocks.addAll(Tag.BAMBOO_PLANTABLE_ON.getValues());
        softBlocks.addAll(Tag.BEDS.getValues());
        softBlocks.add(Material.GRASS_BLOCK);
        softBlocks.add(Material.DIRT_PATH);
        softBlocks.add(Material.BONE);
        softBlocks.add(Material.COAL_BLOCK);

        registerSound(new EasyRangedSound(SoundCache.BULLET_NUKE_HIT.getSoundID(), "nuke_bullet"));
        registerSound(new EasyRangedSound(SoundCache.SILENCER.getSoundID(), "silenced_automatic"));

        registerSound(new SimpleSound(SoundCache.BULLET_ELECTRIC_HIT.getSoundID(), "electric_hit"));
        registerSound(new SimpleSound(SoundCache.BULLET_IMPACT.getSoundID(), "bullet_impact"));
        registerSound(new SimpleSound(SoundCache.EMPTY_CHAMBER.getSoundID(), "empty_chamber"));
    }

    public static void registerSound(AdvancedSound sound) {
        sounds.put(sound.soundID, sound);
    }


    public static void playSound(SoundCache sound, Location location, float pitch, int volume) {
        playSound(sound.getSoundID(), location, pitch, volume);
    }

    public static void playSound(SoundCache sound, Location location, Player p, float pitch, double volume) {
        playSound(sound.getSoundID(), location, p, pitch, volume);
    }

    public static void playSound(String soundID, Location location, float pitch, double volume) {
        if (sounds.containsKey(soundID)) {
            sounds.get(soundID).Play(location, pitch, (int)volume);
        } else {
            try {
                location.getWorld().playSound(location, Sound.valueOf(soundID), (float) volume, pitch);
            } catch (Exception ignored) {
                location.getWorld().playSound(location, soundID, (float) volume, pitch);
            }
        }
    }

    public static void playSound(String soundID, Location location, Player player, float pitch, double volume) {
        if (sounds.containsKey(soundID)) {
            sounds.get(soundID).Play(player, location, pitch, (int)volume);
        } else {
            try {
                player.playSound(location, Sound.valueOf(soundID), (float) volume, pitch);
            } catch (Exception ignored) {
                player.playSound(location, soundID, (float) volume, pitch);
            }
        }
    }

    public static void playSound(String soundID, Location location, Player player) {
        playSound(soundID, location, player, 1, 1);
    }

}
