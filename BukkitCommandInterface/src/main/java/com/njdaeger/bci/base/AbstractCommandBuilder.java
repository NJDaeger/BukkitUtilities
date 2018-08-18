package com.njdaeger.bci.base;

import com.njdaeger.bci.SenderType;
import com.njdaeger.bci.arguments.ArgumentBuilder;
import com.njdaeger.bci.arguments.ArgumentMap;
import com.njdaeger.bci.base.executors.CommandExecutor;
import com.njdaeger.bci.base.executors.TabExecutor;

import java.util.Set;

@SuppressWarnings("unchecked")
public abstract class AbstractCommandBuilder<C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>, B extends AbstractCommandBuilder<C, T, B>> {
    
    private BCICommand<C, T> command;
    
    public AbstractCommandBuilder(String name) {
        this.command = new BCICommand<>(name);
    }
    
    public B arguments(ArgumentMap<C, T> argumentMap) {
        command.setArgumentMap(argumentMap);
        return (B)this;
    }
    
    public ArgumentBuilder<C, T, B> argumentBuilder() {
        return ArgumentBuilder.builder(this);
    }
    
    public B executor(CommandExecutor<C> executor) {
        command.setCommandExecutor(executor);
        return (B)this;
    }
    
    public B completer(TabExecutor<T> executor) {
        command.setTabExecutor(executor);
        return (B)this;
    }
    
    public B aliases(String... aliases) {
        command.setAliases(aliases);
        return (B)this;
    }
    
    public B aliases(Set<String> aliases) {
        return aliases(aliases.toArray(new String[0]));
    }
    
    public B description(String description) {
        command.setDescription(description);
        return (B)this;
    }
    
    public B usage(String usage) {
        command.setUsage(usage);
        return (B)this;
    }
    
    public B permissions(String... permissions) {
        command.setPermissions(permissions);
        return (B)this;
    }
    
    public B minArgs(int minArgs) {
        command.setMinArgs(minArgs);
        return (B)this;
    }
    
    public B maxArgs(int maxArgs) {
        command.setMaxArgs(maxArgs);
        return (B)this;
    }
    
    public B senders(SenderType... allowedSenders) {
        command.setSenderTypes(allowedSenders);
        return (B)this;
    }
    
    public B locatableSenders() {
        return senders(SenderType.BLOCK, SenderType.ENTITY, SenderType.PLAYER);
    }
    
    public BCICommand<C, T> build() {
        return command;
    }

}
