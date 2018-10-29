package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.TriConsumer;
import com.njdaeger.butil.TriPredicate;
import com.njdaeger.butil.gui.IButton;
import com.njdaeger.butil.gui.IGui;
import com.njdaeger.butil.gui.IValueHolder;
import org.apache.commons.lang.Validate;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiFunction;

public class ChoiceButton<T extends IGui<T>, C> implements IButton<T, ChoiceButton<T, C>>, IValueHolder<C> {

    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMinChoice;
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMaxChoice;
    private TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> previousWhen;
    private TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> nextWhen;
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onPrevious;
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onNext;
    private BiFunction<T, ChoiceButton<T, C>, ItemStack> itemStack;
    private boolean loopChoices;
    private C startingChoice;
    private C currentChoice;
    private List<C> choices;
    private int currentIndex;
    private int shiftIndex;
    private T parent;
    private int index;

    ChoiceButton(List<C> choices, C startingChoice, int index, int shiftIndex) {
        Validate.isTrue(choices.contains(startingChoice), "List of choices must contain the starting choice.");
        this.choices = choices;
        this.currentChoice = startingChoice;
        this.startingChoice = startingChoice;
        this.index = index;
        this.shiftIndex = shiftIndex;
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

    @Override
    public ItemStack getCurrent() {
        return itemStack.apply(parent, this);
    }

    @Override
    public ChoiceButton<T, C> setCurrent(BiFunction<T, ChoiceButton<T, C>, ItemStack> stack) {
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
            nextIndex = possibleIndex >= 0 ? possibleIndex : loopChoices ? (choices.size()-1) + possibleIndex : 0;

            this.currentIndex = nextIndex;
            this.currentChoice = choices.get(nextIndex);
            if (onPrevious != null) onPrevious.accept(parent, this, event);
        }
        if (!loopChoices && currentIndex == choices.size()-1 && onMaxChoice != null) onMaxChoice.accept(parent, this, event);
        if (!loopChoices && currentIndex == 0 && onMinChoice != null) onMinChoice.accept(parent, this, event);
    }

    @Override
    public T getParent() {
        return parent;
    }

    @Override
    public C getValue() {
        return currentChoice;
    }

    @Override
    public void setValue(C value) {
        this.currentChoice = value;
        if (!hasChoice(value)) addChoice(value);
    }

    public int getValueIndex() {
        return currentIndex;
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
        } else {
            this.currentIndex = 0;
            this.currentChoice = choices.get(0);
        }
        this.choices = choices;
    }

    public void setChoices(List<C> choices, C currentChoice) {
        //No matter what, if the new current choice is null the current index and current choice will be the same.
        if (currentChoice == null) {
            this.currentIndex = -1;
            this.currentChoice = null;
        //We know the current choice isnt null, now we check if the choice is in the new given choices list. If it is,
        //we will set the current index to the index of the new current choice and set this buttons current choice to
        //the new current choice.
        } else if (choices.contains(currentChoice)) {
            this.currentIndex = choices.indexOf(currentChoice);
            this.currentChoice = currentChoice;
        //If the choices list is empty or if it doesnt contain the new current choice, we need to add the new current
        // choice to it since it is selected.
        } else {
            choices.add(currentChoice);
            this.currentIndex = choices.indexOf(currentChoice);
            this.currentChoice = currentChoice;
        }
        this.choices = choices;
    }

    public void setChoices(List<C> choices, int index) {
        if (index < 0 || index >= choices.size()) {
            this.currentIndex = -1;
            this.currentChoice = null;
        } else {
            this.currentIndex = index;
            this.currentChoice = choices.get(index);
        }
        this.choices = choices;
    }

    public void setLoopChoices(boolean loop) {
        this.loopChoices = loop;
    }

    public boolean loopChoices() {
        return loopChoices;
    }

}
