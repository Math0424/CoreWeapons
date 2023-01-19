package me.Math0424.CoreWeapons.Events.ArmorEvents;


import me.Math0424.CoreWeapons.Armor.Type.Jetpack;
import org.bukkit.entity.Player;

public class JetpackUseEvent extends ArmorUseEvent {

    private final Jetpack pack;

    public JetpackUseEvent(Jetpack pack, Player player) {
        super(pack, player);
        this.pack = pack;
    }

    public Jetpack getJetpack() {
        return pack;
    }

}
