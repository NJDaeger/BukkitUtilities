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

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ChoiceButtonBuilder<T extends IGui<T>, C> {

    //Properties
    private boolean loopChoices = false;
    private C startingChoice;
    private List<C> choices;
    private int startIndex;
    private int shiftIndex;
    private int index;

    //Actions
    private BiFunction<T, ChoiceButton<T, C>, ItemStack> itemStack;
    private TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> previousWhen = (gui, button, event) -> event.getClick().isRightClick();
    private TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> nextWhen = (gui, button, event) -> event.getClick().isLeftClick();
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onPrevious = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        button.setCurrent(itemStack);
        gui.update(player);
    };
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onNext = onPrevious;
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMinChoice = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        ItemStack stack = ItemBuilder.of(Material.BARRIER)
                .displayName("Choice: " + button.getValueIndex()+1 + "/" + choices.size())
                .lore(choices.stream().limit(3).map(Object::toString).collect(Collectors.toList()))
                .build();
        button.setCurrent(stack);
        gui.update(player);
    };
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMaxChoice = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        ItemStack stack = ItemBuilder.of(Material.BARRIER)
                .displayName("Choice: " + button.getValueIndex()+1 + "/" + choices.size())
                .lore(choices.stream().sorted(Collections.reverseOrder()).limit(3).map(Object::toString).collect(Collectors.toList()))
                .build();
        button.setCurrent(stack);
        gui.update(player);
    };

    ChoiceButtonBuilder() {
    }

    public ChoiceButtonBuilder<T, C> itemStack(BiFunction<T, ChoiceButton<T, C>, ItemStack> itemStack) {
        Validate.notNull(itemStack, "A non-null ItemStack must be provided.");
        this.itemStack = itemStack;
        return this;
    }

    public ChoiceButtonBuilder<T, C> itemStack(ItemStack itemStack) {
        Validate.notNull(itemStack, "A non-null ItemStack must be provided.");
        this.itemStack = (gui, button) -> itemStack;
        return this;
    }

    public ChoiceButtonBuilder<T, C> loopChoices() {
        this.loopChoices = true;
        return this;
    }

    public ChoiceButtonBuilder<T, C> choices(C... choices) {
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

    public ChoiceButtonBuilder<T, C> onPrevious(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> previous) {

    }

    public ChoiceButtonBuilder<T, C> previousWhen(TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> previousWhen) {

    }

    public ChoiceButtonBuilder<T, C> onNext(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> next) {

    }

    public ChoiceButtonBuilder<T, C> nextWhen(TriPredicate<T, IncrementalButton<T>, InventoryClickEvent> nextWhen) {

    }

    public ChoiceButtonBuilder<T, C> onMaxChoice(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> onMax) {

    }

    public ChoiceButtonBuilder<T, C> onMinChoice(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent> onMin) {

    }

    public ChoiceButton<T, C> build() {

        if (startingChoice == null )

        ChoiceButton<T, C> button = new ChoiceButton<>(choices, );

    }

}
