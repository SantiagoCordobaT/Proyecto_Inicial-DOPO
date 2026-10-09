/**
 * Symbol that switches between visible and invisible every time it is
 * selected (every time it becomes the symbol shown by its wheel).
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 4.0
 */
public class ShySymbol extends Symbol {

    /**
     * Creates a shy symbol (visible at the beginning).
     *
     * @param color color name of the symbol
     */
    public ShySymbol(String color) {
        super(color);
    }

    @Override
    public String getType() {
        return "shy";
    }

    /** Toggles the visibility. */
    @Override
    public void onSelected() {
        setVisible(!isVisible());
    }

    @Override
    public String getBadgeColor() {
        return "cyan";
    }
}
