package UTILS;

import java.io.*;
import java.util.*;

public class FileUtil {

    public static List<String> readFile(String fileName) {
        List<String> lines = new ArrayList<>();

        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lines;
    }

    public static void writeFile(String fileName, List<String> lines) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void appendFile(String fileName, String line) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
            
            bw.write(line);
            bw.newLine();
            bw.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}