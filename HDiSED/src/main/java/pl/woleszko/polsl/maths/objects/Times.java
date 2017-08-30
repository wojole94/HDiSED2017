package pl.woleszko.polsl.maths.objects;

import java.util.Date;

public class Times {
	Date from;
	Date to;

	public Times(Date from, Date to) {
		this.from = from;
		this.to = to;
	}

	public Boolean dateInPeriod(Date date) {
		if (date.getTime() <= this.to.getTime() && date.getTime() >= this.from.getTime())
			return true;
		return false;
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

//	@Override 
//	public String toString() {
//		String text = new String("Period: " 
//				+ from.getYear() + "-"
//				+ (from.getMonth() + 1) + "-"
//				+ from.getDate() + " "
//				+ from.getHours() + ":"
//				+ from.getMinutes() + ":"
//				+ from.getSeconds() + " -- >"
//				+ to.getYear() + "-"
//				+ (to.getMonth() + 1) + "-"
//				+ to.getDate() + " "
//				+ to.getHours() + ":"
//				+ to.getMinutes() + ":"
//				+ to.getSeconds() + " ");				
//		return text; 
//	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Times other = (Times) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
}
