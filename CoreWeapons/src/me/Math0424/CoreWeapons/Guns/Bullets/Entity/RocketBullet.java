package me.Math0424.CoreWeapons.Guns.Bullets.Entity;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.MyBullet;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class RocketBullet extends MyBullet {

    public RocketBullet(LivingEntity shooter, Container<Gun> cont, Gun gun, double accuracyMultiplier) {
        super(shooter, cont, gun, accuracyMultiplier);
        this.getBukkitEntity().setFireTicks(10000);
    }

    public void genericHit(Location hit) {
        DamageUtil.setExplosionDamage(hit, (int) gun.getBulletPower(), shooter, container, DamageExplainer.ROCKET);
        this.getBukkitEntity().getWorld().createExplosion(hit, gun.getBulletPower(), false, true, this.getBukkitEntity());
    }

}
