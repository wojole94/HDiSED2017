package pl.woleszko.polsl.maths.objects;

import java.util.Date;

import lombok.ToString;

@ToString
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
		return true;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

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
