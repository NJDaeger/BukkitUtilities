package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.TriConsumer;
import com.njdaeger.butil.TriPredicate;
import com.njdaeger.butil.gui.IGui;
import com.njdaeger.butil.gui.IButton;
import com.njdaeger.butil.gui.IValueHolder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

public class IncrementalButton<T extends IGui<T>> implements IButton<T, IncrementalButton<T>>, IValueHolder<Integer> {

    private TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> decrementWhen;
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> onDecrement;
    private TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> incrementWhen;
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> onIncrement;
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> onMax;
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> onMin;
    private BiFunction<T, IncrementalButton<T>, ItemStack> itemStack;
    private final int shiftStep;
    private int currentValue;
    private final int start;
    private final int step;
    private final int min;
    private final int max;
    private T parent;

    IncrementalButton(int min, int max, int start, int step, int shiftStep) {
        this.shiftStep = shiftStep;
        this.start = start;
        this.step = step;
        this.min = min;
        this.max = max;
    }

    IncrementalButton<T> onMax(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> hitMax) {
        this.onMax = hitMax;
        return this;
    }

    IncrementalButton<T> onMin(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> hitMin) {
        this.onMin = hitMin;
        return this;
    }

    IncrementalButton<T> onIncrement(TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> incrementWhen, TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> increment) {
        this.incrementWhen = incrementWhen;
        this.onIncrement = increment;
        return this;
    }

    IncrementalButton<T> onDecrement(TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> decrementWhen, TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> decrement) {
        this.decrementWhen = decrementWhen;
        this.onDecrement = decrement;
        return this;
    }

    @Override
    public boolean hasParentGui() {
        return this.parent != null;
    }

    @Override
    public void setParentGui(T gui) {
        this.parent = gui;
    }

    @Override
    public ItemStack getCurrent() {
        return itemStack.apply(parent, this);
    }

    @Override
    public IncrementalButton<T> setCurrent(BiFunction<T, IncrementalButton<T>, ItemStack> stack) {
        this.itemStack = stack;
        return this;
    }

    @Override
    public IncrementalButton<T> setCurrent(ItemStack stack) {
        return setCurrent((gui, button) -> stack);
    }

    @Override
    public int getSlot() {
        return hasParentGui() ? parent.getSlotOf(this) : -1;
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
    public IncrementalButton<T> moveToSlot(int slot, boolean reset) {
        if (hasParentGui() && parent.isSlotOpen(slot)) {
            parent.removeItem(getSlot());
            setSlot(slot, reset);
        }
        return this;
    }

    @Override
    public final void onClick(InventoryClickEvent event) {
        int shiftedValue = event.getClick().isShiftClick() ? shiftStep : step;

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
    public void remove() {
        if (hasParentGui()) parent.removeItem(getSlot());
    }

    @Override
    public T getGui() {
        return parent;
    }

    @Override
    public Integer getValue() {
        return currentValue;
    }

    @Override
    public void setValue(Integer value) {
        this.currentValue = value;
    }

    private boolean withinBounds() {
        return currentValue < max || currentValue > min;
    }

}
