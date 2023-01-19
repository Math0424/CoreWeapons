package me.Math0424.CoreWeapons.Sound.Types;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class AdvancedSound {
    public final String soundID;

    public AdvancedSound(String soundID) {
        this.soundID = soundID;
    }

    public abstract void Play(Location location, float pitch, int range);

    public abstract void Play(Player player, Location location, float pitch, int range);
}
