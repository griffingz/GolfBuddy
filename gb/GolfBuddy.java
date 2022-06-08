package gb;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GolfBuddy {

    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (Exception e) {
            System.err.println("Failed to sleep program. Exiting.");
            System.exit(2);
        }
    }

    public static void clearScreen() {
        final String os = System.getProperty("os.name");
        try {
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.err.println("Failed to clear screen. Aborting Program.");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        HomeScreen.prompt(scan);
        scan.close();
    }

}