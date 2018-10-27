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
public interface ISlot<T extends IGui<T>, S extends ISlot<T, S>> {

    /**
     * Gets the current item in this slot
     *
     * @return This slots current item
     */
    ItemStack getCurrent();

    /**
     * Sets the new item function for the slot.
     *
     * @param stack The new item function for this slot.
     * @return This button
     */
    S setCurrent(BiFunction<T, S, ItemStack> stack);

    /**
     * Sets the new item for the slot
     *
     * @param stack The new itemstack for this slot
     * @return This button
     */
    S setCurrent(ItemStack stack);

    /**
     * Get this button's current slot.
     *
     * @return This buttons current slot
     */
    int getSlot();

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
     * This will attempt to move this button over to the provided slot if it is empty. Otherwise it will not move. If it
     * is set, the settings from the current button will remain.
     *
     * @param slot The slot to attempt to move this button to
     * @return This button
     */
    default S moveSlot(int slot) {
        return moveSlot(slot, false);
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
    S moveSlot(int slot, boolean reset);

    boolean hasParentGui();

    void setParentGui(T gui);

    void onClick(InventoryClickEvent event);

    void remove();

    T getGui();

}
