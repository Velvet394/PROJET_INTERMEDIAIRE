package modelisation;

import java.util.Random;

public final class Dice {
    private static final Random RNG = new Random();

    public static int roll(int min, int max) {
        return RNG.nextInt(max - min + 1) + min;
    }
}
