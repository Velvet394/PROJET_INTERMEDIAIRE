package modelisation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class HallOfFame {
	
	private HallOfFame() {}
	// on met dans racine
	  private static final Path FILE = Path.of("hall_of_fame.tsv");
	  private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


	  private static String esc(String s) {
	    return s.replace("\t", " ").replace("\n", " ").replace("\r", " ");
	  }

	  public static HallOfFameEntry snapshotFromHero(Hero hero) {
	    Objects.requireNonNull(hero);
	    var bp = hero.getBackpack();

	    // 去重收集背包里的武器
	    Set<Weapon> weapons = new HashSet<>();
	    for (Item it : bp.contenu().values()) {
	      if (Backpack.isweapon(it)) {
	        weapons.add((Weapon)it);
	      }
	    }

	    int totalValue = weapons.stream().mapToInt(Weapon::price).sum();
	    List<String> weaponNames = weapons.stream()
	        .map(Weapon::nom)
	        .sorted()
	        .toList();

	    return new HallOfFameEntry(
	        LocalDateTime.now().format(FMT),
	        hero.or(),
	        totalValue,
	        hero.niveau(),
	        hero.maxHp(),
	        hero.maxMana(),
	        weaponNames
	    );
	  }

	  public static void add(HallOfFameEntry e) {
	    Objects.requireNonNull(e);
	    try {
	      if (!Files.exists(FILE)) {
	        Files.writeString(FILE,
	            "time\tgold\ttotalValue\tlevel\thpMax\tmanaMax\tweapons\n",
	            StandardCharsets.UTF_8,
	            StandardOpenOption.CREATE);
	      }
	      String line = String.join("\t",
	          esc(e.timestamp()),
	          Integer.toString(e.gold()),
	          Integer.toString(e.totalValue()),
	          Integer.toString(e.level()),
	          Integer.toString(e.hpMax()),
	          Integer.toString(e.manaMax()),
	          esc(String.join(",", e.weapons()))
	      ) + "\n";

	      Files.writeString(FILE, line, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
	    } catch (IOException ex) {
	      // 名人堂失败不应该让游戏崩：只打印
	      ex.printStackTrace();
	    }
	  }

	  public static List<HallOfFameEntry> loadAll() {
	    if (!Files.exists(FILE)) return List.of();
	    try {
	      List<String> lines = Files.readAllLines(FILE, StandardCharsets.UTF_8);
	      if (lines.isEmpty()) return List.of();

	      // 跳过表头
	      return lines.stream()
	          .skip(1)
	          .map(String::trim)
	          .filter(s -> !s.isEmpty())
	          .map(HallOfFame::parseLine)
	          .filter(Objects::nonNull)
	          .collect(Collectors.toList());
	    } catch (IOException ex) {
	      ex.printStackTrace();
	      return List.of();
	    }
	  }

	  private static HallOfFameEntry parseLine(String line) {
	    String[] parts = line.split("\t", -1);
	    if (parts.length < 7) return null;

	    String time = parts[0];
	    int gold = parseIntSafe(parts[1]);
	    int totalValue = parseIntSafe(parts[2]);
	    int level = parseIntSafe(parts[3]);
	    int hpMax = parseIntSafe(parts[4]);
	    int manaMax = parseIntSafe(parts[5]);

	    List<String> weapons = parts[6].isEmpty()
	        ? List.of()
	        : Arrays.stream(parts[6].split(",", -1)).map(String::trim).filter(s -> !s.isEmpty()).toList();

	    return new HallOfFameEntry(time, gold, totalValue, level, hpMax, manaMax, weapons);
	  }

	  private static int parseIntSafe(String s) {
	    try { return Integer.parseInt(s.trim()); }
	    catch (Exception e) { return 0; }
	  }
}
