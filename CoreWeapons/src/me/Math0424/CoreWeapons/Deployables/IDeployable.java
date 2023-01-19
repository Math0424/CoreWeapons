package me.Math0424.CoreWeapons.Deployables;

import me.Math0424.CoreWeapons.Deployables.Types.DeployableType;
import org.bukkit.Sound;

public interface IDeployable {

    String name();

    Double maxHealth();

    Integer modelId();

    Double range();

    DeployableType type();

    Sound deploySound();

    Float deployPitch();

    Integer deployVolume();

    static Deployable register(IDeployable deployable) {
        return new Deployable(
                deployable.name(),
                deployable.maxHealth(),
                deployable.range(),
                deployable.type(),
                deployable.modelId(),
                deployable.deploySound(),
                deployable.deployPitch(),
                deployable.deployVolume()
        );
    }

}
