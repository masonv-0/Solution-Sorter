
import java.io.*;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
    public static void main(String[] args) throws IOException {
        JSONParser parser = new JSONParser();

        try {
            FileReader reader = new FileReader("solutions.json");
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray allSolutions = (JSONArray) jsonObject.get("articles");

            File solutionsCSV = new File("allSolutions.csv");
            FileWriter writer = new FileWriter(solutionsCSV);
            PrintWriter writeToCSV = new PrintWriter(writer);
            String[] columnNames = {"Solution Name", "Solution ID", "Views", "Date Created", "Date Modified", "3 years", "4 years", "5+ years"};
            writeToCSV.println(convertToCSV(columnNames));

            int numSolutions = allSolutions.size();
            JSONObject currentSolution;
            String[] inputData;
            String solutionName, solutionID, views, dateCreated, dateModified;
            boolean five;
            boolean four;
            boolean three;

            for (int i = 0; i < numSolutions; i++) {
                inputData = new String[7];
                currentSolution = (JSONObject) allSolutions.get(i);
                solutionName = (String) currentSolution.get("title");
                solutionID = String.valueOf((long) currentSolution.get("id"));
                views = String.valueOf((long) currentSolution.get("views"));
                dateCreated = (String) currentSolution.get("created_at");
                dateModified = (String) currentSolution.get("updated_at");

                five = isBeforeDate(dateCreated, 2020, 3);
                four = isBeforeDate(dateCreated, 2021,3);
                three = isBeforeDate(dateCreated, 2022,3);
                if (five) {
                    inputData = new String[]{solutionName, solutionID, views, convertDate(dateCreated), convertDate(dateModified), "","","X"};
                    writeToCSV.println(convertToCSV(inputData));
                }
                else if (four) {
                    inputData = new String[]{solutionName, solutionID, views, convertDate(dateCreated), convertDate(dateModified), "","X",""};
                    writeToCSV.println(convertToCSV(inputData));
                }
                else if (three) {
                    inputData = new String[]{solutionName, solutionID, views, convertDate(dateCreated), convertDate(dateModified), "X","",""};
                    writeToCSV.println(convertToCSV(inputData));
                }
                else {
                    inputData = new String[]{solutionName, solutionID, views, convertDate(dateCreated), convertDate(dateModified), "","",""};
                    writeToCSV.println(convertToCSV(inputData));
                }
            }
            writeToCSV.flush();
            writeToCSV.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static String convertToCSV(String[] data) {
        return Stream.of(data).collect(Collectors.joining(","));
    }

    public static String convertDate(String inputDate) {
        OffsetDateTime dateTime = OffsetDateTime.parse(inputDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        return dateTime.format(formatter);
    }

    public static boolean isBeforeDate(String inputDate, int year, int month) {
        OffsetDateTime dateTime = OffsetDateTime.parse(inputDate);
        YearMonth inputYearMonth = YearMonth.from(dateTime);
        YearMonth specifiedYearMonth = YearMonth.of(year, month);

        return inputYearMonth.isBefore(specifiedYearMonth);
    }
}
