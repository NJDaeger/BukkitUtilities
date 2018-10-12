package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.gui.IGui;
import com.njdaeger.butil.gui.ISlot;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class IncrementalButton<T> implements ISlot<T, IncrementalButton<T>> {
    
    private BiFunction<T, IncrementalButton<T>, ItemStack> itemStack;
    private final int shiftStep;
    private final int start;
    private final int step;
    private final int min;
    private final int max;
    
    public IncrementalButton(int min, int max, int start, int step, int shiftStep, Function<T, ItemStack> itemStack) {
        this.itemStack = (type, button) -> itemStack.apply(type);
        this.shiftStep = shiftStep;
        this.start = start;
        this.step = step;
        this.min = min;
        this.max = max;
    }
    
    public IncrementalButton(int min, int max, int start, int step, Function<T, ItemStack> itemStack) {
        this(min, max, start, step, 1, itemStack);
    }
    
    public IncrementalButton(int min, int max, int start, Function<T, ItemStack> itemStack) {
        this(min, max, start, 1, itemStack);
    }
    
    public IncrementalButton(int min, int max, Function<T, ItemStack> itemStack) {
        this(min, max, min, itemStack);
    }
    
    public IncrementalButton(int max, Function<T, ItemStack> itemStack) {
        this(0, max, itemStack);
    }
    
    boolean hasParent() {
    
    }
    
    void setParent(IGui gui) {
    
    }
    
    public IncrementalButton<T> onMax(BiConsumer<T, IncrementalButton<T>> hitMax) {
    
    }
    
    public IncrementalButton<T> onMin(BiConsumer<T, IncrementalButton<T>> hitMin) {
    
    }
    
    //TODO:
    //How do we know what action represents an increment or decrement?
    
    //Tri consumer. Take the type of button, incremental button, and the InventoryClickEvent
    public IncrementalButton<T> onIncrement(BiConsumer<T, IncrementalButton<T>> increment) {
    
    }
    
    public IncrementalButton<T> onDecrement(BiConsumer<T, IncrementalButton<T>> decrement) {
    
    }
    
    @Override
    public ItemStack getCurrent() {
        return null;
    }
    
    @Override
    public IncrementalButton<T> setCurrent(BiFunction<T, IncrementalButton<T>, ItemStack> stack) {
    
    }
    
    @Override
    public int getSlot() {
        return 0;
    }
    
    @Override
    public void setSlot(int slot) {
    
    }
}
