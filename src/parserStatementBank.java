
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class parserStatementBank {
    private static String staffFile = "data\\movementList.csv";
    private static final String SKIP_COMMAS_QUOTATION_MARKS = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    private static final String COMMA_AND_NUMBER = "[^,\\d.]";

    public static void main(String[] args){

        try
        {
            List<String> linePars = Files.lines(Path.of(staffFile), StandardCharsets.UTF_8).collect(Collectors.toList());

            linePars.stream().map(line -> line.split(SKIP_COMMAS_QUOTATION_MARKS, -1)).filter(split -> split.length != 8).forEach(split -> {System.out.println("Ошибка при парсинги строки: ");for(int i = 0; i < split.length; i++){System.out.print(split[i] + "\t");}System.out.println("\n");});

            double consumption = linePars.stream().map(line -> line.split(SKIP_COMMAS_QUOTATION_MARKS, -1)).filter(split ->
                    (!split[7].replaceAll(COMMA_AND_NUMBER, "").equals(""))).mapToDouble(split -> Double.parseDouble(split[7].replaceAll("[^,\\d.]", "").replaceAll(",", "."))).sum();

            double coming = linePars.stream().map(line -> line.split(SKIP_COMMAS_QUOTATION_MARKS, -1)).filter(split ->
                    (!split[6].replaceAll(COMMA_AND_NUMBER, "").equals(""))).mapToDouble(split -> Double.parseDouble(split[6].replaceAll("[^,\\d.]", "").replaceAll(",", "."))).sum();


            HashMap<String, Double> expense = new HashMap<>();

            linePars.stream().map(line -> line.split(SKIP_COMMAS_QUOTATION_MARKS, -1)).filter(split ->
                    (!split[7].replaceAll(COMMA_AND_NUMBER, "").equals(""))).filter(split -> Double.parseDouble(split[7].replaceAll("[^,\\d.]", "").replaceAll(",", ".")) > 0).collect(Collectors.toList()).forEach(split ->
            {

                String[] operation = split[5].replaceAll("\\s+", " " ).split("\\d{2}\\.\\d{2}\\.\\d{2}");
                String numberOperation;

                try {
                    numberOperation = operation[0].substring(operation[0].lastIndexOf("\\") + 1).substring(operation[0].lastIndexOf('/') + 1);

                }
                catch (Exception ex)
                {
                    numberOperation = operation[1];
                }

                if(expense.containsKey(numberOperation))
                {
                    expense.put(numberOperation, expense.get(numberOperation) + Double.parseDouble(split[7].replaceAll("[^,\\d.]", "").replaceAll(",", ".")));
                }else
                {
                    expense.put(numberOperation, Double.parseDouble(split[7].replaceAll("[^,\\d.]", "").replaceAll(",", ".")));
                }

            });


            for(String key : expense.keySet())
            {

                System.out.print("\nРасход на: " + key + " = ");
                System.out.printf("%.2f", expense.get(key));

            }
            System.out.print("\n\n============================\n");
            System.out.println("Расход: " + consumption);
            System.out.println("Приход: " + coming);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
