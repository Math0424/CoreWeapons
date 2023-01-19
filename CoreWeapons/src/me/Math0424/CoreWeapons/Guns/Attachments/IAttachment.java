package me.Math0424.CoreWeapons.Guns.Attachments;

import org.bukkit.Material;

import java.util.List;

public interface IAttachment {

    //TODO: fixme

    String parent();

    String name();

    Material material();

    int materialId();

    List<String> lore();

    Double positiveModifier();

    Double negativeModifier();

    static Attachment register(IAttachment attachment) {
        /*return new Attachment(
                attachment.name(),
                attachment.material(),
                attachment.materialId(),
                attachment.lore(),
                attachment.parent(),
                attachment.positiveModifier(),
                attachment.negativeModifier()
        );*/
        return null;
    }

}
