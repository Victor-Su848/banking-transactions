import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** 
 * The purpose of this class is to process a CSV file containing banking
 * transactions occurring in customer accounts. The class generates minimum, 
 * maximum, and ending balances by month for all customers. The class also
 * stores balances for each customer for every day that they make a credit
 * transaction. 
 * 
 * @author victorsu
 */

public class TestReader {
   private HashMap<String, Customer> customers = new HashMap<String, Customer>();

   /**
    * Record a line from the CSV file as either a new customer if the customers
    * HashMap doesn't contain the customer, or add the transaction to the 
    * existing customer.
    * 
    * @param id     the id of customer.
    * @param date   the date the customer made change.
    * @param amount the amount deposited/withdrew.
    * @return true if the line is successfully input, false otherwise.
    */
   public void recordLine(String id, String date, String amount) {
      if (customers.containsKey(id)) // customer exists
         customers.get(id).recordChange(date, Integer.parseInt(amount));

      else { // customer doesn't exist
         customers.put(id, new Customer(id));
         customers.get(id).recordChange(date, Integer.parseInt(amount));
      }
   }

   /**
    * Record a line from the CSV file as a daily credit balance.
    * 
    * @param id     the id of customer.
    * @param date   the date the customer made change.
    * @param amount the amount of credit.
    */
   public void recordDailyBalanceLine(String id, String date, String amount) {
      if (customers.containsKey(id)) { // customer exists
         customers.get(id).recordDailyBalance(date, Integer.parseInt(amount));
      }

      else { // customer doesn't exist
         customers.put(id, new Customer(id));
         customers.get(id).recordChange(date, Integer.parseInt(amount));
      }
   }

   /**
    * @return an ArrayList storing arrays of Strings. Each Array contains elements
    *         storing a customer's ID, MM/YYYY, min balance, max balance, and
    *         ending balance
    */
   public ArrayList<String> displayAverages() {
      ArrayList<String> toReturn = new ArrayList<String>();

      // iterate through customers
      for (Map.Entry<String, Customer> entry : customers.entrySet()) {
         Customer value = entry.getValue();
         String[] lines = value.displayContent();

         // iterate through lines because a customer has a line for each
         // unique month
         for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            toReturn.add(line);
         }
      }

      return toReturn;
   }

   /**
    * Print daily balances of all customers
    */
   public void displayDailyBalances() {
      for (Map.Entry<String, Customer> entry : customers.entrySet()) {
         Customer value = entry.getValue();
         value.displayDailyBalances();
      }
   }

   /**
    * Validates whether a String is valid as a date.
    * 
    * @param date the String to validate.
    * @return true if the String is valid, false otherwise.
    */
   public static boolean isValidDate(String date) {
      String datePattern = "\\d{2}/\\d{2}/\\d{4}";
      return date.matches(datePattern);
   }

   public static void main(String[] args) {
      TestReader reader = new TestReader();
      String fileName = "data.csv";

      ArrayList<String[]> creditLines = new ArrayList<String[]>();
      ArrayList<String[]> allLines = new ArrayList<String[]>();

      // Input section
      BufferedReader bufferedReader;
      try {
         bufferedReader = new BufferedReader(new FileReader(fileName));
         String line;

         // iterate through lines
         while ((line = bufferedReader.readLine()) != null) {
            // System.out.println(line);
            String[] words = line.split(",");

            // check that words array is completely valid
            if (words.length == 3 && !words[0].isEmpty() && isValidDate(words[1]) && !words[2].isEmpty()) {

               if (Integer.parseInt(words[2]) >= 0) // array is a credit transaction
                  creditLines.add(words);

               allLines.add(words);
            }
         }

         // announce that file is done
         System.out.println("The file is done being processed!");
         bufferedReader.close();
      } catch (IOException e) { // catch error if any
         System.err.println(e.getMessage());
      }

      // process all lines
      for (int i = 0; i < allLines.size(); i++) {
         String[] line = allLines.get(i);
         reader.recordLine(line[0], line[1], line[2]);
      }

      // process only credit lines
      for (int i = 0; i < creditLines.size(); i++) {
         String[] line = creditLines.get(i);
         reader.recordDailyBalanceLine(line[0], line[1], line[2]);
      }

      // display the daily balances
      System.out.println("Displaying daily balances");
      reader.displayDailyBalances();

      // Output section
      ArrayList<String> newLines = reader.displayAverages();

      BufferedWriter bufferedWriter;
      try {
         bufferedWriter = new BufferedWriter(new FileWriter("output.csv", false));

         // iterate through output lines and write them to output.csv
         for (int i = 0; i < newLines.size(); i++) {
            bufferedWriter.write(newLines.get(i));
            bufferedWriter.newLine();
         }

         bufferedWriter.close();
         System.out.println("Results can be found in the file output.csv");
      } catch (IOException e) { // catch error if any
         System.err.println(e.getMessage());
      }
   }
}
