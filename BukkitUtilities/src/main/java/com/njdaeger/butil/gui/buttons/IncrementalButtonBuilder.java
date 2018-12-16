package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.ItemBuilder;
import com.njdaeger.butil.TriConsumer;
import com.njdaeger.butil.TriPredicate;
import com.njdaeger.butil.gui.IGui;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

/**
 * An incremental Button Builder.
 *
 * @param <T> The type of parent GUI
 */
@SuppressWarnings("unused")
public final class IncrementalButtonBuilder<T extends IGui<T>> {

    //Properties
    private double shiftStep = 1;
    private double minimum = 0;
    private double maximum = 20;
    private double start = 0;
    private double step = 5;

    //Actions
    private BiFunction<T, IncrementalButton<T>, ItemStack> itemStack;
    private TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> decrementWhen = (gui, button, event) -> event.getClick().isRightClick();
    private TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> incrementWhen = (gui, button, event) -> event.getClick().isLeftClick();
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> decrement = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        button.setStack(itemStack);
        gui.update(player);
    };
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> increment = decrement;
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> hitMax = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        button.setStack(ItemBuilder.of(Material.BARRIER).displayName("Max is " + maximum).build());
        gui.update(player);
    };
    private TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> hitMin = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        button.setStack(ItemBuilder.of(Material.BARRIER).displayName("Min is " + minimum).build());
        gui.update(player);
    };

    IncrementalButtonBuilder() {
    }

    /**
     * Specifies an ItemStack via function
     *
     * @param itemStack The ItemStack to set for this button
     */
    public IncrementalButtonBuilder<T> itemStack(BiFunction<T, IncrementalButton<T>, ItemStack> itemStack) {
        Validate.notNull(itemStack, "A non-null ItemStack must be provided.");
        this.itemStack = itemStack;
        return this;
    }

    /**
     * Specifies an ItemStack via an ItemStack Object
     *
     * @param itemStack The ItemStack to set for this button
     */
    public IncrementalButtonBuilder<T> itemStack(ItemStack itemStack) {
        Validate.notNull(itemStack, "A non-null ItemStack must be provided.");
        this.itemStack = (gui, button) -> itemStack;
        return this;
    }

    /**
     * Set the minimum value allowed for this incremental button. This must be less than the maximum. Default is 0.
     *
     * @param minimum The minimum value allowed for this button
     */
    public IncrementalButtonBuilder<T> min(double minimum) {
        this.minimum = minimum;
        return this;
    }

    /**
     * Set the maximum value allowed for this incremental button. This must be greater than the minimum. Default is 20.
     *
     * @param maximum The maximum value allowed for this button
     */
    public IncrementalButtonBuilder<T> max(double maximum) {
        this.maximum = maximum;
        return this;
    }

    /**
     * Set the starting value this incremental button will start at. This must be less than or equal to the maximum
     * and/or greater than or equal to the minimum. Default is 0.
     *
     * @param start The starting value for this button
     */
    public IncrementalButtonBuilder<T> start(double start) {
        this.start = start;
        return this;
    }

    /**
     * Set the step value for this incremental button. Whenever the increase or decrease event is activated, the button
     * will increase/decrease this specified amount. If the button was shift clicked, this value will not be used. The
     * {@link IncrementalButtonBuilder#shiftStep(double)} value will be used instead. Default is 5.
     *
     * @param step How much to shift the button value whenever increased or decreased.
     */
    public IncrementalButtonBuilder<T> step(double step) {
        this.step = step;
        return this;
    }

    /**
     * Set the shift step value for this incremental button. Whenever the click is a shift click, the button will
     * increase/decrease this specified amount. See {@link IncrementalButtonBuilder#step(double)} for more information on
     * this. Default is 1.
     *
     * @param shiftStep How much to shift the button value whenever shift clicked.
     */
    public IncrementalButtonBuilder<T> shiftStep(double shiftStep) {
        this.shiftStep = shiftStep;
        return this;
    }

    /**
     * Specifies an action to perform when decremented. If this is given a null value, there will be no action performed
     * except for the decreasing of the current button value. By default, when decremented, this will be set to play a
     * sound for the player, change the name of the current item to the current value, and update the gui for the
     * player.
     *
     * @param decrement The action to perform when decremented. This can be null.
     */
    public IncrementalButtonBuilder<T> onDecrement(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> decrement) {
        this.decrement = decrement;
        return this;
    }

    /**
     * Specifies what needs to be true in order to allow the current value to be decremented. The given value can not be
     * null. By default, this will be checking if the current click was a right click or not- and if it was, the value
     * will be decremented and the {@link IncrementalButtonBuilder#onDecrement(TriConsumer)} action will be performed.
     *
     * @param decrementWhen What needs to be true in order to be able to decrement the current value. This
     *         cannot be null.
     */
    public IncrementalButtonBuilder<T> decrementWhen(TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> decrementWhen) {
        Validate.notNull(decrementWhen, "The decrementWhen variable must be specified.");
        this.decrementWhen = decrementWhen;
        return this;
    }

    /**
     * Specifies an action to perform when incremented. If this is given a null value, there will be no action performed
     * except for the increasing of the current button value. By default, when incremented, this will be set to play a
     * sound for the player, change the name of the current item to the current value, and update the gui for the
     * player.
     *
     * @param increment The action to perform when increased. This can be null.
     */
    public IncrementalButtonBuilder<T> onIncrement(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> increment) {
        this.increment = increment;
        return this;
    }

    /**
     * Specifies what needs to be true in order to allow the current value to be incremented. The given value can not be
     * null. By default, this will be checking if the current click was a left click or not- and if it was, the value
     * will be incremented and the {@link IncrementalButtonBuilder#onIncrement(TriConsumer)} action will be performed.
     *
     * @param incrementWhen What needs to be true in order to be able to increment the current value. This
     *         cannot be null.
     */
    public IncrementalButtonBuilder<T> incrementWhen(TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> incrementWhen) {
        Validate.notNull(incrementWhen, "The incrementWhen variable must be specified.");
        this.incrementWhen = incrementWhen;
        return this;
    }

    /**
     * Specifies what action will be performed when the max value is hit. If this is given a null value, there will be
     * no action performed when the max value is hit for this button. By default, when the max is hit, an anvil landing
     * sound will be played for the player, the current item will be changed to a barrier item, and the gui will be
     * updated for the player.
     *
     * @param hitMax The action to perform when the maximum value is hit. This can be null.
     */
    public IncrementalButtonBuilder<T> onMax(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> hitMax) {
        this.hitMax = hitMax;
        return this;
    }

    /**
     * Specifies what action will be performed when the min value is hit. If this is given a null value, there will be
     * no action performed when the min value is hit for this button. By default, when the min is hit, an anvil landing
     * sound will be played for the player, the current item will be changed to a barrier item, and the gui will be
     * updated for the player.
     *
     * @param hitMin The action to perform when the minimum value is hit. This can be null.
     */
    public IncrementalButtonBuilder<T> onMin(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> hitMin) {
        this.hitMin = hitMin;
        return this;
    }

    /**
     * Builds the new incremental button.
     *
     * @return The button
     */
    public IncrementalButton<T> build() {

        Validate.isTrue(minimum < maximum, "Minimum must be less than the maximum.");
        Validate.isTrue(start >= minimum && start <= maximum, "Start must be within the minimum and maximum bounds.");

        IncrementalButton<T> button = new IncrementalButton<>(minimum, maximum, start, step, shiftStep);
        button.setStack(itemStack);
        button.setIndex(start);
        button.onIncrement(incrementWhen, increment);
        button.onDecrement(decrementWhen, decrement);
        button.onMax(hitMax);
        button.onMin(hitMin);
        return button;
    }

}
