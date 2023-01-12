import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Csv {
    static List<Workers> infoWorkers;

    public static void main(String[] args) {
        creatingCSVFile();
        deserialize();
        json(infoWorkers);
    }
    static void json (List<Workers> info){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try (FileWriter writer = new FileWriter("data.json")) {
            writer.write(gson.toJson(info));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void creatingCSVFile() {
        List<Workers> info = new ArrayList<>();
        info.add(new Workers(1, "John", "Smith", "USA", 25));
        info.add(new Workers(2, "Inav", "Petrov", "RU", 23));
        ColumnPositionMappingStrategy<Workers> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Workers.class);
        strategy.setColumnMapping("id", "name", "lastName", "country", "age");
        try (Writer writer = new FileWriter("data.csv")) {
            StatefulBeanToCsv statefulBeanToCsv = new StatefulBeanToCsvBuilder<Workers>(writer)
                    .withMappingStrategy(strategy)
                    .build();
            statefulBeanToCsv.write(info);
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
    }

    static void deserialize() {
        try (CSVReader csvReader = new CSVReader(new FileReader("data.csv"))) {
            ColumnPositionMappingStrategy<Workers> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Workers.class);
            strategy.setColumnMapping("id", "name", "lastName", "country", "age");
            CsvToBean<Workers> csvToBean = new CsvToBeanBuilder<Workers>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            infoWorkers = csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
