package modelisation;

public class Mana {
    private int maxMana;
    private int mana;

    public Mana(int max) {
        this.maxMana = max;
        this.mana = max;
    }

    public void recharge() { 
    	mana = maxMana; 
    }

    public void use(int val) { 
    	mana = Math.max(0, mana - val); 
    }

    public int current() { 
    	return mana; 
    }
}
