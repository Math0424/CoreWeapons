package me.Math0424.CoreWeapons.Resourcepack;

import com.sun.net.httpserver.*;
import me.Math0424.CoreWeapons.Config;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class ResourcepackSocket implements HttpHandler {

    private ResourcepackSocket() {}

    private static HttpServer server;
    private static byte[] File;

    public static void CreateServer(File f) throws IOException {
        File = new byte[(int) f.length()];
        FileInputStream fis = new FileInputStream(f);
        BufferedInputStream bis = new BufferedInputStream(fis);
        bis.read(File, 0, File.length);
        fis.close();
        bis.close();

        //TODO: resourcepack port config
        server = HttpServer.create(new InetSocketAddress(Config.RESOURCEPACK_PORT.getIntVal()), 0);
        server.createContext("/resourcepack", new ResourcepackSocket());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }

    public static void Close() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        System.out.println(ex.getLocalAddress()+":"+ex.getProtocol());
        ex.sendResponseHeaders(200, File.length);
        OutputStream out = ex.getResponseBody();
        out.write(File);
        out.close();
        ex.close();
    }
}
