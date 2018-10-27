package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.gui.IGui;

public final class ButtonBuilder {

    public static <T extends IGui<T>> IncrementalButtonBuilder<T> incremental(Class<T> type) {
        return new IncrementalButtonBuilder<>();
    }



}
