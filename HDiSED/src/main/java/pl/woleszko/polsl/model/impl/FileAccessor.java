package pl.woleszko.polsl.model.impl;

import java.util.List;

import pl.woleszko.polsl.model.entities.Entity;

public interface FileAccessor<T extends Entity> {

	public List<T> getValues();
	public void configure(Class<T> type);
	
}
