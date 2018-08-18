package com.njdaeger.bci.base;

import com.njdaeger.bci.SenderType;
import com.njdaeger.bci.arguments.ArgumentBuilder;
import com.njdaeger.bci.arguments.ArgumentMap;
import com.njdaeger.bci.arguments.ArgumentParser;
import com.njdaeger.bci.arguments.ArgumentTrack;
import com.njdaeger.bci.base.executors.CommandExecutor;
import com.njdaeger.bci.base.executors.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class BCICommand<C extends AbstractCommandContext<C, T>, T extends AbstractTabContext<C, T>> {
    
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
    @SuppressWarnings("unchecked")
    private ArgumentMap<C, T> argumentMap = (ArgumentMap<C, T>)ArgumentBuilder.builder().buildEmptyMap();
    
    public BCICommand(String name) {
        this.name = name;
    }
    
    public void setArgumentMap(ArgumentMap<C, T> argumentMap) {
        this.argumentMap = argumentMap;
    }
    
    public ArgumentMap<C, T> getArgumentMap() {
        return argumentMap;
    }
    
    public ArgumentTrack getArgumentTrack(int index) {
        return argumentMap.getArgumentTrack(index);
    }
    
    public void addArgumentTrack(ArgumentTrack argumentTrack) {
        argumentMap.addArgumentTrack(argumentTrack);
    }
    
    public void removeArgumentTrack(ArgumentTrack argumentTrack) {
        argumentMap.removeArgumentTrack(argumentTrack);
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
            if (!argumentMap.isEmpty()) {
                context.setTrack(new ArgumentParser(context).parse());
            }
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

    public final List<String> complete(T context) {
        
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
