package me.Math0424.CoreWeapons.DamageHandler;

public class DamageExplainer {

    public static final DamageExplainer BULLET = new DamageExplainer("bullet");
    public static final DamageExplainer HEADSHOT = new DamageExplainer("bullet.headshot");
    public static final DamageExplainer FIRE = new DamageExplainer("bullet.fire");
    public static final DamageExplainer ROCKET = new DamageExplainer("bullet.rocket");
    public static final DamageExplainer NUKE = new DamageExplainer("bullet.nuke");
    public static final DamageExplainer ACID = new DamageExplainer("bullet.acid");
    public static final DamageExplainer LASER = new DamageExplainer("bullet.laser");
    public static final DamageExplainer ELECTRIC = new DamageExplainer("bullet.electric");
    public static final DamageExplainer GUNGRENADE = new DamageExplainer("bullet.grenade");

    public static final DamageExplainer INCINERATIONGRENADE = new DamageExplainer("grenade.incineration");
    public static final DamageExplainer THROWABLEGRENADE = new DamageExplainer("grenade.throwable");
    public static final DamageExplainer GASGRENADE = new DamageExplainer("grenade.gas");
    public static final DamageExplainer SINGULARITYGRENADE = new DamageExplainer("grenade.singularity");
    public static final DamageExplainer CLUSTERGRENADE = new DamageExplainer("grenade.cluster");
    public static final DamageExplainer IMPACTGRENADE = new DamageExplainer("grenade.impact");

    //special
    public static final DamageExplainer SHIELD = new DamageExplainer("shield");
    public static final DamageExplainer PUNISHMENT = new DamageExplainer("punishment");
    public static final DamageExplainer SLAM = new DamageExplainer("slam");
    public static final DamageExplainer RUNOVER = new DamageExplainer("runover");

    public static final DamageExplainer WATER = new DamageExplainer("water").setHasNoAttacker();
    public static final DamageExplainer SUICIDE = new DamageExplainer("suicide").setHasNoAttacker();
    public static final DamageExplainer COMBATLOG = new DamageExplainer("combat.log").setHasNoAttacker();
    public static final DamageExplainer BLOCKCRUSH = new DamageExplainer("block.crush").setHasNoAttacker();


    private final String name;
    private boolean hasNoAttacker;

    public DamageExplainer(String name) {
        this.name = name;
        hasNoAttacker = false;
    }

    public DamageExplainer setHasNoAttacker() {
        hasNoAttacker = true;
        return this;
    }

    public boolean hasNoAttacker() {
        return hasNoAttacker;
    }

    public String getName() {
        return name;
    }


}
