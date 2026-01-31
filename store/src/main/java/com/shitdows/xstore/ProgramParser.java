package com.shitdows.xstore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class ProgramParser {

    public static listedProgram[] txtParser() throws Exception {
        boolean foundStatus = false;
        List<listedProgram> programList = new ArrayList<>();

        File downloadLinks = new File("downloadLinks.txt");
        if (downloadLinks.exists()) {
            System.out.println("downloadLinks.txt found");
            foundStatus = true;
        }
        if (foundStatus == true) {
            List<String> lines = FileUtils.readLines(downloadLinks, "UTF-8");
            for (int i = 0; i < lines.size(); i += 2) {
                if (!lines.get(i).isEmpty()) {
                    String link = lines.get(i);
                    String title = lines.get(i + 1);

                    byte architecture = 0; // 1 = x64 por defecto, listado en listedProgram
                    if (title.contains("x86")) {
                        architecture = 0;
                    }
                    else if (title.contains("arm")) {
                        architecture = 2;
                    }

                    String extension = "";
                    int extensionStart = title.lastIndexOf('.');
                    extension = title.substring(extensionStart);

                    programList.add(new listedProgram(title, link, extension, architecture));
                    System.out.println("Created object: "+ new listedProgram(title, link, extension, architecture));
                }
            }
        }

        else {
            System.out.println("Error reading download link");
        }

        listedProgram[] resultArray = new listedProgram[programList.size()];
        return programList.toArray(resultArray);
    }

    public static void main(String[] args) throws Exception {
        txtParser();
    }

}