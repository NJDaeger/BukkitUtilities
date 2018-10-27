package com.njdaeger.butil.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface IGui<T extends IGui<T>> extends Listener {

    <S extends ISlot<T, S>> T setItem(int index, S slot);

    <S extends ISlot<T, S>> T addItem(S slot);

    T removeItem(int index);

    T removeItems(int startIndex, int countToRemove);

    boolean isSlotOpen(int slot);

    int getSlotOf(ISlot<?, ?> slot);

    ISlot<T, ?> getSlot(int slot);

    <S extends ISlot<T, S>> ISlot<T, S> getSlot(Class<S> slotType, int slot);

    void update(Player player);

    void open(Player player);

    void closeAll();

    void destroy();

}
