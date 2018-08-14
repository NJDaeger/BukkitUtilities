package com.njdaeger.bci.base;

import com.njdaeger.bci.SenderType;
import com.njdaeger.bci.base.executors.CommandExecutor;
import com.njdaeger.bci.base.executors.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class BCICommand<C extends AbstractCommandContext, T extends AbstractTabContext> {
    
    private final String name;
    
    private int maxArgs;
    private int minArgs;
    private String usage;
    private String[] aliases;
    private String description;
    private String[] permissions;
    private SenderType[] senderTypes;
    private TabExecutor<T> tabExecutor;
    private CommandExecutor<C> commandExecutor;
    
    public BCICommand(String name) {
        this.name = name;
    }
    
    public void setCommandExecutor(CommandExecutor<C> commandExecutor) {
        this.commandExecutor = commandExecutor;
    }
    
    public CommandExecutor<C> getCommandExecutor() {
        return commandExecutor;
    }
    
    public void setTabExecutor(TabExecutor<T> tabExecutor) {
        this.tabExecutor = tabExecutor;
    }
    
    public TabExecutor<T> getTabExecutor() {
        return tabExecutor;
    }
    
    public void setSenderTypes(SenderType... types) {
        this.senderTypes = types;
    }
    
    public SenderType[] getSenderTypes() {
        return senderTypes;
    }
    
    public void setPermissions(String... permissions) {
        this.permissions = permissions;
    }
    
    public String[] getPermissions() {
        return permissions;
    }
    
    public void setMaxArgs(int maxArgs) {
        this.maxArgs = maxArgs;
    }
    
    public int getMaxArgs() {
        return maxArgs;
    }
    
    public void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }
    
    public int getMinArgs() {
        return minArgs;
    }
    
    public String getName() {
        return name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setUsage(String usage) {
        this.usage = usage;
    }
    
    public String getUsage() {
        return usage;
    }
    
    public void setAliases(String... aliases) {
        this.aliases = aliases;
    }
    
    public String[] getAliases() {
        return aliases;
    }
    
    public void register() {
    
    }
    
    public final boolean execute(C context) {
        
        try {
            if (senderTypes != null && senderTypes.length != 0) {
                List<SenderType> types = Arrays.asList(senderTypes);
                if (!types.contains(SenderType.of(context.getSender()))) context.invalidSender();
            }
            
            if (permissions != null) {
                if (!context.hasAnyPermission(permissions)) context.noPermission();
            }
            
            if (context.getLength() < minArgs && minArgs > -1) {
                context.notEnoughArgs();
            }
            
            if (context.getLength() > maxArgs && maxArgs > -1) {
                context.tooManyArgs();
            }
            
            commandExecutor.execute(context);
        } catch (BCIException e) {
            e.showError(context.getSender());
        }
        return true;
        
    }
    
    @SuppressWarnings("unchecked")
    public final List<String> complete(T context) {
        
        try {
            List<String> possible = new ArrayList<>();
    
            if (context.getCurrent() == null) {
                return (List<String>)context.currentPossibleCompletions();
            }
            
            if (tabExecutor != null) {
                tabExecutor.complete(context);
                
                for (String completion : (List<String>)context.currentPossibleCompletions()) {
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
