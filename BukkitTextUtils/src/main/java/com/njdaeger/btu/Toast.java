package com.njdaeger.btu;

import com.google.gson.JsonObject;
import net.minecraft.server.v1_13_R2.PacketPlayOutAdvancements;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.UUID;

/**
 * This class is meant to allow the immediate sending of toast messages to a player for whatever reason.
 */
public abstract class Toast {


    public static Toaster of(String text) {
        return new Toaster(text);
    }

    public static void sendTo(Toaster toast, Player player) {
        Bukkit.getUnsafe().loadAdvancement(toast.getNamespacedKey(), toast.toString());
    }

    public static class Toaster extends Toast {

        private Text title;
        private String icon;
        private String iconNbt;
        private Frame frame;
        private Text description;
        private boolean showToast;
        private boolean announce;
        private boolean hidden;

        private final NamespacedKey namespacedKey;

        public Toaster() {
            this.namespacedKey = new NamespacedKey("BukkitTextUtil", UUID.randomUUID().toString());
        }

        public Toaster(String title) {
            this(Text.of(title));
        }

        public Toaster(Text title) {
            this();
            this.title = title;
        }

        public Toaster title(String title) {
            this.title = Text.of(title);
            return this;
        }

        public Toaster title(Text title) {
            this.title = title;
            return this;
        }

        public Toaster description(String description) {
            this.description = Text.of(description);
            return this;
        }

        public Toaster frame(Frame frame) {
            this.frame = frame;
            return this;
        }

       /* public JsonObject getJson() {

            JsonObject display = new JsonObject();


        }*/

        public NamespacedKey getNamespacedKey() {
            return namespacedKey;
        }

        /*@Override
        public String toString() {

        }*/
    }

    public enum Frame {

        CHALLENGE("challenge"),
        GOAL("goal"),
        TASK("task");

        private String frame;

        Frame(String frame) {
            this.frame = frame;
        }

        public String getFrameName() {
            return frame;
        }

    }


}
