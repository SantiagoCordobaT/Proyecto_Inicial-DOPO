import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test class integrating tests from different student groups.
 */
public class SlotMachineCTest {

    private SlotMachine machine;

    @Before
    public void setUp() {
        machine = new SlotMachine();
    }

    // ==========================================
    // PRUEBAS - GRUPO: GomezCarrero
    // ==========================================

    /**
     * Verifies insertion at the first position.
     */
    @Test
    public void testAddWheelInFirstPosition() {
        machine.addWheel(1);
        machine.addWheel(1);
        assertEquals(2, machine.wheelCount());
        assertTrue(machine.ok());
    }

    /**
     * Verifies that positions above the maximum are adjusted correctly.
     */
    @Test
    public void testAddWheelPositionAboveMaximum() {
        machine.addWheel(100);
        assertEquals(1, machine.wheelCount());
        assertTrue(machine.ok());
    }

    // ==========================================
    // PRUEBAS - GRUPO 4: BUSTOS-ZORRO
    // ==========================================

    /**
     * An existing symbol should be removed from the machine.
     */
    @Test
    public void accordingZGBCShouldDeleteExistingSymbol() {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.delSymbol("red");
        assertTrue(machine.ok());
    }

    /**
     * A symbol that does not exist should not be removed.
     */
    @Test
    public void accordingZGBCShouldNotDeleteMissingSymbol() {
        machine.addWheel(1);
        machine.delSymbol("green");
        assertFalse(machine.ok());
    }

    // ==========================================
    // PRUEBAS - GRUPO: GualdronL-VillagranR (G06) [Adaptadas]
    // ==========================================

    @Test
    public void accordingGlVrshouldCreateSlotMachine() {
        SlotMachine sltmchn = new SlotMachine();
        assertNotNull(sltmchn);
        sltmchn.addWheel(1);
        assertTrue(sltmchn.ok());
    }

    @Test
    public void accordingGlVrshouldAddWheel() {
        SlotMachine sltmchn = new SlotMachine();
        sltmchn.addWheel(1);
        sltmchn.addWheel(2);
        assertEquals(2, sltmchn.wheelCount());
        assertTrue(sltmchn.ok());
    }

    @Test
    public void accordingGlVrshouldDelWheel() {
        SlotMachine sltmchn = new SlotMachine();
        sltmchn.addWheel(1);
        sltmchn.delWheel(1);
        assertEquals(0, sltmchn.wheelCount());
        assertTrue(sltmchn.ok());
    }

    @Test
    public void accordingGlVrshouldGiveSymbols() {
        SlotMachine sltmchn = new SlotMachine();
        int numberWheelsToAdd = 5;
        for (int i = 0; i < numberWheelsToAdd; i++) {
            sltmchn.addWheel(i + 1);
        }
        sltmchn.addSymbol(1, "magenta");
        sltmchn.addSymbol(2, "red");
        sltmchn.addSymbol(3, "yellow");
        sltmchn.addSymbol(4, "blue");
        sltmchn.addSymbol(5, "green");
        
        String[] proof = sltmchn.symbols();
        assertNotNull(proof);
        assertTrue(proof.length > 0);
    }
}