import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Creates symbols from their type name. New symbol types are made available
 * with {@link #register(String, Function)}, so {@link SlotMachine} never has
 * to be edited when a type is added (extensibility).
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 4.0
 */
public final class SymbolFactory {
    private static final Map<String, Function<String, Symbol>> CREATORS = new LinkedHashMap<>();

    static {
        register("normal", NormalSymbol::new);
        register("ephemeral", EphemeralSymbol::new);
        register("shy", ShySymbol::new);
        register("neon", NeonSymbol::new);
    }

    private SymbolFactory() {
    }

    /**
     * Registers (or replaces) a symbol type.
     *
     * @param type    name of the type
     * @param creator function that builds a symbol from its color
     */
    public static void register(String type, Function<String, Symbol> creator) {
        CREATORS.put(type.toLowerCase(), creator);
    }

    /**
     * Tells whether a type is known.
     *
     * @param type name of the type
     * @return true if it is registered
     */
    public static boolean isRegistered(String type) {
        return type != null && CREATORS.containsKey(type.toLowerCase());
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
        return CREATORS.get(type.toLowerCase()).apply(color);
    }
}
