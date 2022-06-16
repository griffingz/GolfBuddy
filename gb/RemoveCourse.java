package gb;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class RemoveCourse {

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

    public static void prompt(Scanner scan) {
        while(true) {
            GolfBuddy.clearScreen();
            System.out.println( "+-----------------------------------------------+\n" +
                                "|\n" +
                                "| Remove A Course\n" +
                                "|\n" +
                                "| Pick from the list of courses below:");
            ArrayList<String> courseList = readNamesFile();
            for (int i = 0; i < courseList.size(); i++) {
                System.out.println("| " + (i+1) + ". " + courseList.get(i));
            }
            System.out.println("|\n" + "+-----------------------------------------------+\n");
            String choice = scan.nextLine();
            if (validCourseChoice(choice, courseList.size())) {
                removeCourseFromFile(courseList.get(Integer.parseInt(choice)-1));
                System.out.println("Successfully removed course from file!");
                GolfBuddy.sleep(3);
                HomeScreen.prompt(scan);
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

    private static void removeCourseFromFile(String name) {
        ArrayList<String> courseList = new ArrayList<>();
        try {
            File courseFile = new File("CourseFile.csv");
            Scanner fileScan = new Scanner(courseFile);
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                String lineName = line.substring(0, line.indexOf(','));
                if (!lineName.equals(name)) {
                    courseList.add(line + "\n");
                }
            }
            fileScan.close();
        } catch (Exception e) {
            System.err.println("Failed to remove course from file. Exiting.");
            e.printStackTrace();
            System.exit(3);
        }
        try {
            File courseFile = new File("CourseFile.csv");
            FileWriter fw = new FileWriter(courseFile);
            for (String course : courseList) {
                fw.write(course);
            }
            fw.close();
        } catch (Exception e) {
            System.err.println("Failed to remove course from file. Exiting.");
            e.printStackTrace();
            System.exit(3);
        }
    }
    
}
