package me.Math0424.CoreWeapons.Sound;

public enum SoundCache {

    BULLET_ELECTRIC_HIT("bullet_electric_hit"),
    BULLET_NUKE_HIT("bullet_nuke_hit"),
    BULLET_IMPACT("bullet_generic_impact"),

    EMPTY_CHAMBER("gun_empty_chamber"),

    SILENCER("gun_silencer_shoot");

    private final String soundID;

    SoundCache(String soundID) {
        this.soundID = soundID;
    }

    public String getSoundID() {
        return soundID;
    }
}
