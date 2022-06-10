package gb;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;

public class ReviewScores {

    private static ArrayList<String> getCourseNames() {
        ArrayList<String> courses = new ArrayList<>();
        try {
            File courseFile = new File("CourseFile.csv");
            Scanner fileScan = new Scanner(courseFile);
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                String courseName = line.substring(0, line.indexOf(','));
                courses.add(courseName);
            }
            fileScan.close();
        } catch (Exception e) {
            System.err.println("Failed to read course file. Exiting.");
            System.exit(3);
        }
        return courses;
    }

    private static boolean validCourse(String choice, int numCourses) {
        if (choice.matches("^[0-9]{1,3}$"))
            if (Integer.parseInt(choice) > 0 && Integer.parseInt(choice) <= numCourses)
                return true;
        return false;
    }

    private static String[][] getCourseScorecards(String courseName) {
        ArrayList<String[]> scorecards = new ArrayList<>();
        try {
            File scorecardFile = new File("ScorecardFile.csv");
            Scanner fileScan = new Scanner(scorecardFile);
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                String lineName = line.substring(0, line.indexOf(','));
                if (courseName.equals(lineName))
                    scorecards.add(line.split(","));
            }
            fileScan.close();
        } catch (Exception e ) {
            System.err.println("Failed to read course file for stats. Exiting.");
            e.printStackTrace();
            System.exit(3);
        }
        String[][] scorecardsArray = new String[scorecards.size()][0];
        scorecards.toArray(scorecardsArray);
        return scorecardsArray;
    }

    private static Integer[] getCoursePars(String courseName) {
        ArrayList<Integer> pars = new ArrayList<>();
        try {
            File courseFile = new File("CourseFile.csv");
            Scanner fileScan = new Scanner(courseFile);
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                String lineName = line.substring(0, line.indexOf(','));
                if (courseName.equals(lineName)) {
                    line = line.substring(line.indexOf(',')+1);
                    int numHoles = Integer.parseInt(line.substring(0, line.indexOf(',')));
                    for (int i = 0; i < numHoles; i++) {
                        line = line.substring(line.indexOf(',')+1);
                        Integer par = Integer.parseInt(line.substring(0, line.indexOf(',')));
                        pars.add(par);
                    }
                    if (pars.size() != numHoles) {
                        System.err.println("Failed to get course pars. Exiting.");
                        System.exit(6);
                    }
                    break;
                }
            }
            fileScan.close();
        } catch (Exception e) {
            System.err.println("Failed to get course pars. Exiting.");
            e.printStackTrace();
            System.exit(3);
        }
        Integer[] parsArray = new Integer[0];
        return pars.toArray(parsArray);
    }

    private static Integer getCourseOptimal(String[][] scorecards, Integer[] pars) {
        int optimalScore = 0;
        if (scorecards.length == 0)
            return 0;
        for (int i = 2; i < scorecards[0].length; i++) {
            int holeBest = 99999;
            for (int j = 0; j < scorecards.length; j++) {
                int currentHoleScore = Integer.parseInt(scorecards[j][i]);
                if (currentHoleScore < holeBest)
                    holeBest = currentHoleScore;
            }
            optimalScore = optimalScore + holeBest;
        }
        int parTotal = 0;
        for (Integer par : pars) {
            parTotal = parTotal + par;
        }
        return optimalScore - parTotal;
    }

    private static void printCourseStats(String[][] scorecards, Integer[] pars) {
        int bestScore = 99999;
        int worstScore = -99999;
        int sumScores = 0;
        for (int i = 0; i < scorecards.length; i++) {
            int scorecardScore = 0;
            for (int j = 2; j < scorecards[i].length; j++) {
                int score = Integer.parseInt(scorecards[i][j]);
                int par = pars[j-2];
                int adjustedHoleScore = score - par;
                scorecardScore = scorecardScore + adjustedHoleScore;
            }
            if (scorecardScore < bestScore)
                bestScore = scorecardScore;
            if (scorecardScore > worstScore)
                worstScore = scorecardScore;
            sumScores = sumScores + scorecardScore;
        }
        if (scorecards.length == 0) {
            bestScore = 0;
            worstScore = 0;
        }
        double averageScore = 0;
        if (scorecards.length > 0)
            averageScore = sumScores / (double) scorecards.length;
        String avgString = String.format("%,.2f", averageScore);
        int optimalScore = getCourseOptimal(scorecards, pars);
        System.out.println( "| Best Score: " + bestScore + "\n" +
                            "| Optimal Score: " + optimalScore + "\n" +
                            "| Average Score: " + avgString + "\n" +
                            "| Worst Score: " + worstScore + "\n" +
                            "| Times Played: " + scorecards.length + "\n" +
                            "|");
    }

    private static void printHoleStats(String[][] scorecards) {
        if (scorecards.length > 0) {
            for (int i = 2; i < scorecards[0].length; i++) {
                int holeBest = 99999;
                int holeWorst = -99999;
                int holeSum = 0;
                for (int j = 0; j < scorecards.length; j++) {
                    int currentHoleScore = Integer.parseInt(scorecards[j][i]);
                    holeSum = holeSum + currentHoleScore;
                    if (currentHoleScore < holeBest) {
                        holeBest = currentHoleScore;
                    }
                    if (currentHoleScore > holeWorst) {
                        holeWorst = currentHoleScore;
                    }
                }
                double holeAverage = holeSum / (double) scorecards.length;
                String avgString = String.format("%,.2f", holeAverage);
                System.out.println("| Hole " + (i-1) + ": Best: " + holeBest + " Average: " + avgString + " Worst: " + holeWorst);
            }
        }
    }

    private static void printCourseScorecards(String courseName) {
        String[][] scorecards = getCourseScorecards(courseName);
        System.out.println("| |#|Date      |Holes");
        for (int i = 0; i < scorecards.length; i++) {
            System.out.print("| |" + (i+1) + "|");
            for (int j = 1; j < scorecards[i].length; j++) {
                if (scorecards[i][j].equals("UNKNOWN"))
                    System.out.print(scorecards[i][j] + "   |");
                else
                    System.out.print(scorecards[i][j] + "|");
            }
            System.out.println();
        }
    }

    private static boolean validScorecard(String courseName, String choice) {
        String[][] scorecards = getCourseScorecards(courseName);
        choice = choice.toUpperCase();
        if (choice.matches("^[0-9]{1,5}[d-eD-E]{1}$")) {
            int endIndex = 0;
            if (choice.indexOf('D') != -1)
                endIndex = choice.indexOf('D');
            if (choice.indexOf('E') != -1)
                endIndex = choice.indexOf('E');
            int scorecard = Integer.parseInt(choice.substring(0, endIndex));
            if (scorecard >= 1 && scorecard <= scorecards.length) {
                return true;
            }
        }
        return false;
    }

    private static void deleteScorecard(String courseName, String choice) {
        ArrayList<String> scorecardLines = new ArrayList<>();
        try {
            File scorecardFile = new File("ScorecardFile.csv");
            Scanner fileScan = new Scanner(scorecardFile);
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                scorecardLines.add(line);
            }
            fileScan.close();
        } catch (Exception e) {
            System.err.println("Failed to remove scorecard from file. Exiting.");
            e.printStackTrace();
            System.exit(7);
        }
        int endIndex = 0;
        choice = choice.toUpperCase();
        if (choice.indexOf('D') != -1)
            endIndex = choice.indexOf('D');
        if (choice.indexOf('E') != -1)
            endIndex = choice.indexOf('E');
        int scorecard = Integer.parseInt(choice.substring(0, endIndex));

        ArrayList<String> courseLines = new ArrayList<>();
        for (String line : scorecardLines) {
            if (line.substring(0, line.indexOf(',')).equals(courseName)) {
                courseLines.add(line);
            }
        }
        scorecardLines.remove(courseLines.get(scorecard-1));
        try {
            File scorecardFile = new File("ScorecardFile.csv");
            FileWriter scorecardWriter = new FileWriter(scorecardFile);
            for (String line : scorecardLines) {
                scorecardWriter.write((line + "\n"));
            }
            scorecardWriter.close();
        } catch (Exception e) {
            System.err.println("Failed to remove scorecard from file. Exiting.");
            e.printStackTrace();
            System.exit(7);
        }
    }

    public static void prompt(Scanner scan) {
        while (true) {
            GolfBuddy.clearScreen();
            System.out.println( "+-----------------------------------------------+\n" +
                                "|\n" +
                                "| Review Scores\n" +
                                "|\n" +
                                "| Select A Course From The List Below:");
            ArrayList<String> courseNames = getCourseNames();
            for (int i = 0; i < courseNames.size(); i++) {
                System.out.println("| " + (i+1) + ". " + courseNames.get(i));
            }
            System.out.println("|\n" + "+-----------------------------------------------+\n");
            String choice = scan.nextLine();
            if (validCourse(choice, courseNames.size())) {
                courseOverview(courseNames.get(Integer.parseInt(choice)-1), scan);
                break;
            } else if (choice.toUpperCase().equals("HOME")) {
                HomeScreen.prompt(scan);
                break;
            } else {
                System.out.println("INVALID COURSE! Please pick a number.");
                GolfBuddy.sleep(3);
            }
        }
    }

    private static void courseOverview(String courseName, Scanner scan) {
        while (true) {
            GolfBuddy.clearScreen();
            System.out.println( "+-----------------------------------------------+\n" +
                                "|\n" +
                                "| " + courseName + " Overview\n" +
                                "|");
            String[][] scorecards = getCourseScorecards(courseName);
            Integer[] pars = getCoursePars(courseName);
            printCourseStats(scorecards, pars);
            printHoleStats(scorecards);
            System.out.println( "|\n" +
                                "| Enter \"VIEW\" to view scorecards\n" +
                                "+-----------------------------------------------+");
            String choice = scan.nextLine();
            if (choice.toUpperCase().equals("HOME")) {
                HomeScreen.prompt(scan);
                break;
            } else if (choice.toUpperCase().equals("VIEW")) {
                courseScorecardsView(courseName, scan);
                break;
            } else {
                System.out.println("INVALID OPTION!");
                GolfBuddy.sleep(3);
            }
        }
    }

    private static void courseScorecardsView(String courseName, Scanner scan) {
        while (true) {
            GolfBuddy.clearScreen();
            System.out.println( "+-----------------------------------------------+\n" +
                                "|\n" +
                                "| " + courseName + " Scorecards\n" +
                                "|");
            printCourseScorecards(courseName);
            System.out.println("|\n" +
                                "| Enter a scorecard number followed by a letter\n" +
                                "| below to perform the following actions:\n" +
                                "| E: Edit scorecard\n" +
                                "| D: Delete scorecard\n" +
                                "|\n" +
                                "| Or enter \"BACK\" to go back to the course\n" +
                                "| overview screen.\n" +
                                "+-----------------------------------------------+");
            String choice = scan.nextLine();
            if (validScorecard(courseName, choice)) {
                if (choice.toUpperCase().indexOf('D') != -1) {
                    deleteScorecard(courseName, choice);
                    System.out.println("Scorecard successfully deleted.");
                    GolfBuddy.sleep(3);
                    courseScorecardsView(courseName, scan);
                    break;
                } else {
                    // TODO: Implement editing scorecards
                    System.out.println("THIS FEATURE IS UNDER CONSTRUCTION. CHECK BACK LATER.");
                    GolfBuddy.sleep(3);
                }
            } else if (choice.toUpperCase().equals("HOME")) {
                HomeScreen.prompt(scan);
                break;
            } else if (choice.toUpperCase().equals("BACK")) {
                courseOverview(courseName, scan);
                break;
            } else {
                System.out.println("INVALID OPTION!");
                GolfBuddy.sleep(3);
            }
        }
    }
    
}
