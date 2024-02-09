/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package posapplication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
        exportDatabase();
       // uploadToGoogleDrive();
        // TODO code application logic here
    }
    
    private static void exportDatabase(String databaseName, String url) {
        Process p = null;
        try{
            Runtime runtime = Runtime.getRuntime();
            p = runtime.exec("mysqldump -uroot -proot --add-drop-databse -B " + databaseName + " -r" + "" + url
                + ""+ "" +databaseName +"" + ".sql");
        }catch(Exception e){}
    }
    
}
