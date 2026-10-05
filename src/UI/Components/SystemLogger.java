package UI.Components;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemLogger {
    
    public static void log(String userId, String role, String action) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("audit.txt", true))) {
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            bw.write(time + "," + userId + "," + role + "," + action);
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Failed to write to audit log.");
        }
    }
}