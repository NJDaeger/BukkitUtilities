package com.njdaeger.butil.gui;

public interface IGui<T> {

    T setSlot(ISlot<?, ?> slot);

    ISlot<?, ?> getSlot(int index);
    
}
