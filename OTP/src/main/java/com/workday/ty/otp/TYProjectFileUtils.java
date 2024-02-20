package com.workday.ty.otp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TYProjectFileUtils {

    static String filename = "TYStudent.txt";
    static TYProjectFileUtils instance = null;

    private TYProjectFileUtils(String filename) {
        this.filename = filename;
    }
    public TYProjectFileUtils() {
        if (instance == null) {
            instance = new TYProjectFileUtils(filename);
            init();
        }
    }

    private static boolean createFile() {
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                return true;
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return false;
    }

    private static void deleteFileAndCreateUpdatedFile(List<TYTeamDetails> details) {
        File myObj = new File(filename);
        boolean isDeleted = myObj.delete();
        if (!isDeleted) {
            System.out.println("An error occurred trying to delete the file.");
        }

        try {
            myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                writeFile(details);
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static List<TYTeamDetails> getTeamDetailsFromFile() {
        String data;
        ArrayList<TYTeamDetails> results = new ArrayList<>();
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                //System.out.println(data);
                results.add(createTeamDetailsObject(data));
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return results;
    }

    private static void writeFile(List<TYTeamDetails> studentList) {
        try {
            FileWriter myWriter = new FileWriter(filename);
            for (TYTeamDetails tyTeamDetails : studentList) {
                myWriter.write(tyTeamDetails.toString());
                myWriter.write("\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static String[] splitLine(String text) {

        return text.split(",");
    }

    private static TYTeamDetails createTeamDetailsObject(String data) {

        String[] results = splitLine(data);
        TYTeamDetails student = new TYTeamDetails();
        for (int i = 0; i < results.length; i++) {
            String part = results[i].trim();
            switch(i) {
                case 0: student.setUsername(part);
                case 1: student.setPassword(part);
                case 2: student.setEmail(part);
                case 3: student.setCode(part);
            }
        }
        return student;
    }

    /**
     * Set up file with Admin user once.
     */
    private static void init() {

        boolean isNewFile = createFile();
        if (isNewFile) {
            ArrayList<TYTeamDetails> studentList = new ArrayList<>();
            TYTeamDetails admin = new TYTeamDetails("Administrator", "password", "tyexperienceprogramme@gmail.com", "000000");
            studentList.add(admin);
            writeFile(studentList);
        }
    }

    public static TYTeamDetails checkUsernamePassword(String username, String password) {
       // init();
        List<TYTeamDetails> details = getTeamDetailsFromFile();
        for (TYTeamDetails tempTeam : details) {
            if ((tempTeam.getUsername().equalsIgnoreCase(username)) && (tempTeam.getPassword().equals(password))) {
                return tempTeam;
            }
        }
        return null;
    }

    /**
     * Adds six-digit code to the matching username and email.  If there is more than one
     * matching username and email it will just add code to the first (ignores case) match.
     */
    public static boolean addSixDigitCodeToFile(String username, String email, String code) {
        List<TYTeamDetails> details =  getTeamDetailsFromFile();
        for (int i = 0; i < details.size(); i++) {
            if ((details.get(i).getUsername().equalsIgnoreCase(username)) && (details.get(i).getEmail().equalsIgnoreCase(email))) {
                // persist code to file
                details.get(i).setCode(code);
                deleteFileAndCreateUpdatedFile(details);
                return true;
            }
        }
        return false;  // no match found
    }

    public static boolean validateCode(String username, String email, String code) {
        List<TYTeamDetails> details = getTeamDetailsFromFile();
        for (TYTeamDetails detail : details) {
            if ((detail.getUsername().equalsIgnoreCase(username)) && (detail.getEmail().equalsIgnoreCase(email))) {
                if (detail.getCode().equals(code)) {
                    detail.setCode(code);
                    return true;
                }
            }
        }
        return false;
    }

}