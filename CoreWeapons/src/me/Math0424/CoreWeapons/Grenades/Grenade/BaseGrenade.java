package me.Math0424.CoreWeapons.Grenades.Grenade;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Sound.SoundSystem;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public abstract class BaseGrenade {

    protected Player player;
    protected Grenade grenade;

    public BaseGrenade(Player p, Container<Grenade> cont, Double strength) {
        this.player = p;
        this.grenade = cont.getObject();

        ItemStack stack = new ItemStack(cont.getItemStack());
        stack.setAmount(1);

        Item item = p.getWorld().dropItem(p.getEyeLocation(), stack);
        item.setVelocity(p.getLocation().getDirection().multiply(strength));
        item.setPickupDelay(10000);
        SoundSystem.playSound(cont.getObject().getSoundID(), item.getLocation(), 1, 1);
        thrown(item);
    }

    protected abstract void thrown(Item item);

    public double random() {
        return (new Random().nextDouble() - new Random().nextDouble());
    }

}
