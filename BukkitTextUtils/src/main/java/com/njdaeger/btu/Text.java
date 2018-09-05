package com.njdaeger.btu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.njdaeger.btu.Util.getNMSClass;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class Text {
    
    /**
     * Creates a new Text.
     * @param text The beginning text
     * @return A new Text Section. This will clear all existing text sections.
     */
    public static TextSection of(String text) {
        return new TextSection(true, true, null).setText(text);
    }
    
    public static void sendTo(TextSection text, Player player) {
        try {
            Object craftPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = craftPlayer.getClass().getField("playerConnection").get(craftPlayer);
            Class<?> baseComponent = getNMSClass("IChatBaseComponent");
            Class<?> serializer = getNMSClass("IChatBaseComponent$ChatSerializer");
            Class<?> chatPacket = getNMSClass("PacketPlayOutChat");
            Constructor packet = chatPacket.getConstructor(baseComponent, getNMSClass("ChatMessageType"));
        
            Object component = serializer.getDeclaredMethod("a", String.class).invoke(null, text.toString());
            connection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(connection, packet.newInstance(component, getChatMessageType((byte)0)));
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException | InstantiationException | ClassNotFoundException e) {
            e.getCause().printStackTrace();
        }
    }
    
    private static Object getChatMessageType(byte type) {
        try {
            Class<?> chatMessage = getNMSClass("ChatMessageType");
            return chatMessage.getMethod("a", byte.class).invoke(null, type);
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Represents a section of text
     */
    public static class TextSection extends Text {
    
        public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        
        private String text = "";
        private String insertion;
        private boolean isParent;
        private TextSection parent;
        private HoverEvent hoverEvent;
        private ClickEvent clickEvent;
        private boolean canHaveEvents;
        private ChatColor color = ChatColor.RESET;
        private List<TextSection> extra;
        private boolean bold = false;
        private boolean italics = false;
        private boolean underline = false;
        private boolean obfuscated = false;
        private boolean strikethrough = false;
        
        private TextSection(boolean events, boolean isParent, TextSection parent) {
            this.canHaveEvents = events;
            this.isParent = isParent;
            if (!isParent) {
                this.parent = parent;
            } else extra = new ArrayList<>();
        }
        
        public final void sendTo(Player player) {
            sendTo(this, player);
        }
        
        /**
         * Adds a text section to the current Text
         * @param text The text to append to this current section
         * @return The newly created text section
         */
        public final TextSection append(String text) {
            TextSection section = new TextSection(true, false, getParent()).setText(text);
            getExtra().add(section);
            return section;
        }
        
        /**
         * Adds a text section to the current text
         * @param consumer The text to append to this current section
         */
        public final TextSection append(Consumer<TextSection> consumer) {
            TextSection section = new TextSection(true, false, getParent());
            consumer.accept(section);
            getExtra().add(section);
            return this;
        }
        
        /**
         * Adds a text section to the current text.
         * @param textSection The text to append to this current section
         */
        public final TextSection append(TextSection textSection) {
            getExtra().add(textSection.getParent());
            return this;
        }
        
        /**
         * Set the text of this TextSection
         * @param text The text
         * @return This TextSection
         */
        public TextSection setText(String text) {
            this.text = text;
            return this;
        }
        
        /**
         * Set the color of the current text.
         * @param color The color.
         *
         *              <p>
         *              Any can be set. Any formatting such as bold or italics must be set with the appropriate methods.
         *              </p>
         */
        public TextSection setColor(ChatColor color) {
            if (21 > color.ordinal() && color.ordinal() > 15) {
                return this;
            }
            this.color = color;
            return this;
        }
        
        /**
         * Sets this Section to bold
         * @param bold True to set bold, false otherwise
         * @return This TextSection
         */
        public TextSection setBold(boolean bold) {
            this.bold = bold;
            return this;
        }
        
        /**
         * Sets this section to Italics
         * @param italics True to set italics, false otherwise
         * @return This TextSection
         */
        public TextSection setItalics(boolean italics) {
            this.italics = italics;
            return this;
        }
        
        /**
         * Sets this section to underlined
         * @param underline True to set underlined, false otherwise
         * @return This TextSection
         */
        public TextSection setUnderlined(boolean underline) {
            this.underline = underline;
            return this;
        }
        
        /**
         * Sets this section to obfuscated
         * @param obfuscated True to set obfuscated, false otherwise
         * @return This TextSection
         */
        public TextSection setObfuscated(boolean obfuscated) {
            this.obfuscated = obfuscated;
            return this;
        }
        
        /**
         * Sets this section to Strikethroughed
         * @param strikethrough True to set strikethrough, false otherwse
         * @return This TextSection
         */
        public TextSection setStrikethrough(boolean strikethrough) {
            this.strikethrough = strikethrough;
            return this;
        }
        
        /**
         * When the current text is shift clicked, this will insert the given text
         * @param insertion The text to insert
         * @return This TextSection
         */
        public TextSection shiftClickEvent(String insertion) {
            if (!canHaveEvents) return this;
            this.insertion = insertion;
            return this;
        }
        
        /**
         * Adds a hover event to this text section
         * @param hoverEvent The hover event
         * @return This TextSection
         */
        public TextSection hoverEvent(Consumer<HoverEvent> hoverEvent) {
            if (!canHaveEvents) return this;
            this.hoverEvent = new HoverEvent(parent);
            hoverEvent.accept(this.hoverEvent);
            return this;
        }
        
        /**
         * Adds a hover event to this text section
         * @param hoverEvent The hover event
         * @return This TextSection
         */
        public TextSection hoverEvent(HoverEvent hoverEvent) {
            if (!canHaveEvents) return this;
            this.hoverEvent = hoverEvent;
            return this;
        }
        
        /**
         * Adds a click event to this text section
         * @param clickEvent The click event
         * @return This TextSection
         */
        public TextSection clickEvent(Consumer<ClickEvent> clickEvent) {
            if (!canHaveEvents) return this;
            this.clickEvent = new ClickEvent();
            clickEvent.accept(this.clickEvent);
            return this;
        }
        
        /**
         * Adds a click event to this text section
         * @param clickEvent The click event
         * @return This TextSection
         */
        public TextSection clickEvent(ClickEvent clickEvent) {
            if (!canHaveEvents) return this;
            this.clickEvent = clickEvent;
            return this;
        }
        
        /**
         * Checks if this current section is a parent section
         * @return True if this section is the parent section, false otherwise.
         */
        public final boolean isParent() {
            return isParent;
        }
        
        /**
         * Gets all the additional text sections
         * @return all the additional text sections.
         */
        public List<TextSection> getExtra() {
            if (isParent) return extra;
            else return getParent().getExtra();
        }
        
        /**
         * Get the text currently represented by this section
         * @return The section text
         */
        public String getText() {
            return text;
        }
        
        //Set the sections parent
        private void setParent(TextSection p) {
            parent = p;
        }
        
        /**
         * Returns the parent of the entire TextComponent
         * @return The parent TextSection
         */
        public final TextSection getParent() {
            if (isParent) return this;
            else return parent;
        }
        
        /**
         * Returns this TextSection as a JsonObject
         * @return The TextSection JsonObject.
         */
        public JsonObject getJson() {
            
            JsonObject hover = null;
            if (hoverEvent != null && canHaveEvents) {
                hover = new JsonObject();
                List<JsonObject> val = new ArrayList<>();
                val.add(hoverEvent.getHover().getJson());
                hover.addProperty("action", hoverEvent.getAction().toString().toLowerCase());
                hover.add("value", GSON.toJsonTree(val));
            }
            
            JsonObject click = null;
            if (clickEvent != null && canHaveEvents) {
                click = new JsonObject();
                click.addProperty("action", clickEvent.getAction().toString().toLowerCase());
                click.addProperty("value", clickEvent.getClick());
            }
            
            JsonObject json = new JsonObject();
            json.addProperty("text", text);
            json.addProperty("color", color.asBungee().getName());
            if (bold) json.addProperty("bold", true);
            if (italics) json.addProperty("italic", true);
            if (underline) json.addProperty("underlined", true);
            if (obfuscated) json.addProperty("obfuscated", true);
            if (insertion != null) json.addProperty("insertion", insertion);
            if (strikethrough) json.addProperty("strikethrough", true);
            if (hover != null) {
                json.add("hoverEvent", hover);
            }
            if (click != null) {
                json.add("clickEvent", click);
            }
            if (isParent && !extra.isEmpty()) {
                List<JsonObject> sections = new ArrayList<>();
                for (TextSection section : extra) {
                    sections.add(section.getJson());
                }
                json.add("extra", GSON.toJsonTree(sections));
            }
            return json;
        }
        
        /**
         * Gets a simplified String that isn't a json object
         * @return - Formatted String
         */
        public String getFormatted() {
            StringBuilder stringBuilder = new StringBuilder();
            if (bold) stringBuilder.append(ChatColor.BOLD);
            if (italics) stringBuilder.append(ChatColor.ITALIC);
            if (underline) stringBuilder.append(ChatColor.UNDERLINE);
            if (strikethrough) stringBuilder.append(ChatColor.STRIKETHROUGH);
            
            stringBuilder.append(color);
            stringBuilder.append(text);
            if (isParent) {
                if (!extra.isEmpty()) getExtra().forEach(textSection -> stringBuilder.append(textSection.getFormatted()));
            }
            
            return stringBuilder.toString();
        }
        
        /**
         * Turns this text object into the proper JSON format
         * @return Json string
         */
        @Override
        public String toString() {
            if (isParent) return getJson().toString();
            return parent.getJson().toString();
        }
        
        public String toUnformatted() {
            StringBuilder builder = new StringBuilder();
            getParent().getSections().stream().map(TextSection::getText).forEach(builder::append);
            return builder.toString();
        }
        
        public List<TextSection> getSections() {
            List<TextSection> sections = new ArrayList<>(getParent().getExtra());
            sections.add(0, getParent());
            return sections;
        }
        
    }
    
    /**
     * Represents a hover event in chat
     */
    public class HoverEvent {
        
        private HoverAction action = HoverAction.SHOW_TEXT;
        private TextSection text;
        
        public HoverEvent(TextSection parent) {
            this.text = new TextSection(false, true, null);
        }
        
        public final void action(HoverAction action) {
            this.action = action;
        }
        
        public final HoverAction getAction() {
            return action;
        }
        
        public final void hover(Consumer<TextSection> text) {
            text.accept(this.text);
        }
        
        public final TextSection getHover() {
            return text;
        }
    }
    
    /**
     * The {@link HoverEvent} actions
     */
    public enum HoverAction {
        /**
         * Display text when the user hovers over the message
         */
        SHOW_TEXT,
        /**
         * Display an item when the user hovers over the message
         */
        SHOW_ITEM,
        /**
         * Display an entity when the user hovers over the message
         */
        SHOW_ENTITY
        
    }
    
    /**
     * Represents a click event in chat
     */
    public class ClickEvent {
        
        private ClickAction action = ClickAction.SUGGEST_COMMAND;
        private String click;
        
        public void action(ClickAction action) {
            this.action = action;
        }
        
        public ClickAction getAction() {
            return action;
        }
        
        public void click(String click) {
            this.click = click;
        }
        
        public String getClick() {
            return click;
        }
    }
    
    /**
     * The {@link ClickEvent} actions
     */
    public enum ClickAction {
        
        /**
         * Open a URL for a user when clicked
         */
        OPEN_URL,
        /**
         * Run a command for a user when clicked
         */
        RUN_COMMAND,
        /**
         * Suggest a command for a user when clicked
         */
        SUGGEST_COMMAND,
        /**
         * Change the page for a user when clicked
         */
        CHANGE_PAGE
        
    }
}