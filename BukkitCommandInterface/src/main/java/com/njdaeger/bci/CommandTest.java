package com.njdaeger.bci;

import com.njdaeger.bci.base.BCICommand;
import com.njdaeger.bci.base.BCIException;
import com.njdaeger.bci.defaults.BCIBuilder;
import com.njdaeger.bci.defaults.CommandContext;
import com.njdaeger.bci.defaults.TabContext;
import org.bukkit.ChatColor;

public class CommandTest {
    
    public CommandTest() {
    
        BCICommand<CommandContext, TabContext> command =BCIBuilder.create("bci")
            .executor(this::command)
            .completer(this::completion)
            .minArgs(1)
            .maxArgs(3)
            .description("Test the BCI")
            .usage("/bci [player] [test]")
            .permissions("bci.test")
            .senders(SenderType.CONSOLE, SenderType.PLAYER)
            .build();
    
        BCIPlugin.getCommandStore().registerCommand(command);
        
    }
    
    public void command(CommandContext context) throws BCIException {
        if (context.hasArgAt(1)) {
            if (context.argAt(1).equalsIgnoreCase("test3")) {
                context.noPermission(ChatColor.RED + "You don't have permission to use test3");
            }
        }
        context.send(context.joinArgs());
    }
    
    public void completion(TabContext context) {
        
        if (context.isCurrent("test3")) {
            context.send("ye");
        }
        context.playerCompletionAt(0);
        if (context.isPrevious("NJDaeger3")) {
            context.completion("test", "test2", "test3");
        }
        
    }
    
}
