package process;

import java.io.*;

public class MergeJson {

    public static void main(String[] args) {

        String inputPath = "F:/project/Entity Augmentation data/WebTables_books";
        String outputPath = "F:/project/Entity Augmentation data/books";
        File file = new File(inputPath);
        String[] jsonPath = file.list();
        try {
            FileWriter fileWriter = new FileWriter(outputPath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (int i = 0; i < jsonPath.length; i++) {
                String path = inputPath + "/" + jsonPath[i];
                System.out.println(path);
                File jsonFile = new File(path);
                BufferedReader bufferedReader;

                bufferedReader = new BufferedReader(new FileReader(jsonFile));
                String jsonContent;
                while ((jsonContent = bufferedReader.readLine()) != null) {
                    printWriter.write(jsonContent);
                    printWriter.println();
                }
                bufferedReader.close();
            }
            fileWriter.close();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
