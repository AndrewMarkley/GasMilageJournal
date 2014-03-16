package andrews.gas.milage.journal;

import java.util.Comparator;

public class carComparator implements Comparator<Car> {
    @Override
	public int compare(Car one, Car two) {
   	 
        String d1 = one.getName();
        String d2 = two.getName();
        return d1.compareTo(d2);
    }
}
