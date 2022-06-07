package gb;

import java.util.Scanner;

public class HomeScreen {

    public static String prompt(Scanner scan) {
        boolean choosing = true;
        while (choosing) {
            GolfBuddy.clearScreen();
            System.out.println( "+-----------------------------------------------+\n" +
                                "|                                               |\n" +
                                "| Welcome To Golf Buddy!                        |\n" +
                                "|                                               |\n" +
                                "| What would you like to do?                    |\n" +
                                "| 1. Enter a new scorecard                      |\n" +
                                "| 2. Add a new course                           |\n" +
                                "| 3. Review scores                              |\n" +
                                "| Type \"HOME\" to go back home from any screen   |\n" +
                                "| Type \"EXIT\" to exit Golf Buddy                |\n" +
                                "|                                               |\n" +
                                "+-----------------------------------------------+\n");
            String choice = scan.nextLine();
            if (choice.equals("1")                  || 
                choice.equals("2")                  ||
                choice.equals("3")                  ||
                choice.toUpperCase().equals("HOME") ||
                choice.toUpperCase().equals("EXIT")) {
                    choosing = false;
                    return choice;
            }
        }
        return "ERROR";
    }

    public static void executeChoice(String choice, Scanner scan) {
        if (choice.equals("1")) {
            NewScorecard.prompt(scan);
        } else if (choice.equals("2")) {
            NewCourse.prompt(scan);
        } else if (choice.equals("3")) {
            ReviewScores.prompt(scan);
        } else if (choice.toUpperCase().equals("EXIT")) {
            System.out.println("Exiting Golf Buddy...");
            System.exit(0);
        }
    }

}