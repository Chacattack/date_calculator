import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
    
    public static void main(String[] args) {
        String start = "";
		String end = "";
		int totalDays = 0;
		if(args.length > 0) {
			try (InputStreamReader instream = new FileReader(args[0]); BufferedReader buffer = new BufferedReader(instream)) {
				String fs = "";
				String line;
				while ((line = buffer.readLine()) != null) {
					fs = line;
					start = line.split("-")[0].trim();
					end = line.split("-")[1].trim();
					totalDays = findDays(start, end);
					System.out.println(start + " - " + end + ": " + totalDays);
				} 
			} catch (Exception e){
				System.out.println("System encountered error when reading file");
				System.exit(1);
			}			
		} else {
			System.out.println("Please enter 2 dates in the format \'dd/mm/yyyy - dd/mm/yyyy\'");
			Scanner in = new Scanner(System.in);
			String s = in.nextLine();
			start = s.split("-")[0].trim();
			end = s.split("-")[1].trim();		
			totalDays = findDays(start, end);
			System.out.println(start + " - " + end + ": " + totalDays);
		}
    }
    
    public static int findDays(String start, String end){
        int[] dates = breakUpAndOrderDates(start, end);
        int startYear = dates[0];
        int startMonth = dates[1];
        int startDay = dates[2];
        int endYear = dates[3];
        int endMonth = dates[4];
        int endDay = dates[5];
        
        int daysFromYearsAndMonths = findDaysInYearsAndMonths(startYear, endYear, startMonth, endMonth, startDay, endDay);
        int daysFromDays = findDaysInDays(startDay, endDay);
        
		int totalDays = daysFromYearsAndMonths + daysFromDays;
		
        return totalDays > 0 ? totalDays : 0;
    }
    
    public static int[] breakUpAndOrderDates(String first, String second){
        int startYear, startMonth, startDay, endYear, endMonth, endDay;
        startYear = startMonth = startDay = endYear = endMonth = endDay = 0;
        boolean flip = false;
        try{
            startYear = Integer.parseInt(first.split("/")[2]);
            startMonth = Integer.parseInt(first.split("/")[1]);
            startDay = Integer.parseInt(first.split("/")[0]);
            endYear = Integer.parseInt(second.split("/")[2]);
            endMonth = Integer.parseInt(second.split("/")[1]);
            endDay = Integer.parseInt(second.split("/")[0]);
			
			if (startMonth == 2 || endMonth == 2){
				if (startDay > 29 || endDay > 29){
					throw new IllegalArgumentException("Invalid day for February.");
				}
			}
			
			if (startYear < 1901 || endYear > 2999) {
				throw new IllegalArgumentException("Invalid Year.");
			}
			
			if (startMonth < 0 || endMonth < 0 || startMonth > 12 || endMonth > 12){
				throw new IllegalArgumentException("Invalid Month.");
			}
			
			if (startDay < 0 || endDay < 0 || startDay > 31 || endDay > 31){
				throw new IllegalArgumentException("Invalid Day.");
			}
            
        } catch (Exception e){
            System.out.println("Date was not entered correctly. " + e.getMessage() + " Exiting program.");
            System.exit(1);
        }
                
        if (startYear > endYear){
          flip = true;  
        } else if (startYear == endYear && startMonth > endMonth){
            flip = true;
        } else if (startYear == endYear && startMonth == endMonth && startDay > endDay){
            flip = true;
        }

        if (flip){
            return new int[]{endYear, endMonth, endDay, startYear, startMonth, startDay};
        } else {
            return new int[]{startYear, startMonth, startDay, endYear, endMonth, endDay};
        }
        
    }
    
    public static int findDaysInYearsAndMonths(int startYear, int endYear, int startMonth, 
                                               int endMonth, int startDay, int endDay){
    
        
        //this is done to ensure we don't overshoot by a year
        if (startMonth > endMonth){
            endYear--; 
        } else if (startMonth == endMonth && startDay > endDay){
            endYear--;
        }
        
        int days = 0;
        for (int x = startYear; x < endYear; ++x){
            if (x % 400 == 0 || (x % 4 == 0 && x % 100 != 0)){
                days += 366;
            } else {
                days += 365;
            }
        }
                
        if (startYear > endYear && endYear % 4 == 0 && startMonth > 2) {
            days++; //calculator believes we are after February in a leap year
        }
                
        boolean leapYear = (endYear % 400 == 0 || (endYear % 4 == 0 && endYear % 100 != 0));
        int daysFromMonths = findDaysInMonths(startMonth, endMonth, startDay, endDay, leapYear);
        
        return days + daysFromMonths;
    }
    
    public static int findDaysInMonths(int startMonth, int endMonth, int startDay, int endDay, boolean leapYear){  
        int days = 0;
        if (startMonth == endMonth && startDay > endDay){
            days += daysInMonth(startMonth, leapYear);
            startMonth++;
        }
        
        if (startMonth > endMonth){
            endMonth += 12;
        }
        for (int x = startMonth; x < endMonth; ++x){
            days += daysInMonth(x, leapYear);
        }
        
        return days;
    }
    
    public static int findDaysInDays(int startDay, int endDay){
        return endDay - startDay - 1; //last day not included
    }
    
    public static int daysInMonth(int month, boolean leapYear){
        if (month > 12){
            month = month % 12;
        }
        switch (month){
            case 1: return 31;
            case 2: return (leapYear) ? 29 : 28;
            case 3: return 31;
            case 4: return 30;
            case 5: return 31;
            case 6: return 30;
            case 7: return 31;
            case 8: return 31;
            case 9: return 30;
            case 10: return 31;
            case 11: return 30;
            case 12: return 31;
            default: return -1;
        }
    }
    
}