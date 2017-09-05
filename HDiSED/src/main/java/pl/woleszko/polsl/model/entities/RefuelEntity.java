package pl.woleszko.polsl.model.entities;

import java.util.Date;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

@CsvRecord(separator = ";")
public class RefuelEntity extends Entity {
	
	@DataField(pos = 1, pattern="yyyy-MM-dd HH:mm:ss")
	private Date date;
	
	@DataField(pos = 2)
	private Integer tankId;
	
	@DataField(pos = 3, decimalSeparator=",")
	private Double fuelVol;

	@DataField(pos = 4, decimalSeparator=",")
	private Double speed;
	
	
}
