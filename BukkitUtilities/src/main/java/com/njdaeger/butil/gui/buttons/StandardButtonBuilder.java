package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.TriConsumer;
import com.njdaeger.butil.gui.IGui;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

public class StandardButtonBuilder<T extends IGui<T>> {

    private TriConsumer<T, StandardButton<T>, InventoryClickEvent> onClick = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        gui.update(player);
    };
    private BiFunction<T, StandardButton<T>, ItemStack> itemStack;

    StandardButtonBuilder() {
    }

    public final StandardButtonBuilder<T> itemStack(BiFunction<T, StandardButton<T>, ItemStack> itemStack) {
        Validate.notNull(itemStack, "ItemStack cannot be null");
        this.itemStack = itemStack;
        return this;
    }

    public final StandardButtonBuilder<T> itemStack(ItemStack itemStack) {
        return itemStack((gui, button) -> itemStack);
    }

    public final StandardButtonBuilder<T> itemStack(Material material) {
        return itemStack(new ItemStack(material));
    }

    public final StandardButtonBuilder<T> onClick(TriConsumer<T, StandardButton<T>, InventoryClickEvent> onClick) {
        this.onClick = onClick;
        return this;
    }

    public final StandardButton<T> build() {
        StandardButton<T> button = new StandardButton<>();

        button.setOnClick(onClick);
        button.setStack(itemStack);

        return button;
    }

}
