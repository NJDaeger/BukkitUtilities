package com.njdaeger.butil.gui;

import com.njdaeger.butil.gui.buttons.IncrementalButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Test {

    public void test() {

        //BasicGui gui = new BasicGui(/*plugin*/);
        /*

        IncrementalButton<T> button = ButtonBuilder.incremental(T.class)
                    .min(int) //Possibly double
                    .max(int) //Possibly double
                    .step(int) //Possibly double
                    .start(int) //Possibly double
                    .decrementWhen(ClickAction)
                    .incrementWhen(ClickAction)
                    .item(Function<T, ItemStack>)
                    .build();

        //when the decrement is called
        button.onDecrement(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent>);

        //when the increment is called
        button.onIncrement(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent>);

        //when the minimum value is called
        button.onMin(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent>);

        //when the maximum value is called
        button.onMax(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent>);

        //Set the current item in the stack
        button.setCurrent(BiFunction<T, ItemStack>)

        //Set the current value for the button. Updates the stack name automatically
        button.setValue(int) //Possibly double

        //Set the current value for the button. If boolean is false the slot will not automatically update.
        button.setValue(int, boolean) //Possibly double

        //get the current value
        button.getValue(TriConsumer<T, IncrementalButton<T>, InventoryClickEvent>);



         */

        /*IncrementalButton<BasicGui> size = new IncrementalButton<>(2, 4, (p) -> new ItemStack(Material.ACACIA_BOAT));
        size.onDecrement((g, s, c) -> {

        });



        g.setSlot(new IncrementalButton<Player>(2, 4, (p) -> new ItemStack(Material.ACACIA_BOAT)));*/

    }
    
}
