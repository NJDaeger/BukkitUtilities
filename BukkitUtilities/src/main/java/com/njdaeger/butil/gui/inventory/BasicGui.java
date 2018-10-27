package com.njdaeger.butil.gui.inventory;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;

public class BasicGui extends AbstractInventory<BasicGui> {

    /**
     * Creates a basic GUI
     *
     * @param plugin The plugin which owns this GUI
     * @param size The size of the GUI. (Multiples of 9 required)
     * @param title The title of this GUI
     */
    public BasicGui(Plugin plugin, int size, Function<Player, String> title) {
        super(plugin, size, title);
    }

    /**
     * Creates a basic GUI
     *
     * @param plugin The plugin which owns this GUI
     * @param size The size of the GUI. (Multiples of 9 required)
     * @param title The title of this GUI
     */
    public BasicGui(Plugin plugin, int size, String title) {
        super(plugin, size, title);
    }

}
