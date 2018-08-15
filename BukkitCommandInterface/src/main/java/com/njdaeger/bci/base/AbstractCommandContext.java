package com.njdaeger.bci.base;

import com.njdaeger.bci.Utils;
import com.njdaeger.bci.arguments.ArgumentMap;
import com.njdaeger.bci.arguments.ArgumentParser;
import com.njdaeger.bci.arguments.ArgumentTrack;
import com.njdaeger.bci.exceptions.ArgumentParseException;
import com.njdaeger.bci.exceptions.InvalidSenderException;
import com.njdaeger.bci.exceptions.NotEnoughArgsException;
import com.njdaeger.bci.exceptions.PermissionDeniedException;
import com.njdaeger.bci.exceptions.TooManyArgsException;
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

@SuppressWarnings({"unused", "WeakerAccess", "unchecked"})
public abstract class AbstractCommandContext<C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>> {
    
    protected final BCICommand<C, T> command;
    protected final CommandSender sender;
    protected final Plugin plugin;
    protected final String[] args;
    protected final String alias;
    
    public AbstractCommandContext(Plugin plugin, BCICommand<C, T> command, CommandSender sender, String[] args, String alias) {
        this.command = command;
        this.plugin = plugin;
        this.sender = sender;
        this.alias = alias;
        this.args = args;
    }
    
    protected abstract String getPluginMessagePrefix();
    
    public ArgumentMap<C, T> getArgumentMap() {
        return command.getArgumentMap();
    }
    
    public ArgumentTrack getArgumentTrack(int index) throws ArgumentParseException {
        return new ArgumentParser(this).parse();
    }
    
    public BCICommand<C, T> getCommand() {
        return command;
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
    
    public <S extends CommandSender> boolean is(Class<S> type) {
        return type.isInstance(sender);
    }
    
    public <S extends CommandSender> S as(Class<S> type) {
        return is(type) ? (S)sender : null;
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
    
    public boolean hasArgAt(int index) {
        return argAt(index) != null;
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
    
    public void send(String message, Object... placeholders) {
        send(Utils.formatString(message, placeholders));
    }
    
    public void pluginMessage(String message) {
        send(getPluginMessagePrefix() + " " + message);
    }
    
    public void pluginMessage(String message, Object... placeholders) {
        pluginMessage(Utils.formatString(message, placeholders));
    }
    
    public void noPermission() throws BCIException {
        throw new PermissionDeniedException();
    }
    
    public void noPermission(String message) throws BCIException {
        throw new PermissionDeniedException(message);
    }
    
    public void tooManyArgs() throws BCIException {
        throw new TooManyArgsException();
    }
    
    public void tooManyArgs(String message) throws BCIException {
        throw new TooManyArgsException(message);
    }
    
    public void notEnoughArgs() throws BCIException {
        throw new NotEnoughArgsException();
    }
    
    public void notEnoughArgs(String message) throws BCIException {
        throw new NotEnoughArgsException(message);
    }
    
    public void invalidSender() throws BCIException {
        throw new InvalidSenderException();
    }
    
    public void invalidSender(String message) throws BCIException {
        throw new InvalidSenderException(message);
    }
    
}
