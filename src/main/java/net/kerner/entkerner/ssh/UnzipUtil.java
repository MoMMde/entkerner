package net.kerner.entkerner.ssh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtil {

    public static void unzip(String zipFilePath, String destDir, String... filter) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            assert ze != null;
            new File(destDir + File.separator + ze.getName().split("/")[0]).mkdirs();
            while(ze != null){

                String fileName = ze.getName();

                if (Arrays.stream(Arrays.stream(filter).toArray()).toList().contains(fileName.split("/")[1])) {
                    File newFile = new File(destDir + File.separator + fileName);
                    newFile.createNewFile();

                    System.out.println("Unzipping to " + newFile.getAbsolutePath());

                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    zis.close();
                    fis.close();
                    return;
                }

                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}