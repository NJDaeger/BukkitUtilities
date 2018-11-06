package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.TriConsumer;
import com.njdaeger.butil.TriPredicate;
import com.njdaeger.butil.gui.IButton;
import com.njdaeger.butil.gui.IGui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

public class BooleanButton<T extends IGui<T>> implements IButton<T, BooleanButton<T>> {

    private TriPredicate<T, BooleanButton<T>, InventoryClickEvent> deselectWhen;
    private TriPredicate<T, BooleanButton<T>, InventoryClickEvent> selectWhen;
    private TriConsumer<T, BooleanButton<T>, InventoryClickEvent> onDeselect;
    private TriConsumer<T, BooleanButton<T>, InventoryClickEvent> onSelect;
    private BiFunction<T, BooleanButton<T>, ItemStack> itemStack;

    private final boolean startingSelection;
    private boolean isSelected;
    private T parent;

    BooleanButton(boolean selected) {
        this.startingSelection = selected;
    }

    void selectWhen(TriPredicate<T, BooleanButton<T>, InventoryClickEvent> selectWhen) {
        this.selectWhen = selectWhen;
    }

    void onSelect(TriConsumer<T, BooleanButton<T>, InventoryClickEvent> onSelect) {
        this.onSelect = onSelect;
    }

    void deselectWhen(TriPredicate<T, BooleanButton<T>, InventoryClickEvent> deselectWhen) {
        this.deselectWhen = deselectWhen;
    }

    void onDeselect(TriConsumer<T, BooleanButton<T>, InventoryClickEvent> onDeselect) {
        this.onDeselect = onDeselect;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    @Override
    public ItemStack getStack() {
        return itemStack.apply(parent, this);
    }

    @Override
    public BooleanButton<T> setStack(BiFunction<T, BooleanButton<T>, ItemStack> stack) {
        this.itemStack = stack;
        return this;
    }

    @Override
    public BooleanButton<T> setSlot(int slot, boolean reset) {
        if (hasParentGui()) {
            if (reset) this.isSelected = startingSelection;
            parent.setItem(slot, this);
        }
        return this;
    }

    @Override
    public void setParentGui(T gui) {
        this.parent = gui;
    }

    @Override
    public final void onClick(InventoryClickEvent event) {

        if (selectWhen.test(parent, this, event)) {
            this.isSelected = true;
            onSelect.accept(parent, this, event);
        }
        if (deselectWhen.test(parent, this, event)) {
            this.isSelected = false;
            onDeselect.accept(parent, this, event);
        }
    }

    @Override
    public T getParent() {
        return parent;
    }
}
