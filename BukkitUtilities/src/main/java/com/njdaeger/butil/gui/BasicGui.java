package com.njdaeger.butil.gui;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;

public class BasicGui extends AbstractInventory<BasicGui> {

    public BasicGui(Plugin plugin, int size, Function<Player, String> title) {
        super(plugin, size, title);
    }

    public BasicGui(Plugin plugin, int size, String title) {
        super(plugin, size, title);
    }

}
