package modelisation;

public class Exp {
    private int exp;
    private int maxExp;

    public Exp(int start, int max) {
        this.exp = start;
        this.maxExp = max;
    }

    public void add(int val) { exp += val; }

    public boolean levelUp() { return exp >= maxExp; }
}
