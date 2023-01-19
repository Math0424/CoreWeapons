package me.Math0424.CoreWeapons.Guns.Attachments;

import me.Math0424.CoreWeapons.Core.SerializableItem;

import java.util.HashMap;
import java.util.Map;

public class Attachment extends SerializableItem<Attachment> {

    private String name;
    private String classifier;
    private HashMap<AttachmentModifier, Double> modifiers;

    public Attachment() {
    }

    public Attachment(String name, String classifier, HashMap<AttachmentModifier, Double> modifiers) {
        this.name = name;
        this.classifier = classifier;
        this.modifiers = modifiers;
    }

    @Override
    public void serialize(Map<String, Object> map) {
        map.put("name", name);
        map.put("classifier", classifier);

        HashMap<String, Double> addMap = new HashMap<>();
        for(var x : modifiers.keySet()) {
            addMap.put(x.toString(), modifiers.get(x));
        }
        map.put("modifiers", addMap);
    }

    @Override
    public void deSerialize(Map<String, Object> map) {
        name = (String) map.get("name");
        classifier = (String) map.get("classifier");

        modifiers = new HashMap<>();
        HashMap<String, Double> addMap = (HashMap<String, Double>) map.get("modifiers");
        for(var x : addMap.keySet()) {
            modifiers.put(AttachmentModifier.valueOf(x), addMap.get(x));
        }
    }

    @Override
    public String friendlyName() {
        return "Attachment";
    }

    public String getName() {
        return name;
    }

    public String getClassifier() {
        return classifier;
    }

    public HashMap<AttachmentModifier, Double> getModifiers() {
        return modifiers;
    }
}
