package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.ItemBuilder;
import com.njdaeger.butil.TriConsumer;
import com.njdaeger.butil.TriPredicate;
import com.njdaeger.butil.gui.IGui;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

public final class IncrementalButtonBuilder<T extends IGui<T>> {

    //Properties
    private int shiftStep = 1;
    private int minimum = 0;
    private int maximum = 20;
    private int start = 0;
    private int step = 5;

    //Actions
    private BiFunction<T, IncrementalButton<T>, ItemStack> itemStack;
    private TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> decrementWhen = (gui, button, event) -> event.getClick().isRightClick();
    private TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> incrementWhen = (gui, button, event) -> event.getClick().isLeftClick();
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> decrement = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        button.setCurrent(itemStack);
        gui.update(player);
    };
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> increment = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        button.setCurrent(itemStack);
        gui.update(player);
    };
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> hitMax = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        button.setCurrent(ItemBuilder.of(Material.BARRIER).displayName("Max is " + maximum).build());
        gui.update(player);
    };
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> hitMin = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        button.setCurrent(ItemBuilder.of(Material.BARRIER).displayName("Min is " + minimum).build());
        gui.update(player);
    };

    IncrementalButtonBuilder() {}

    public IncrementalButtonBuilder<T> itemstack(BiFunction<T, IncrementalButton<T>, ItemStack> itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public IncrementalButtonBuilder<T> itemstack(ItemStack itemStack) {
        this.itemStack = (gui, button) -> itemStack;
        return this;
    }

    public IncrementalButtonBuilder<T> min(int minimum) {
        this.minimum = minimum;
        return this;
    }

    public IncrementalButtonBuilder<T> max(int maximum) {
        this.maximum = maximum;
        return this;
    }

    public IncrementalButtonBuilder<T> start(int start) {
        this.start = start;
        return this;
    }

    public IncrementalButtonBuilder<T> step(int step) {
        this.step = step;
        return this;
    }

    public IncrementalButtonBuilder<T> shiftStep(int shiftStep) {
        this.shiftStep = shiftStep;
        return this;
    }

    public IncrementalButtonBuilder<T> onDecrement(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> decrement) {
        this.decrement = decrement;
        return this;
    }

    public IncrementalButtonBuilder<T> decrementWhen(TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> decrementWhen) {
        this.decrementWhen = decrementWhen;
        return this;
    }

    public IncrementalButtonBuilder<T> onIncrement(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> increment) {
        this.increment = increment;
        return this;
    }

    public IncrementalButtonBuilder<T> incrementWhen(TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> incrementWhen) {
        this.incrementWhen = incrementWhen;
        return this;
    }

    public IncrementalButtonBuilder<T> onMax(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> hitMax) {
        this.hitMax = hitMax;
        return this;
    }

    public IncrementalButtonBuilder<T> onMin(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> hitMin) {
        this.hitMin = hitMin;
        return this;
    }

    public IncrementalButton<T> build() {
        IncrementalButton<T> button = new IncrementalButton<>(minimum, maximum, start, step, shiftStep);
        button.setCurrent(itemStack);
        button.setValue(start);
        button.onIncrement(incrementWhen, increment);
        button.onDecrement(decrementWhen, decrement);
        button.onMax(hitMax);
        button.onMin(hitMin);
        return button;
    }

}
