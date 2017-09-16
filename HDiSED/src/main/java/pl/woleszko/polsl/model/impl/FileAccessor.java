package pl.woleszko.polsl.model.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.woleszko.polsl.model.entities.Entity;

public interface FileAccessor<T extends Entity> {
	Logger log = LoggerFactory.getLogger(CSVHandler.class);
	public List<T> getValues();
	public void configure(Class<T> type);
	
}
