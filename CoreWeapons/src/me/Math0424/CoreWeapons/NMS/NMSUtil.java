package me.Math0424.CoreWeapons.NMS;

import NMS17a.NMS17a;
import NMS18a.NMS18a;
import org.bukkit.Bukkit;

public class NMSUtil {

    public enum NMSVersion {
        NMS17a,
        NMS18a
    }

    private static NMSVersion version;
    private static INMS nms = null;
    private static final String nmsID;

    static {
        String v = Bukkit.getServer().getClass().getPackage().getName();
        v = v.substring(v.lastIndexOf('.') + 1);
        nmsID = v;
        switch (v) {
            case "v1_17_R1" -> {
                nms = new NMS17a();
                version = NMSVersion.NMS17a;
            }
            case "v1_18_R1" -> {
                nms = new NMS18a();
                version = NMSVersion.NMS18a;
            }
        }
    }

    public static boolean isSupported() {
        return nms != null;
    }

    public static String getNmsID() {
        return nmsID;
    }

    public static NMSVersion nmsId() {return version;}

    public static INMS NMS() {
        return nms;
    }
}
