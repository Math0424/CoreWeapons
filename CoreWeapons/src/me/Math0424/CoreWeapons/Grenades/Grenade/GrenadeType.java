package me.Math0424.CoreWeapons.Grenades.Grenade;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Data.PlayerData;
import me.Math0424.CoreWeapons.Grenades.Types.*;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public enum GrenadeType {

    GRENADE(BasicGrenade.class),
    SINGULARITY(SingularityGrenade.class),
    IMPACT(ImpactGrenade.class),
    SMOKE(SmokeGrenade.class),
    FLASHBANG(FlashBangGrenade.class),
    GAS(GasGrenade.class),
    STICKY(StickyGrenade.class),
    //LANDMINE(null),
    //ORBITALMARKER(null), beams from the sky
    //TRACKER(null), //proximity beeping and flying towards nearest target
    INCINERATOR(IncinerationGrenade.class),
    EMP(EmpGrenade.class),
    CLUSTER(ClusterGrenade.class);

    Class<? extends BaseGrenade> clazz;

    GrenadeType(Class<? extends BaseGrenade> clazz) {
        this.clazz = clazz;
    }

    public void throwGrenade(Player p, Container<Grenade> cont) {
        throwGrenade(p, cont, cont.getObject().getThrowMultiplier());
    }

    public void throwGrenade(Player p, Container<Grenade> cont, double strength) {
        try {
            Constructor<? extends BaseGrenade> constructor = cont.getObject().getGrenadeType().getClazz().getConstructor(Player.class, Container.class, Double.class);
            constructor.newInstance(p, cont, strength);
            PlayerData.getPlayerData(p.getUniqueId()).addTo("grenades-thrown", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Class<? extends BaseGrenade> getClazz() {
        return this.clazz;
    }

}
