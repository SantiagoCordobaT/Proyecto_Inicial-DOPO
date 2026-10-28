/**
 * The ordinary symbol: it never changes its size nor its visibility.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 4.0
 */
public class NormalSymbol extends Symbol {

    /**
     * Creates a normal symbol.
     *
     * @param color color name of the symbol
     */
    public NormalSymbol(String color) {
        super(color);
    }
    
    @Override
    public String getType() {
        return "normal";
    }
}
