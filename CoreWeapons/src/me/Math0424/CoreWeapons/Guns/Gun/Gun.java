package me.Math0424.CoreWeapons.Guns.Gun;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Core.SerializableItem;
import me.Math0424.CoreWeapons.Guns.Attachments.Attachment;
import me.Math0424.CoreWeapons.Guns.Attachments.AttachmentModifier;
import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Gun extends SerializableItem<Gun> {

    //Special
    private String name;

    private UUID uuid;
    private long creationDate;
    private UUID owner;
    private boolean isPrimaryGun;

    private String ammoID;

    private String fireSoundID;
    private float firePitch;
    private int fireSoundRange;

    private GunType gunType;
    private BulletType bulletType;

    private List<AttachmentModifier> incompatibleModifiers;
    private List<Attachment> attachments;

    //Stats
    private float quality;
    private int playerKills;
    private int entityKills;
    private int totalShotsFired;
    private int totalBulletsFired;

    private int currentBurstCount;
    private int currentShotCount;
    private boolean isJammed;

    //Operations
    private int maxShotCount;
    private int maxBurstCount;

    private int shotsAddedPerReloadCycle;
    private int bulletsDrawnPerReloadCycle;

    private int reloadSpeed;
    private int fireSpeed;

    //Bullet
    private double bulletDrop;
    private double bulletSpeed;
    private double bulletDamage;
    private double headshotMultiplier;
    private double bulletFalloff;
    private double bulletSpread;
    private float bulletPower;
    private int bulletCountPerShot;

    public Gun() {}

    public Gun(String name, boolean isPrimaryGun, String ammoID, String fireSoundID, float firePitch, int fireSoundRange,
               GunType gunType, BulletType bulletType, List<AttachmentModifier> incompatibleModifiers, float quality,
               int maxBurstCount, int maxAmmoCount, int shotsAddedPerReloadCycle, int bulletsDrawnPerReloadCycle,
               int reloadSpeed, int fireSpeed, double bulletDrop, double bulletSpeed, double bulletDamage,
               double headshotMultiplier, double bulletFalloff, double bulletSpread, float bulletPower, int bulletCountPerShot) {

        this.uuid = UUID.randomUUID();
        this.creationDate = System.currentTimeMillis();
        this.currentShotCount = maxAmmoCount;
        this.attachments = new ArrayList<>();

        this.name = name;
        this.isPrimaryGun = isPrimaryGun;
        this.ammoID = ammoID;
        this.fireSoundID = fireSoundID;
        this.firePitch = firePitch;
        this.fireSoundRange = fireSoundRange;
        this.gunType = gunType;
        this.bulletType = bulletType;
        this.incompatibleModifiers = incompatibleModifiers != null ? incompatibleModifiers : new ArrayList<>();
        this.quality = quality;
        this.maxBurstCount = maxBurstCount;
        this.maxShotCount = maxAmmoCount;
        this.shotsAddedPerReloadCycle = shotsAddedPerReloadCycle;
        this.bulletsDrawnPerReloadCycle = bulletsDrawnPerReloadCycle;
        this.reloadSpeed = reloadSpeed;
        this.fireSpeed = fireSpeed;
        this.bulletDrop = bulletDrop;
        this.bulletSpeed = bulletSpeed;
        this.bulletDamage = bulletDamage;
        this.headshotMultiplier = headshotMultiplier;
        this.bulletFalloff = bulletFalloff;
        this.bulletSpread = bulletSpread;
        this.bulletPower = bulletPower;
        this.bulletCountPerShot = bulletCountPerShot;
    }

    public boolean hasAttachments() {
        return attachments.size() != 0;
    }

    public boolean canAddAttachment(Attachment attachment) {
        for(var v : attachment.getModifiers().keySet())
            if (incompatibleModifiers.contains(v))
                return false;
        for (var a : attachments)
            if (a.getClassifier().equals(attachment.getClassifier()))
                return false;
        return true;
    }

    public Optional<Double> getAttachmentValue(AttachmentModifier modifier) {
        for (Attachment a : attachments) {
            if (a.getModifiers().containsKey(modifier)) {
                return Optional.of(a.getModifiers().get(modifier));
            }
        }
        return Optional.empty();
    }

    public void clearAttachments() {
        for (Attachment a : attachments) {
            applyAttachment(a, true);
        }
        attachments.clear();
    }

    public void addAttachment(Attachment... attachment) {
        addAttachment(Arrays.stream(attachment).toList());
    }

    public void addAttachment(List<Attachment> attachment) {
        for (Attachment a : attachment) {
            if (a != null) {
                attachments.add(a);
                applyAttachment(a, false);
            }
        }
    }

    public void removeAttachment(Attachment attachment) {
        for (Attachment a : attachments) {
            if (a.getName().equals(attachment.getName())) {
                attachments.remove(a);
                applyAttachment(a, true);
                break;
            }
        }
    }

    private void applyAttachment(Attachment a, boolean remove) {
        int r = remove ? -1 : 1;
        for (AttachmentModifier m : a.getModifiers().keySet()) {
            double v = (a.getModifiers().get(m) * r);
            switch (m) {
                case BULLET_DROP -> bulletDrop += v;
                case BULLET_SPEED -> bulletSpeed += v;
                case BULLET_DAMAGE -> bulletDamage += v;
                case HEADSHOT_MULTIPLIER -> headshotMultiplier += v;
                case BULLET_FALLOFF -> bulletFalloff += v;
                case BULLET_SPREAD -> bulletSpread += v;
                case BULLET_POWER -> bulletPower += v;
                case BULLET_COUNT_PER_SHOT -> bulletCountPerShot += v;
                case BURST_COUNT -> maxBurstCount += v;
                case AMMO_COUNT -> maxShotCount += v;
                case RELOAD_SPEED -> reloadSpeed += v;
                case SHOOT_SPEED -> fireSpeed += v;
            }
        }
    }

    public Gun getAppliedQualityDegradation() {
        Gun apply = this.clone();

        if (quality > .85) {
            apply.headshotMultiplier -= .1 / quality;
            apply.bulletPower -= .1 / quality;
        }
        if (quality > .5) {
            apply.headshotMultiplier -= .1 / quality;
            apply.bulletDrop += .1 / quality;
            apply.bulletSpread += 1 / quality;
        }
        if (quality > .25) {
            if (MyUtil.random(100) >= 95) apply.isJammed = true;
            apply.bulletSpeed -= .05 / quality;
            apply.bulletDamage -= .1 / quality;

            apply.bulletFalloff += .05 / quality;
        }
        if (quality > .1) {
            if (MyUtil.random(100) >= 85) apply.isJammed = true;
            apply.reloadSpeed += .1 / quality;
        }

        return apply;
    }

    public void addToPlayerKills() {
        playerKills++;
        addToEntityKills();
    }

    public void addToEntityKills() {
        entityKills++;
    }

    public void fireOnce() {
        totalShotsFired++;
        totalBulletsFired += bulletCountPerShot;
        currentShotCount--;
        if (gunType == GunType.BURST_FIRE)
            currentBurstCount++;
    }

    public void reloadAmount(int amount) {
        currentShotCount = Math.min(maxShotCount, currentShotCount + amount);
    }

    public boolean equals(ItemStack item) {
        Container<Gun> cont = Container.getContainerItem(Gun.class, item);
        if (cont != null) {
            return uuid.equals(cont.getObject().uuid);
        }
        return false;
    }

    public static boolean isSimilar(ItemStack item, Gun g1) {
        Container<Gun> cont = Container.getContainerItem(Gun.class, item);
        if (cont != null) {
            Gun g2 = cont.getObject();
            return g1.bulletType == g2.bulletType &&
                    g1.ammoID.equals(g2.ammoID) &&
                    g1.attachments.hashCode() == g2.attachments.hashCode() &&
                    g1.quality == g2.quality &&
                    g1.isPrimaryGun == g2.isPrimaryGun &&
                    g1.currentShotCount == g2.currentShotCount &&
                    g1.bulletCountPerShot == g2.bulletCountPerShot &&
                    g1.bulletDamage == g2.bulletDamage &&
                    g1.bulletDrop == g2.bulletDrop;
        }
        return false;
    }


    @Override
    public void serialize(Map<String, Object> map) {
        map.put("name", name);

        map.put("uuid", uuid);
        map.put("creationDate", creationDate);
        map.put("owner", owner);
        map.put("isPrimaryGun", isPrimaryGun);

        map.put("ammoID", ammoID);

        map.put("fireSoundID", fireSoundID);
        map.put("firePitch", firePitch);
        map.put("fireSoundRange", fireSoundRange);

        map.put("gunType", gunType);
        map.put("bulletType", bulletType);

        map.put("incompatibleModifiers", incompatibleModifiers);
        map.put("attachments", attachments);

        map.put("quality", quality);
        map.put("playerKills", playerKills);
        map.put("entityKills", entityKills);
        map.put("totalShotsFired", totalShotsFired);
        map.put("totalBulletsFired", totalBulletsFired);

        map.put("isJammed", isJammed);

        map.put("maxBurstCount", maxBurstCount);
        map.put("currentBurstCount", currentBurstCount);

        map.put("maxShotCount", maxShotCount);
        map.put("currentShotCount", currentShotCount);

        map.put("shotsAddedPerReloadCycle", shotsAddedPerReloadCycle);
        map.put("bulletsDrawnPerReloadCycle", bulletsDrawnPerReloadCycle);

        map.put("reloadSpeed", reloadSpeed);
        map.put("fireSpeed", fireSpeed);

        map.put("bulletDrop", bulletDrop);
        map.put("bulletSpeed", bulletSpeed);
        map.put("bulletDamage", bulletDamage);
        map.put("headshotMultiplier", headshotMultiplier);
        map.put("bulletFalloff", bulletFalloff);
        map.put("bulletSpread", bulletSpread);
        map.put("bulletPower", bulletPower);
        map.put("bulletCountPerShot", bulletCountPerShot);
    }

    @Override
    public void deSerialize(Map<String, Object> map) {
        name = (String) map.get("name");

        uuid = (UUID) map.get("uuid");
        creationDate = (long) map.get("creationDate");
        owner = (UUID) map.get("owner");

        isPrimaryGun = (boolean) map.get("isPrimaryGun");
        ammoID = (String) map.get("ammoID");

        fireSoundID = (String) map.get("fireSoundID");
        firePitch = (float) map.get("firePitch");
        fireSoundRange = (int) map.get("fireSoundRange");

        gunType = (GunType) map.get("gunType");
        bulletType = (BulletType) map.get("bulletType");

        incompatibleModifiers = (List<AttachmentModifier>) map.getOrDefault("incompatibleModifiers", new ArrayList<>());
        attachments = (List<Attachment>) map.getOrDefault("attachments", new ArrayList<>());

        maxBurstCount = (int) map.get("maxBurstCount");
        maxShotCount = (int) map.get("maxShotCount");

        //GetOrDefaults
        quality = (float) map.getOrDefault("quality", 1f);
        playerKills = (int) map.getOrDefault("playerKills", 0);
        entityKills = (int) map.getOrDefault("entityKills", 0);
        totalShotsFired = (int) map.getOrDefault("totalShotsFired", 0);
        totalBulletsFired = (int) map.getOrDefault("totalBulletsFired", 0);

        isJammed = (boolean) map.getOrDefault("isJammed", false);
        currentShotCount = (int) map.getOrDefault("currentShotCount", maxShotCount);
        currentBurstCount = (int) map.getOrDefault("currentBurstCount", 0);
        //End

        shotsAddedPerReloadCycle = (int) map.get("shotsAddedPerReloadCycle");
        bulletsDrawnPerReloadCycle = (int) map.get("bulletsDrawnPerReloadCycle");

        reloadSpeed = (int) map.get("reloadSpeed");
        fireSpeed = (int) map.get("fireSpeed");

        bulletDrop = (double) map.get("bulletDrop");
        bulletSpeed = (double) map.get("bulletSpeed");
        bulletDamage = (double) map.get("bulletDamage");
        headshotMultiplier = (double) map.get("headshotMultiplier");
        bulletFalloff = (double) map.get("bulletFalloff");
        bulletSpread = (double) map.get("bulletSpread");
        bulletPower = (float) map.get("bulletPower");
        bulletCountPerShot = (int) map.get("bulletCountPerShot");
    }

    @Override
    public String friendlyName() {
        return "Gun";
    }


    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void resetBurstRoundCount() {
        currentBurstCount = 0;
    }

    public void setCurrentShotCount(int currentShotCount) {
        this.currentShotCount = currentShotCount;
    }

    public void setQuality(float quality) {
        this.quality = quality;
    }

    public String getName() {
        return name;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isPrimaryGun() {
        return isPrimaryGun;
    }

    public String getFireSoundID() {
        return fireSoundID;
    }

    public float getFirePitch() {
        return firePitch;
    }

    public int getFireSoundRange() {
        return fireSoundRange;
    }

    public String getAmmoID() {
        return ammoID;
    }

    public GunType getGunType() {
        return gunType;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public List<AttachmentModifier> getIncompatibleModifiers() {
        return incompatibleModifiers;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public int getBulletsDrawnPerReloadCycle() {
        return bulletsDrawnPerReloadCycle;
    }

    public float getQuality() {
        return quality;
    }

    public int getPlayerKills() {
        return playerKills;
    }

    public int getEntityKills() {
        return entityKills;
    }

    public int getTotalShotsFired() {
        return totalShotsFired;
    }

    public int getTotalBulletsFired() {
        return totalBulletsFired;
    }

    public boolean isJammed() {
        return isJammed;
    }

    public int getMaxBurstCount() {
        return maxBurstCount;
    }

    public int getCurrentBurstCount() {
        return currentBurstCount;
    }

    public int getMaxShotCount() {
        return maxShotCount;
    }

    public int getCurrentShotCount() {
        return currentShotCount;
    }

    public int getShotsAddedPerReloadCycle() {
        return shotsAddedPerReloadCycle;
    }

    public int getReloadSpeed() {
        return reloadSpeed;
    }

    public int getFireSpeed() {
        return fireSpeed;
    }

    public double getBulletDrop() {
        return bulletDrop;
    }

    public double getBulletSpeed() {
        return bulletSpeed;
    }

    public double getBulletDamage() {
        return bulletDamage;
    }

    public double getHeadshotMultiplier() {
        return headshotMultiplier;
    }

    public double getBulletFalloff() {
        return bulletFalloff;
    }

    public double getBulletSpread() {
        return bulletSpread;
    }

    public float getBulletPower() {
        return bulletPower;
    }

    public int getBulletCountPerShot() {
        return bulletCountPerShot;
    }
}

