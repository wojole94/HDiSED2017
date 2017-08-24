package pl.woleszko.polsl.model.entities;

import java.util.Date;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
@CsvRecord(separator = ";")
public class NozzleMeasuresEntity implements Entity{
	
	
	
	public NozzleMeasuresEntity() {
		super();
		this.date = null;
		this.locId = null;
		this.nozId = null;
		this.tankId = null;
		this.literCounter = null;
		this.totalCounter = null;
		this.status = null;
	}

	@DataField(pos = 1, pattern="yyyy-MM-dd HH:mm:ss")
	private Date date;
	
	@DataField(pos = 2)
	private Long locId;
	
	@DataField(pos = 3)
	private Long nozId;

	@DataField(pos = 4)
	private Long tankId;
	
	@DataField(pos = 5)
	private String literCounter;
	
	@DataField(pos = 6)
	private String totalCounter;
	
	@DataField(pos = 7)
	private Integer status;

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

	public Long getNozId() {
		return nozId;
	}

	public void setNozId(Long nozId) {
		this.nozId = nozId;
	}

	public Long getTankId() {
		return tankId;
	}

	public void setTankId(Long tankId) {
		this.tankId = tankId;
	}

	public Double getLiterCounter() {
		
		return Double.parseDouble(literCounter.replace(',', '.'));
	}

	public void setLiterCounter(String literCounter) {
		this.literCounter = literCounter;
	}

	public Double getTotalCounter() {
		
		return Double.parseDouble(totalCounter.replace(',', '.'));
	}

	public void setTotalCounter(String totalCounter) {
		
		this.totalCounter = totalCounter;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
