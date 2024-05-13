package org.example;

import javax.imageio.IIOException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int CHAR_LOW = 65;
    private static final int CHAR_HIGH = 91;
    private static final String TO_SEARCH = "GeekBrains";
    private static final Random random = new Random();

    public static void main(String[] args) throws IOException {
        writeFileContents("sample01.txt", 60, 5);
        writeFileContents("sample02.txt", 90, 5);
        concatenate("sample01.txt", "sample02.txt", "sample01_out.txt");

        long i = 0;
        while ((i = searchInFile("sample01_out.txt", i, TO_SEARCH)) > 0){
            System.out.println("Файл содержит искомое слово, смещение " + i);
        }
        System.out.println("Конец поиска");

        String[] filenames = new String[10];
        for (int j = 0; j < filenames.length; j++) {
            filenames[j] = "file_" + j + ".txt";
            writeFileContents(filenames[j], 30, 3);
            System.out.println("создан файл " + filenames[j]);
        }

        List<String> result = searchMatch(new File("."), TO_SEARCH);
        for(String s : result){
            System.out.printf("Файл %s содержит искомое слово %s\n", s, TO_SEARCH);
        }

    }

    private static String generateSymbols(int amount){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            stringBuilder.append((char) (random.nextInt(CHAR_HIGH - CHAR_LOW) + CHAR_LOW));
        }
        return stringBuilder.toString();
    }

    private static void writeFileContents(String fileName, int length) throws IOException {
        try(FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            fileOutputStream.write(generateSymbols(length).getBytes(StandardCharsets.UTF_8));
        }
    }

    private static void writeFileContents(String fileName, int length, int words) throws IOException {
        try(FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            for (int i = 0; i < words; i++) {
                if(random.nextInt(4) == 0){
                    fileOutputStream.write(TO_SEARCH.getBytes(StandardCharsets.UTF_8));
                }else {
                    fileOutputStream.write(generateSymbols(length).getBytes(StandardCharsets.UTF_8));
                }
            }
        }
    }

    private static void concatenate(String file1, String file2, String fileOut) throws IOException {
        try(FileOutputStream fileOutputStream = new FileOutputStream(fileOut)){
            int c;
            try (FileInputStream fileInputStream = new FileInputStream(file1)){
                while ((c = fileInputStream.read()) != -1){
                    fileOutputStream.write(c);
                }
            }
            try (FileInputStream fileInputStream = new FileInputStream(file2)){
                while ((c = fileInputStream.read()) != -1){
                    fileOutputStream.write(c);
                }
            }
        }
    }

    private static long searchInFile(String fileName, String search) throws IOException {
        return searchInFile(fileName, 0, search);
    }

    private static long searchInFile(String fileName, long offset, String search) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(fileName)){
            fileInputStream.skipNBytes(offset);
            byte[] searchData = search.getBytes(StandardCharsets.UTF_8);
            int c;
            int i = 0;
            while ((c = fileInputStream.read()) != -1){
                if(c == searchData[i]){
                    i++;
                }else {
                    i = 0;
                    if(c == searchData[i]){
                        i++;
                    }
                }
                if(i == searchData.length){
                    return offset;
                }
                offset++;
            }
            return -1;
        }
    }

    static List<String> searchMatch(File file, String search) throws IOException {
        List<String> list = new ArrayList<>();
        File[] files = file.listFiles();
        if(files == null){
            return list;
        }
        for (int i = 0; i < files.length; i++) {
            if(files[i].isFile()){
                if(searchInFile(files[i].getCanonicalPath(), search) > -1){
                    list.add(files[i].getCanonicalPath());
                }
            }
        }
        return list;
    }
}