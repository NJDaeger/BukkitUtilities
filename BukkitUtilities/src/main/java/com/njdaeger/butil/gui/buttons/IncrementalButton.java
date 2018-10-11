package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.gui.ISlot;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class IncrementalButton<T> implements ISlot {
    
    public IncrementalButton(int min, int max, int start, int step, Function<T, ItemStack> itemStack) {
    
    }
    
    public IncrementalButton(int min, int max, int start, Function<T, ItemStack> itemStack) {
    
    }
    
    public IncrementalButton(int min, int max, Function<T, ItemStack> itemStack) {
    
    }
    
    public IncrementalButton(int max, Function<T, ItemStack> itemStack) {
    
    }
    
    public void onMax(BiConsumer<T, IncrementalButton<T>> hitMax) {
    
    }
    
    public void onMin(BiConsumer<T, IncrementalButton<T>> hitMin) {
    
    }
    
    //TODO:
    //How do we know what action represents an increment or decrement?
    
    //Tri consumer. Take the type of button, incremental button, and the InventoryClickEvent
    public void onIncrement(BiConsumer<T, IncrementalButton<T>> increment) {
    
    }
    
    public void onDecrement(BiConsumer<T, IncrementalButton<T>> decrement) {
    
    }
    
    @Override
    public ItemStack getCurrent() {
        return null;
    }
    
    @Override
    public void setCurrent(ItemStack stack) {
    
    }
    
    @Override
    public int getSlot() {
        return 0;
    }
    
    @Override
    public void setSlot(int slot) {
    
    }
}
