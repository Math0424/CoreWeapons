package me.Math0424.CoreWeapons.Sound.Types;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RangedSound extends AdvancedSound {

    private final String shortSound;
    private final String mediumSound;
    private final String longSound;

    public RangedSound(String soundID, String shortSound, String mediumSound, String longSound) {
        super(soundID);
        this.shortSound = shortSound;
        this.mediumSound = mediumSound;
        this.longSound = longSound;
    }

    @Override
    public void Play(Location location, float pitch, int range) {
        for (Player p : location.getWorld().getPlayers()) {
            Play(p, location, pitch, range);
        }
    }

    @Override
    public void Play(Player player, Location location, float pitch, int range) {
        double dist = player.getLocation().distance(location);
        if (dist < 10) {
            player.playSound(location, shortSound, 2, pitch);
        } else if (dist < range * .33) {
            playDistantSound(location, player, shortSound, pitch);
        } else if (dist < range * .66) {
            playDistantSound(location, player, mediumSound, pitch);
        } else if (dist < range) {
            playDistantSound(location, player, longSound, pitch);
        }
    }

    private void playDistantSound(Location loc, Player player, String sound, float pitch) {
        Vector playVector = loc.toVector().subtract(player.getLocation().toVector()).normalize().multiply(10);
        player.playSound(player.getLocation().add(playVector), sound, 2, pitch);
    }
}
