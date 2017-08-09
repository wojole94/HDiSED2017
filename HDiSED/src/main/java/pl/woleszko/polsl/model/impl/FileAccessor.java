package pl.woleszko.polsl.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;

import pl.woleszko.polsl.model.entities.Entity;

public interface FileAccessor {

	public List<Entity> getValues();
	public void configure();
	
}
