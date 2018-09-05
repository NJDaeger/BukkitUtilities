package com.njdaeger.bci.base;

import com.njdaeger.bci.SenderType;
import com.njdaeger.bci.base.executors.CommandExecutor;
import com.njdaeger.bci.base.executors.TabExecutor;
import com.njdaeger.bci.flags.AbstractFlag;

import java.util.Set;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess"})
public abstract class AbstractCommandBuilder<C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>, B extends AbstractCommandBuilder<C, T, B>> {
    
    protected BCICommand<C, T> command;
    
    /**
     * Create a new command builder
     * @param name The name of this command to build.
     */
    public AbstractCommandBuilder(String name) {
        this.command = new BCICommand<>(name);
    }
    
    public B flag(AbstractFlag<?> flag) {
        command.addFlag(flag);
        return (B)this;
    }
    
    /**
     * Sets the CommandExecutor to be used for this command.
     * @param executor The command executor. (this is usually a method reference)
     */
    public B executor(CommandExecutor<C> executor) {
        command.setCommandExecutor(executor);
        return (B)this;
    }
    
    /**
     * Sets the TabExecutor to be used for this command.
     * @param executor The tab executor. (this is usually a method reference)
     */
    public B completer(TabExecutor<T> executor) {
        command.setTabExecutor(executor);
        return (B)this;
    }
    
    /**
     * Sets the aliases to be used for this command.
     * @param aliases The aliases to be used for this command.
     */
    public B aliases(String... aliases) {
        command.setAliases(aliases);
        return (B)this;
    }
    
    /**
     * Sets the aliases to be used for this command.
     * @param aliases The aliases to be used for this command
     */
    public B aliases(Set<String> aliases) {
        return aliases(aliases.toArray(new String[0]));
    }
    
    /**
     * Sets the description of the command.
     * @param description The command description.
     */
    public B description(String description) {
        command.setDescription(description);
        return (B)this;
    }
    
    /**
     * Sets the usage of the command.
     * @param usage The command usage.
     */
    public B usage(String usage) {
        command.setUsage(usage);
        return (B)this;
    }
    
    /**
     * Sets the permissions required to run the command
     * @param permissions The permissions required
     */
    public B permissions(String... permissions) {
        command.setPermissions(permissions);
        return (B)this;
    }
    
    /**
     * Sets the minimum amount of required arguments in order for this command to run.
     * @param minArgs The minimum amount of arguments required
     */
    public B minArgs(int minArgs) {
        command.setMinArgs(minArgs);
        return (B)this;
    }
    
    /**
     * Sets the maximim amount of required arguments in order for this command to run.
     * @param maxArgs The maximum amount of arguments required
     */
    public B maxArgs(int maxArgs) {
        command.setMaxArgs(maxArgs);
        return (B)this;
    }
    
    /**
     * Sets the allowed senders of this command. Whichever is in this list will be allowed to run this command.
     * @param allowedSenders The allowed senders.
     */
    public B senders(SenderType... allowedSenders) {
        command.setSenderTypes(allowedSenders);
        return (B)this;
    }
    
    /**
     * Sets the allowed senders to all the types that can be located in a world since some commands specifically require a location to be run.
     * <p>
     * <code>SenderType.BLOCK, SenderType.ENTITY, and SenderType.PLAYER</code>
     */
    public B locatableSenders() {
        return senders(SenderType.BLOCK, SenderType.ENTITY, SenderType.PLAYER);
    }
    
    /**
     * Builds this command
     * @return The built BCICommand object.
     */
    public BCICommand<C, T> build() {
        return command;
    }

}
