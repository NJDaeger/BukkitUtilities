package com.njdaeger.butil.gui.inventory;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;

public class PagedGui extends AbstractInventory<PagedGui> {

    public PagedGui(Plugin plugin, int size, Function<Player, String> title) {
        super(plugin, size, title);
    }
}
