package com.njdaeger.butil;

import com.njdaeger.butil.gui.buttons.ButtonBuilder;
import com.njdaeger.butil.gui.buttons.ChoiceButton;
import com.njdaeger.butil.gui.buttons.IncrementalButton;
import com.njdaeger.butil.gui.inventory.BasicGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public class BUtilMain extends JavaPlugin {

    private IncrementalButton<BasicGui> button = ButtonBuilder.incremental(BasicGui.class)
            .itemStack((gui, button) -> ItemBuilder.of(Material.ACACIA_LOG).displayName("Current " + button.getIndex()).build())
            .start(10)
            .step(2)
            .shiftStep(1)
            .build();

    private ChoiceButton<BasicGui, String> choiceButton = ButtonBuilder.choice(BasicGui.class, String.class)
            .itemStack((gui, button) -> ItemBuilder.of(Material.BEDROCK)
                    .displayName("Choice: " + (button.getChoiceIndex() + 1) + "/" + button.getChoices().size())
                    .lore(() -> {
                        /*
                        size = 10;
                        skip

                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9

                        index = 4;
                        index > 0 && index < 9; skip = 3

                        index = 5;
                        index > 0 && index < 9; skip = 4

                        index = 8;
                        index > 0 && index < 9; skip = 7

                        index = 9;
                        index >= 9; skip = 7

                        index = 0;
                        skip = 0;

                        index = 1;
                        index > 0 && index < 9; skip = 0

                        index = 2;
                        index > 0 && index < 9; slip = 1

                         */
                        int skip;
                        int size = button.getChoices().size();
                        int index = button.getChoiceIndex();
                        if (index > 0 && index < size-1) skip = index-1;
                        else if (index >= size-1) skip = size-3;
                        else skip = 0;
                        return button.getChoices().stream().skip(skip).limit(3).map(s -> {
                            if (button.getChoice().equals(s)) return ChatColor.BOLD + s;
                            else return s;
                        }).collect(Collectors.toList());
                    }).build())
            .choices("Hello", "Bork", "Poop", "number4", "number5", "tenise")
            .start("Hello")
            .loopChoices()
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
