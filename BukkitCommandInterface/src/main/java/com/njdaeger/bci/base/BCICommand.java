package com.njdaeger.bci.base;

import com.njdaeger.bci.SenderType;
import com.njdaeger.bci.base.executors.CommandExecutor;
import com.njdaeger.bci.base.executors.TabExecutor;
import com.njdaeger.bci.flags.AbstractFlag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public class BCICommand<C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>> {
    
    private final String name;
    
    private int maxArgs = -1;
    private int minArgs = -1;
    private String usage = "";
    private String[] aliases = new String[0];
    private String description = "";
    private String[] permissions = null;
    private SenderType[] senderTypes = null;
    private TabExecutor<T> tabExecutor = null;
    private CommandExecutor<C> commandExecutor = null;
    private final Map<Character, AbstractFlag<?>> flags = new HashMap<>();
    
    /**
     * Creates a new BCICommand object
     * @param name The name of the command
     */
    public BCICommand(String name) {
        this.name = name;
    }
    
    /**
     * Gets the possible flags for this command
     * @return The commands possible flags
     */
    public List<AbstractFlag<?>> getFlags() {
        return new ArrayList<>(flags.values());
    }
    
    /**
     * Adds a flag to this command
     * @param flag The flag to add
     */
    public void addFlag(AbstractFlag<?> flag) {
        flags.put(flag.getFlagCharacter(), flag);
    }
    
    /**
     * Removes a flag from this command
     * @param flag The flag to remove
     */
    public void removeFlag(char flag) {
        flags.remove(flag);
    }
    
    /**
     * Checks if this command can have a particular flag
     * @param flag The flag to check
     * @return True if this command can have the particular flag
     */
    public boolean hasFlag(char flag) {
        return flags.containsKey(flag);
    }
    
    /**
     * Checks if this command can have any flags at all
     * @return True if this command can have flags, false otherwise.
     */
    public boolean hasFlags() {
        return !flags.isEmpty();
    }
    
    /**
     * Sets the command executor (method reference)
     * @param commandExecutor The command executor
     */
    public void setCommandExecutor(CommandExecutor<C> commandExecutor) {
        this.commandExecutor = commandExecutor;
    }
    
    /**
     * Gets the command executor
     * @return The command executor
     */
    public CommandExecutor<C> getCommandExecutor() {
        return commandExecutor;
    }
    
    /**
     * Sets the tab executor (method reference)
     * @param tabExecutor The tab executor
     */
    public void setTabExecutor(TabExecutor<T> tabExecutor) {
        this.tabExecutor = tabExecutor;
    }
    
    /**
     * Gets the tab executor
     * @return The tab executor
     */
    public TabExecutor<T> getTabExecutor() {
        return tabExecutor;
    }
    
    /**
     * Sets the allowed sender types of this command
     * @param types The commands allowed sender types
     */
    public void setSenderTypes(SenderType... types) {
        this.senderTypes = types;
    }
    
    /**
     * Gets the allowed sender types of this command
     * @return The commands allowed sender types
     */
    public SenderType[] getSenderTypes() {
        return senderTypes;
    }
    
    /**
     * Sets the permissions required for this command
     * @param permissions The required permissions for this command
     */
    public void setPermissions(String... permissions) {
        this.permissions = permissions;
    }
    
    /**
     * Gets the permissions required for this command
     * @return The required permissions for this command
     */
    public String[] getPermissions() {
        return permissions;
    }
    
    /**
     * Sets the maximum args for this command.
     * @param maxArgs The maximum amount of args allowed to run this command
     */
    public void setMaxArgs(int maxArgs) {
        this.maxArgs = maxArgs;
    }
    
    /**
     * Gets the maximum args for this command
     * @return The maximum amount of args allowed to run this command
     */
    public int getMaxArgs() {
        return maxArgs;
    }
    
    /**
     * Sets the minimum args for this command
     * @param minArgs The minimum amount of args allowed to run this command
     */
    public void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }
    
    /**
     * Gets the minimum args for this command
     * @return The minimum amount of args allowed to run this command
     */
    public int getMinArgs() {
        return minArgs;
    }
    
    /**
     * Gets the name of this command
     * @return The name of this command
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the command description
     * @param description The command description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Gets the command description
     * @return The command description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the command usage
     * @param usage The command usage
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }
    
    /**
     * Gets the command usage
     * @return The command usage
     */
    public String getUsage() {
        return usage;
    }
    
    /**
     * Sets the aliases this command can use
     * @param aliases The command aliases
     */
    public void setAliases(String... aliases) {
        this.aliases = aliases;
    }
    
    /**
     * Gets the command aliases
     * @return The command aliases
     */
    public String[] getAliases() {
        return aliases;
    }
    
    /**
     * Checks to see if the command passes the minimum argument check
     * @param context The command context
     * @throws BCIException If there arent enough arguments supplied to run this command
     */
    public void minimumCheck(C context) throws BCIException {
        if (context.getLength() < minArgs && minArgs > -1) context.notEnoughArgs();
    }
    
    /**
     * Checks to see if the command passes the maximum argument check
     * @param context The command context
     * @throws BCIException If there are too many arguments supplied to run this command
     */
    public void maximumCheck(C context) throws BCIException {
        if (context.getLength() > maxArgs && maxArgs > -1) context.tooManyArgs();
    }
    
    /**
     * Checks to see if the sender has permission to run this command
     * @param context The command context
     * @throws BCIException If the sender does not have permission to run this command
     */
    public void permissionCheck(C context) throws BCIException {
        if (!context.hasAnyPermission(permissions)) context.noPermission();
    }
    
    public void senderCheck(C context) throws BCIException {
        if (senderTypes != null && senderTypes.length != 0) {
            List<SenderType> types = Arrays.asList(senderTypes);
            if (!types.contains(SenderType.of(context.getSender()))) context.invalidSender();
        }
    }
    
    public void customCheck(C context) {}
    
    /**
     * The main executing method. Runs the command after doing pre checks
     * @param context The command context
     * @return True all the time because why not
     */
    public boolean execute(C context) {
        try {
            senderCheck(context);
            permissionCheck(context);
            
            //Parse flags before we check length that way we can take the length of the flags out of consideration when checking the length
            if (hasFlags()) Parser.parseFlags(context);
            
            minimumCheck(context);
            maximumCheck(context);
            customCheck(context);
            
            commandExecutor.execute(context);
        } catch (BCIException e) {
            e.showError(context.getSender());
        }
        return true;
        
    }

    public List<String> complete(T context) {
        
        try {
            List<String> possible = new ArrayList<>();
    
            if (context.getCurrent() == null) {
                return context.currentPossibleCompletions();
            }
            
            if (tabExecutor != null) {
                tabExecutor.complete(context);
                
                for (String completion : context.currentPossibleCompletions()) {
                    if (completion.toLowerCase().startsWith(context.getCurrent())) {
                        possible.add(completion);
                    }
                }
                return possible;
                
            }
        } catch (BCIException e) {
            e.showError(context.getSender());
        }
        return null;
    }

}
