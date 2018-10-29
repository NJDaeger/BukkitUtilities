package com.njdaeger.butil.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

/**
 * Represents a slot/button in an inventory
 *
 * @param <T> The type of GUI
 * @param <S> The current slot type
 */
public interface IButton<T extends IGui<T>, S extends IButton<T, S>> {

    /**
     * Gets the current item in this slot
     *
     * @return This slots current item
     */
    ItemStack getStack();

    /**
     * Sets the new item function for the slot.
     *
     * @param stack The new item function for this slot.
     * @return This button
     */
    S setStack(BiFunction<T, S, ItemStack> stack);

    /**
     * Will move this button over to a new slot. Optionally keeping all its current values.<p> This will replace any
     * existing item which is currently in the specified slot.
     *
     * @param slot The new slot to move this button to
     * @param reset True will reset the current values associated with this button. False will keep the current
     *         values.
     * @return This button
     */
    S setSlot(int slot, boolean reset);

    /**
     * Sets the parent gui for this button
     *
     * @param gui The parent gui
     */
    void setParentGui(T gui);

    /**
     * Represents a click event and will execute procedures this specific button has
     *
     * @param event The click event
     */
    void onClick(InventoryClickEvent event);


    /**
     * Gets the current parent gui
     *
     * @return The parent gui
     */
    T getParent();

    /**
     * Check whether this specific button has a parent gui.
     *
     * @return True if it has a parent gui, false otherwise.
     */
    default boolean hasParentGui() {
        return getParent() != null;
    }

    /**
     * Sets the new item for the slot
     *
     * @param stack The new itemstack for this slot
     * @return This button
     */
    default S setStack(ItemStack stack) {
        return setStack((gui, button) -> stack);
    }

    /**
     * Get this button's current slot.
     *
     * @return This buttons current slot
     */
    default int getSlot() {
        return hasParentGui() ? getParent().getSlotOf(this) : -1;
    }

    /**
     * Will move this button over to a new slot. All the current values will remain the
     * same.<p> This will replace any existing item which is currently in the specified slot.
     *
     * @param slot The new slot to move this button to
     * @return This button
     */
    default S setSlot(int slot) {
        return setSlot(slot, false);
    }

    /**
     * This will attempt to move this button over to the provided slot if it is empty. Otherwise it will not move. If it
     * is set, the settings from the current button will remain.
     *
     * @param slot The slot to attempt to move this button to
     * @return This button
     */
    default S moveToSlot(int slot) {
        return moveToSlot(slot, false);
    }

    /**
     * This will attempt to move this button over to the provided slot if it is empty. Otherwise it will not move. If it
     * is set, the button will optionally either be reset or keep its current settings.
     *
     * @param slot The slot to attempt to move this button to
     * @param reset True will reset the current values associated with this button. False will keep the current
     *         values
     * @return This button
     */
    default S moveToSlot(int slot, boolean reset) {
        if (hasParentGui() && getParent().isSlotOpen(slot)) {
            getParent().removeItem(getSlot());
            setSlot(slot, reset);
        }
        return (S)this;
    }

    /**
     * Removes this button.
     */
    default void remove() {
        if (hasParentGui()) getParent().removeItem(getSlot());
    }

}
