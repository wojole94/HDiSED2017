package pl.woleszko.polsl.model.entities;

import java.util.Date;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
@CsvRecord(separator = ";")
public class RefuelEntity extends Entity {
	
	@DataField(pos = 1, pattern="yyyy-MM-dd HH:mm:ss")
	private Date date;
	
	@DataField(pos = 2)
	private Long tankId;
	
	@DataField(pos = 3)
	private String fuelVol;
	
	// Getters and Setters
	@Override
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

	public Double getFuelVol() {
		
		return Double.parseDouble(fuelVol.replace(',', '.'));
	}

	public void setFuelVol(String fuelVol) {
		this.fuelVol = fuelVol;
	}

	public Double getSpeed() {
		
		return Double.parseDouble(speed.replace(',', '.'));
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	@DataField(pos = 4)
	private String speed;
	
	
}
