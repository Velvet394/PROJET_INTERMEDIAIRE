package modelisation;
/**
 * Represents an effect applied by a weapon or item during combat.
 */
public interface Effect {
	void apply(Hero h,Ennemi e,Combat c);
}
