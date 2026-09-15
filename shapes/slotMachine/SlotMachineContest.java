import java.util.ArrayList;
import java.util.List;

/**
 * Solucionador y simulador para el problema Slot Machine.
 * Basado en: https://www.youtube.com/watch?v=gYkF0kZ1bkE
 */
public class SlotMachineContest {

    // Almacena la secuencia de acciones (rueda, pasos) para que simulate() pueda reproducirlas
    private List<int[]> actions;

    /**
     * Constructor de la clase SlotMachineContest.
     */
    public SlotMachineContest() {
        this.actions = new ArrayList<>();
    }

    /**
     * Resuelve el juego a ciegas tratando la maquina como un oraculo de caja negra.
     * Debe permanecer invisible durante toda la ejecucion.
     * 
     * @param n Cantidad de ruedas y simbolos
     * @return Cantidad total de acciones realizadas para ganar
     */
    public int solve(int n) {
        this.actions.clear();

        SlotMachine machine = new SlotMachine(n);
        machine.makeInvisible();

        if (machine.distinctSymbols() == 1) {
            return 0;
        }

        // =========================================================================
        // FASE 1: Obtener N símbolos distintos (distinctSymbols() == n)
        // =========================================================================
        // TODO: Deja la rueda 1 quieta. Recorre desde la rueda 2 hasta la n.
        // Para cada rueda, pruébala rotándola paso a paso y déjala en la posición
        // que maximice machine.distinctSymbols().
        
        
        // =========================================================================
        // FASE 2: Descubrir la posición/desfase relativo de cada rueda
        // =========================================================================
        // TODO: Con los n símbolos distintos, mueve la rueda 1 y prueba girar
        // las demás ruedas de forma diferencial para identificar cuál rueda
        // contiene cada símbolo respecto a la primera rueda.
        int[] offset = new int[n + 1]; // Guarda cuántos pasos le faltan a cada rueda
        
        
        // =========================================================================
        // FASE 3: Alinear todas las ruedas al mismo símbolo (Jackpot)
        // =========================================================================
        // TODO: Aplica los giros calculados en offset[] para cada rueda (2 hasta n)
        // para que coincidan con la rueda 1.
        

        return this.actions.size();
    }

    /**
     * Simula visualmente las acciones calculadas por solve(n) sobre una maquina visible.
     * 
     * @param n Cantidad de ruedas y simbolos
     */
    public void simulate(int n) {
        // 1. Calcula las acciones resolviendo primero el problema
        this.solve(n);

        // 2. Crea la máquina para demostración visual
        SlotMachine visualMachine = new SlotMachine(n);
        visualMachine.makeVisible();
        sleep(1000);

        // 3. Reproduce paso a paso las acciones registradas
        for (int[] action : this.actions) {
            int wheel = action[0];
            int steps = action[1];
            visualMachine.spin(wheel, steps);
            sleep(600); // Pausa para permitir la apreciación visual
        }

        if (visualMachine.isJackpot()) {
            System.out.println("Jackpot!!!");
        }
    }

    /**
     * Metodo auxiliar para registrar y ejecutar un giro tanto en el oraculo.
     */
    private void recordAndSpin(SlotMachine machine, int wheel, int steps) {
        machine.spin(wheel, steps);
        this.actions.add(new int[]{wheel, steps});
    }

    /**
     * Pausa auxiliar para la animacion visual en simulate.
     */
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}