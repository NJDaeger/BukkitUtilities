package com.njdaeger.butil.gui;

import org.bukkit.inventory.ItemStack;

public interface ISlot {
    
    ItemStack getCurrent();
    
    void setCurrent(ItemStack stack);
    
    int getSlot();
    
    void setSlot(int slot);
    
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
