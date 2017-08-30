package pl.woleszko.polsl.model.entities;

import java.util.Date;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;

@CsvRecord(separator = ";")
public abstract class Entity implements Comparable<Entity> {
    
	public abstract Date getDate();

    @Override
    public int compareTo(Entity o) {
        return (o instanceof Entity)
            ? this.getDate().compareTo(((Entity)o).getDate()) : 0;
    }
}
