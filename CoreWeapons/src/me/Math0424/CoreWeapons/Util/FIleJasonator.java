package me.Math0424.CoreWeapons.Util;

import java.io.File;

public class FIleJasonator {

    public static void main(String[] args) {


    }

    private static void Sound() {
        String path = "pathheretosound";
        File f = new File(path);
        for (File f1 : f.listFiles()) {
            for (File sound : f1.listFiles()) {
                String pathDivergence = sound.getPath().split("\\\\")[12];
                String soundName = sound.getName().replace(".ogg", "");
                System.out.println("\"" + soundName + "_" + pathDivergence.replace("_random", "") + "\": {\"sounds\": [\"custom/" + pathDivergence + "/" + soundName + "\"]},\n");
            }
        }
    }


}
