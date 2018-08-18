package com.njdaeger.bci;

import com.njdaeger.bci.arguments.ArgumentBuilder;
import com.njdaeger.bci.arguments.defaults.BooleanArg;
import com.njdaeger.bci.arguments.defaults.DoubleArg;
import com.njdaeger.bci.arguments.defaults.IntegerArg;
import com.njdaeger.bci.base.BCICommand;
import com.njdaeger.bci.base.BCIException;
import com.njdaeger.bci.defaults.BCIBuilder;
import com.njdaeger.bci.defaults.CommandContext;
import com.njdaeger.bci.defaults.TabContext;
import org.bukkit.ChatColor;

public class CommandTest {
    
    public CommandTest() {
    
        BCICommand<CommandContext, TabContext> command = BCIBuilder.create("bci")
            .executor(this::command)
            .completer(this::completion)
            .minArgs(1)
            .maxArgs(3)
            .description("Test the BCI")
            .usage("/bci [player] [test]")
            .permissions("bci.test")
            .senders(SenderType.CONSOLE, SenderType.PLAYER)
            .argumentBuilder()
            .index(0)
            .arguments(new BooleanArg("<boolean>"), new IntegerArg("<integer>"))
            .index(1)
            .argumentsAfter(BooleanArg.class, new BooleanArg("<afterBoolBool>"), new DoubleArg("<afterBoolDouble>"))
            .argumentsAfter(IntegerArg.class, new BooleanArg("<afterIntDouble>"), new IntegerArg("afterIntInt"))
            .arguments(new DoubleArg("<doubleArg>"))
            .build()
            .build();
    
        BCIPlugin.getCommandStore().registerCommand(command);
        
    }
    
    public void command(CommandContext context) throws BCIException {
        if (context.hasArgAt(1)) {
            if (context.argAt(1).equalsIgnoreCase("test3")) {
                context.noPermission(ChatColor.RED + "You don't have permission to use test3");
            }
        }
        context.getArgumentMap().forEach(System.out::println);
        context.send("CURRENT: " + context.getTrack().toString());
        
        context.send("" + context.getTrack().nextBoolean());
        context.send("" + context.getTrack().nextBoolean());
        
    }
    
    public void completion(TabContext context) {
        
        context.completionAt(0, "true", "false");
        context.completionAt(1, "true", "false", "15.56");
        
    }
    
}
