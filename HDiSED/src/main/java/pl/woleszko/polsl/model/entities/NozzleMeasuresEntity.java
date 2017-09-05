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

public class NozzleMeasuresEntity extends Entity{

	@DataField(pos = 1, pattern="yyyy-MM-dd HH:mm:ss")
	private Date date;
	
	@DataField(pos = 2)
	private Integer locId;
	
	@DataField(pos = 3)
	private Integer nozId;

	@DataField(pos = 4)
	private Integer tankId;
	
	@DataField(pos = 5, decimalSeparator=",")
	private Double literCounter;
	
	@DataField(pos = 6, decimalSeparator=",")
	private Double totalCounter;
	
	@DataField(pos = 7)
	private Integer status;

}
