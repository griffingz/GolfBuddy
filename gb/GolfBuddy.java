package gb;

import java.util.Scanner;

public class GolfBuddy {

    public static void clearScreen() {
        final String os = System.getProperty("os.name");
        try {
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (Exception e) {
            System.err.println("Failed to clear screen. Aborting Program.");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String choice = HomeScreen.prompt(scan);
            HomeScreen.executeChoice(choice, scan);
            running = false;
        }
        scan.close();
    }

}