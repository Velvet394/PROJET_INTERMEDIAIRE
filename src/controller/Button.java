package controller;

public class Button {
	int x, y, width, height;
    String text;

    Button(int x, int y, int w, int h, String text) {
        this.x = x; this.y = y;
        this.width = w; this.height = h;
        this.text = text;
    }

    void draw(Graphics2D g) {
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.drawString(text, x + 10, y + height / 2);
    }

    boolean isInside(int mx, int my) {
        return mx >= x && mx <= x + width &&
               my >= y && my <= y + height;
    }
}
