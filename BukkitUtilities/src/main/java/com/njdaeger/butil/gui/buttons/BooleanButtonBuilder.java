package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.ItemBuilder;
import com.njdaeger.butil.TriConsumer;
import com.njdaeger.butil.TriPredicate;
import com.njdaeger.butil.gui.IGui;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

public final class BooleanButtonBuilder<T extends IGui<T>> {

    private TriPredicate<T, BooleanButton<T>, InventoryClickEvent> deselectWhen = (gui, button, event) -> event.isRightClick() || (button.isSelected() && event.isLeftClick());
    private TriPredicate<T, BooleanButton<T>, InventoryClickEvent> selectWhen = (gui, button, event) -> event.isLeftClick() || (!button.isSelected() && event.isRightClick());
    private TriConsumer<T, BooleanButton<T>, InventoryClickEvent> onDeselect = (gui, button, event) -> {
        button.setStack(ItemBuilder.of(button.getStack()).clearEnchants().build());
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        gui.update(player);
    };
    private TriConsumer<T, BooleanButton<T>, InventoryClickEvent> onSelect = (gui, button, event) -> {
        button.setStack(ItemBuilder.of(button.getStack()).unbreakable().build());
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        gui.update(player);
    };
    private BiFunction<T, BooleanButton<T>, ItemStack> itemStack;
    private boolean startingSelection = false;

    BooleanButtonBuilder() {
    }

    public final BooleanButtonBuilder<T> itemStack(BiFunction<T, BooleanButton<T>, ItemStack> itemStack) {
        Validate.notNull(itemStack, "A non-null ItemStack must be provided.");
        this.itemStack = itemStack;
        return this;
    }

    public final BooleanButtonBuilder<T> itemStack(ItemStack itemStack) {
        Validate.notNull(itemStack, "A non-null ItemStack must be provided.");
        this.itemStack = (gui, button) -> itemStack;
        return this;
    }

    public final BooleanButtonBuilder<T> itemStack(Material material) {
        Validate.notNull(material, "Material cannot be null");
        return itemStack(new ItemStack(material));
    }

    public final BooleanButtonBuilder<T> selected() {
        this.startingSelection = true;
        return this;
    }

    public final BooleanButtonBuilder<T> deselectWhen(TriPredicate<T, BooleanButton<T>, InventoryClickEvent> deselectWhen) {
        Validate.notNull(deselectWhen, "The deselectWhen variable must be specified.");
        this.deselectWhen = deselectWhen;
        return this;
    }

    public final BooleanButtonBuilder<T> onDeselect(TriConsumer<T, BooleanButton<T>, InventoryClickEvent> onDeselect) {
        this.onDeselect = onDeselect;
        return this;
    }

    public final BooleanButtonBuilder<T> selectWhen(TriPredicate<T, BooleanButton<T>, InventoryClickEvent> selectWhen) {
        Validate.notNull(selectWhen, "The selectWhen variable must be specified.");
        this.selectWhen = selectWhen;
        return this;
    }

    public final BooleanButtonBuilder<T> onSelect(TriConsumer<T, BooleanButton<T>, InventoryClickEvent> onSelect) {
        this.onSelect = onSelect;
        return this;
    }

    public final BooleanButton<T> build() {

        BooleanButton<T> button = new BooleanButton<>(startingSelection);

        button.onDeselect(onDeselect);
        button.onSelect(onSelect);
        button.deselectWhen(deselectWhen);
        button.selectWhen(selectWhen);
        button.setStack(itemStack);
        return button;
    }

}
