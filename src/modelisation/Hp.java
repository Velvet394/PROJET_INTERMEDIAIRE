package modelisation;

public  class Hp {
    private int maxHp;
    private int hp;

    public Hp(int max) {
        this.maxHp = max;
        this.hp = max;
    }

    public void damage(int dmg) { 
    	hp = Math.max(0, hp - dmg); 
    }

    public void heal(int val) { 
    	hp = Math.min(maxHp, hp + val); 
    }

    public boolean isDead() { 
    	return hp <= 0;
    }

    public boolean isAlive() { 
    	return hp > 0; 
    }
    
    public int current() { 
    	return hp; 
    }

    public int max() { 
    	return maxHp; 
    }
    
    @Override
    public boolean equals(Object obj) {
    	return obj instanceof Hp h &&
    			this.maxHp==h.maxHp &&
    			this.hp==h.hp;
    }
}

