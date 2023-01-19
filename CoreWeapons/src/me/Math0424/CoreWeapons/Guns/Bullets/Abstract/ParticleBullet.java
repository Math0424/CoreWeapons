package me.Math0424.CoreWeapons.Guns.Bullets.Abstract;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public abstract class ParticleBullet extends MyBullet {

    protected ParticleBullet(LivingEntity shooter, Container<Gun> container, Gun gun, double accuracyMultiplier) {
        super(shooter, container, gun, accuracyMultiplier);
    }

    public Vector getShootDir() {
        double spread = (gun.getBulletSpread() / 150.0) * accuracy;
        Vector vec = new org.bukkit.util.Vector(spread * random(), spread * random(), spread * random());
        Vector pvec = shooter.getLocation().getDirection().normalize();
        return pvec.multiply(gun.getBulletSpeed()).add(vec);
    }

    @Override
    public void init() {

    }

    protected double random() {
        return MyUtil.random() - MyUtil.random();
    }

}
