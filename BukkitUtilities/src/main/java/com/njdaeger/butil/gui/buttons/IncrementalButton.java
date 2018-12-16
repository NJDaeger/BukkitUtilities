package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.TriConsumer;
import com.njdaeger.butil.TriPredicate;
import com.njdaeger.butil.gui.IButton;
import com.njdaeger.butil.gui.IGui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

public class IncrementalButton<T extends IGui<T>> implements IButton<T, IncrementalButton<T>> {

    private TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> decrementWhen;
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> onDecrement;
    private TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> incrementWhen;
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> onIncrement;
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> onMax;
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> onMin;
    private BiFunction<T, IncrementalButton<T>, ItemStack> itemStack;
    private final double shiftStep;
    private double currentValue;
    private final double start;
    private final double step;
    private final double min;
    private final double max;
    private T parent;

    IncrementalButton(double min, double max, double start, double step, double shiftStep) {
        this.shiftStep = shiftStep;
        this.start = start;
        this.step = step;
        this.min = min;
        this.max = max;
    }

    void onMax(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> hitMax) {
        this.onMax = hitMax;
    }

    void onMin(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> hitMin) {
        this.onMin = hitMin;
    }

    void onIncrement(TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> incrementWhen, TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> increment) {
        this.incrementWhen = incrementWhen;
        this.onIncrement = increment;
    }

    void onDecrement(TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> decrementWhen, TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> decrement) {
        this.decrementWhen = decrementWhen;
        this.onDecrement = decrement;
    }

    /**
     * Get the current button index value.
     * @return The current button index value.
     */
    public Double getIndex() {
        return currentValue;
    }

    /**
     * Set the current button index value.
     * @param value The new current button index value.
     */
    public void setIndex(Double value) {
        this.currentValue = value;
    }

    @Override
    public void setParentGui(T gui) {
        this.parent = gui;
    }

    @Override
    public ItemStack getStack() {
        return itemStack.apply(parent, this);
    }

    @Override
    public IncrementalButton<T> setStack(BiFunction<T, IncrementalButton<T>, ItemStack> stack) {
        this.itemStack = stack;
        return this;
    }

    @Override
    public IncrementalButton<T> setSlot(int slot, boolean reset) {
        if (hasParentGui()) {
            if (reset) this.currentValue = start;
            parent.setItem(slot, this);
        }
        return this;
    }

    @Override
    public final void onClick(InventoryClickEvent event) {
        double shiftedValue = event.getClick().isShiftClick() ? shiftStep : step;

        if (withinBounds() && (decrementWhen == null || decrementWhen.test(parent, this, event))) {
            if (currentValue - shiftedValue < min) this.currentValue = min;
            else this.currentValue -= shiftedValue;
            if (onDecrement != null) onDecrement.accept(parent, this, event);
        }
        if (withinBounds() && (incrementWhen == null || incrementWhen.test(parent, this, event))) {
            if (currentValue + shiftedValue > max) this.currentValue = max;
            else this.currentValue += shiftedValue;
            if (onDecrement != null) onIncrement.accept(parent, this, event);
        }

        if (max == currentValue && onMax != null) onMax.accept(parent, this, event);
        if (min == currentValue && onMin != null) onMin.accept(parent, this, event);

    }

    @Override
    public T getParent() {
        return parent;
    }

    private boolean withinBounds() {
        return currentValue < max || currentValue > min;
    }

}
