package gb;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;

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
        if (choice.matches("^[0-9]{1,3}$")) {
            if (Integer.parseInt(choice) > 0 && Integer.parseInt(choice) <= numCourses) {
                return true;
            }
        }
        return false;
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
                GolfBuddy.sleep(5);
            }
        }
    }

    private static String[][] getCourseScorecards(String courseName) {
        ArrayList<String[]> scorecards = new ArrayList<>();
        try {
            File scorecardFile = new File("ScorecardFile.csv");
            Scanner fileScan = new Scanner(scorecardFile);
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                String lineName = line.substring(0, line.indexOf(','));
                if (courseName.equals(lineName)) {
                    scorecards.add(line.split(","));
                }
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

    private static Integer[] getCourseStats(String[][] scorecards, Integer[] pars) {
        ArrayList<Integer> statsList = new ArrayList<>();
        statsList.add(scorecards.length);
        int bestScore = 99999;
        for (int i = 0; i < scorecards.length; i++) {
            int scorecardScore = 0;
            for (int j = 2; j < scorecards[i].length; j++) {
                int score = Integer.parseInt(scorecards[i][j]);
                int par = pars[j-2];
                int adjustedHoleScore = score - par;
                scorecardScore = scorecardScore + adjustedHoleScore;
            }
            // TODO: FINISH IMPLEMENTING THIS
        }
    }

    public static void courseOverview(String courseName, Scanner scan) {
        while (true) {
            GolfBuddy.clearScreen();
            System.out.println( "+-----------------------------------------------+\n" +
                                "|\n" +
                                "| " + courseName + " Overview\n" +
                                "|\n");
            String[][] scorecards = getCourseScorecards(courseName);
            Integer[] pars = getCoursePars(courseName);
            Integer[] courseStats = getCourseStats(scorecards, pars);
            // courseStats:
            // [0] = number of times played
            // [1] = best score
            // [2] = average score
            // [3] = worst score
        }
    }
    
}
