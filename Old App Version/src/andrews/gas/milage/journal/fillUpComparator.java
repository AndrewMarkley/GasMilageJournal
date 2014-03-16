package andrews.gas.milage.journal;

import java.util.Calendar;
import java.util.Comparator;

public class fillUpComparator implements Comparator<FillUp> {
    @Override
	public int compare(FillUp one, FillUp two) {
   	 
        Calendar d1 = one.getCalendar();
        Calendar d2 = two.getCalendar();
        if (d1.after(d2)){
            return 1;
        }else if (d1.before(d2)){
            return -1;
        }else{
            return 0;
        }
    }
}
