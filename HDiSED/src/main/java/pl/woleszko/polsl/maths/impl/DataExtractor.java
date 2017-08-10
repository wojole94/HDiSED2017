package pl.woleszko.polsl.maths.impl;

import java.util.ArrayList;
import java.util.HashMap;

import pl.woleszko.polsl.model.entities.Entity;
import pl.woleszko.polsl.model.impl.FileAccessorCSV;

public abstract class DataExtractor {
	
	protected ArrayList<Entity> getEntities(String fileName) {
		// TODO Auto-generated method stub
		FileAccessorCSV access = new FileAccessorCSV(fileName);
		ArrayList<Entity> list = (ArrayList<Entity>) access.getValues();
		return list;

	}
	
	public abstract HashMap<Long,Double> getWeeklyTotals();

	
	
}
