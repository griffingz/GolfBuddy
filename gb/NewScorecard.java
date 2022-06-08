package gb;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Collections;

public class NewScorecard {

    private static ArrayList<String> readNamesFile() {
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

    private static boolean validCourseChoice(String choice, int numCourses) {
        if (choice.matches("^[0-9]{1,3}$")) {
            if (Integer.parseInt(choice) > 0 && Integer.parseInt(choice) <= numCourses) {
                return true;
            }
        }
        return false;
    }

    private static boolean validScores(String scores, String courseName) {
        int numHoles = -1;
        try {
            File courseFile = new File("CourseFile.csv");
            Scanner fileScan = new Scanner(courseFile);
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                String lineName = line.substring(0, line.indexOf(','));
                if (courseName.equals(lineName)) {
                    String numbers = line.substring(line.indexOf(',')+1, line.length());
                    numHoles = Integer.parseInt(numbers.substring(0, numbers.indexOf(',')));
                    break;
                }
            }
            fileScan.close();
        } catch (Exception e) {
            System.err.println("Failed to read number of holes of course from file. Exiting.");
            e.printStackTrace();
            System.exit(3);
        }

        String regex = "^";
        for (int i = 1; i <= numHoles; i++) {
            if (i != numHoles) {
                regex = regex + "[1-9]{1,2},";
            } else {
                regex = regex + "[1-9]{1,2}";
            }
        }
        regex = regex + "$";
        return scores.matches(regex);
    }

    private static boolean validDate(String date) {
        return date.matches("^[0-1][0-9]/[0-3][0-9]/[0-9]{4}$");
    }
    
    public static void prompt(Scanner scan) {
        while (true) {
            GolfBuddy.clearScreen();
            System.out.println( "+-----------------------------------------------+\n" +
                                "|\n" +
                                "| Add New Scorecard\n" +
                                "|\n" +
                                "| Pick from the list of courses below:");
            ArrayList<String> courseList = readNamesFile();
            for (int i = 0; i < courseList.size(); i++) {
                System.out.println("| " + (i+1) + ". " + courseList.get(i));
            }
            System.out.println("|\n" + "+-----------------------------------------------+\n");
            String choice = scan.nextLine();
            if (validCourseChoice(choice, courseList.size())) {
                promptDate(courseList.get(Integer.parseInt(choice)-1), scan);
                break;
            } else if (choice.toUpperCase().equals("HOME")) {
                HomeScreen.prompt(scan);
                break;
            } else {
                System.out.println("INVALID COURSE! Please pick a number.");
                GolfBuddy.sleep(5);
            }
        }
    }

    private static void promptDate(String courseName, Scanner scan) {
        while (true) {
            GolfBuddy.clearScreen();
            System.out.println( "+-----------------------------------------------+\n" +
                                "|\n" +
                                "| New Scorecard For " + courseName + "\n" +
                                "|\n" +
                                "| Enter The Date Of The Scorecard. If unknown,\n" +
                                "| type \"UNKNOWN\". Example:\n" +
                                "| 05/27/2022\n" +
                                "+-----------------------------------------------+\n");
            String date = scan.nextLine();
            if (validDate(date)) {
                promptScores(courseName, date, scan);
                break;
            } else if (date.toUpperCase().equals("HOME")) {
                HomeScreen.prompt(scan);
                break;
            } else if (date.toUpperCase().equals("UNKNOWN")) {
                promptScores(courseName, date.toUpperCase(), scan);
                break;
            } else {
                System.out.println("INVALID DATE!");
                GolfBuddy.sleep(5);
            }
        }
    }

    private static void promptScores(String courseName, String date, Scanner scan) {
        while (true) {
            GolfBuddy.clearScreen();
            System.out.println( "+-----------------------------------------------+\n" +
                                "|\n" +
                                "| New Scorecard For " + courseName + " on " + date + "\n" +
                                "|\n" +
                                "| Enter your scores for each hole separated by\n" +
                                "| commas. Example:\n" +
                                "| 3,3,3,3,3,4,3,4,3\n" +
                                "|\n" +
                                "+-----------------------------------------------+\n");
            String scores = scan.nextLine();
            if (validScores(scores, courseName)) {
                addScorecardToFile(courseName, date, scores);
                System.out.println("Successfully Added New Scorecard.");
                GolfBuddy.sleep(3);
                HomeScreen.prompt(scan);
                break;
            } else if (scores.toUpperCase().equals("HOME")) {
                HomeScreen.prompt(scan);
                break;
            } else {
                System.out.println("INVALID SCORES!");
                GolfBuddy.sleep(5);
            }
        }
    }

    private static void addScorecardToFile(String courseName, String date, String scores) {
        try {
            File scorecardFile = new File("ScorecardFile.csv");
            scorecardFile.createNewFile();
            BufferedWriter scorecardBufferedWriter = new BufferedWriter(new FileWriter("ScorecardFile.csv", true));
            String line = courseName + "," + date + "," + scores + ",\n";
            scorecardBufferedWriter.append(line);
            scorecardBufferedWriter.close();
            sortScorecardFile();
        } catch (Exception e) {
            System.err.println("Failed to write to scorecard file. Exiting.");
            e.printStackTrace();
            System.exit(4);
        }
    }

    private static void sortScorecardFile() {
        try {
            File scorecardFile = new File("ScorecardFile.csv");
            Scanner scoreScan = new Scanner(scorecardFile);
            ArrayList<String> lines = new ArrayList<>();
            while (scoreScan.hasNextLine()) {
                lines.add(scoreScan.nextLine() + "\n");
            }
            scoreScan.close();
            Collections.sort(lines);
            FileWriter scoreWriter = new FileWriter("ScorecardFile.csv");
            for (String line : lines) {
                scoreWriter.write(line);
            }
            scoreWriter.close();
        } catch (Exception e) {
            System.err.println("Failed to sort scorecard file. Exiting.");
            System.exit(5);
        }
    }

}
