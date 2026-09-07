public class DemoInteractiva {
    public static void main(String[] args) {
        System.out.println("Iniciando simulador de SlotMachine.");
        
        SlotMachine sm = new SlotMachine();
        
        System.out.println("1. Encendiendo la maquina.");
        sm.makeVisible();
        sleep(1500);

        System.out.println("2. Configurando simbolos permitidos.");
        sm.addSymbol(1, "red");
        sm.addSymbol(2, "blue");
        sm.addSymbol(3, "green");
        sm.addSymbol(4, "magenta"); 
        sleep(1000);

        System.out.println("3. Instalando 3 ruedas.");
        sm.addWheel();
        sleep(500);
        sm.addWheel();
        sleep(500);
        sm.addWheel();
        sleep(1000);

        System.out.println("4. Agregando simbolos a las ruedas.");
        for (int w = 1; w <= 3; w++) {
            sm.placeSymbol(w, "red");
            sm.placeSymbol(w, "blue");
            sm.placeSymbol(w, "green");
            sm.placeSymbol(w, "magenta");
        }
        sleep(1000);

        System.out.println("5. Girando todas las ruedas.");
        sm.spin();
        sleep(1500);

        System.out.println("6. Girando la rueda 1 por 6 pasos.");
        sm.spin(1, 6);
        sleep(1500);

        System.out.println("7. Bloqueando la rueda 2.");
        sm.lock(2);
        sleep(2000);

        System.out.println("8. Intercambiando las ruedas 1 y 3.");
        sm.swap(1, 3);
        sleep(2000);

        System.out.println("9. Desbloqueando la rueda 2.");
        sm.unlock(2);
        sleep(1500);

        System.out.println("10. Configurando combinacion ganadora (Jackpot).");
        sm.spin(new String[]{"magenta", "magenta", "magenta"});
        sm.isJackpot();
        sleep(4500);

        System.out.println("11. Apagando la maquina.");
        sm.exit();
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {}
    }
}