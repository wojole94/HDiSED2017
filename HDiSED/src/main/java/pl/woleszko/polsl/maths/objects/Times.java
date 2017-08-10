package pl.woleszko.polsl.maths.objects;

import java.util.Date;

public class Times {
	Date from;
	Date to;
	
	public Times(Date from, Date to){
		this.from = from;
		this.to = to;
	}
	
	public Long getDuration() {	
		return to.getTime() - from.getTime();	
	}

	public Date getFrom() {
		return from;
	}

	public Date getTo() {
		return to;
	}


}
