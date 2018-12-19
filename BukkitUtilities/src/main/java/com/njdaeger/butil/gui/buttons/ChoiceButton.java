package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.ItemBuilder;
import com.njdaeger.butil.TriConsumer;
import com.njdaeger.butil.TriPredicate;
import com.njdaeger.butil.gui.IButton;
import com.njdaeger.butil.gui.IGui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiFunction;

public class ChoiceButton<T extends IGui<T>, C> implements IButton<T, ChoiceButton<T, C>> {

    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMinChoice;
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMaxChoice;
    private TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> previousWhen;
    private TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> nextWhen;
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onPrevious;
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onClick;
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onNext;
    private BiFunction<T, ChoiceButton<T, C>, List<String>> loreFunction;
    private BiFunction<T, ChoiceButton<T, C>, String> selectedFormat;
    private BiFunction<T, ChoiceButton<T, C>, ItemStack> itemStack;
    private final boolean loopChoices;
    private final int shiftIndex;
    private int currentIndex;
    private C startingChoice;
    private C currentChoice;
    private List<C> choices;
    private final int index;
    private T parent;

    ChoiceButton(int index, int shiftIndex, boolean loopChoices) {
        this.index = index;
        this.shiftIndex = shiftIndex;
        this.loopChoices = loopChoices;
    }

    void nextWhen(TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> nextWhen) {
        this.nextWhen = nextWhen;
    }

    void previousWhen(TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> previousWhen) {
        this.previousWhen = previousWhen;
    }

    void onNext(TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onNext) {
        this.onNext = onNext;
    }

    void onPrevios(TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onPrevious) {
        this.onPrevious = onPrevious;
    }

    void onMinChoice(TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMinChoice) {
        this.onMinChoice = onMinChoice;
    }

    void onMaxChoice(TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMaxChoice) {
        this.onMaxChoice = onMaxChoice;
    }

    void onClick(TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onClick) {
        this.onClick = onClick;
    }

    @Override
    public ItemStack getStack() {
        if (loreFunction != null) return ItemBuilder.of(itemStack.apply(parent, this)).lore(loreFunction.apply(parent, this)).build();
        else return itemStack.apply(parent, this);
    }

    @Override
    public ChoiceButton<T, C> setStack(BiFunction<T, ChoiceButton<T, C>, ItemStack> stack) {
        this.itemStack = stack;
        return this;
    }

    @Override
    public ChoiceButton<T, C> setSlot(int slot, boolean reset) {
        if (hasParentGui()) {
            if (reset) this.currentChoice = startingChoice;
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
        int shift = event.getClick().isShiftClick() ? shiftIndex : index;
        int possibleIndex;
        int nextIndex;

        if (nextWhen.test(parent, this, event)) {
            //Get the possible index by adding the shift to the current index.
            possibleIndex = currentIndex + shift;
            //Calculate the next index. No matter if we are looping choices or not, if the possible index is less than
            //the size of choices- the next choice will be the value of possible index. If possible index is greater
            //than or equal to the size of choices, we then must check if we are looping choices or not. If we are
            //looping choices, we will subtract the current size of choices from the possible index to get the next
            //index, otherwise we will get the size of choices minus 1.
            nextIndex = possibleIndex < choices.size() ? possibleIndex : loopChoices ? possibleIndex - choices.size() : choices.size() - 1;

            //Setting the values in and accepting the consumer
            this.currentIndex = nextIndex;
            this.currentChoice = choices.get(nextIndex);
            if (onNext != null) onNext.accept(parent, this, event);
        }
        if (previousWhen.test(parent, this, event)) {
            //Get the possible index by subtracting the shift to the current index
            possibleIndex = currentIndex - shift;
            //Calculate the next index. No matter if we are looping choices or not, if the possible index is greater
            //than or equal to 0, the next choice will be the value of possible index. If possible index is less than 0,
            //we must check if we are looping choices or not. If we are looping choices, we will add the possible index
            //to the size of choices (minus 1), to get the next index, otherwise we just assume the next index is 0.
            nextIndex = possibleIndex >= 0 ? possibleIndex : loopChoices ? choices.size() + possibleIndex : 0;

            this.currentIndex = nextIndex;
            this.currentChoice = choices.get(nextIndex);
            if (onPrevious != null) onPrevious.accept(parent, this, event);
        }
        if (!loopChoices && currentIndex == choices.size() - 1 && onMaxChoice != null)
            onMaxChoice.accept(parent, this, event);
        if (!loopChoices && currentIndex == 0 && onMinChoice != null) onMinChoice.accept(parent, this, event);

        if (onClick != null) onClick.accept(parent, this, event);
    }

    @Override
    public T getParent() {
        return parent;
    }

    public BiFunction<T, ChoiceButton<T, C>, List<String>> getLoreFunction() {
        return loreFunction;
    }

    public void setLoreFunction(BiFunction<T, ChoiceButton<T, C>, List<String>> loreFunction) {
        this.loreFunction = loreFunction;
    }

    public BiFunction<T, ChoiceButton<T, C>, String> getSelectedFormat() {
        return selectedFormat;
    }

    public void setSelectedFormat(BiFunction<T, ChoiceButton<T, C>, String> selectedFormat) {
        this.selectedFormat = selectedFormat;
    }

    public int getChoiceIndex() {
        return currentIndex;
    }

    public C getChoice() {
        return currentChoice;
    }

    public void setChoice(C choice) {
        if (!hasChoice(choice)) choices.add(choice);
        this.currentIndex = choices.indexOf(choice);
        this.currentChoice = choice;
    }

    public boolean hasChoice(C choice) {
        return choices.contains(choice);
    }

    public boolean isSelected(C selection) {
        return currentChoice == selection;
    }

    public void addChoice(C choice) {
        choices.add(choice);
    }

    public List<C> getChoices() {
        return choices;
    }

    public void setChoices(List<C> choices) {
        if (choices.contains(currentChoice)) currentIndex = choices.indexOf(currentChoice);
        else if (choices.isEmpty()) {
            this.currentIndex = -1;
            this.currentChoice = null;
            this.startingChoice = null;
        } else {
            this.currentIndex = 0;
            this.currentChoice = choices.get(0);
            this.startingChoice = currentChoice;
        }
        this.choices = choices;
    }

    public void setChoices(List<C> choices, C currentChoice) {
        //No matter what, if the new current choice is null the current index and current choice will be the same.
        if (currentChoice == null) {
            this.currentIndex = -1;
            this.currentChoice = null;
            this.startingChoice = null;
            //We know the current choice isnt null, now we check if the choice is in the new given choices list. If it is,
            //we will set the current index to the index of the new current choice and set this buttons current choice to
            //the new current choice.
        } else if (choices.contains(currentChoice)) {
            this.currentIndex = choices.indexOf(currentChoice);
            this.currentChoice = currentChoice;
            this.startingChoice = currentChoice;
            //If the choices list is empty or if it doesnt contain the new current choice, we need to add the new current
            // choice to it since it is selected.
        } else {
            choices.add(currentChoice);
            this.currentIndex = choices.indexOf(currentChoice);
            this.currentChoice = currentChoice;
            this.startingChoice = currentChoice;
        }
        this.choices = choices;
    }

    public void setChoices(List<C> choices, int index) {
        if (index < 0 || index >= choices.size()) {
            this.currentIndex = -1;
            this.currentChoice = null;
            this.startingChoice = null;
        } else {
            this.currentIndex = index;
            this.currentChoice = choices.get(index);
            this.startingChoice = currentChoice;
        }
        this.choices = choices;
    }

    public boolean loopChoices() {
        return loopChoices;
    }

}
