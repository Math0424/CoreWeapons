package me.Math0424.CoreWeapons.Resourcepack;

import me.Math0424.CoreWeapons.Config;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.NMS.NMSUtil;
import me.Math0424.CoreWeapons.Util.FileUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ResourcepackManager {

    private static boolean isReady = true;
    private static final List<Pack> registered = new ArrayList<>();
    private static boolean isClosed = false;

    static {
        String s = NMSUtil.NMS().GetServerResourcePackUrl();
        if (!s.trim().isEmpty() && System.getProperty("CoreWeapons-Reloaded") == null) {
            addPack(s, 0);
        }
    }

    private ResourcepackManager() {}

    public static boolean addPack(String URL, int priority) {
        if (!isClosed) {
            if (URL.isEmpty()) {
                return false;
            }

            isReady = false;

            Pack p = new Pack(URL, priority);
            registered.add(p);
            downloadPack(p);
            return true;
        }
        return false;
    }

    public static void close() {
        if (!isClosed) {
            isClosed = true;
            assemblePacks();
        }
    }

    private static void assemblePacks() {
        Thread loading = new Thread(() -> {
            try {
                boolean finished = false;
                while(!finished) {
                    boolean allFinished = true;
                    for (Pack p : registered) {
                        if (p.working) {
                            allFinished = false;
                            break;
                        }
                    }
                    Thread.sleep(1000);
                    finished = allFinished;
                }
                Path packs = CoreWeaponsAPI.getPlugin().getDataFolder().toPath().resolve("Packs");

                registered.sort((p, i) -> p.priority);
                if (Config.RESOURCEPACK_MESHING.getBoolVal()) {
                    MyUtil.log(Level.INFO, "Merging Packs, please wait...");
                    Path finalLocation = packs.resolve("MergedPacks/");
                    NMSUtil.NMS().DeleteDir(finalLocation.toFile());
                    finalLocation.toFile().mkdirs();

                    for (Pack p : registered) {
                        Path x = packs.resolve(p.extractedName);
                        for(Path pa : Files.find(x, 10, (pa, bfa) -> bfa.isRegularFile()).collect(Collectors.toList())) {
                            File f = pa.toFile();
                            Path rel = x.relativize(pa);

                            if (f.isDirectory()) {
                                var dir = finalLocation.resolve(rel).toFile();
                                dir.mkdirs();
                            } else {
                                var paste = finalLocation.resolve(rel).toFile();
                                if (!paste.exists()) {
                                    NMSUtil.NMS().CopyFile(f, paste);
                                } else {
                                    if (f.getName().endsWith(".json")) {
                                        mergeJsonFilesToOne(paste, f);
                                    } else {
                                        paste.delete();
                                        NMSUtil.NMS().CopyFile(f, paste);
                                    }
                                }
                            }
                        }
                    }

                    Path mergedPack = packs.resolve("MergedPack");
                    mergedPack.toFile().createNewFile();
                    FileUtil.ZipFile(finalLocation, mergedPack);
                    MyUtil.log(Level.INFO, "Finished merging " + registered.size() + " Packs. Final Pack size of " + ChatColor.AQUA + new DecimalFormat("#.##").format(mergedPack.toFile().length() / (1024.0 * 1024.0)) + "mb");

                    ResourcepackSocket.CreateServer(mergedPack.toFile());
                    String dlLink = "http://" + MyUtil.getIp() + ":" + Config.RESOURCEPACK_PORT.getIntVal() + "/resourcepack";
                    String hash = FileUtil.bytesToHexString(FileUtil.createSha1(new FileInputStream(mergedPack.toFile()))).toLowerCase();
                    NMSUtil.NMS().SetServerResourcePackUrl(dlLink, hash);

                    MyUtil.log(Level.INFO, "Pack download link " + dlLink);
                } else if (registered.size() != 0){
                    NMSUtil.NMS().SetServerResourcePackUrl(registered.get(0).URL, registered.get(0).hash);
                }
                MyUtil.log(Level.INFO, "ResourcePack assembly completed");

            } catch (Exception e) {
                e.printStackTrace();
                MyUtil.log(Level.SEVERE, "CRITICAL PACK FAILURE");
            }
            isReady = true;
        });
        loading.start();
    }

    private static void mergeJsonFilesToOne(File mergeTo, File merge) {
        System.out.println("Fixing merge conflict " + mergeTo.getName());
        //TODO: this
    }

    private static void downloadPack(Pack p) {

        Path packs = CoreWeaponsAPI.getPlugin().getDataFolder().toPath().resolve("Packs");
        packs.toFile().mkdirs();
        File file = packs.resolve(p.fileName).toFile();

        Thread loadResourcePack = new Thread(() -> {
            try {
                NMSUtil.NMS().CopyDirFromURL(p.URL, file);
                p.hash = FileUtil.bytesToHexString(FileUtil.createSha1(new FileInputStream(file))).toLowerCase();
                FileUtil.UnZipFile(file.toPath(), packs.resolve(p.extractedName));
            } catch (Exception e) {
                MyUtil.log(Level.SEVERE, "ResourcePack acquisition failed!");
                e.printStackTrace();
            } finally {
                p.working = false;
                MyUtil.log(Level.INFO, "ResourcePack(" + p.priority + ") acquired with a size of " + ChatColor.AQUA + new DecimalFormat("#.##").format(file.length() / (1024.0 * 1024.0)) + "mb (" + p.hash + ")");
            }
        });
        loadResourcePack.start();
    }

    private static class Pack {
        private static int count = 0;
        public Pack(String URL, int priority) {
            this.URL = URL;
            this.fileName = "Pack" + count;
            this.extractedName = "Ex"+fileName+File.separator;
            this.priority = priority;
            count++;
        }
        String URL;
        String hash = "";
        String fileName;
        String extractedName;
        int priority;
        boolean working = true;
    }

    public static boolean isReady() {
        return isReady;
    }


}
