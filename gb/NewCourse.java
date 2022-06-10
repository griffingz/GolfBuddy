package gb;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collections;

public class NewCourse {

    private static boolean checkNamesFile(String name) {
        try {
            File courseFile = new File("CourseFile.csv");
            if (courseFile.createNewFile()) {
                return false;
            } else {
                Scanner fileScan = new Scanner(courseFile);
                while (fileScan.hasNextLine()) {
                    String line = fileScan.nextLine();
                    if (line.substring(0, line.indexOf(',')).equals(name)) {
                        fileScan.close();
                        return true;
                    }
                }
                fileScan.close();
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to check course file. Exiting.");
            System.exit(3);
        }
        return true;
    }

    private static boolean validCourseName(String name) {
        if (name.matches("^[a-zA-Z0-9_]{1,50}$")) {
            if (!checkNamesFile(name)) {
                return true;
            } else {
                System.out.println("THIS COURSE ALREADY EXISTS!");
                return false;
            }
        }
        return name.matches("^[a-zA-Z0-9_]{1,50}$");
    }

    private static boolean validNumHoles(String numHoles) {
        if (numHoles.matches("^[0-9]{1,2}$")) {
            if (Integer.parseInt(numHoles) > 0) {
                return true;
            }
        }
        return false;
    }

    private static boolean validPars(String pars, int numHoles) {
        String regex = "^";
        for (int i = 1; i <= numHoles; i++) {
            if (i != numHoles) {
                regex = regex + "[1-9],";
            } else {
                regex = regex + "[1-9]";
            }
        }
        regex = regex + "$";
        return pars.matches(regex);
    }

    public static void prompt(Scanner scan) {
        while (true) {
            GolfBuddy.clearScreen();
            System.out.println( "+-----------------------------------------------+\n" +
                                "|\n" +
                                "| Add New Course\n" +
                                "|\n" +
                                "| Enter the name of the new course:\n" +
                                "|\n" +
                                "+-----------------------------------------------+\n");
            String name = scan.nextLine();
            if (validCourseName(name)) {
                if (name.toUpperCase().equals("HOME")) {
                    HomeScreen.prompt(scan);
                    break;
                } else {
                    promptNumHoles(name, scan);
                    break;
                }
            } else {
                System.out.println("NOT A VALID COURSE NAME!\n" +
                                    "Course names must be unique, alphanumeric and can include underscores.");
                GolfBuddy.sleep(3);
            }
        }
    }

    private static void promptNumHoles(String name, Scanner scan) {
        while (true) {
            GolfBuddy.clearScreen();
            System.out.println( "+-----------------------------------------------+\n" +
                                "|\n" +
                                "| " + name + "\n" +
                                "|\n" +
                                "| Enter the number of holes for the new course:\n" +
                                "|\n" +
                                "+-----------------------------------------------+\n");
            String numHoles = scan.nextLine();
            if (validNumHoles(numHoles)) {
                promptPars(name, Integer.parseInt(numHoles), scan);
                break;
            } else if (numHoles.toUpperCase().equals("HOME")) {
                HomeScreen.prompt(scan);
                break;
            } else {
                System.out.println("NOT A VALID NUMBER OF HOLES!\n" +
                                    "Number of holes must be between 1 and 99.");
                GolfBuddy.sleep(3);
            }
        }
    }

    private static void promptPars(String name, int numHoles, Scanner scan) {
        while (true) {
            GolfBuddy.clearScreen();
            System.out.println( "+-----------------------------------------------+\n" +
                                "|\n" +
                                "| " + name + " - " + numHoles + " Holes\n" +
                                "|\n" +
                                "| Enter a string containing the pars for each\n" + 
                                "| hole, separated by commas. Example:\n" +
                                "| 3,3,3,3,3,4,3,4,3\n" +
                                "|\n" +
                                "+-----------------------------------------------+\n");
            String pars = scan.nextLine();
            if (validPars(pars, numHoles)) {
                addCourseToFile(name, numHoles, pars);
                System.out.println("Course Successfully Added!");
                GolfBuddy.sleep(3);
                HomeScreen.prompt(scan);
                break;
            } else if (pars.toUpperCase().equals("HOME")) {
                HomeScreen.prompt(scan);
            } else {
                System.out.println("NOT A VALID STRING OF PARS!\n");
                GolfBuddy.sleep(3);
            }
        }
    }

    private static void addCourseToFile(String name, int numHoles, String pars) {
        try {
            File courseFile = new File("CourseFile.csv");
            courseFile.createNewFile();
            BufferedWriter courseBufferedWriter = new BufferedWriter(new FileWriter("CourseFile.csv", true));
            String line = name + "," + numHoles + "," + pars + ",\n";
            courseBufferedWriter.append(line);
            courseBufferedWriter.close();
            sortCourseFile();
        } catch (Exception e) {
            System.err.println("Failed to write to course file. Exiting.");
            e.printStackTrace();
            System.exit(3);
        }
    }

    private static void sortCourseFile() {
        try {
            File courseFile = new File("CourseFile.csv");
            Scanner courseScan = new Scanner(courseFile);
            ArrayList<String> lines = new ArrayList<>();
            while (courseScan.hasNextLine()) {
                lines.add(courseScan.nextLine() + "\n");
            }
            courseScan.close();
            Collections.sort(lines);
            FileWriter courseWriter = new FileWriter("CourseFile.csv");
            for (String line : lines) {
                courseWriter.write(line);
            }
            courseWriter.close();
        } catch (Exception e) {
            System.err.println("Failed to sort course file. Exiting.");
            System.exit(5);
        }
    }
    
}
