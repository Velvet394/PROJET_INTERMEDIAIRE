package modelisation;

import java.util.List;

public record HallOfFameEntry(String timestamp,   // 保存一条时间
	    int gold,           // hero.or()
	    int totalValue,     // 背包武器价值总和
	    int level,          // hero.niveau()
	    int hpMax,          // hero.maxHp()
	    int manaMax,        // hero.maxMana()
	    List<String> weapons // 武器名字列表
	    ) {
}
