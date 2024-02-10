/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package posapplication;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author New
 */
public class DatabaseBackup {
    
     // Your Google Drive credentials file path
    private static final String CREDENTIALS_FILE_PATH = "../credentials/client_credentials.json";


    // ID of the folder in Google Drive where you want to upload the backup
    private static final String DRIVE_FOLDER_ID = "17ZX3FxwX1q4aB997a5Vcr-wxdWSASXPx";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        scheduleSchemaDownload(23,00,00);
       // uploadToGoogleDrive();
        // TODO code application logic here
    }
    
    
    
}
