import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Suite de pruebas unitarias automatizadas para SlotMachine (Ciclos 1 y 2).
 * Todas las pruebas se ejecutan en modo invisible para no interrumpir con diálogos.
 * 
 * @author Santiago Cordoba - Camilo Rivas
 * @version 3.0 (Cobertura Total)
 */
public class SlotMachineC2Test {
    private SlotMachine machine;

    @Before
    public void setUp() {
        // Arrange general: instancia limpia antes de cada prueba
        machine = new SlotMachine();
    }

    // =========================================================================
    // CICLO 1: REQUISITOS 1 A 8 (ESTADO, INSERCIÓN Y JACKPOT)
    // =========================================================================

    @Test
    public void shouldCreateEmptyMachineWithOkStatus() {
        // Assert
        assertTrue("La maquina recien creada debe iniciar en estado ok", machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    @Test
    public void shouldAddWheelsWithClampingRules() {
        // Act: pos < 1 debe insertarse en la posicion 1
        machine.addWheel(0);
        // Act: pos > size + 1 debe insertarse al final
        machine.addWheel(99);

        // Assert
        assertTrue(machine.ok());
        assertEquals(2, machine.configuration().length);
    }

    @Test
    public void shouldNotAllowDuplicateSymbols() {
        // Act
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "red"); // Duplicado

        // Assert: que no deberia hacer
        assertFalse("No debe permitir registrar simbolos duplicados", machine.ok());
    }

    @Test
    public void shouldEvaluateJackpotCorrectly() {
        // Arrange: dos ruedas y dos simbolos permitidos
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        // Ambas ruedas inician mostrando "red" 
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        // Agregamos "blue" a la rueda 2 (queda en su lista: ["red", "blue"])
        machine.placeSymbol(2, "blue");

        // Assert: ambas muestran "red", debe ser jackpot
        assertTrue("Debe ser jackpot si todas las ruedas muestran el mismo simbolo", machine.isJackpot());
        assertTrue(machine.ok());

        // Act: giramos la rueda 2 para que avance a su siguiente simbolo ("blue")
        machine.spin(2);

        // Assert: rueda 1 muestra "red" y rueda 2 muestra "blue", NO debe ser jackpot
        assertFalse("No debe ser jackpot si difieren los simbolos", machine.isJackpot());
        assertTrue(machine.ok());
    }

    // =========================================================================
    // CICLO 1: PRUEBAS COMPLEMENTARIAS (ELIMINACIÓN, CONSULTAS Y VISIBILIDAD)
    // =========================================================================

    @Test
    public void shouldDeleteWheelAndSymbolCorrectly() {
        // Arrange
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        // Act & Assert (Ruedas)
        machine.delWheel(2);
        assertTrue("Debe permitir borrar una rueda existente", machine.ok());
        assertEquals(1, machine.configuration().length);

        machine.delWheel(5); // Intento de borrar fuera de límite
        assertTrue("Debe acotar y borrar la ultima rueda", machine.ok());
        assertEquals(0, machine.configuration().length);

        // Act & Assert (Símbolos)
        machine.delSymbol("red");
        assertTrue("Debe permitir borrar un simbolo existente", machine.ok());

        machine.delSymbol("green"); // Símbolo inexistente
        assertFalse("Debe fallar al borrar un simbolo que no existe", machine.ok());
    }

    @Test
    public void shouldSpinAllWheelsSimultaneously() {
        // Arrange: 3 ruedas con la misma secuencia
        for (int i = 1; i <= 3; i++) {
            machine.addWheel(i);
            machine.addSymbol(i, "red");
            machine.addSymbol(i + 1, "blue");
            machine.placeSymbol(i, "red");
            machine.placeSymbol(i, "blue");
        }
        
        // Las tres ruedas inician mostrando "red". 
        // Act: Giramos todas a la vez (Requisito 4)
        machine.spin();

        // Assert: Las tres ruedas deben mostrar ahora "blue"
        assertTrue(machine.ok());
        String[] config = machine.configuration();
        assertEquals("blue", config[0]);
        assertEquals("blue", config[1]);
        assertEquals("blue", config[2]);
    }

    @Test
    public void shouldConsultSymbolsAndDistinctsCorrectly() {
        // Arrange
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");

        // Rueda 1: red, blue
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        // Rueda 2: blue, green
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(2, "green");

        // Act & Assert: symbols() (Requisito 5 - Solo los de la primera rueda)
        String[] firstWheelSymbols = machine.symbols();
        assertEquals(2, firstWheelSymbols.length);
        assertEquals("red", firstWheelSymbols[0]);
        assertEquals("blue", firstWheelSymbols[1]);

        // Act & Assert: distinctSymbols() (Requisito 5 - Únicos en toda la máquina)
        int distinct = machine.distinctSymbols();
        assertEquals("Debe contar exactamente 3 simbolos unicos (red, blue, green)", 3, distinct);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldHandleVisibilityAndExitProperly() {
        // Arrange
        machine.addWheel(1); 
        
        // Act & Assert: Visibilidad (Requisito 7)
        machine.makeVisible();
        assertTrue("Debe cambiar a estado visible sin errores", machine.ok());
        
        machine.makeInvisible();
        assertTrue("Debe cambiar a estado invisible sin errores", machine.ok());
        
        // Act & Assert: Salida (Requisito 8)
        machine.exit();
        assertTrue("Debe ejecutar la salida correctamente", machine.ok());
    }

    // =========================================================================
    // CICLO 2: REQUISITOS 9 Y 10 (MANAGE WHEELS: SWAP, LOCK, UNLOCK)
    // =========================================================================

    @Test
    public void shouldSwapTwoWheelsCorrectly() {
        // Arrange: Rueda 1 con "red", Rueda 2 con "blue"
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");

        // Act: Requisito 9
        machine.swap(1, 2);

        // Assert
        assertTrue(machine.ok());
        String[] config = machine.configuration();
        assertEquals("blue", config[0]);
        assertEquals("red", config[1]);
    }

    @Test
    public void shouldNotSwapSameWheel() {
        // Arrange
        machine.addWheel(1);
        machine.addWheel(2);

        // Act: intentar intercambiar la misma rueda
        machine.swap(1, 1);

        // Assert: que no deberia hacer
        assertFalse("No debe permitir intercambiar una rueda consigo misma", machine.ok());
    }

    @Test
    public void shouldNotSpinLockedWheel() {
        // Arrange
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue"); // Símbolo visible inicial: "red"

        // Act: Requisito 10
        machine.lock(1);
        machine.spin(1, 1); // Intento de rotar rueda bloqueada

        // Assert: no debe rotar ni estar en ok
        assertFalse("No debe permitir girar una rueda fijada", machine.ok());
        assertEquals("red", machine.configuration()[0]);

        // Act: desbloquear y girar
        machine.unlock(1);
        machine.spin(1, 1);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    // =========================================================================
    // CICLO 2: REQUISITOS 11 Y 12 (SPIN STEPS Y CONFIGURACION)
    // =========================================================================

    @Test
    public void shouldSpinWheelArbitraryStepsForwardAndBackward() {
        // Arrange: Rueda con red (0), blue (1), green (2)
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(1, "green");

        // Act: Requisito 11 (Avanzar 2 pasos hacia adelante)
        machine.spin(1, 2);
        assertEquals("green", machine.configuration()[0]);

        // Act: Retroceder 1 paso (-1)
        machine.spin(1, -1);
        assertEquals("blue", machine.configuration()[0]);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldSetSpecificConfigurationSuccessfully() {
        // Arrange: 3 ruedas con varios colores
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");

        for (int i = 1; i <= 3; i++) {
            machine.placeSymbol(i, "red");
            machine.placeSymbol(i, "blue");
            machine.placeSymbol(i, "green");
        }

        // Act: Requisito 12
        String[] targetConfig = new String[]{"green", "red", "blue"};
        machine.spin(targetConfig);

        // Assert
        assertTrue(machine.ok());
        assertArrayEquals(targetConfig, machine.configuration());
    }

    @Test
    public void shouldRejectConfigurationIfSymbolNotPresentInWheel() {
        // Arrange
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red"); // Rueda 1 solo tiene "red"
        machine.placeSymbol(2, "blue");

        // Act: Intentar fijar "blue" en la rueda 1 que no lo posee
        String[] invalidConfig = new String[]{"blue", "blue"};
        machine.spin(invalidConfig);

        // Assert: que no deberia hacer
        assertFalse("Debe rechazar la configuracion si una rueda no tiene el simbolo", machine.ok());
    }
}