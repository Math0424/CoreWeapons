package me.Math0424.CoreWeapons.Guns.Bullets;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.MyBullet;
import me.Math0424.CoreWeapons.Guns.Bullets.Entity.RegularBullet;
import me.Math0424.CoreWeapons.Guns.Bullets.Entity.*;
import me.Math0424.CoreWeapons.Guns.Bullets.Other.GrenadeLauncherBullet;
import me.Math0424.CoreWeapons.Guns.Bullets.Particle.ElectricBullet;
import me.Math0424.CoreWeapons.Guns.Bullets.Particle.LaserBullet;
import me.Math0424.CoreWeapons.Guns.Bullets.Particle.TeleportBullet;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public enum BulletType {

    REGULAR(RegularBullet.class, 1),
    EXPLOSIVE(ExplosiveBullet.class, 4),
    ROCKET(RocketBullet.class, 5),
    FLAME(FlameBullet.class, 5),
    TRACER(TracerBullet.class, 1),
    TRACER_ROCKET(TracerRocketBullet.class, 5),
    BLINDNESS(BlindnessBullet.class, 1),
    LASER(LaserBullet.class),
    TELEPORT(TeleportBullet.class),
    ACID(AcidBullet.class, 2),
    NUKE(NukeBullet.class, 3),

    ELECTRIC(ElectricBullet.class),
    GRENADE_LAUNCHER(GrenadeLauncherBullet.class),
    SMART(null),

    NONE(null);

    //TODO: Smart bullet (will turn towards target/entity)
    //TODO: Watergun
    //TODO: Snowgun freeze ray

    Class<? extends MyBullet> clazz;
    int materialID;

    BulletType(Class<? extends MyBullet> clazz) {
        this.clazz = clazz;
    }

    BulletType(Class<? extends MyBullet> clazz, int materialID) {
        this.clazz = clazz;
        this.materialID = materialID;
    }

    /**
     * Fire a bullet
     * @param p player who shot
     * @param cont container of the original gun
     */
    public void fire(Player p, Container<Gun> cont) {
        try {
            if (cont.getObject().getBulletType().getClazz() != null) {
                Constructor<? extends MyBullet> constructor = cont.getObject().getBulletType().getClazz().getConstructor(LivingEntity.class, Container.class, Gun.class, double.class);
                for (int i = 0; i < cont.getObject().getBulletCountPerShot(); i++) {
                    constructor.newInstance(p, cont, cont.getObject().getAppliedQualityDegradation(), p.isSprinting() ? 1.5 : 1);
                }
            }

			/*   recoil code is'nt as cool as i though it would be
			Vector recoil = p.getLocation().getDirection();
			recoil.normalize().setY(recoil.getY() + (g.getRecoilAmount()/1000.0));
			PacketPlayOutLookAt packet = new PacketPlayOutLookAt(ArgumentAnchor.Anchor.EYES,
					p.getEyeLocation().getX() + recoil.getX(),
					p.getEyeLocation().getY() + recoil.getY(),
					p.getEyeLocation().getZ() + recoil.getZ()
			);
			((EntityPlayer)((CraftPlayer)p).getHandle()).playerConnection.sendPacket(packet);
			 */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Class<? extends MyBullet> getClazz() {
        return this.clazz;
    }

    public int getMaterialID() {
        return materialID;
    }
}
