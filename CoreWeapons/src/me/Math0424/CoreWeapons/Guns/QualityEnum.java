package me.Math0424.CoreWeapons.Guns;

public enum QualityEnum {

    FACTORY(1f),
    NEW(9f),
    MINT(.8f),
    FAIR(.7f),
    USED(.6f),
    WORN(.5f),
    POOR(.4f),
    AWFUL(.3f),
    BROKEN(.1f),
    NONE(0);

    private final float value;

    QualityEnum(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public static QualityEnum closest(float value) {
        for(QualityEnum x : QualityEnum.values()) {
            if (x.value <= value) {
                return x;
            }
        }
        return BROKEN;
    }
}
