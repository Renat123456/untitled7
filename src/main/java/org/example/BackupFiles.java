package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BackupFiles {
    public static void main(String[] args) {
        File directory = new File(".");
        backupFiles(directory);
    }

    public static void backupFiles(File directory) {
        File backupDir = new File(directory.getParent() + "/backup");

        if (!backupDir.exists()) {
            backupDir.mkdir();
        }

        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    FileOutputStream fos = new FileOutputStream(new File(backupDir.getPath() + "/" + file.getName()));
                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }

                    fis.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (file.isDirectory()) {
                backupFiles(file);
            }
        }
    }
}
