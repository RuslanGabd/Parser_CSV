package parser.unitel.uz;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws IOException {
        System.out.println("Файл должен называться Unitel.csv");
        System.out.println("Введите цифру подраздела");
        Scanner scanner = new Scanner(System.in);

        int x = scanner.nextInt();

        String filePath = "Unitel.csv";
        List<Unitel> Unitels = ParseUnitelCsv(filePath);
        HashSet<String> stringHashSet = new HashSet<>();


        for (Unitel s : Unitels) {
            String[] fullPathArray = s.fullPath.toString().split("\\\\");
            if (fullPathArray.length >= x + 1) {
                stringHashSet.add(fullPathArray[x]);
            }
        }
        System.out.println("Получены файлы:");
        System.out.println(stringHashSet);
        int n = 0;
        for (String m : stringHashSet
        ) {
            FileWriter csvWriter = new FileWriter(m + ".csv");
            csvWriter.append("InternalName;UID_Person;System;Login;AccountDisabled;Access_type;Description;Job_Title;FullPathInternalName");
            csvWriter.append("\n");

            for (Unitel rowData : Unitels) {

                String[] fullPathArray2 = rowData.fullPath.toString().split("\\\\");
                if (fullPathArray2.length >= x + 1 && m.equals(fullPathArray2[x])) {
                    csvWriter.append(String.join(";", rowData.internalName, rowData.uidPerson, rowData.system, rowData.login, rowData.accountDisabled, rowData.accessType, rowData.jobTitle, rowData.fullPath));
                    csvWriter.append("\n");
                }
            }
            csvWriter.flush();
            csvWriter.close();
        }
        System.out.println("Выполнено");
    }

    private static List<Unitel> ParseUnitelCsv(String filePath) throws IOException {

        List<Unitel> Unitels = new ArrayList<Unitel>();
        List<String> fileLines = Files.readAllLines(Paths.get(filePath), Charset.forName("CP1251"));
        int n = 0;
        for (String fileLine : fileLines) {
            n++;
            String[] splitedText = fileLine.split(";");
            ArrayList<String> columnList = new ArrayList<String>();
            for (int i = 0; i < splitedText.length; i++) {

                if (IsColumnPart(splitedText[i])) {
                    String lastText = columnList.get(columnList.size() - 1);
                    columnList.set(columnList.size() - 1, lastText + ";" + splitedText[i]);
                } else {
                    columnList.add(splitedText[i]);
                }
            }
            System.out.println(n);
            System.out.println(columnList.size());
            Unitel Unitel = new Unitel();
            if (columnList.size() > -1) {
                Unitel.internalName = columnList.get(0);
                if (columnList.size() > 1) {
                    Unitel.uidPerson = columnList.get(1);
                    Unitel.system = columnList.get(2);
                    Unitel.login = columnList.get(3);
                    Unitel.accountDisabled = columnList.get(4);
                    if (columnList.size() > 7) {
                        Unitel.accessType = columnList.get(5);

                        Unitel.description = columnList.get(6);

                        Unitel.jobTitle = columnList.get(7);

                        Unitel.fullPath = columnList.get(8);
                    }
                }
            }
            Unitels.add(Unitel);
        }
        return Unitels;
    }


    private static boolean IsColumnPart(String text) {
        String trimText = text.trim();
        return trimText.indexOf("\"") == trimText.lastIndexOf("\"") && trimText.endsWith("\"");
    }
}
