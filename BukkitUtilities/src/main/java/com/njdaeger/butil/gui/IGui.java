package com.njdaeger.butil.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Represents a GUI
 *
 * @param <T> The type of gui this is.
 */
public interface IGui<T extends IGui<T>> extends Listener {

    /**
     * Set a button at a specified index. Will override any current button that is there.
     *
     * @param index The index to put the specified button at
     * @param button The button to set
     * @param <S> The Type of button to set
     * @return This GUI
     */
    <S extends IButton<T, S>> T setItem(int index, S button);

    /**
     * Add a button at the next open index. If no open slot is found the button will not be placed.
     *
     * @param button The button to add
     * @param <S> The type of button to add
     * @return This GUI
     */
    <S extends IButton<T, S>> T addItem(S button);

    /**
     * Removes a button at a specified slot
     *
     * @param slot The slot of the button to remove.
     * @return This GUI
     */
    T removeButton(int slot);

    /**
     * Removes a certain amount of buttons from the gui
     *
     * @param startIndex Where to start removing buttons from (inclusive)
     * @param countToRemove How many buttons to remove
     * @return This GUI
     */
    T removeButtons(int startIndex, int countToRemove);

    /**
     * Check if this GUI has any slots currently open.
     *
     * @return True if there's an open slot in the inventory.
     */
    boolean hasSlotOpen();

    /**
     * Check if the given slot is open or not.
     *
     * @param slot The slot to check
     * @return True if the slot is empty, false otherwise.
     */
    boolean isSlotOpen(int slot);

    /**
     * Attempts to find the next open slot in this GUI
     *
     * @return The slot number of the next open slot, or -1 if there are no open slots.
     */
    default int firstOpenSlot() {
        if (!hasSlotOpen()) return -1;
        int i = 0;
        while (getButton(i) != null) i++;
        return i;
    }

    /**
     * Attempt to get the slot of a button
     *
     * @param button The button to look for
     * @return The slot number of the button, or -1 if the button was not found.
     */
    int getSlotOf(IButton<?, ?> button);

    /**
     * Attempt to get a button at the specified slot
     *
     * @param slot The slot to get a button from
     * @return The button if it exists in that slot, null otherwise.
     */
    IButton<T, ?> getButton(int slot);

    /**
     * Attempt to get a button at the specified slot
     *
     * @param slotType The type of button to return
     * @param slot The slot to get the button from
     * @param <S> The type of button to return
     * @return The button if it exists, null otherwise.
     */
    <S extends IButton<T, S>> IButton<T, S> getButton(Class<S> slotType, int slot);

    /**
     * Get an array of all the buttons currently in the GUI.
     *
     * @return An array of all this GUI's buttons.
     */
    IButton<T, ?>[] getButtons();

    /**
     * Gets an array of buttons starting from index 0 until the limit is reached.
     *
     * @param limit How large of an array of buttons to return.
     * @return An array of buttons which is the same size as the limit provided, unless the limit is greater than or
     *         equal to the size of the GUI, then the buttons array is returned without trimming.
     */
    @SuppressWarnings("unchecked")
    default IButton<T, ?>[] getButtons(int limit) {
        if (limit >= getButtons().length) return getButtons();
        IButton<T, ?>[] buttons = (IButton<T, ?>[]) new Object[limit];
        for (int i = 0; i < limit; i++) {
            buttons[i] = getButton(i);
        }
        return buttons;
    }

    /**
     * Update the current GUI for the specified player
     *
     * @param player The player to update the gui for
     */
    void update(Player player);

    /**
     * Opens the current GUI for the specified player
     *
     * @param player The player to open the gui for
     */
    void open(Player player);

    /**
     * Closes all the inventories for all players who currently have this specific GUI open
     */
    void closeAll();

    /**
     * Completely removes this GUI from all events and closes it for everyone.
     */
    void destroy();

}
