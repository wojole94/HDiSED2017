package pl.woleszko.polsl.model.entities;

import java.util.Date;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
@CsvRecord(separator = ";")
public class TankMeasuresEntity implements Entity{

	@DataField(pos = 1, pattern="yyyy-MM-dd HH:mm:ss")
	private Date date;
	
	@DataField(pos = 2)
	private Long locId;
	
	@DataField(pos = 3)
	private Long meterId;

	@DataField(pos = 4)
	private Long tankId;
	
	@DataField(pos = 5)
	private Long fuelLvl;
	
	@DataField(pos = 6)
	private Long fuelVol;
	
	@DataField(pos = 7)
	private Integer fuelTemp;
	
	@DataField(pos = 8)
	private Long waterLvl;
	
	@DataField(pos = 9)
	private Long waterVol;
	
	// Getters and Setters

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

	public Long getFuelLvl() {
		return fuelLvl;
	}

	public void setFuelLvl(Long fuelLvl) {
		this.fuelLvl = fuelLvl;
	}

	public Long getFuelVol() {
		return fuelVol;
	}

	public void setFuelVol(Long fuelVol) {
		this.fuelVol = fuelVol;
	}

	public Integer getFuelTemp() {
		return fuelTemp;
	}

	public void setFuelTemp(Integer fuelTemp) {
		this.fuelTemp = fuelTemp;
	}

	public Long getWaterLvl() {
		return waterLvl;
	}

	public void setWaterLvl(Long waterLvl) {
		this.waterLvl = waterLvl;
	}

	public Long getWaterVol() {
		return waterVol;
	}

	public void setWaterVol(Long waterVol) {
		this.waterVol = waterVol;
	}
	
	
}
