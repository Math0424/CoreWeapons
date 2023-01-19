package NMS17a.Entity;

import NMS17a.NMS17a;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitBlockEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitEntityEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitNonLivingEntityEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletTickEvent;
import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.IEntityBullet;
import me.Math0424.CoreWeapons.Guns.Bullets.Abstract.MyBullet;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.projectile.EntitySnowball;
import net.minecraft.world.phys.MovingObjectPosition;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.MovingObjectPositionEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

public class EntityBullet extends EntitySnowball implements IEntityBullet {

    protected MyBullet b;

    public EntityBullet(MyBullet bullet) {
        super(EntityTypes.aG, NMS17a.GetWorld(bullet.shooter));

        bullet.setEntity(this.getBukkitEntity());
        b = bullet;
        projectileSource = bullet.shooter;
        this.getBukkitEntity().setCustomName(String.valueOf(b.gun.getBulletDamage()));
        this.getBukkitEntity().setGravity(false);
        ((Snowball)this.getBukkitEntity()).setShooter(b.shooter);
        this.getBukkitEntity().setVelocity(b.getShootDir());


        this.setItem(NMS17a.NMSItem(ItemStackUtil.createItemStack(Material.SNOWBALL, b.gun.getBulletType().getMaterialID())));
        this.setPosition(b.shooter.getLocation().getX(), b.shooter.getLocation().getY() + b.shooter.getEyeHeight(), b.shooter.getLocation().getZ());

        NMS17a.GetWorld(b.shooter).addEntity(this);
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (movingobjectposition.getType() == MovingObjectPosition.EnumMovingObjectType.c) {
            Entity entity = ((MovingObjectPositionEntity) movingobjectposition).getEntity().getBukkitEntity();
            if (entity instanceof LivingEntity hit) {
                //hit event on entity
                BulletHitEntityEvent hitEvent = new BulletHitEntityEvent(hit, b.gun, b.shooter);
                Bukkit.getPluginManager().callEvent(hitEvent);
                if (!hitEvent.isCancelled()) {
                    b.genericHit(hit.getLocation());
                    if (b.entityHit(hit)) {
                        this.t.broadcastEntityEffect(this, (byte) 3);
                        this.getBukkitEntity().remove();
                    }
                }
            } else {
                BulletHitNonLivingEntityEvent hitEvent = new BulletHitNonLivingEntityEvent(entity, b.gun, b.shooter);
                Bukkit.getPluginManager().callEvent(hitEvent);
                if (!hitEvent.isCancelled()) {
                    this.t.broadcastEntityEffect(this, (byte) 3);
                    this.getBukkitEntity().remove();
                }
            }

        } else if (movingobjectposition.getType() == MovingObjectPosition.EnumMovingObjectType.b) {
            BlockPosition vec = ((MovingObjectPositionBlock) movingobjectposition).getBlockPosition();
            Block block = new Location(this.getBukkitEntity().getWorld(), vec.getX(), vec.getY(), vec.getZ()).getBlock();
            if (!block.isPassable()) {
                //hit event on block
                BulletHitBlockEvent hitEvent = new BulletHitBlockEvent(block, b.gun, b.shooter);

                Bukkit.getPluginManager().callEvent(hitEvent);
                if (!hitEvent.isCancelled()) {
                    b.genericHit(new Location(block.getWorld(), vec.getX(), vec.getY(), vec.getZ()));
                    if (b.blockHit(new Location(block.getWorld(), vec.getX(), vec.getY(), vec.getZ()), block)) {
                        this.t.broadcastEntityEffect(this, (byte) 3);
                        this.getBukkitEntity().remove();
                    }
                }

            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        //override tick
        BulletTickEvent bulletTick = new BulletTickEvent(b.gun, b.shooter, b);
        Bukkit.getPluginManager().callEvent(bulletTick);
        if (!bulletTick.isCancelled()) {
            b.ticked();
        }

        //move tick (can be cancelled)
        if (b.nextTick()) {
            Entity e = this.getBukkitEntity();
            e.setVelocity(new Vector(e.getVelocity().getX(), e.getVelocity().getY() - (b.gun.getBulletDrop() / 15.0), e.getVelocity().getZ()));

            if (b.getDamage() >= 1) {
                b.setDamage(b.getDamage() - b.gun.getBulletFalloff());
            }
        } else {
            b.nextTick(true);
        }
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {

    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {

    }
}
