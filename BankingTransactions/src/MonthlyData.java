/** 
 * The purpose of this class is to store the minimum, maximum, and ending
 * balance of a month and year for a customer. The class contains a method to
 * make a transaction.
 * 
 * @author victorsu
 *
 */

public class MonthlyData {
   private int endingBal;
   private int minBal;
   private int maxBal;
   
   public MonthlyData(int amount) {
      endingBal = amount;
      minBal = amount;
      maxBal = amount;
   }
   
   /**
    * Record a deposit or withdrawal for current object's month and year.
    * @param amount the amount deposited/withdrew.
    * @return true.
    */
   public boolean balance(int amount) {
      System.out.println("The amount transacted is " + amount);
      
      endingBal += amount; // make change to endingBal
      
      if (endingBal < minBal) // check for new minBal
         minBal = endingBal;
      else if (endingBal > maxBal) // check for new maxBal
         maxBal = endingBal;
      
      return true;
   }
   
   public String toString() {
      System.out.println("min is " + minBal + ". max is " + maxBal + 
            ". ending is " + endingBal);
      return minBal + "," + maxBal + "," + endingBal;
   }
}
