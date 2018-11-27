package com.njdaeger.bci.base;

import com.njdaeger.bci.SenderType;
import com.njdaeger.bci.Utils;
import com.njdaeger.bci.base.executors.TabExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess"})
public abstract class AbstractTabContext<C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>> {

    private final List<String> possible;
    private final BCICommand<C, T> command;
    private final C commandContext;
    private final String[] args;

    /**
     * Creates a new TabContext
     *
     * @param commandContext The Command Context executing this command
     */
    public AbstractTabContext(C commandContext) {
        this.command = commandContext.command;
        this.commandContext = commandContext;
        this.possible = new ArrayList<>();
        this.args = commandContext.args;
    }

    /**
     * Whether this commands sender has the specified permission
     *
     * @param permission The permission the command sender needs
     * @return True if the sender has the permission, false otherwise.
     */
    public boolean hasPermission(String permission) {
        return commandContext.hasPermission(permission);
    }

    /**
     * Whether this commands sender has any one of the given permissions
     *
     * @param permissions The permissions to check
     * @return True if the sender has any one of the given permissions, false if they have none.
     */
    public boolean hasAnyPermission(String... permissions) {
        return commandContext.hasAnyPermission(permissions);
    }

    /**
     * Whether this commands sender has all of the given permissions
     *
     * @param permissions The permissions to check
     * @return True if the sender has all the given permissions, false otherwise.
     */
    public boolean hasAllPermissions(String... permissions) {
        return commandContext.hasAllPermissions(permissions);
    }

    /**
     * Check whether the command sender is the console or not.
     *
     * @return True if the console executed the command, false otherwise.
     */
    public boolean isConsole() {
        return getSender() instanceof ConsoleCommandSender;
    }

    /**
     * Get the sender as a ConsoleCommandSender
     *
     * @return The CommandSender as a ConsoleCommandSender, or null if the sender is not the console.
     */
    public ConsoleCommandSender asConsole() {
        return isConsole() ? (ConsoleCommandSender) getSender() : null;
    }

    /**
     * Check whether the command sender is a player or not.
     *
     * @return True if a player executed this command, false otherwise.
     */
    public boolean isPlayer() {
        return getSender() instanceof Player;
    }

    /**
     * Get the sender as a Player
     *
     * @return The CommandSender as a Player, or null if the sender is not a player.
     */
    public Player asPlayer() {
        return isPlayer() ? (Player) getSender() : null;
    }

    /**
     * Check whether the command sender is a block or not.
     *
     * @return True if a block executed this command, false otherwise.
     */
    public boolean isBlock() {
        return getSender() instanceof BlockCommandSender;
    }

    /**
     * Get the sender as a BlockCommandSender
     *
     * @return The CommandSender as a BlockCommandSender, or null if the sender is not a block.
     */
    public BlockCommandSender asBlock() {
        return isBlock() ? (BlockCommandSender) getSender() : null;
    }

    /**
     * Check whether the command sender is an entity or not.
     *
     * @return True if an entity executed this command, false otherwise.
     */
    public boolean isEntity() {
        return getSender() instanceof Entity;
    }

    /**
     * Get the sender as an Entity
     *
     * @return The CommandSender as an Entity, or null if the sender is not an entity.
     */
    public Entity asEntity() {
        return isEntity() ? (Entity) getSender() : null;
    }

    /**
     * Check whether the sender is of a specific type.
     *
     * @param type A class which is a subclass of CommandSender
     * @param <S> The subclass type.
     * @return True if the sender is an instance of the given subclass. False otherwise.
     */
    public <S extends CommandSender> boolean is(Class<S> type) {
        return type.isInstance(getSender());
    }

    /**
     * Get the sender as the specified type.
     *
     * @param type The class type to get the sender as
     * @param <S> The type to return
     * @return The CommandSender as the specified subclass.
     */
    public <S extends CommandSender> S as(Class<S> type) {
        return is(type) ? (S) getSender() : null;
    }

    /**
     * Whether the sender is locatable. This is true for all types except the console.
     *
     * @return True if the sender is locatable, false otherwise.
     */
    public boolean isLocatable() {
        return !isConsole();
    }

    /**
     * Get the location of the sender if they are locatable.
     *
     * @return Get the location of the sender, or null if the sender is not locatable.
     */
    public Location getLocation() {
        return isLocatable() ? (isBlock() ? asBlock().getBlock().getLocation() : asEntity().getLocation()) : null;
    }

    /**
     * Get the base command sender
     *
     * @return The CommandSender
     */
    public CommandSender getSender() {
        return commandContext.getSender();
    }

    /**
     * Get the type of sender of the current CommandSender
     *
     * @return The current sender type.
     */
    public SenderType getSenderType() {
        return SenderType.of(getSender());
    }

    /**
     * Check if this sender type is a certain type.
     *
     * @param check The type being checked against
     * @return True if the sender and check match. False otherwise.
     */
    public boolean isSenderType(SenderType check) {
        return getSenderType().equals(check);
    }

    /**
     * Get the plugin this command executing this command.
     *
     * @return The owning plugin
     */
    public Plugin getPlugin() {
        return commandContext.getPlugin();
    }

    /**
     * Get the alias used in the execution of this command
     *
     * @return The alias used in the execution of this command
     */
    public String getAlias() {
        return commandContext.getAlias();
    }

    /**
     * Get the command context used to run this command
     *
     * @return This commands current command context
     */
    public C getCommandContext() {
        return commandContext;
    }

    /**
     * Gets the BCICommand object tied to this command context
     *
     * @return The BCICommand object.
     */
    public BCICommand<C, T> getCommand() {
        return command;
    }

    /**
     * Get how many arguments are in this command.
     *
     * @return How many arguments were used in this commands execution
     */
    public int getLength() {
        return args.length;
    }

    /**
     * Check how many arguments are currently in the arguments array
     *
     * @param length The length to test
     * @return True if the length given and the amount of arguments matches.
     */
    public boolean isLength(int length) {
        return getLength() == length;
    }

    /**
     * Gets the previous argument from the current index. If the current index is
     *
     * @return The previous argument
     */
    public String getPrevious() {
        return getLength() <= 1 ? null : argAt(getLength() - 2);
    }

    /**
     * Checks if the previous arg matches a specific regex pattern.
     *
     * @param pattern The pattern to match
     * @return True if the pattern matches, false if the previous arg is null or if the pattern doesn't match.
     */
    public boolean isPrevious(Pattern pattern) {
        return getPrevious() != null && getPrevious().matches(pattern.pattern());
    }

    /**
     * Checks if the previous arg matches a specific string. Not case sensitive.
     *
     * @param previous The previous text to match
     * @return True if it matches, false if the previous is null or if it doesn't match.
     */
    public boolean isPrevious(String previous) {
        return isPrevious(true, previous);
    }

    /**
     * Checks if the previous arg matches a specific string. This can be set to be case sensitive
     *
     * @param ignoreCase True ignores case, false does not.
     * @param previous The previous text to match
     * @return True if it matches, false if the previous is null or if it doesn't match
     */
    public boolean isPrevious(boolean ignoreCase, String previous) {
        return getPrevious() != null && (ignoreCase ? getPrevious().equalsIgnoreCase(previous) : getPrevious().equals(previous));
    }

    /**
     * Gets the current argument being typed
     *
     * @return The current arg being typed
     */
    public String getCurrent() {
        return isLength(1) ? null : argAt(getLength() - 1);
    }

    /**
     * Checks if the current argument being typed matches the specific pattern.
     *
     * @param pattern The pattern to match
     * @return True if it matches, false if the current arg is null or if the pattern doesnt match
     */
    public boolean isCurrent(Pattern pattern) {
        return getCurrent() != null && getCurrent().matches(pattern.pattern());
    }

    /**
     * Checks if the current argument being typed matches the specific string. This is not case sensitive
     *
     * @param current The text to match
     * @return True if it matches, false if the current is null or if it doesnt match
     */
    public boolean isCurrent(String current) {
        return isCurrent(true, current);
    }

    /**
     * Checks if the current argument being typed matches the specific string. This can be set to be case sensitive
     *
     * @param ignoreCase True ignores case, false does not.
     * @param current The text to match
     * @return True if it matches, false if the current arg is null or if it doesnt match
     */
    public boolean isCurrent(boolean ignoreCase, String current) {
        return getCurrent() != null && (ignoreCase ? getCurrent().equalsIgnoreCase(current) : getCurrent().equals(current));
    }

    /**
     * Gets an argument at a specific index
     *
     * @param index The index to get the arg from
     * @return The arg, null if it doesn't exist or if its out of bounds.
     */
    public String argAt(int index) {
        if (index < 0 || index >= args.length) return null;
        else return args[index];
    }

    /**
     * Gets all the args used in the execution of this command. This includes the flags being used.
     *
     * @return A list of all the arguments used in the execution of this command
     */
    public List<String> getArgs() {
        return Arrays.asList(args);
    }

    /**
     * Checks if the command ran had any arguments or not. This includes the flags being used.
     *
     * @return True if the command had arguments, false otherwise.
     */
    public boolean hasArgs() {
        return getArgs().isEmpty();
    }

    /**
     * Sends a message to the player/console.
     *
     * @param message The message to send.
     */
    public void send(String message) {
        getSender().sendMessage(message);
    }

    /**
     * Sends the sender a regular message with placeholder parsing
     *
     * @param message The message to send
     * @param placeholders The placeholders to use
     */
    public void send(String message, Object... placeholders) {
        send(Utils.formatString(message, placeholders));
    }

    /**
     * Sends a formatted plguin message.
     * <p>
     * <p>Ex. {@link AbstractCommandContext#getPluginMessagePrefix()} MESSAGE FROM THE PARAMETER</p>
     *
     * @param message The message to send with the plugin name before it.
     */
    public void pluginMessage(String message) {
        send(commandContext.getPluginMessagePrefix() + " " + message);
    }

    /**
     * Sends a formatted plugin message.
     *
     * @param message The message to format
     * @param placeholders The objects used to replace the placeholders.
     */
    public void pluginMessage(String message, Object... placeholders) {
        pluginMessage(Utils.formatString(message, placeholders));
    }

    /**
     * Creates a player completion list at the current index.
     */
    public void playerCompletion() {
        possible.clear();
        possible.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
    }

    /**
     * Creates a player completion list at the specified index.
     *
     * @param index The index to create the completions
     */
    public void playerCompletionAt(int index) {
        if (isLength(index+1)) playerCompletion();
    }

    /**
     * Creates a player completion list after the previous argument matches the given pattern.
     *
     * @param pattern The pattern to match
     */
    public void playerCompletionAfter(Pattern pattern) {
        if (isPrevious(pattern)) playerCompletion();
    }

    /**
     * Creates a player completion list after the previous argument matches the given string. This is not case
     * sensitive.
     *
     * @param previousArg The previous arg to match
     */
    public void playerCompletionAfter(String previousArg) {
        if (isPrevious(previousArg)) playerCompletion();
    }

    /**
     * Creates a player completion list after the previous argument matches the given string. The string can be case
     * sensitive.
     *
     * @param ignoreCase Whether to ignore case or not
     * @param previousArg The previous arg to match
     */
    public void playerCompletionAfter(boolean ignoreCase, String previousArg) {
        if (isPrevious(ignoreCase, previousArg)) playerCompletion();
    }

    /**
     * Creates a player completion list only if the predicate is true.
     *
     * @param predicate The predicate to test
     */
    public void playerCompletionIf(Predicate<T> predicate) {
        if (predicate.test((T) this)) playerCompletion();
    }

    /**
     * Creates a completion list from the given arguments at the current index
     *
     * @param completions The arguments to add to the completion list
     */
    public void completion(String... completions) {
        possible.clear();
        possible.addAll(Arrays.asList(completions));
    }

    /**
     * Creates a completion list from the given arguments at the specified index.
     *
     * @param index The index to create the completions
     * @param completions The arguments to add to the completion list.
     */
    public void completionAt(int index, String... completions) {
        if (isLength(index+1)) completion(completions);
    }

    /**
     * Creates a completion list from the given arguments after the previous argument matches the specified pattern.
     *
     * @param pattern The pattern needed to match
     * @param completions The arguments to add to the completion list.
     */
    public void completionAfter(Pattern pattern, String... completions) {
        if (isPrevious(pattern)) completion(completions);
    }

    /**
     * Creates a completion list from the given arguments after the previous argument matches the specified string. This
     * is not case sensitive
     *
     * @param previousArg The previous arg to match
     * @param completions The arguments to add to the completion list.
     */
    public void completionAfter(String previousArg, String... completions) {
        if (isPrevious(previousArg)) completion(completions);
    }

    /**
     * Creates a completion list from the given arguments after the previous argument matches the specified string. This
     * can be set to case sensitive.
     *
     * @param ignoreCase Whether to ignore case or not.
     * @param previousArg The previous arg needed to be matched
     * @param completions The arguments to add to the completion list.
     */
    public void completionAfter(boolean ignoreCase, String previousArg, String... completions) {
        if (isPrevious(ignoreCase, previousArg)) completion(completions);
    }

    /**
     * Creates a completion list from the given arguments after the predicate given is true.
     *
     * @param predicate The predicate to test
     * @param completions The arguments to add to the completion list.
     */
    public void completionIf(Predicate<T> predicate, String... completions) {
        if (predicate.test((T) this)) completion(completions);
    }

    /**
     * Creates a completion list from the given function
     *
     * @param function The function to apply
     */
    public void completion(Function<T, List<String>> function) {
        possible.clear();
        possible.addAll(function.apply((T) this));
    }

    /**
     * Creates a completion list from the given function at the specified index.
     *
     * @param index The index to use the completions
     * @param function The function to apply
     */
    public void completionAt(int index, Function<T, List<String>> function) {
        if (isLength(index+1)) completion(function);
    }

    /**
     * Creates a completion list from the given function after the previous argument matches the given regex
     *
     * @param pattern The pattern to match
     * @param function The function to apply
     */
    public void completionAfter(Pattern pattern, Function<T, List<String>> function) {
        if (isPrevious(pattern)) completion(function);
    }

    /**
     * Creates a completion list from the given function after the previous argument matches the given string. This is
     * not case sensitive
     *
     * @param previousArg The previous arg to match
     * @param function The function to apply
     */
    public void completionAfter(String previousArg, Function<T, List<String>> function) {
        if (isPrevious(previousArg)) completion(function);
    }

    /**
     * Creates a completion list from the given function after the previous argument matches the given string. This can
     * be set to case sensitive
     *
     * @param ignoreCase Whether to ignore case or not
     * @param previousArg The previous arg to match
     * @param function The function to apply
     */
    public void completionAfter(boolean ignoreCase, String previousArg, Function<T, List<String>> function) {
        if (isPrevious(ignoreCase, previousArg)) completion(function);
    }

    /**
     * Creates a completion list from the given function after the given predicate is true.
     *
     * @param predicate The predicate to match
     * @param function The function to apply
     */
    public void completionIf(Predicate<T> predicate, Function<T, List<String>> function) {
        if (predicate.test((T) this)) completion(function);
    }

    /**
     * Runs a sub completion of this tab completion at the current index
     *
     * @param executor The tab executor (typically method reference)
     * @throws BCIException If the completion was unsuccessful.
     */
    public void subCompletion(TabExecutor<T> executor) throws BCIException {
        executor.complete((T) this);
    }

    /**
     * Runs a sub completion of this tab completion at the current index if the sender is the correct type.
     *
     * @param senderType The sender type to match
     * @param executor The tab executor (typically method reference)
     * @return True if the sender type matched, false otherwise.
     * @throws BCIException If the completion was unsuccessful.
     */
    public boolean subCompletion(SenderType senderType, TabExecutor<T> executor) throws BCIException {
        if (SenderType.of(getSender()).equals(senderType)) {
            subCompletion(executor);
            return true;
        }
        return false;
    }

    /**
     * Runs a sub completion of this tab completion at the specified index if the current index matches it.
     *
     * @param index The index needed to run this completion
     * @param executor The tab executor (typically method reference)
     * @return True if the sender type matched, false otherwise.
     * @throws BCIException If the completion was unsuccessful.
     */
    public boolean subCompletionAt(int index, TabExecutor<T> executor) throws BCIException {
        if (isLength(index+1)) {
            subCompletion(executor);
            return true;
        }
        return false;
    }

    /**
     * Runs a sub completion of this tab completion at the specified index if the sender is the correct type.
     *
     * @param index The index needed to be able to run this completion
     * @param senderType The sender type to match
     * @param executor The tab executor (typically method reference)
     * @return True if the sender type matched the current sender type and index matched the current index, false
     *         otherwise.
     * @throws BCIException If the completion was unsuccessful.
     */
    public boolean subCompletionAt(int index, SenderType senderType, TabExecutor<T> executor) throws BCIException {
        if (isLength(index+1)) {
            return subCompletion(senderType, executor);
        }
        return false;
    }

    /**
     * Runs a sub completion of this tab completion at the specified index if the previous argument matches the given
     * pattern
     *
     * @param index The index needed to be able to run this completion
     * @param pattern The pattern needed to be matched
     * @param executor The tab executor (typically method reference)
     * @return True if the pattern matched the previous arg and the index matched the current index, false otherwise.
     * @throws BCIException If the completion was unsuccessful
     */
    public boolean subCompletionAt(int index, Pattern pattern, TabExecutor<T> executor) throws BCIException {
        if (isLength(index+1) && isPrevious(pattern)) {
            subCompletion(executor);
            return true;
        }
        return false;
    }

    /**
     * Runs a sub completion of this tab completion at the specified index if the previous argument matches the given
     * string. This is not case sensitive
     *
     * @param index The index needed to be able to run this completion
     * @param previousArg The previous argument to match
     * @param executor The tab executor (typically method reference)
     * @return True if the string given matched the previous arg and the index matched the current index, false
     *         otherwise.
     * @throws BCIException If the completion was unsuccessful
     */
    public boolean subCompletionAt(int index, String previousArg, TabExecutor<T> executor) throws BCIException {
        return subCompletionAt(index, true, previousArg, executor);
    }

    /**
     * Runs a sub completion of this tab completion at the specified index if the previous argument matches the given
     * string. This is able to be case sensitive
     *
     * @param index The index needed to be able to run this completion
     * @param ignoreCase Whether this is case sensitive or not.
     * @param previousArg The previous argument to match
     * @param executor The tab executor (typically method reference)
     * @return True if the string given matched the previous arg and the index matched the current index, false
     *         otherwise.
     * @throws BCIException If the completion was unsuccessful
     */
    public boolean subCompletionAt(int index, boolean ignoreCase, String previousArg, TabExecutor<T> executor) throws BCIException {
        if (isLength(index+1) && isPrevious(ignoreCase, previousArg)) {
            subCompletion(executor);
            return true;
        }
        return false;
    }

    /**
     * Runs a sub completion of this tab completion at the specified index if the given predicate is true.
     *
     * @param predicate The predicate to test
     * @param executor The tab executor (typically method reference)
     * @return True if the predicate was true, false otherwise.
     * @throws BCIException If the completion was unsuccessful.
     */
    public boolean subCompletionIf(Predicate<T> predicate, TabExecutor<T> executor) throws BCIException {
        if (predicate.test((T) this)) {
            subCompletion(executor);
            return true;
        }
        return false;
    }

    //Get the current completions
    List<String> currentPossibleCompletions() {
        return possible;
    }

}
