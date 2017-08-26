package pl.woleszko.polsl.model.entities;

import java.util.Date;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
@CsvRecord(separator = ";")
public class TankMeasuresEntity extends Entity{

	@DataField(pos = 1, pattern="yyyy-MM-dd HH:mm:ss")
	private Date date;
	
	@DataField(pos = 2)
	private Long locId;
	
	@DataField(pos = 3)
	private Long meterId;

	@DataField(pos = 4)
	private Long tankId;
	
	@DataField(pos = 5)
	private String fuelLvl;
	
	@DataField(pos = 6)
	private String fuelVol;
	
	@DataField(pos = 7)
	private Integer fuelTemp;
	
	
	// Getters and Setters
	@Override
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getLocId() {
		return locId;
	}

	public void setLocId(Long locId) {
		this.locId = locId;
	}

	public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	public Long getTankId() {
		return tankId;
	}

	public void setTankId(Long tankId) {
		this.tankId = tankId;
	}

	public Double getFuelLvl() {
		return Double.parseDouble(fuelLvl.replace(',', '.'));
	}

	public void setFuelLvl(String fuelLvl) {
		this.fuelLvl = fuelLvl;
	}

	public Double getFuelVol() {
		return Double.parseDouble(fuelVol.replace(',', '.'));
	}

	public void setFuelVol(String fuelVol) {
		this.fuelVol = fuelVol;
	}

	public Integer getFuelTemp() {
		return fuelTemp;
	}

	public void setFuelTemp(Integer fuelTemp) {
		this.fuelTemp = fuelTemp;
	}


	
	
}
