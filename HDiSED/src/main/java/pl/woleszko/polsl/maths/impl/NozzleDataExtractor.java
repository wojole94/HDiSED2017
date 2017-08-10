package pl.woleszko.polsl.maths.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import pl.woleszko.polsl.model.entities.Entity;
import pl.woleszko.polsl.model.entities.NozzleMeasuresEntity;
import pl.woleszko.polsl.model.impl.FileAccessor;
import pl.woleszko.polsl.model.impl.FileAccessorCSV;

public class NozzleDataExtractor extends DataExtractor {
	ArrayList<NozzleMeasuresEntity> entities = new ArrayList<NozzleMeasuresEntity>();

	
	public NozzleDataExtractor(){
		ArrayList<Entity> list = getEntities("nozzleMeasures.csv");
		for(Entity ent : list) {
			entities.add((NozzleMeasuresEntity) ent);
		}
	}
		
	
	public HashMap<Long,Double> getWeeklyTotals() {
		
		Double tempSum = (double) 0;
		Date date = entities.get(entities.size()-1).getDate();

		HashMap<Long,Double> totals = new HashMap<Long,Double>();
		
		for(NozzleMeasuresEntity entity : entities) {
			if (entity.getDate().getTime() > date.getTime()) date = entity.getDate();
			if (!totals.containsKey(entity.getTankId())) totals.put(entity.getTankId(), (double) 0);
		}
		for(NozzleMeasuresEntity entity : entities) {
			if (entity.getDate().getTime() == date.getTime()) {
				tempSum = totals.get(entity.getTankId());
				tempSum += entity.getTotalCounter();
				totals.put(entity.getTankId(), tempSum);
			}
		}	
		
		return totals;
	}
}
