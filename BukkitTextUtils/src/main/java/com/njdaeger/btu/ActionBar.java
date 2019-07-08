package com.njdaeger.btu;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public abstract class ActionBar {

    /**
     * Create an action bar with the initial text provided from the parameter
     * @param text The text to start the actionbar with
     * @return The newly created actionbar
     */
    public static Action of(String text) {
        return new Action(Text.of(text));
    }

    /**
     * Create an action bar with the initial {@link Text} provided from the parameter
     * @param text The text to use in this actionbar
     * @return The newly created actionbar
     */
    public static Action of(Text text) {
        return new Action(text);
    }

    /**
     * Sends an actionbar to a player
     * @param actionBar The actionbar to send to the player
     * @param player The player to send the actionbar to
     */
    public static void sendTo(Action actionBar, Player player) {
        try {
            Class<?> baseCompClass = Util.getNMSClass("IChatBaseComponent");
            Class<?> messageTypeClass = Util.getNMSClass("ChatMessageType");
            Constructor<?> constructor = Util.getNMSClass("PacketPlayOutChat").getConstructor(baseCompClass, messageTypeClass);
            
            Object base = baseCompClass.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, actionBar.text.toString());
            Object packet = constructor.newInstance(base, messageTypeClass.getEnumConstants()[2]);
            Object basePlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = basePlayer.getClass().getField("playerConnection").get(basePlayer);
            
            connection.getClass().getMethod("sendPacket", Util.getNMSClass("Packet")).invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Action extends ActionBar {
        
        private final Text text;
        
        private Action(Text text) {
            this.text = text;
        }
        
        public void sendTo(Player player) {
            sendTo(this, player);
        }
        
    }

}
