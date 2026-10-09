/**
 * Symbol that shrinks every time its wheel spins until it is just a dot.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 4.0
 */
public class EphemeralSymbol extends Symbol {
    
    public static final int STEP = 5;
    public static final int MIN_SIZE = 3;

    /**
     * Creates an ephemeral symbol.
     *
     * @param color color name of the symbol
     */
    public EphemeralSymbol(String color) {
        super(color);
    }

    @Override
    public String getType() {
        return "ephemeral";
    }

    /** Decrements the size. */
    @Override
    public void onSpin() {
        setSize(Math.max(MIN_SIZE, getSize() - STEP));
    }

    @Override
    public String getBadgeColor() {
        return "orange";
    }
}
