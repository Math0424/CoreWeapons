package me.Math0424.CoreWeapons.DamageHandler;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;

public class CoreDamage {

    private static final HashMap<LivingEntity, CoreDamage> lastDamage = new HashMap<>();

    private final Entity damager;
    private final LivingEntity damaged;
    private final DamageExplainer cause;
    private final Container<Gun> gun;
    private Double damageAmount;

    public CoreDamage(LivingEntity damaged, Entity damager, Container<Gun> gun, Double damageAmount, DamageExplainer cause) {
        this.damager = damager;
        this.damaged = damaged;
        this.cause = cause;
        this.gun = gun;
        this.damageAmount = damageAmount;
        lastDamage.put(damaged, this);
    }

    public static CoreDamage getLastDamage(LivingEntity damaged) {
        return lastDamage.get(damaged);
    }

    public static void clearLastDamage(LivingEntity damaged) {
        lastDamage.remove(damaged);
    }

    public LivingEntity getDamaged() {
        return damaged;
    }

    public Entity getDamager() {
        return damager;
    }

    public DamageExplainer getCause() {
        return cause;
    }

    public Double getDamageAmount() {
        return damageAmount;
    }

    public void setDamageAmount(Double damageAmount) {
        this.damageAmount = damageAmount;
    }

    public Container<Gun> getGun() {
        return gun;
    }

}
