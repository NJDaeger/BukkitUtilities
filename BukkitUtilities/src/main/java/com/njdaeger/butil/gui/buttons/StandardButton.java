package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.gui.IButton;
import com.njdaeger.butil.gui.IGui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiFunction;

public class StandardButton<T extends IGui<T>> implements IButton<T, StandardButton<T>> {
    
    
    @Override
    public
    ItemStack getStack() {
        return null;
    }
    
    @Override
    public
    StandardButton<T> setStack(BiFunction<T, StandardButton<T>, ItemStack> stack) {
        return null;
    }
    
    @Override
    public
    StandardButton<T> setSlot(int slot, boolean reset) {
        return null;
    }
    
    @Override
    public
    void setParentGui(T gui) {
    
    }
    
    @Override
    public
    void onClick(InventoryClickEvent event) {
    
    }
    
    @Override
    public
    T getParent() {
        return null;
    }
}
