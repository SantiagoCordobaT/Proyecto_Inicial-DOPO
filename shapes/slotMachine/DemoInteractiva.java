public class DemoInteractiva {
    public static void main(String[] args) {
        SlotMachine sm = new SlotMachine();
        
        System.out.println("1. Encendiendo maquina (makeVisible)");
        sm.makeVisible();
        sleep(1000);

        System.out.println("2. Cargando sistema de pagos");
        sm.addSymbol(1, "red");
        sm.addSymbol(2, "blue");
        sm.addSymbol(3, "green");
        sm.addSymbol(4, "magenta"); // Uso magenta porque 'yellow' esta reservado para el marco

        System.out.println("3. Instalando 3 carretes en el chasis");
        sm.addWheel();
        sleep(400);
        sm.addWheel();
        sleep(400);
        sm.addWheel();
        sleep(800);

        System.out.println("4. Surtimiento de simbolos");
        for (int w = 1; w <= 3; w++) {
            sm.placeSymbol(w, "red");
            sm.placeSymbol(w, "blue");
            sm.placeSymbol(w, "green");
            sm.placeSymbol(w, "magenta");
        }
        sleep(1000);

        System.out.println("5. Giro animado de la Rueda 1 (6 pasos)");
        sm.spin(1, 6);
        sleep(1000);

        System.out.println("6. Bloqueando la rueda 2 (marco rojo)");
        sm.lock(2);
        sleep(1500);

        System.out.println("7. Intercambiando Rueda 1 y 3 animado");
        sm.swap(1, 3);
        sleep(1500);

        System.out.println("8. Forzando Jackpot (Marquesina y marco cambian de color)");
        sm.unlock(2);
        sm.spin(new String[]{"magenta", "magenta", "magenta"});
        sm.isJackpot();
        sleep(4000);

        System.out.println("9. Apagando maquina");
        sm.exit();
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {}
    }
}