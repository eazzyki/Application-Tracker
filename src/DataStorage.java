import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DataStorage {
    private String pathToDatabase = ".database/database.csv";

    public ArrayList<Item> readData() {
        ArrayList<Item> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToDatabase))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); 
                Item item = new Item(values.length);
                for (int i = 0; i < values.length; i++) {
                    item.setValueAt(i, values[i]);
                }
                items.add(item);
            }
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        } catch (IOException io) {
            io.getStackTrace();
        }
        return items;
    }

    public boolean writeData(ArrayList<Item> data) {
        File csvOutputFile = new File(pathToDatabase);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            data.stream()
            .map(this::convertToCSV)
            .forEach(pw::println);
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        }
        return true;
    }

    private String convertToCSV(Item item) {
        String[] dataAsString = asString(item);
        return String.join(",", dataAsString);
    }

    private String[] asString(Item item) {
        String[] strArr = new String[item.length()];
        String[] strColumns = item.getColumnsStr();
        for (int i = 0; i < strColumns.length; i++) {
            strArr[i] = strColumns[i];
        }
        strArr[item.length()-1] = item.getState().toString();
        return strArr;
    }    
}