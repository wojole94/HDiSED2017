package pl.woleszko.polsl.maths.objects;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DateValue {
	Date dateStamp;
	Double value;
	
	public DateValue(Date date, Double value){
		this.dateStamp = date;
		this.value = value;
	}
}
