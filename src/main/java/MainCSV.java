import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainCSV {
    public static void main(String[] args) throws IOException {
        //Порядок расположения полей:
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        //Имя считываемого файла:
        String fileName = "src/main/resources/data.csv";


        //Парсим CSV:
        List<Employee> list = parseCSV(columnMapping, fileName);
        list.forEach(System.out::println);

        String json = listToJson(list);
        String jsonFile = "data.json";
        writeString(json, jsonFile);

    }


    public static List<Employee> parseCSV (String[] mapping, String file) {
        List<Employee> objlist = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(mapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            objlist = csv.parse();

       } catch (IOException e) {
            System.out.println(e.getMessage());
       }
       return objlist;
    }


    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        Type type = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, type);
    }


    public static void writeString(String jsonText, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(jsonText);
            writer.flush();
        } catch (IOException e) {
            e.getMessage();
        }
    }

}


