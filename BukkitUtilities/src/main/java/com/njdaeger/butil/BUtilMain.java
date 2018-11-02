package com.njdaeger.butil;

import com.njdaeger.butil.gui.buttons.ButtonBuilder;
import com.njdaeger.butil.gui.buttons.ChoiceButton;
import com.njdaeger.butil.gui.buttons.IncrementalButton;
import com.njdaeger.butil.gui.inventory.BasicGui;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BUtilMain extends JavaPlugin {

    private IncrementalButton<BasicGui> button = ButtonBuilder.incremental(BasicGui.class)
            .itemStack((gui, button) -> ItemBuilder.of(Material.ACACIA_LOG).displayName("Current " + button.getIndex()).build())
            .start(10)
            .step(2)
            .shiftStep(1)
            .build();

    private ChoiceButton<BasicGui, String> choiceButton = ButtonBuilder.choice(BasicGui.class, String.class)
            .itemStack(Material.BEDROCK)
            .choices("Hello", "Bork", "Poop", "number4", "number5", "tenise")
            .build();

    @Override
    public void onEnable() {
        getCommand("btest").setExecutor(this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("btest")) {
            BasicGui gui = new BasicGui(this, 9, "TestGui");
            gui.addItem(button);
            gui.addItem(choiceButton);
            gui.open((Player) sender);
        }
        return true;
    }
}
