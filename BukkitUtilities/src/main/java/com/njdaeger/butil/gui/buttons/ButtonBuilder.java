package com.njdaeger.butil.gui.buttons;

import com.njdaeger.butil.gui.IGui;

public final class ButtonBuilder {

    public static <T extends IGui<T>> IncrementalButtonBuilder<T> incremental(Class<T> type) {
        return new IncrementalButtonBuilder<>();
    }

    public static <T extends IGui<T>, C> ChoiceButtonBuilder<T, C> choice(Class<T> type, Class<C> choice) {
        return new ChoiceButtonBuilder<>();
    }

    public static <T extends IGui<T>> StandardButtonBuilder<T> standard(Class<T> type) {
        return new StandardButtonBuilder<>();
    }

}
