import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The purpose of this class is to represent a Customer of a banking company.
 * The class stores an ID, data for every unique month and year, and balance
 * for every day that a credit transaction is made. The class has methods to
 * record changes in monthly data, record changes in daily balance, and return
 * an array containing Strings of monthly data.
 * 
 * @author victorsu
 *
 */

public class Customer {
   private String ID;
   private HashMap<String, MonthlyData> monthAndYears;
   private HashMap<String, Integer> dailyBalances;

   public Customer(String id) {
      ID = id;
      monthAndYears = new HashMap<String, MonthlyData>();
      dailyBalances = new HashMap<String, Integer>();
   }

   /**
    * Records the daily balance for the current object Customer
    * 
    * @param date   the day the credit was transacted.
    * @param amount the amount of credit.
    */
   public void recordDailyBalance(String date, int amount) {
      // if daily balance exists in map, make change to day's balance
      if (dailyBalances.containsKey(date))
         dailyBalances.put(date, dailyBalances.get(date) + amount);

      // if daily balance doesn't exist, create new key-value and record
      // the balance
      else
         dailyBalances.put(date, amount);
   }

   /**
    * Record a change in a customer's monthly data.
    * 
    * @param date   the date a transaction was made.
    * @param amount the amount of credit/debit.
    * @return true.
    */
   public void recordChange(String date, int amount) {
      String month = date.substring(0, 2);
      String year = date.substring(6);
      String monthAndYear = month + "/" + year;

      // if monthAndYear already exists in map, record the change in balance
      // in the key's value
      if (monthAndYears.containsKey(monthAndYear))
         monthAndYears.get(monthAndYear).balance(amount);

      // if monthAndYear doesn't exist in map, create new key-value and record
      // the new change in balance
      else
         monthAndYears.put(monthAndYear, new MonthlyData(amount));
   }

   /**
    * Get the balance on a specified day if it exists.
    * 
    * @param date the date to get the daily balance of
    * @return the daily balance of a date if it exists, else return 0.
    */
   public int getDailyBalance(String date) {
      if (dailyBalances.containsKey(date))
         return dailyBalances.get(date);
      return 0;
   }

   /**
    * Return an array where each element is a string of containing the current
    * object customer's ID, MM/YYYY, min balance, max balance, and ending balance
    */
   public String[] displayContent() {
      String[] toReturn = new String[monthAndYears.size()];

      // iterate through sorted monthAndYears and put line into each array index
      int i = 0;
      for (Map.Entry<String, MonthlyData> entry : monthAndYears.entrySet()) {
         // swap the date so the String can be sorted in ascending order
         String date = entry.getKey();

         String swappedDate = swapSubstrings(date);
         System.out.println("Swapped date is " + swappedDate);

         String line = ID + "," + swappedDate + "," 
         + entry.getValue().toString();
         toReturn[i] = line;
         i++;
      }

      // sort lines
      quickSort(toReturn, 0, toReturn.length - 1);
      
      // unswap month and year in every line
      for (int j = 0; j < toReturn.length; j++) {
         String line = toReturn[j];

         int index = line.indexOf(",");
        
         String swappedDate = line.substring(index + 1, index + 8);
         String unswappedDate = swapSubstrings
               (line.substring(index + 1, index + 8));
         
         toReturn[j] = line.replace(swappedDate, unswappedDate);
      }

      return toReturn;
   }

   /**
    * Swap substrings between "/".
    * @param str the String that will have its substrings swapped.
    * @return the String after its substrings are swapped
    */
   public String swapSubstrings(String str) {
      int index = str.indexOf("/");
      if (index == -1) {
         return str;
      }
      return str.substring(index + 1) + "/" + str.substring(0, index);

   }

   /**
    * Quick sort an array in ascending order.
    * @param arr the array to sort.
    * @param low the lowest index of the array to sort.
    * @param high the highest index of the array to sort.
    */
   public void quickSort(String[] arr, int low, int high) {
      if (low < high) {
         int partitionIndex = partition(arr, low, high);

         quickSort(arr, low, partitionIndex - 1);
         quickSort(arr, partitionIndex + 1, high);
      }
   }

   /**
    * Quick sort helper method to return a partition index of an array.
    * @param arr the array to sort.
    * @param low the lowest index of the array to sort. 
    * @param high the highest index of the array to sort.
    * @return the index to pivot the array.
    */
   private int partition(String[] arr, int low, int high) {
      String pivot = arr[high];
      int i = low - 1;

      for (int j = low; j < high; j++) {
         if (arr[j].compareTo(pivot) < 0) {
            i++;
            swap(arr, i, j);
         }
      }
      swap(arr, i + 1, high);

      return i + 1;
   }

   /**
    * Quick sort helper method to swap elements in an array.
    * @param arr the array to swap elements in.
    * @param i the first index of the array.
    * @param j the second index of the array.
    */
   private void swap(String[] arr, int i, int j) {
      String temp = arr[i];
      arr[i] = arr[j];
      arr[j] = temp;
   }

   /**
    * Prints out the daily balances of a customer. This method could be 
    * reworked to only return the balance on a given day.
    */
   public void displayDailyBalances() {
      for (Entry<String, Integer> entry : dailyBalances.entrySet()) {
         String key = entry.getKey();
         Object value = entry.getValue();
         System.out.println(ID + " | " + key + " | " + value);
      }
   }
}
