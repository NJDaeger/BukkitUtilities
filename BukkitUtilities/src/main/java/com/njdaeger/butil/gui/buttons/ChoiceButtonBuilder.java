package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.ItemBuilder;
import com.njdaeger.butil.Pair;
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
    private int index = 1;
    private int shiftIndex = index;

    /*

    Actions and defaults

     */
    private Material material;
    private BiFunction<T, ChoiceButton<T, C>, String> buttonName = (gui, button) -> "Choice: " + (button.getChoiceIndex() + 1) + "/" + button.getChoices().size();
    //The name mapper for mapping the name to the lore.
    private Function<C, String> nameMapper = C::toString;

    //The function which creates the lore
    private BiFunction<T, ChoiceButton<T, C>, List<String>> loreFunction = (gui, button) -> {
        int skip;
        int size = button.getChoices().size();
        int index = button.getChoiceIndex();
        if (index > 0 && index < size - 1) skip = index - 1;
        else if (index >= size - 1) skip = size - 3;
        else skip = 0;
        return button.getChoices()
                .stream()
                .skip(skip)
                .limit(3)
                .map(obj -> new Pair<>(obj, nameMapper.apply(obj)))
                .map(pair -> button.isSelected(pair.getFirst()) ? ChatColor.BOLD + pair.getSecond() : pair.getSecond())
                .collect(Collectors.toList());
    };

    //The default itemstack settings
    private BiFunction<T, ChoiceButton<T, C>, ItemStack> itemStack = (gui, button) -> ItemBuilder.of(material)
            .displayName(buttonName.apply(gui, button))
            .lore(loreFunction.apply(gui, button)).build();

    //When to go to the previous option
    private TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> previousWhen = (gui, button, event) -> event.getClick().isRightClick();

    //When to go to the next option
    private TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> nextWhen = (gui, button, event) -> event.getClick().isLeftClick();

    //What to do when we go to the previous option
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onPrevious = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        button.setStack(itemStack);
        gui.update(player);
    };

    //What to do when we go to the next option
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onNext = onPrevious;

    //What to do when the minimum choice is selected
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMinChoice = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        button.setStack(ItemBuilder.of(Material.BARRIER)
                .displayName(buttonName.apply(gui, button))
                .lore(loreFunction.apply(gui, button))
                .build());
        gui.update(player);
    };

    //What to do when the maximum choice is selected
    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMaxChoice = (gui, button, event) -> {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        ItemStack stack = ItemBuilder.of(Material.BARRIER)
                .displayName(buttonName.apply(gui, button))
                .lore(loreFunction.apply(gui, button))
                .build();
        button.setStack(stack);
        gui.update(player);
    };

    private TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onClick;

    ChoiceButtonBuilder() {
    }

    /**
     * Specifies an ItemStack via function
     *
     * @param itemStack The ItemStack to set for this button
     */
    public final ChoiceButtonBuilder<T, C> itemStack(BiFunction<T, ChoiceButton<T, C>, ItemStack> itemStack) {
        Validate.notNull(itemStack, "A non-null ItemStack must be provided.");
        this.itemStack = itemStack;
        return this;
    }

    /**
     * Specifies an ItemStack via an ItemStack Object
     *
     * @param itemStack The ItemStack to set for this button
     */
    public final ChoiceButtonBuilder<T, C> itemStack(ItemStack itemStack) {
        Validate.notNull(itemStack, "A non-null ItemStack must be provided.");
        this.itemStack = (gui, button) -> itemStack;
        return this;
    }

    /**
     * Specifies the material to be used in the inventory for this button. By default, the display name, the name
     * mapper, and the lore are pre set. The display name is set to {@code '"Choice: " + (the current choice index) +
     * "/" + (the total amount of choices available)'}. The name mapper defaults to calling the {@link C#toString()}
     * method to get the name of the current object. This works well for things like strings and booleans, but not so
     * well for other objects which use the default formatting of {@link Object#toString()} or some form of
     * serialization to the method. The lore of this button is set to show three selections and the currently selected
     * one being set to bold. The currently selected choice will almost always remain in the middle of the lore, with
     * the previous and next choices being displayed on top and on bottom respectively.
     *
     * @param material The material to use for this button in the inventory
     */
    public final ChoiceButtonBuilder<T, C> itemStack(Material material) {
        Validate.notNull(material, "Material cannot be null");
        this.material = material;
        return this;
    }

    /**
     * Specifies the material to be used in the inventory for this button. By default, the lore and the name of this
     * button are pre set. The lore of this button will be showing the three options closest to the one selected. The
     * selection option will usually always remain in the middle unless it is the first selection or the last selection
     * in a non looping button. The name of this selection will be retrieved by calling the {@link C#toString()} method
     * which is what will be shown in the lore.
     *
     * @param material The material to use for this button in the inventory
     * @param buttonName The display name of the button in the inventory
     * @apiNote None of these values can be null.
     */
    public final ChoiceButtonBuilder<T, C> itemStack(Material material, String buttonName) {
        return itemStack(material, (gui, button) -> buttonName);
    }

    /**
     * Specifies the material to be used in the inventory for this button. By default, the lore and the name of this
     * button are pre set. The lore of this button will be showing the three options closest to the one selected. The
     * selection option will usually always remain in the middle unless it is the first selection or the last selection
     * in a non looping button. The name of this selection will be retrieved by calling the {@link C#toString()} method
     * which is what will be shown in the lore.
     *
     * @param material The material to use for this button in the inventory
     * @param buttonName The display name of the button in the inventory
     * @apiNote None of these values can be null.
     */
    public final ChoiceButtonBuilder<T, C> itemStack(Material material, BiFunction<T, ChoiceButton<T, C>, String> buttonName) {
        Validate.notNull(buttonName, "Name cannot be null");
        this.buttonName = buttonName;
        return itemStack(material);
    }

    /**
     * Specifies the material to be used in the inventory for this button. By default, the lore of this button is pre
     * set. The lore of this button will be showing three options closest to the one selected. The currently selected
     * option will usually remain in the middle of the surrounding selections unless the selection is the first or last
     * selection in the list. Then the selection will be the top or the bottom of the three possible selections
     * respectively.
     *
     * @param material The material to use for this button in the inventory
     * @param buttonName The display name of the button in the inventory
     * @param nameMapper The names of the objects being represented and how they should be applied
     * @apiNote None of these values can be null.
     */
    public final ChoiceButtonBuilder<T, C> itemStack(Material material, String buttonName, Function<C, String> nameMapper) {
        return itemStack(material, (gui, button) -> buttonName, nameMapper);
    }

    /**
     * Specifies the material to be used in the inventory for this button. By default, the lore of this button is pre
     * set. The lore of this button will be showing three options closest to the one selected. The currently selected
     * option will usually remain in the middle of the surrounding selections unless the selection is the first or last
     * selection in the list. Then the selection will be the top or the bottom of the three possible selections
     * respectively.
     *
     * @param material The material to use for this button in the inventory
     * @param buttonName The display name of the button in the inventory
     * @param nameMapper The names of the objects being represented and how they should be applied
     * @apiNote None of these values can be null.
     */
    public final ChoiceButtonBuilder<T, C> itemStack(Material material, BiFunction<T, ChoiceButton<T, C>, String> buttonName, Function<C, String> nameMapper) {
        Validate.notNull(nameMapper, "Name mapper cannot be null.");
        this.nameMapper = nameMapper;
        return itemStack(material, buttonName);
    }

    /**
     * Specifies the material to be used in the inventory for this button. In addition, it also specifies the display
     * name of the button, how to map the objects this button iterates over into string names, and how to supply the
     * lore, if any is wanted.
     *
     * @param material The material to use for this button in the inventory
     * @param buttonName The display name of the button in the inventory
     * @param nameMapper The names of the objects being represented and how they should be applied
     * @param loreFunction The function to use to create the lore for this button. This value can be null,
     *         meaning which the button will not have any lore.
     * @apiNote None of these values can be null except for the loreFunction.
     */
    public final ChoiceButtonBuilder<T, C> itemStack(Material material, String buttonName, Function<C, String> nameMapper, BiFunction<T, ChoiceButton<T, C>, List<String>> loreFunction) {
        return itemStack(material, (gui, button) -> buttonName, nameMapper, loreFunction);
    }

    /**
     * Specifies the material to be used in the inventory for this button. In addition, it also specifies the display
     * name of the button, how to map the objects this button iterates over into string names, and how to supply the
     * lore, if any is wanted.
     *
     * @param material The material to use for this button in the inventory
     * @param buttonName The display name of the button in the inventory
     * @param nameMapper The names of the objects being represented and how they should be applied
     * @param loreFunction The function to use to create the lore for this button. This value can be null,
     *         meaning which the button will not have any lore.
     * @apiNote None of these values can be null except for the loreFunction.
     */
    public final ChoiceButtonBuilder<T, C> itemStack(Material material, BiFunction<T, ChoiceButton<T, C>, String> buttonName, Function<C, String> nameMapper, BiFunction<T, ChoiceButton<T, C>, List<String>> loreFunction) {
        this.loreFunction = loreFunction;
        return itemStack(material, buttonName, nameMapper);
    }

    /**
     * Whether to loop the choices of this button. by default, looping is disabled, so when we cant go any higher or
     * lower in the list of choices, the correct {@link ChoiceButtonBuilder#onMaxChoice(TriConsumer)} and {@link
     * ChoiceButtonBuilder#onMinChoice(TriConsumer)} method will be ran. If this s true, those methods will not be run
     * and the button will loop and therefore never hitting the minimum or maximum choice. Eg. if enabled, and the
     * current choice is at index 0, and the player decides to go down one more index, the index will go to the end of
     * the list rather than stopping completely.
     *
     * @param loopChoices Whether to loop choices or not.
     */
    public final ChoiceButtonBuilder<T, C> loopChoices(boolean loopChoices) {
        this.loopChoices = loopChoices;
        return this;
    }

    /**
     * Provides a list of choices for this choice button. The given value cannot be null.
     *
     * @param choices The array of choices to allow for this choice button.
     */
    @SafeVarargs
    public final ChoiceButtonBuilder<T, C> choices(C... choices) {
        Validate.notNull(choices, "List of choices cannot be null");
        return choices(Stream.of(choices).collect(Collectors.toList()));
    }

    /**
     * Provides a list of choices for this choice button. The given value cannot be null. Value CAN be empty, though.
     *
     * @param choices The list of choices to allow for this choice button
     */
    public final ChoiceButtonBuilder<T, C> choices(List<C> choices) {
        Validate.notNull(choices, "List of choices cannot be null");
        this.choices = choices;
        return this;
    }

    /**
     * What choice to start this button out on. If either both this method and {@link ChoiceButtonBuilder#start(int)}
     * are not called, there will be no starting choice. (only one of the two methods needs to be called if a starting
     * value is desired)
     *
     * @param choice The choice to start out with
     */
    public final ChoiceButtonBuilder<T, C> start(C choice) {
        this.startingChoice = choice;
        return this;
    }

    /**
     * What choice to start this button out on. If either both this method and {@link ChoiceButtonBuilder#start(int)}
     * are not called, there will be no starting choice. (only one of the two methods needs to be called if a starting
     * value is desired)
     *
     * @param choice The index of the choice to start out with. If the index is out of bounds, there will be no
     *         starting choice.
     */
    public final ChoiceButtonBuilder<T, C> start(int choice) {
        this.startIndex = choice;
        return this;
    }

    /**
     * How many choices to jump when the button is shift clicked. By default there is no difference between a shift
     * click and a regular click. The jumps are the same
     *
     * @param shiftStep How many choices to jump when the button is shift clicked.
     */
    public final ChoiceButtonBuilder<T, C> shiftStep(int shiftStep) {
        this.shiftIndex = shiftStep;
        return this;
    }

    /**
     * How many choices to jump when the button is clicked normally. By default this is one choice.
     *
     * @param step How many choices to jump when the button is clicked
     */
    public final ChoiceButtonBuilder<T, C> step(int step) {
        this.index = step;
        return this;
    }

    /**
     * Specifies the action to perform when the previous choice is selected. By default, a sound is played to the player
     * clicking the button, the display name of the current button is updated, the lore is updated, and the gui is
     * updated to show all the final results. This can be null for no action to be performed.
     *
     * @param previous The action to perform when the previous choice is selected. This can be null.
     */
    public final ChoiceButtonBuilder<T, C> onPrevious(TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> previous) {
        this.onPrevious = previous;
        return this;
    }

    /**
     * Specifies when the button should be allowed to go to the previous option. By default, the button is allowed to go
     * to the previous option only if the button is right clicked.
     *
     * @param previousWhen What needs to be true to allow the button to go to the previous choice. This cannot
     *         be null.
     */
    public final ChoiceButtonBuilder<T, C> previousWhen(TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> previousWhen) {
        Validate.notNull(previousWhen, "The previousWhen variable must be specified.");
        this.previousWhen = previousWhen;
        return this;
    }

    /**
     * Specifies the action to perform when the next choice is selected. By default, a sound is played to the player
     * clicking the button, the display name of the current button is updated, the lore is updated, and the gui is
     * updated to show all the final results. This can be null for no action to be performed.
     *
     * @param next The action to perform when the next choice is selected. This can be null.
     */
    public final ChoiceButtonBuilder<T, C> onNext(TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> next) {
        this.onNext = next;
        return this;
    }

    /**
     * Specifies when the button should be allowed to go to the next option. By default, the button is allowed to go to
     * the next option only if the button is left clicked.
     *
     * @param nextWhen What needs to be true to allow the button to go to the next choice. This cannot be null.
     */
    public final ChoiceButtonBuilder<T, C> nextWhen(TriPredicate<T, ChoiceButton<T, C>, InventoryClickEvent> nextWhen) {
        Validate.notNull(previousWhen, "The nextWhen variable must be specified.");
        this.nextWhen = nextWhen;
        return this;
    }

    /**
     * Specifies the action to perform when the maximum choice is selected. This will only run if the {@link
     * ChoiceButtonBuilder#loopChoices(boolean)} is not enabled. Otherwise the button cannot hit its last choice. By default,
     * the current item is set to a barrier, the anvil sound is played to the player, the lore is updated, the item
     * display name is updated, and the gui is updated to show all the changes. This value can be null in order to do
     * nothing when the final choice is selected.
     *
     * @param onMax What to do when the last choice is selected.
     */
    public final ChoiceButtonBuilder<T, C> onMaxChoice(TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMax) {
        this.onMaxChoice = onMax;
        return this;
    }

    /**
     * Specifies the action to perform when the minimum choice is selected. This will only run if the {@link
     * ChoiceButtonBuilder#loopChoices(boolean)} is not enabled. Otherwise the button cannot hit its first choice. By default,
     * the current item is set to a barrier, the anvil sound is played to the player, the lore is updated, the item
     * display name is updated, and the gui is updated to show all the changes. This value can be null in order to do
     * nothing when the first choice is selected.
     *
     * @param onMin What to do when the beginning choice is selected.
     */
    public final ChoiceButtonBuilder<T, C> onMinChoice(TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onMin) {
        this.onMinChoice = onMin;
        return this;
    }

    /**
     * This executes any time the button is pressed, but rather than handling any of the button specific code, this is
     * simply for overlay purposes. For example, if the original intended behavior of the given button is still wanted,
     * and doesn't want to be rewritten, this can be used to do that.
     *
     * @param onClick The additional action to perform when this button is clicked, this can be null to indicate
     *         no additional action is to be performed.
     */
    public final ChoiceButtonBuilder<T, C> onClick(TriConsumer<T, ChoiceButton<T, C>, InventoryClickEvent> onClick) {
        this.onClick = onClick;
        return this;
    }

    /**
     * Builds the new choice button.
     *
     * @return The button.
     */
    public final ChoiceButton<T, C> build() {

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
        button.onClick(onClick);
        return button;
    }

}
