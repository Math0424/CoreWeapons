package me.Math0424.CoreWeapons.Grenades.Grenade;

import me.Math0424.CoreWeapons.Core.SerializableItem;

import java.util.Map;

public class Grenade extends SerializableItem<Grenade> {

    private String name;
    private GrenadeType grenadeType;
    private String soundID;
    private float throwPitch;
    private double throwVolume;

    private int explodeTime;
    private float throwMultiplier;
    private float explosiveYield;

    public Grenade() {}

    public Grenade(String name, GrenadeType grenadeType, String soundID, float throwPitch, int throwVolume, int explodeTime, float throwMultiplier, float explosiveYield) {
        this.name = name;
        this.grenadeType = grenadeType;
        this.soundID = soundID;
        this.throwPitch = throwPitch;
        this.throwVolume = throwVolume;
        this.explodeTime = explodeTime;
        this.throwMultiplier = throwMultiplier;
        this.explosiveYield = explosiveYield;
    }

    @Override
    public void serialize(Map<String, Object> map) {
        map.put("name", name);
        map.put("grenadeType", grenadeType);

        map.put("soundID", soundID);
        map.put("throwPitch", throwPitch);
        map.put("throwVolume", throwVolume);
        map.put("explodeTime", explodeTime);
        map.put("throwMultiplier", throwMultiplier);
        map.put("explosiveYield", explosiveYield);
    }

    @Override
    public void deSerialize(Map<String, Object> map) {
        name = (String) map.get("name");
        grenadeType = (GrenadeType) map.get("grenadeType");

        soundID = (String) map.get("soundID");
        throwPitch = (float) map.get("throwPitch");
        throwVolume = (double) map.get("throwVolume");
        explodeTime = (int) map.get("explodeTime");
        throwMultiplier = (float) map.get("throwMultiplier");
        explosiveYield = (float) map.get("explosiveYield");
    }

    @Override
    public String friendlyName() {
        return "Grenade";
    }

    public GrenadeType getGrenadeType() {
        return grenadeType;
    }

    public String getSoundID() {
        return soundID;
    }

    public String getName() {
        return name;
    }

    public int getExplodeTime() {
        return explodeTime;
    }

    public float getThrowMultiplier() {
        return throwMultiplier;
    }

    public float getExplosiveYield() {
        return explosiveYield;
    }

    public float getThrowPitch() {
        return throwPitch;
    }

    public double getThrowVolume() {
        return throwVolume;
    }
}
