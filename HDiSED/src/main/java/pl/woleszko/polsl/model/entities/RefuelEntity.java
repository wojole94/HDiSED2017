package pl.woleszko.polsl.model.entities;

import java.util.Date;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
@CsvRecord(separator = ";")
public class RefuelEntity implements Entity {
	
	@DataField(pos = 1, pattern="yyyy-MM-dd HH:mm:ss")
	private Date date;
	
	@DataField(pos = 2)
	private Long tankId;
	
	@DataField(pos = 3)
	private Long fuelVol;
	
	// Getters and Setters
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getTankId() {
		return tankId;
	}

	public void setTankId(Long tankId) {
		this.tankId = tankId;
	}

	public Long getFuelVol() {
		return fuelVol;
	}

	public void setFuelVol(Long fuelVol) {
		this.fuelVol = fuelVol;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	@DataField(pos = 4)
	private Integer speed;
	
	
}
