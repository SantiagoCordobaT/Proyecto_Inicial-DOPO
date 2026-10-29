import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Creates symbols from their type name.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 4.0
 */
public final class SymbolFactory {
    private static final Map<String, Symbol> PROTOTYPES = new LinkedHashMap<>();

    static {
        register("normal", new NormalSymbol("white"));
        register("ephemeral", new EphemeralSymbol("white"));
        register("shy", new ShySymbol("white"));
        register("neon", new NeonSymbol("white"));
    }

    private SymbolFactory() {
    }

    /**
     * Registers (or replaces) a symbol type.
     *
     * @param type      name of the type
     * @param prototype base symbol of that type
     */
    public static void register(String type, Symbol prototype) {
        PROTOTYPES.put(type.toLowerCase(), prototype);
    }

    /**
     * Tells whether a type is known.
     *
     * @param type name of the type
     * @return true if it is registered
     */
    public static boolean isRegistered(String type) {
        return type != null && PROTOTYPES.containsKey(type.toLowerCase());
    }

    /**
     * Creates a symbol.
     *
     * @param type  name of the type (case insensitive)
     * @param color color of the symbol
     * @return the new symbol
     * @throws IllegalArgumentException if the type is unknown or the color is invalid
     */
    public static Symbol create(String type, String color) {
        if (!isRegistered(type)) {
            throw new IllegalArgumentException("Tipo de simbolo desconocido: " + type);
        }
        return PROTOTYPES.get(type.toLowerCase()).createNew(color);
    }
}
