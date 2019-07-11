package com.njdaeger.bci.base;

import com.njdaeger.bci.SenderType;
import com.njdaeger.bci.Utils;
import com.njdaeger.bci.base.executors.CommandExecutor;
import com.njdaeger.bci.exceptions.ArgumentParseException;
import com.njdaeger.bci.exceptions.InvalidSenderException;
import com.njdaeger.bci.exceptions.NotEnoughArgsException;
import com.njdaeger.bci.exceptions.PermissionDeniedException;
import com.njdaeger.bci.exceptions.TooManyArgsException;
import com.njdaeger.bci.flags.LiveFlag;
import com.njdaeger.bci.types.ParsedType;
import com.njdaeger.bci.types.defaults.BooleanType;
import com.njdaeger.bci.types.defaults.DoubleType;
import com.njdaeger.bci.types.defaults.FloatType;
import com.njdaeger.bci.types.defaults.IntegerType;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "WeakerAccess", "unchecked"})
public abstract class AbstractCommandContext<C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>> {
    
    private final Map<String, LiveFlag> flags;
    protected final BCICommand<C, T> command;
    protected final CommandSender sender;
    protected final Plugin plugin;
    protected final String alias;
    protected String[] args;
    
    /**
     * Creates a new CommandContext
     * @param plugin The plugin creating this command context
     * @param command The command this command context is being created for
     * @param sender The command sender of this command
     * @param args The arguments used in this command
     * @param alias The alias used in the execution of this command.
     */
    public AbstractCommandContext(Plugin plugin, BCICommand<C, T> command, CommandSender sender, String[] args, String alias) {
        this.flags = new HashMap<>();
        this.command = command;
        this.plugin = plugin;
        this.sender = sender;
        this.alias = alias;
        this.args = args;
    }
    
    /**
     * This is used for the prefix in the {@link #pluginMessage(String)} method
     * @return The plugin message prefix.
     */
    protected abstract String getPluginMessagePrefix();
    
    /**
     * Gets a desired flag from the current execution
     * @param flag The flag to attempt to get
     * @return The LiveFlag, unless the flag doesnt exist, in that case it's null.
     */
    public LiveFlag getFlag(String flag) {
        return flags.get(flag);
    }
    
    /**
     * Check if this command context contains certain flags.
     * @param flag The flag to check
     * @return True if the flag was within the command, false otherwise.
     */
    public boolean hasFlag(String flag) {
        return flags.get(flag) != null;
    }

    //This is used to set the flags used in this particular command execution
    void setFlags(List<LiveFlag> flags) {
        flags.forEach(f -> this.flags.put(f.getFlag().getFlagString(), f));
    }
    
    //This is used to set the arguments after the flags and their parts have been excluded.
    void setArgs(String... args) {
        this.args = args;
    }
    
    /**
     * Gets the BCICommand object tied to this command context
     * @return The BCICommand object.
     */
    public BCICommand<C, T> getCommand() {
        return command;
    }
    
    /**
     * Whether this commands sender has the specified permission
     * @param permission The permission the command sender needs
     * @return True if the sender has the permission, false otherwise.
     */
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }
    
    /**
     * Whether this commands sender has any one of the given permissions
     * @param permissions The permissions to check
     * @return True if the sender has any one of the given permissions, false if they have none.
     */
    public boolean hasAnyPermission(String... permissions) {
        return Stream.of(permissions).anyMatch(sender::hasPermission);
    }
    
    /**
     * Whether this commands sender has all of the given permissions
     * @param permissions The permissions to check
     * @return True if the sender has all the given permissions, false otherwise.
     */
    public boolean hasAllPermissions(String... permissions) {
        return Stream.of(permissions).allMatch(sender::hasPermission);
    }
    
    /**
     * Check whether the command sender is the console or not.
     * @return True if the console executed the command, false otherwise.
     */
    public boolean isConsole() {
        return sender instanceof ConsoleCommandSender;
    }
    
    /**
     * Get the sender as a ConsoleCommandSender
     * @return The CommandSender as a ConsoleCommandSender, or null if the sender is not the console.
     */
    public ConsoleCommandSender asConsole() {
        return isConsole() ? (ConsoleCommandSender)sender : null;
    }
    
    /**
     * Check whether the command sender is a player or not.
     * @return True if a player executed this command, false otherwise.
     */
    public boolean isPlayer() {
        return sender instanceof Player;
    }
    
    /**
     * Get the sender as a Player
     * @return The CommandSender as a Player, or null if the sender is not a player.
     */
    public Player asPlayer() {
        return isPlayer() ? (Player)sender : null;
    }
    
    /**
     * Check whether the command sender is a block or not.
     * @return True if a block executed this command, false otherwise.
     */
    public boolean isBlock() {
        return sender instanceof BlockCommandSender;
    }
    
    /**
     * Get the sender as a BlockCommandSender
     * @return The CommandSender as a BlockCommandSender, or null if the sender is not a block.
     */
    public BlockCommandSender asBlock() {
        return isBlock() ? (BlockCommandSender)sender : null;
    }
    
    /**
     * Check whether the command sender is an entity or not.
     * @return True if an entity executed this command, false otherwise.
     */
    public boolean isEntity() {
        return sender instanceof Entity;
    }
    
    /**
     * Get the sender as an Entity
     * @return The CommandSender as an Entity, or null if the sender is not an entity.
     */
    public Entity asEntity() {
        return isEntity() ? (Entity)sender : null;
    }
    
    /**
     * Check whether the sender is of a specific type.
     * @param type A class which is a subclass of CommandSender
     * @param <S> The subclass type.
     * @return True if the sender is an instance of the given subclass. False otherwise.
     */
    public <S extends CommandSender> boolean is(Class<S> type) {
        return type.isInstance(sender);
    }
    
    /**
     * Get the sender as the specified type.
     * @param type The class type to get the sender as
     * @param <S> The type to return
     * @return The CommandSender as the specified subclass.
     */
    public <S extends CommandSender> S as(Class<S> type) {
        return is(type) ? (S)sender : null;
    }
    
    /**
     * Whether the sender is locatable. This is true for all types except the console.
     * @return True if the sender is locatable, false otherwise.
     */
    public boolean isLocatable() {
        return !isConsole();
    }
    
    /**
     * Get the location of the sender if they are locatable.
     * @return Get the location of the sender, or null if the sender is not locatable.
     */
    public Location getLocation() {
        return isLocatable() ? (isBlock() ? asBlock().getBlock().getLocation() : asEntity().getLocation()) : null;
    }
    
    /**
     * Get the base command sender
     * @return The CommandSender
     */
    public CommandSender getSender() {
        return sender;
    }
    
    /**
     * Get the type of sender of the current CommandSender
     * @return The current sender type.
     */
    public SenderType getSenderType() {
        return SenderType.of(sender);
    }
    
    /**
     * Check if this sender type is a certain type.
     * @param check The type being checked against
     * @return True if the sender and check match. False otherwise.
     */
    public boolean isSenderType(SenderType check) {
        return getSenderType().equals(check);
    }
    
    /**
     * Get the plugin this command executing this command.
     * @return The owning plugin
     */
    public Plugin getPlugin() {
        return plugin;
    }
    
    /**
     * Get the alias used in the execution of this command
     * @return The alias used in the execution of this command
     */
    public String getAlias() {
        return alias;
    }
    
    /**
     * Get how many arguments are in this command.
     * @return How many arguments were used in this commands execution
     */
    public int getLength() {
        return args.length;
    }
    
    /**
     * Check how many arguments this command contained upon execution
     * @param length The length to test
     * @return True if the length given and the amount of arguments matches.
     */
    public boolean isLength(int length) {
        return args.length == length;
    }

    /**
     * Check if the amount of arguments provided is greater than the specified number
     * @param length The length to be greater than
     * @return True if there are more arguments than the specified number
     */
    public boolean isGreater(int length) {
        return args.length > length;
    }

    /**
     * Check if the amount of arguments provided is greater than or equal to the specified number
     * @param length The length to be greater than or equal to
     * @return True if there are more or the same amount of arguments than the specified number
     */
    public boolean isGreaterOrEqual(int length) {
        return args.length >= length;
    }

    /**
     * Check if the amount of arguments provided is less than the specified number
     * @param length The length to be less than
     * @return True if there are less arguments than the specified number.
     */
    public boolean isLess(int length) {
        return args.length < length;
    }

    /**
     * Check if the amount of arguments provided is less than or equal to the specified number
     * @param length The length to be less than or equal to
     * @return True if there are less or the same amount of arguments than the specified number.
     */
    public boolean isLessOrEqual(int length) {
        return args.length <= length;
    }

    /**
     * Gets the first argument in the argument array
     * @return The first argument in the argument array, or null if there are no arguments
     */
    public String first() {
        return argAt(0);
    }

    public <P extends ParsedType<V>, V> V first(Class<P> asType) throws BCIException {
        if (first() != null) {
            try {
                return asType.getDeclaredConstructor().newInstance().parse(first());
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        throw new ArgumentParseException("Argument at index 0 is not parsable to the type " + asType.getSimpleName());
    }

    public <P extends ParsedType<V>, V> V first(Class<P> asType, V defaultValue) {
        try {
            return isArgAt(0 ,asType) ? argAt(0, asType) : defaultValue;
        }
        catch (BCIException ignored) {}
        return defaultValue;
    }

    /**
     * Gets the last argument in the argument array
     * @return The last argument in the argument array, or null if there are no arguments
     */
    public String last() {
        return argAt(args.length-1);
    }

    /**
     * Gets the last argument in the argument array as a specific type.
     * @param asType The argument parser type
     * @param <P> The ParsedType
     * @param <V> The value returned
     * @return The parsed argument as the specified value
     * @throws BCIException If the argument at the specified index is not parsable to the specified type, or if the last arg is null.
     */
    public <P extends ParsedType<V>, V> V last(Class<P> asType) throws BCIException {
        if (last() != null) {
            try {
                return asType.getDeclaredConstructor().newInstance().parse(last());
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        throw new ArgumentParseException("Argument at index " + args.length + " is not parsable to the type " + asType.getSimpleName());
    }

    /**
     * Gets the last argument in the argument array as a specific type
     * @param asType The argument parser type
     * @param defaultValue The value to return if the argument is not parsable
     * @param <P> The ParsedType
     * @param <V> The value returned
     * @return The parsed argument if it is parsable, the default argument is returned otherwise.
     */
    public <P extends ParsedType<V>, V> V last(Class<P> asType, V defaultValue) {
        try {
            return isArgAt(args.length-1 ,asType) ? argAt(args.length-1, asType) : defaultValue;
        }
        catch (BCIException ignored) {}
        return defaultValue;
    }

    /**
     * Gets the argument at the desired index parsed as the specified type
     * @param index The index to parse
     * @param asType The argument parser type
     * @param <P> The ParsedType
     * @param <V> The value returned
     * @return The parsed argument as the specified value
     * @throws ArgumentParseException If the argument at the specified index is not parsable to the specified type. or if the index arg is null
     */
    public <P extends ParsedType<V>, V> V argAt(int index, Class<P> asType) throws BCIException {
        if (hasArgAt(index)) {
            try {
                return asType.getDeclaredConstructor().newInstance().parse(argAt(index));
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        throw new ArgumentParseException("Argument at index " + index + " is not parsable to the type " + asType.getSimpleName());
    }
    
    /**
     * Gets the argument at the desired index parsed as the specified type
     * @param index The index to parse
     * @param asType The argument parser type
     * @param defaultValue default return value
     * @param <P> The ParsedType
     * @param <V> The value returned
     * @return The parsed argument as the specified value, or the default value if there was a parsing error
     */
    public <P extends ParsedType<V>, V> V argAt(int index, Class<P> asType, V defaultValue) {
        try {
            return isArgAt(index,asType) ? argAt(index, asType) : defaultValue;
        }
        catch (BCIException ignored) {}
        return defaultValue;
    }
    
    /**
     * Checks if the argument at the specified index is parsable by the given parser type
     * @param index The index to check
     * @param type The argument parser type
     * @param <P> The ParsedType
     * @return True if the argument at the given index can be parsed by the given parser, false otherwise.
     */
    public <P extends ParsedType> boolean isArgAt(int index, Class<P> type) {
        try {
            argAt(index, type);
        }
        catch (BCIException e) {
            return false;
        }
        return true;
    }
    
    /**
     * Gets the desired argument as a boolean
     * @param index The index to get the boolean at
     * @return The boolean value of the argument
     * @throws ArgumentParseException If the argument at the specified index is not parsable as a boolean
     */
    public Boolean booleanAt(int index) throws BCIException {
        return argAt(index, BooleanType.class);
    }
    
    /**
     * Gets the desired argument as a boolean
     * @param index The index to get the boolean at
     * @param defaultValue The default return value
     * @return The boolean value of the argument, or the default if it could not be parsed.
     */
    public Boolean booleanAt(int index, Boolean defaultValue) {
        return argAt(index, BooleanType.class, defaultValue);
    }
    
    /**
     * Checks if the argument at the specified index is a boolean or not
     * @param index The index to check
     * @return True if the argument at the given index can be parsed as a boolean, false otherwise
     */
    public boolean isBooleanAt(int index) {
        return isArgAt(index, BooleanType.class);
    }
    
    /**
     * Gets the desired argument as an integer
     * @param index The index to get the integer at
     * @return The integer value of the argument
     * @throws ArgumentParseException If the argument at the specified index is not parsable as an integer
     */
    public Integer integerAt(int index) throws BCIException {
        return argAt(index, IntegerType.class);
    }
    
    /**
     * Gets the desired argument as an integer
     * @param index The index to get the integer at
     * @param defaultValue The default return value
     * @return The integer value of the argument, or the default if it could not be parsed
     */
    public Integer integerAt(int index, Integer defaultValue) {
        return argAt(index, IntegerType.class, defaultValue);
    }
    
    /**
     * Checks if the argument at the specified index is an integer or not
     * @param index The index to check
     * @return True if the argument at the given index can be parsed as an integer, false otherwise
     */
    public boolean isIntegerAt(int index) {
        return isArgAt(index, IntegerType.class);
    }
    
    /**
     * Gets the desired argument as a double
     * @param index The index to get the double at
     * @return The double value of the argument
     * @throws ArgumentParseException If the argument at the specified index is not parsable as a double
     */
    public Double doubleAt(int index) throws BCIException {
        return argAt(index, DoubleType.class);
    }
    
    /**
     * Gets the desired argument as a double
     * @param index The index to get the double at
     * @param defaultValue The default return value
     * @return The double value of the argument, or the default if it could not be parsed
     */
    public Double doubleAt(int index, Double defaultValue) {
        return argAt(index, DoubleType.class, defaultValue);
    }
    
    /**
     * Checks if the argument at the specified index is a double or not
     * @param index The index to check
     * @return True if the argument at the given index can be parsed as a double, false otherwise
     */
    public boolean isDoubleAt(int index) {
        return isArgAt(index, DoubleType.class);
    }
    
    /**
     * Gets the desired argument as a float
     * @param index The index to get the float at
     * @return The float value of the argument
     * @throws ArgumentParseException If the argument at the specified index is not parsable as a float
     */
    public Float floatAt(int index) throws BCIException {
        return argAt(index, FloatType.class);
    }
    
    /**
     * Gets the desired argument as a float
     * @param index The index to get the float at
     * @param defaultValue The default return value
     * @return The float value of the argument, or the default if it could not be parsed
     */
    public Float floatAt(int index, Float defaultValue) {
        return argAt(index, FloatType.class, defaultValue);
    }
    
    /**
     * Checks if the argument at the specified index is a float ot not
     * @param index The index to check
     * @return True if the argument at the given index can be parsed as a float, false otherwise.
     */
    public boolean isFloatAt(int index) {
        return isArgAt(index, FloatType.class);
    }
    
    /**
     * Gets a list of all the arguments which were used in this execution
     * @return A list of current arguments
     */
    public List<String> getArgs() {
        return Arrays.asList(args);
    }
    
    /**
     * Whether this command had any arguments this execution
     * @return True if the length of arguments is greater than 0
     */
    public boolean hasArgs() {
        return !isLength(0);
    }
    
    /**
     * Whether this command has an argument at the specified index
     * @param index The index to check the argument for
     * @return true if the current execution has arguments at the given index, false otherwise.
     */
    public boolean hasArgAt(int index) {
        return argAt(index) != null;
    }
    
    /**
     * Whether the arguments has the specified arguments at the given index.
     * @param index Index to check the argument for
     * @param match The argument to match
     * @return True if the argument exists at the index and the argument matches the given string
     */
    public boolean hasArgAt(int index, String match) {
        return hasArgAt(index) && match.matches(argAt(index));
    }
    
    /**
     * Get an argument at the given index.
     * @param index The index to get the argument from
     * @return The argument at the desired index, or null if none exists/index is out of bounds.
     */
    public String argAt(int index) {
        if (index < 0 || index >= args.length) return null;
        else return args[index];
    }
    
    /**
     * Joins all the arguments between the two index points.
     * @param start (inclusive) the start point
     * @param finish (exclusive) the ending point
     * @return A string with all the arguments from the starting index to the finishing index. or null if any one of the
     * starting/finishing indexes are out of bounds.
     */
    public String joinArgs(int start, int finish) {
        if (!hasArgs() || start < 0 || start > finish || finish > args.length) return null;
        else return String.join(" ", getArgs().subList(start, finish));
        
    }
    
    /**
     * Joins all the arguments from the starting index onwards
     * @param start (inclusive) The starting point of the joining
     * @return A string containing all the strings from the starting index onwards.
     */
    public String joinArgs(int start) {
        return joinArgs(start, args.length);
    }
    
    /**
     * Creates one string with all the arguments in it.
     * @return A string containing all the currently used arguments
     */
    public String joinArgs() {
        return joinArgs(0);
    }
    
    /**
     * Sends an object to the player
     * @param object The object to send
     */
    public void send(Object object) {
        send(object.toString());
    }
    
    /**
     * Sends the sender a message
     * @param message The message to send
     */
    public void send(String message) {
        sender.sendMessage(message);
    }
    
    /**
     * Sends the sender a message with placeholders replaced with the given objects
     * @param message The message to send
     * @param placeholders The main placeholders to use to replace.
     *
     * <p>
     * Example usage:
     * <p>
     * <code>send("Hello, {0}! Welcome to the server. You are in world {1}, {0}.", "NJDaeger", "MyWorld")</code>
     * <p>
     * Would send: "Hello, NJDaeger! Welcome to the server. You are in world MyWorld, NJDaeger."
     */
    public void send(String message, Object... placeholders) {
        send(Utils.formatString(message, placeholders));
    }
    
    public void pluginMessage(Object object) {
        pluginMessage(object.toString());
    }
    
    /**
     * Sends the sender a plugin message with the prefix from {@link #getPluginMessagePrefix()}
     * @param message The message to send.
     */
    public void pluginMessage(String message) {
        send(getPluginMessagePrefix() + " " + message);
    }
    
    /**
     * Sends the sender a plugin message with the prefix from {@link #getPluginMessagePrefix()} and the placeholders replaced with the given objects.
     * @param message The message to send
     * @param placeholders The main placeholders to replace. See {@link #send(String, Object...)} to view how placeholders work.
     */
    public void pluginMessage(String message, Object... placeholders) {
        pluginMessage(Utils.formatString(message, placeholders));
    }
    
    /**
     * Sends the default no permissions message to the sender
     */
    public void noPermission() throws BCIException {
        throw new PermissionDeniedException();
    }
    
    /**
     * Sends a no permissions message to the sender
     * @param message The message to send
     */
    public void noPermission(String message) throws BCIException {
        throw new PermissionDeniedException(message);
    }
    
    /**
     * Sends the default too many arguments message to the sender
     */
    public void tooManyArgs() throws BCIException {
        throw new TooManyArgsException();
    }
    
    /**
     * Sends a too many arguments message to the sender
     * @param message The message to send
     */
    public void tooManyArgs(String message) throws BCIException {
        throw new TooManyArgsException(message);
    }
    
    /**
     * Sends the default not enough arguments message to the sender
     */
    public void notEnoughArgs() throws BCIException {
        throw new NotEnoughArgsException();
    }
    
    /**
     * Sends a not enough arguments message to the sender
     * @param message The message to send
     */
    public void notEnoughArgs(String message) throws BCIException {
        throw new NotEnoughArgsException(message);
    }
    
    /**
     * Sends the default invalid sender message to the sender.
     */
    public void invalidSender() throws BCIException {
        throw new InvalidSenderException();
    }
    
    /**
     * Sends an invalid sender message to the sender
     * @param message The message to send
     */
    public void invalidSender(String message) throws BCIException {
        throw new InvalidSenderException(message);
    }
    
    /**
     * Runs a sub command of this command if the sender matches the given sender type
     *
     * @param senderType The type of sender needed to run the subCommand
     * @param executor   The command method (method reference)
     * @return true if the senderType passed. False otherwise
     */
    public boolean subCommand(SenderType senderType, CommandExecutor<C> executor) throws BCIException {
        if (getSenderType() == senderType) {
            executor.execute((C)this);
            return true;
        }
        return false;
    }
    
    /**
     * Runs a sub command of this command if the index of the command matches the given index
     *
     * @param index    The index needed to run the command
     * @param executor The command method (method reference)
     * @return true if the index passed. False otherwise.
     */
    public boolean subCommandAt(int index, CommandExecutor<C> executor) throws BCIException {
        if (isGreaterOrEqual(index + 1)) {
            executor.execute((C)this);
            return true;
        }
        return false;
    }
    
    /**
     * Runs a sub command of this command if the index of the command matches the given argument
     *
     * @param index index to look for the specified argument
     * @param match The argument needed to be matched
     * @param ignoreCase Ignores the case of the given argument
     * @param executor The command method (method reference)
     * @return true if the arg at the specified index matches the given string
     */
    public boolean subCommandAt(int index, String match, boolean ignoreCase, CommandExecutor<C> executor) throws BCIException {
        if (isGreaterOrEqual(index + 1) && (ignoreCase ? argAt(index).equalsIgnoreCase(match) : argAt(index).equals(match))) {
            executor.execute((C)this);
            return true;
        }
        return false;
    }
    
    /**
     * Runs a sub command of this command if the index matches the given index and if the sender matches the given sender type
     *
     * @param index      The index needed to run the command
     * @param senderType The type of sender needed to run the subCommand
     * @param executor   The command method (method reference)
     * @return true if the index and the senderType passed. False otherwise
     */
    public boolean subCommandAt(int index, SenderType senderType, CommandExecutor<C> executor) throws BCIException {
        if (isGreaterOrEqual(index + 1) && getSenderType() == senderType) {
            executor.execute((C)this);
            return true;
        }
        return false;
    }
    
    /**
     * Runs a sub command of this command if the predicate passes.
     *
     * @param predicate The predicate needed to pass
     * @param executor  The command method (method reference)
     * @return true if the predicate passed. False otherwise
     */
    public boolean subCommand(Predicate<AbstractCommandContext<C, T>> predicate, CommandExecutor<C> executor) throws BCIException {
        if (predicate.test(this)) {
            executor.execute((C)this);
            return true;
        }
        return false;
    }
    
}
