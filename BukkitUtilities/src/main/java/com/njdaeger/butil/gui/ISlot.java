package com.njdaeger.butil.gui;

import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

/**
 * Represents a slot/button in an inventory
 *
 * @param <T> The type using this button
 * @param <U> The current slot type
 */
public interface ISlot<T, U extends ISlot<T, U>> {

    /**
     * Gets the current item in this slot
     *
     * @return This slots current item
     */
    ItemStack getCurrent();

    /**
     * Sets the new item for the slot and automatically updates
     *
     * @param stack The new item function for this slot.
     * @return This button
     */
    U setCurrent(BiFunction<T, U, ItemStack> stack);

    /**
     * Sets the new item for this current button and optionally allows you to update it automatically or not.
     *
     * @param stack The new item function for this slot
     * @param update True will update this slot automatically, false will not update it automatically.
     * @return This button
     */
    U setCurrent(BiFunction<T, U, ItemStack> stack, boolean update);

    /**
     * Get this button's current slot.
     *
     * @return This buttons current slot
     */
    int getSlot();

    /**
     * Will move this button over to a new slot (automatically updating it). All the current values will remain the
     * same.<p> This will replace any existing item which is currently in the specified slot.
     *
     * @param slot The new slot to move this button to
     * @return This button
     */
    default U setSlot(int slot) {
        return setSlot(slot, false);
    }

    /**
     * Will move this button over to a new slot (automatically updating it). Optionally keeping all its current
     * values.<p> This will replace any existing item which is currently in the specified slot.
     *
     * @param slot The new slot to move this button to
     * @param reset True will reset the current values associated with this button. False will keep the current
     *         values.
     * @return This button
     */
    default U setSlot(int slot, boolean reset) {
        return setSlot(slot, reset, true);
    }

    /**
     * Will move this button over to a new slot. Optionally updating it upon setting, also optionally keeping all its
     * current values.<p> This will replace any existing item which is currently in the specified slot.
     *
     * @param slot The new slot to move this button to
     * @param reset True will reset the current values associated with this button. False will keep the current
     *         values.
     * @param update True will update the slot and show results instantly, false will require you to call the
     *         update method.
     * @return This button
     */
    U setSlot(int slot, boolean reset, boolean update);

    /**
     * @param slot
     * @return
     */
    default U moveSlot(int slot) {

    }

    default U moveSlot(int slot, boolean reset) {

    }

    /**
     * This will attempt to move this button over to a new slot if the slot provided is empty. Otherwise it will not
     * move. Optionally updating it upon setting, also optionally keeping all its current values.
     *
     * @param slot The slot to attempt to move this button to
     * @param reset True will reset the current values associated with this button. False will keep the current
     *         values.
     * @param update True will update the slot and show results instantly, false will require you to call the
     *         update method.
     * @return This button
     */
    U moveSlot(int slot, boolean reset, boolean update);

    void update();

    void remove();

    IGui<T> getGui();
    
    /*
    
    How i envision this:
    
    PagedGui gui = new PagedGui();
    
    new BooleanButton(Item on, Item off);
        //When the button is pressed to turn it on
        void onClick(InventoryClickEvent event);
        
        //When the button is pressed to turn it off
        void offClick(InventoryClickEvent event);
        
        boolean isOn();
        
        void setOn(boolean value);
    
    gui.setSlot(new Buttion));
    
     */


}
