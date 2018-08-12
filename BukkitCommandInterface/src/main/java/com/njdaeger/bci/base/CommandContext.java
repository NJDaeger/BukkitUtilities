package com.njdaeger.bci.base;

import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CommandContext {
    
    private final CommandSender sender;
    private final Plugin plugin;
    private final String[] args;
    private final String alias;
    
    public CommandContext(Plugin plugin, CommandSender sender, String[] args, String alias) {
        this.plugin = plugin;
        this.sender = sender;
        this.alias = alias;
        this.args = args;
    }
    
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }
    
    public boolean hasAnyPermission(String... permissions) {
        return Stream.of(permissions).anyMatch(sender::hasPermission);
    }
    
    public boolean hasAllPermissions(String... permissions) {
        return Stream.of(permissions).allMatch(sender::hasPermission);
    }
    
    public boolean isConsole() {
        return sender instanceof ConsoleCommandSender;
    }
    
    public ConsoleCommandSender asConsole() {
        return isConsole() ? (ConsoleCommandSender)sender : null;
    }
    
    public boolean isPlayer() {
        return sender instanceof Player;
    }
    
    public Player asPlayer() {
        return isPlayer() ? (Player)sender : null;
    }
    
    public boolean isBlock() {
        return sender instanceof BlockCommandSender;
    }
    
    public BlockCommandSender asBlock() {
        return isBlock() ? (BlockCommandSender)sender : null;
    }
    
    public boolean isEntity() {
        return sender instanceof Entity;
    }
    
    public Entity asEntity() {
        return isEntity() ? (Entity)sender : null;
    }
    
    public <T extends CommandSender> boolean is(Class<T> type) {
        return type.isInstance(sender);
    }
    
    public <T extends CommandSender> T as(Class<T> type) {
        return is(type) ? (T)sender : null;
    }
    
    public boolean isLocatable() {
        return !isConsole();
    }
    
    public Location getLocation() {
        return isLocatable() ? (isBlock() ? asBlock().getBlock().getLocation() : asEntity().getLocation()) : null;
    }
    
    public CommandSender getSender() {
        return sender;
    }
    
    public Plugin getPlugin() {
        return plugin;
    }
    
    public String getAlias() {
        return alias;
    }
    
    public int getLength() {
        return args.length;
    }
    
    public boolean isLength(int length) {
        return args.length == length;
    }
    
    public List<String> getArgs() {
        return Arrays.asList(args);
    }
    
    public boolean hasArgs() {
        return !isLength(0);
    }
    
    public String argAt(int index) {
        if (index < 0 || index >= args.length) return null;
        else return args[index];
    }
    
    public String joinArgs(int start, int finish) {
        if (!hasArgs() || start < 0 || start > finish || finish > args.length) return null;
        else return String.join(" ", getArgs().subList(start, finish));
        
    }
    
    public String joinArgs(int start) {
        return joinArgs(start, args.length);
    }
    
    public String joinArgs() {
        return joinArgs(0);
    }
    
    public void send(String message) {
        sender.sendMessage(message);
    }
    
    public void
    
}
