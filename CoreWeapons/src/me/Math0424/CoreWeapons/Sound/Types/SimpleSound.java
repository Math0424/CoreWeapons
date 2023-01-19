package me.Math0424.CoreWeapons.Sound.Types;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SimpleSound extends AdvancedSound {

    private final String sound;

    public SimpleSound(String soundID, String sound) {
        super(soundID);
        this.sound = sound;
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
        if (dist < range) {
            playDistantSound(location, player, sound, pitch);
        }
    }

    private void playDistantSound(Location loc, Player player, String sound, float pitch) {
        Vector playVector = loc.toVector().subtract(player.getLocation().toVector()).normalize().multiply(10);
        player.playSound(player.getLocation().add(playVector), sound, 2, pitch);
    }
}
