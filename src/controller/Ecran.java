package controller;

import java.awt.Graphics2D;

import com.github.forax.zen.*;

public interface Ecran {
	default void handleEvent(Event e) {}

    default void gererClique(PointerEvent p) {}

    default void handleKeyboard(KeyboardEvent k) {}

    // 渲染当前屏幕
    default void render(Graphics2D g) {}
}
