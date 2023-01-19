package me.Math0424.CoreWeapons.Util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class DrawUtil {

    public static void drawLine(Location start, Location end, Particle p) {
        double distance = start.distance(end);
        Vector startVec = start.toVector();
        Vector dist = end.toVector().clone().subtract(startVec).normalize();
        for (double d = .01; d < distance; d += .5) {
            dist.multiply(d);
            startVec.add(dist);
            start.getWorld().spawnParticle(p, startVec.getX(), startVec.getY(), startVec.getZ(), 1, 0, 0, 0, 0, null, true);
            startVec.subtract(dist);
            dist.normalize();
        }
    }

    public static void drawColoredLine(Location start, Location end, Color c, double spacing) {
        double distance = start.distance(end);
        Vector startVec = start.toVector();
        Vector dist = end.toVector().clone().subtract(startVec).normalize();
        for (double d = .01; d < distance; d += spacing) {
            dist.multiply(d);
            startVec.add(dist);
            start.getWorld().spawnParticle(Particle.REDSTONE, startVec.getX(), startVec.getY(), startVec.getZ(), 1, 0, 0, 0, 0, new Particle.DustOptions(c, 1), true);
            startVec.subtract(dist);
            dist.normalize();
        }
    }

    public static void drawColoredLine(Location start, Location end, Color c) {
        drawColoredLine(start, end, c, .5);
    }

    public static void drawColoredLine(Location start, Vector direction, double distance, Color c) {
        drawColoredLine(start, direction, distance, c, .5);
    }

    public static void drawColoredLine(Location start, Vector direction, double distance, Color c, double spacing) {
        Vector startVec = start.toVector();
        Vector dist = direction.clone().normalize();
        for (double d = .01; d < distance; d += spacing) {
            dist.multiply(d);
            startVec.add(dist);
            start.getWorld().spawnParticle(Particle.REDSTONE, startVec.getX(), startVec.getY(), startVec.getZ(), 1, 0, 0, 0, 0, new Particle.DustOptions(c, 1), true);
            startVec.subtract(dist);
            dist.normalize();
        }
    }

    public static void drawColoredLine(Location start, Location end, Color c, int size, double spacing, Player p) {
        double distance = start.distance(end);
        Vector startVec = start.toVector();
        Vector dist = end.toVector().clone().subtract(startVec).normalize();
        for (double d = .01; d < distance; d += spacing) {
            dist.multiply(d);
            startVec.add(dist);
            p.spawnParticle(Particle.REDSTONE, startVec.getX(), startVec.getY(), startVec.getZ(), 1, new Particle.DustOptions(c, size));
            startVec.subtract(dist);
            dist.normalize();
        }
    }

    public static void drawCircle(Location center, double radius, int amount, Color c) {
        double increment = (2 * Math.PI) / amount;
        for (int i = 0; i < amount; i++) {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            center.getWorld().spawnParticle(Particle.REDSTONE, x, center.getY(), z, 1, new Particle.DustOptions(c, 1));
        }
    }

    public static void drawCircle(Location center, double radius, int amount, Color c, Player p) {
        double increment = (2 * Math.PI) / amount;
        for (int i = 0; i < amount; i++) {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            p.spawnParticle(Particle.REDSTONE, x, center.getY(), z, 1, new Particle.DustOptions(c, 1));
        }
    }

    public static void drawParticleSphere(Location center, double radius, int stackCount, Color c, float particleSize) {
        for (Vector v : generateParticleSphere(center, stackCount, radius)) {
            center.getWorld().spawnParticle(Particle.REDSTONE, v.getX(), v.getY(), v.getZ(), 1, new Particle.DustOptions(c, particleSize));
        }
    }

    public static List<Vector> generateParticleSphere(Location center, int stackCount, double size) {
        List<Vector> locations = new ArrayList<>();
        Vector start = center.toVector();
        locations.add(new Vector(0, size, 0).add(start));
        locations.add(new Vector(0, -size, 0).add(start));

        for (double i = 0; i <= Math.PI; i += Math.PI / stackCount) {
            double radius = Math.sin(i) * size;
            double y = Math.cos(i) * size;
            if (y != size && y != -size) {
                for (double a = 0; a < Math.PI * 2; a += Math.PI / stackCount) {
                    double x = Math.cos(a) * radius;
                    double z = Math.sin(a) * radius;
                    locations.add(new Vector(x, y, z).add(start));
                }
            }
        }
        return locations;
    }

}
