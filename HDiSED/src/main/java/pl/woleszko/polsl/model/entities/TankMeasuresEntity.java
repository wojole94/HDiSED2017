package pl.woleszko.polsl.model.entities;

import java.util.Date;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@CsvRecord(separator = ";")

@Getter
@Setter
@ToString

public class TankMeasuresEntity extends Entity{

	@DataField(pos = 1, pattern="yyyy-MM-dd HH:mm:ss")
	private Date date;
	
	@DataField(pos = 2)
	private Long locId;
	
	@DataField(pos = 3)
	private Long meterId;

	@DataField(pos = 4)
	private Integer tankId;
	
	@DataField(pos = 5, decimalSeparator=",")
	private Double fuelLvl;
	
	@DataField(pos = 6, decimalSeparator=",")
	private Double fuelVol;
	
	@DataField(pos = 7)
	private Integer fuelTemp;
	
	



	
	
}
