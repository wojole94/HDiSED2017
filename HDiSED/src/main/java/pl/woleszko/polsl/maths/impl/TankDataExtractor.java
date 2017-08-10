package pl.woleszko.polsl.maths.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import pl.woleszko.polsl.model.entities.Entity;
import pl.woleszko.polsl.model.entities.NozzleMeasuresEntity;
import pl.woleszko.polsl.model.entities.TankMeasuresEntity;
import pl.woleszko.polsl.model.impl.FileAccessorCSV;

public class TankDataExtractor extends DataExtractor {
	ArrayList<TankMeasuresEntity> entities = new ArrayList<TankMeasuresEntity>();
	
	public TankDataExtractor(){
		ArrayList<Entity> list = getEntities("tankMeasures.csv");
		for(Entity ent : list) {
			entities.add((TankMeasuresEntity) ent);
		}	
	}
	
	public HashMap<Long,Double> getWeeklyTotals(){
		HashMap<Long,Double> totals = new HashMap<Long,Double>();
		Date lastDate = entities.get(entities.size()-1).getDate();
		Date firstDate = entities.get(0).getDate();
		
		for(TankMeasuresEntity entity : entities) {
			if (entity.getDate().getTime() > lastDate.getTime()) lastDate = entity.getDate();
			if (entity.getDate().getTime() < firstDate.getTime()) firstDate = entity.getDate();
			if (!totals.containsKey(entity.getTankId())) totals.put(entity.getTankId(), (double) 0);
		}
		
		for(TankMeasuresEntity entity : entities) {
			if (entity.getDate().getTime() == lastDate.getTime()) {
				Double tempSum = totals.get(entity.getTankId());
				tempSum -= entity.getFuelVol();
				totals.put(entity.getTankId(), tempSum);	
			}
			if (entity.getDate().getTime() == firstDate.getTime()) {
				Double tempSum = totals.get(entity.getTankId());
				tempSum += entity.getFuelVol();
				totals.put(entity.getTankId(), tempSum);
			}
		
		}
		
		return totals;
		
	}
}
