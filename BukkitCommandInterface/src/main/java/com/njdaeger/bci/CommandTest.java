package com.njdaeger.bci;

import com.njdaeger.bci.base.BCICommand;
import com.njdaeger.bci.base.BCIException;
import com.njdaeger.bci.defaults.BCIBuilder;
import com.njdaeger.bci.defaults.CommandContext;
import com.njdaeger.bci.defaults.TabContext;
import com.njdaeger.bci.flags.defaults.IntegerFlag;
import com.njdaeger.bci.flags.defaults.OptionalFlag;
import org.bukkit.ChatColor;

public class CommandTest {
    
    public CommandTest() {
    
        BCICommand<CommandContext, TabContext> command = BCIBuilder.create("bci")
            .executor(this::command)
            .completer(this::completion)
            .minArgs(1)
            .maxArgs(2)
            .description("Test the BCI")
            .usage("/bci [player] [test]")
            .permissions("bci.test")
            .senders(SenderType.CONSOLE, SenderType.PLAYER)
            .flag(new OptionalFlag('o'))
            .flag(new IntegerFlag('i'))
            .build();
        
        BCIPlugin.getCommandStore().registerCommand(command);
        
    }
    /*
    
    @Flag(OptionalFlag.class, 'c', true)
    @Flag(CustomFlag.class)
     */
    public void command(CommandContext context) throws BCIException {
        if (context.hasArgAt(1)) {
            if (context.argAt(1).equalsIgnoreCase("test3")) {
                context.noPermission(ChatColor.RED + "You don't have permission to use test3");
            }
        }
        if (context.hasFlag('o')) context.send("has flag");
        if (context.hasFlag('i')) context.send(context.getFlag('i').getInteger());
        context.send(context.integerAt(0));
        context.send(context.booleanAt(1));
    }
    
    public void completion(TabContext context) {
        
        context.completionAt(0, "true", "false");
        context.completionAt(1, "true", "false", "15.56");
        
    }
    
}
