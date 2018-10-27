package com.njdaeger.butil;

import com.njdaeger.butil.gui.inventory.BasicGui;
import com.njdaeger.butil.gui.buttons.ButtonBuilder;
import com.njdaeger.butil.gui.buttons.IncrementalButton;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BUtilMain extends JavaPlugin {

    private IncrementalButton<BasicGui> button = ButtonBuilder.incremental(BasicGui.class)
            .itemstack((gui, button) -> ItemBuilder.of(Material.ACACIA_LOG).displayName("Current " + button.getValue()).build())
            .start(10)
            .step(2)
            .shiftStep(1)
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
            gui.open((Player) sender);
        }
        return true;
    }
}
