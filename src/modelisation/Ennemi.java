package modelisation;
import java.util.*;
import java.io.*;
import java.time.chrono.MinguoChronology;

public class Ennemi {
    private final String nom;
    
    //private final Hp hp;
    private int hp;
    private final int maxHp;
    
    private final int dmg;
    private int block;
    private final int blockPoint;
    private final int healPoint;
    
    private final ArrayList<EnnemyActionType> intentions;
    
    private final int tendanceATTACK; 
    private final int tendanceSKILL;
    private final int tendanceDEFEND; 
    private final int tendanceHEAL;
    //private final int tendanceCURSE;
    private final int tendanceBUFF;

    public Ennemi(String nom, int pv, int d ,/*List<EnnemyActionType> skills,*/int b,int h, int att,int sk,int def, int heal,/*int cur,*/int buf) {
    	Objects.requireNonNull(nom);
    	//Objects.requireNonNull(skills);
    	if(pv<=0||b<0||h<0||d<0||att<0||sk<0||def<0||heal<0||/*cur<0||*/buf<0) {
    		throw new IllegalArgumentException("Argument(s) de ennemi est/sont inferieur que 0");
    	}
        this.nom = nom;
        
        this.hp = pv;
        this.maxHp=pv;
        this.blockPoint=b;
        this.healPoint=h;
        this.intentions = /*skills;*/new ArrayList<>();
        this.block = 0;
        this.dmg=d;
        
        
        tendanceATTACK=att;
        tendanceSKILL=sk;
        tendanceDEFEND=def;
        tendanceHEAL=heal;
        tendanceBUFF=buf;
        //tendanceCURSE=cur;
    }

    public boolean estMort() { 
    	return hp<=0; 
    }

    public EnnemyActionType choisirIntent() {
    	/*
    	if (hp <= maxHp/2) {
    		if(Dice.roll(0, 100)<=tendanceHEAL)
    			{return EnnemyActionType.HEAL;}
    		else if(Dice.roll(0, 100)<=tendanceDEFEND) {
    			return EnnemyActionType.DEFEND;
    		}
    	}
    	if(Dice.roll(0, 10)<=5) {
    		if(Dice.roll(0, 100)<=tendanceSKILL) {
    			return EnnemyActionType.SKILL;
    		}
    		if(Dice.roll(0, 100)<=tendanceBUFF) {
    			return EnnemyActionType.BUFF;
    		}
    	}
    	if(Dice.roll(0, 100)<=tendanceATTACK) {
    		return EnnemyActionType.ATTACK;
    	}
    	return EnnemyActionType.CURSE;*/
    	int roll = Dice.roll(1, 550); // 1~100

        if (hp <= maxHp / 2) {
            int thresholdHeal = tendanceHEAL;
            int thresholdDefend = thresholdHeal + tendanceDEFEND;
            int thresholdAttack = thresholdDefend + tendanceATTACK;

            if (roll <= thresholdHeal) {
                return EnnemyActionType.HEAL;
            } else if (roll <= thresholdDefend) {
                return EnnemyActionType.DEFEND;
            } else if (roll <= thresholdAttack) {
                return EnnemyActionType.ATTACK;
            } else {
                return EnnemyActionType.CURSE;
            }
        }
        
        int thresholdAttack =tendanceATTACK;
        int thresholdDefend = thresholdAttack + tendanceDEFEND;
        int thresholdSkill = thresholdDefend + tendanceSKILL;
        int thresholdBuff = thresholdSkill + tendanceBUFF;
        
        if(roll<=thresholdAttack) {
        	return EnnemyActionType.ATTACK;
        } else if(roll<=thresholdDefend) {
        	return EnnemyActionType.DEFEND;
        } else if (roll <= thresholdSkill) {
            return EnnemyActionType.SKILL;
        } else if (roll <= thresholdBuff) {
            return EnnemyActionType.BUFF;
        } else {
            return EnnemyActionType.CURSE;
        }
        
    } 

    public int hp() { 
    	return hp; 
    }
    
    public void Action() {
    	intentions.clear();
    	var num=Dice.roll(1, 3);
    	for(int i=0;i<num;i++) {
    		intentions.add(choisirIntent());
    	}
    }
    
    public void executerAction(Hero hero) {
    	Objects.requireNonNull(hero);
        for(var i:intentions) {
        	switch (i) {
            case ATTACK:
                attack(hero);
                break;
            case DEFEND:
                defend();
                break;
            case HEAL:
                heal();
                break;
            case CURSE:
                curse(hero);
                break;
            case BUFF:
                buff();
                break;
            case SKILL:
                skill();
                break;
            default:
                throw new IllegalArgumentException("Action inconnue : " + i);
        }
        }
        intentions.clear();
        return;
    }
    
    private void attack(Hero hero) {
    	Objects.requireNonNull(hero);
        if (!estMort()) {
            hero.damage(this.dmg);
            IO.println(nom + " attaque et inflige " + dmg + " points de dégât.");
        }
    }
    
    private void defend() { 
        block += blockPoint;
        IO.println(nom + " se défend et augmente sa protection de " + blockPoint + " points.");
    }
    
    private void heal() {
        if (!estMort()) {
            if(hp+healPoint>=maxHp) {
            	hp = maxHp;
            	IO.println(nom + " se guérit de " + healPoint + " points.");
            	return;
            }
            hp+=healPoint;
            IO.println(nom + " se guérit de " + healPoint + " points.");
            return;
        }
    }
    
    private void curse(Hero hero) {
    	Objects.requireNonNull(hero);
        int curseStrength = 1;
        hero.addMalediction(curseStrength);
        IO.println(nom + " applique une malédiction au héros.");
    }
    
    private void buff() {
        int buffAmount = 3; 
        block += buffAmount; 
        System.out.println(nom + " se buff et gagne " + buffAmount + " points de protection.");
    }
    
    private void skill() {
    }
    
    public void damage(int dmg) {
    	var res=block-dmg;
    	if(res>0) {
    		this.block=res;
    		return;
    	}
    	hp = Math.max(0, hp + block - dmg);
    	block = 0;
    	return;
    }
    
    public int dmg() {
    	return this.dmg;
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(nom,hp,blockPoint,healPoint,maxHp,intentions,dmg,block,tendanceATTACK,tendanceBUFF,tendanceDEFEND,tendanceHEAL,tendanceSKILL);
    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return super.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
    	return obj instanceof Ennemi e &&
    			this.intentions.equals(e.intentions) &&
    			this.nom.equals(e.nom) &&
    			//this.hp.equals(e.hp) &&
    			this.hp==e.hp &&
    			this.maxHp==e.maxHp &&
    			this.dmg==e.dmg &&
    			this.block==e.block &&
    			this.blockPoint==e.blockPoint &&
    			this.healPoint==e.healPoint &&
    			this.tendanceATTACK==e.tendanceATTACK &&
    			this.tendanceBUFF==e.tendanceBUFF &&
    			this.tendanceDEFEND==e.tendanceDEFEND &&
    			this.tendanceHEAL==e.tendanceHEAL &&
    			this.tendanceSKILL==e.tendanceSKILL;
    }
    
}