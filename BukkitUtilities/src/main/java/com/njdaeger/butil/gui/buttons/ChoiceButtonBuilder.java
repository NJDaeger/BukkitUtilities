package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.ItemBuilder;
import com.njdaeger.butil.TriConsumer;
import com.njdaeger.butil.TriPredicate;
import com.njdaeger.butil.gui.IGui;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A choice button builder
 *
 * @param <T> The type of parent GUI
 * @param <C> The choice type this button has.
 */
public final class ChoiceButtonBuilder<T extends IGui<T>, C> {

    //Properties
    private boolean loopChoices = false;
    private C startingChoice;
    private List<C> choices;
    private int startIndex;
    private int shiftIndex = 2;
    private int index = 1;

    //Actions
    private Function<C, String> nameMapper;
    private BiFunction<T, ChoiceButton<T, C>, ItemStack> itemStack;
    private TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> previousWhen = (gui, button, event) -> event.getClick().isRightClick();
    private TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> nextWhen = (gui, button, event) -> event.getClick().isLeftClick();
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onPrevious = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        button.setStack(itemStack);
        gui.update(player);
    };
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onNext = onPrevious;
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMinChoice = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        button.setStack(ItemBuilder.of(Material.BARRIER)
                .displayName("Choice: " + (button.getChoiceIndex() + 1) + "/" + choices.size())
                .lore(choices.stream()
                        .limit(3)
                        .map(C::toString)
                        .map(s -> button.isSelected((C) s) ? ChatColor.BOLD + s : s)
                        .collect(Collectors.toList()))
                .build());
        gui.update(player);
    };
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMaxChoice = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        ItemStack stack = ItemBuilder.of(Material.BARRIER)
                .displayName("Choice: " + (button.getChoiceIndex() + 1) + "/" + choices.size())
                .lore(choices.stream()
                        .skip(choices.size() - 3)
                        .limit(3)
                        .map(C::toString)
                        .map(s -> button.isSelected((C) s) ? ChatColor.BOLD + s : s)
                        .collect(Collectors.toList()))
                .build();
        button.setStack(stack);
        gui.update(player);
    };

    ChoiceButtonBuilder() {
    }

    /**
     * Specifies an ItemStack via function
     *
     * @param itemStack The ItemStack to set for this button
     */
    public ChoiceButtonBuilder<T, C> itemStack(BiFunction<T, ChoiceButton<T, C>, ItemStack> itemStack) {
        Validate.notNull(itemStack, "A non-null ItemStack must be provided.");
        this.itemStack = itemStack;
        return this;
    }

    /**
     * Specifies an ItemStack via an ItemStack Object
     *
     * @param itemStack The ItemStack to set for this button
     */
    public ChoiceButtonBuilder<T, C> itemStack(ItemStack itemStack) {
        Validate.notNull(itemStack, "A non-null ItemStack must be provided.");
        this.itemStack = (gui, button) -> itemStack;
        return this;
    }

    public ChoiceButtonBuilder<T, C> nameMapper(Function<C, String> nameMapper) {
        this.nameMapper = nameMapper;
        return this;
    }

    /**
     * Whether to loop the choices of this button. by default, looping is disabled, so when we cant go any higher or
     * lower in the list of choices, the correct {@link ChoiceButtonBuilder#onMaxChoice(TriConsumer)} and {@link
     * ChoiceButtonBuilder#onMinChoice(TriConsumer)} method will be ran. If this s true, those methods will not be run
     * and the button will loop and therefore never hitting the minimum or maximum choice. Eg. if enabled, and the
     * current choice is at index 0, and the player decides to go down one more index, the index will go to the end of
     * the list rather than stopping completely.
     */
    public ChoiceButtonBuilder<T, C> loopChoices() {
        this.loopChoices = true;
        return this;
    }

    /**
     * Provides a list of choices for this choice button. The given value cannot be null.
     *
     * @param choices The array of choices to allow for this choice button.
     */
    public ChoiceButtonBuilder<T, C> choices(C... choices) {
        Validate.notNull(choices, "List of choices cannot be null");
        return choices(Stream.of(choices).collect(Collectors.toList()));
    }

    public ChoiceButtonBuilder<T, C> choices(List<C> choices) {
        this.choices = choices;
        return this;
    }

    public ChoiceButtonBuilder<T, C> start(C choice) {
        this.startingChoice = choice;
        return this;
    }

    public ChoiceButtonBuilder<T, C> start(int choice) {
        this.startIndex = choice;
        return this;
    }

    public ChoiceButtonBuilder<T, C> shiftStep(int shiftStep) {
        this.shiftIndex = shiftStep;
        return this;
    }

    public ChoiceButtonBuilder<T, C> step(int step) {
        this.index = step;
        return this;
    }

    public ChoiceButtonBuilder<T, C> onPrevious(TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> previous) {
        this.onPrevious = previous;
        return this;
    }

    public ChoiceButtonBuilder<T, C> previousWhen(TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> previousWhen) {
        this.previousWhen = previousWhen;
        return this;
    }

    public ChoiceButtonBuilder<T, C> onNext(TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> next) {
        this.onNext = next;
        return this;
    }

    public ChoiceButtonBuilder<T, C> nextWhen(TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> nextWhen) {
        this.nextWhen = nextWhen;
        return this;
    }

    public ChoiceButtonBuilder<T, C> onMaxChoice(TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMax) {
        this.onMaxChoice = onMax;
        return this;
    }

    public ChoiceButtonBuilder<T, C> onMinChoice(TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMin) {
        this.onMinChoice = onMin;
        return this;
    }

    public ChoiceButton<T, C> build() {

        ChoiceButton<T, C> button = new ChoiceButton<>(index, shiftIndex, loopChoices);

        if (startingChoice != null) {
            button.setChoices(choices, startingChoice);
        } else if (startIndex >= 0) {
            button.setChoices(choices, startIndex);
        } else button.setChoices(choices);

        button.setStack(itemStack);
        button.onPrevios(onPrevious);
        button.previousWhen(previousWhen);
        button.onNext(onNext);
        button.nextWhen(nextWhen);
        button.onMaxChoice(onMaxChoice);
        button.onMinChoice(onMinChoice);
        return button;
    }

}
