package me.Math0424.CoreWeapons.Deployables.Types;

import me.Math0424.CoreWeapons.Deployables.Deployable;
import org.bukkit.Location;

import java.lang.reflect.Constructor;

public enum DeployableType {

    SHIELD(ShieldDeployable.class),
    PLAYER(PlayerShield.class),
    //unlimited anti player shield BASE(null),
    HEAL(HealDeployable.class);

    Class<? extends BaseDeployable> clazz;

    DeployableType(Class<? extends BaseDeployable> clazz) {
        this.clazz = clazz;
    }

    public void deployStructure(Deployable d, Location l, String o) {
        try {
            Constructor constructor = d.getType().getClazz().getConstructor(Deployable.class, Location.class, String.class);
            constructor.newInstance(d, l, o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Class<? extends BaseDeployable> getClazz() {
        return this.clazz;
    }

}
