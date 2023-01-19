package me.Math0424.CoreWeapons.Sound.Types;

public class EasyRangedSound extends RangedSound {
    public EasyRangedSound(String soundID, String sound) {
        super(soundID, sound + "_close", sound + "_nearby", sound + "_far");
    }
}
