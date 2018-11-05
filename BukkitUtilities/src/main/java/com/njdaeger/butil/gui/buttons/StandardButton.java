package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.TriConsumer;
import com.njdaeger.butil.gui.IButton;
import com.njdaeger.butil.gui.IGui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class StandardButton<T extends IGui<T>> implements IButton<T, StandardButton<T>> {

    private TriConsumer<T, StandardButton<T>, InventoryClickEvent> event;
    private BiFunction<T, StandardButton<T>, ItemStack> startingStack;
    private BiFunction<T, StandardButton<T>, ItemStack> itemStack;
    private T parent;

    StandardButton() {
    }

    void setOnClick(TriConsumer<T, StandardButton<T>, InventoryClickEvent> event) {
        this.event = event;
    }

    @Override
    public ItemStack getStack() {
        return itemStack.apply(parent, this);
    }

    @Override
    public StandardButton<T> setStack(BiFunction<T, StandardButton<T>, ItemStack> stack) {
        this.itemStack = stack;
        return this;
    }

    @Override
    public StandardButton<T> setSlot(int slot, boolean reset) {
        if (hasParentGui()) {
            if (reset) this.itemStack = startingStack;
            parent.setItem(slot, this);
        }
        return this;
    }

    @Override
    public void setParentGui(T gui) {
        this.parent = gui;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        this.event.accept(parent, this, event);
    }

    @Override
    public T getParent() {
        return parent;
    }

}
