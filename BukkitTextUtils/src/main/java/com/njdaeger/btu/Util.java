package com.njdaeger.btu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;

public final class Util {

    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Util() {}
    
    public static Class<?> getNMSClass(String className) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return Class.forName("net.minecraft.server." + version + "." + className);
    }
    
    public static Class<?> getOBCClass(String className) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return Class.forName("org.bukkit.craftbukkit." + version + "." + className);
    }
    
}
