package pl.woleszko.polsl.maths.impl;

import java.util.ArrayList;
import java.util.HashMap;

import pl.woleszko.polsl.maths.objects.Period;
import pl.woleszko.polsl.maths.objects.Times;
import pl.woleszko.polsl.model.entities.Entity;
import pl.woleszko.polsl.model.entities.RefuelEntity;
import pl.woleszko.polsl.model.entities.TankMeasuresEntity;
import pl.woleszko.polsl.model.impl.FileAccessorCSV;

public abstract class DataExtractor {
//	ArrayList<Entity> entities = new ArrayList<Entity>();

	protected ArrayList<Entity> getEntities(String fileName) {
		// TODO Auto-generated method stub
		FileAccessorCSV access = new FileAccessorCSV(fileName);
		ArrayList<Entity> list = (ArrayList<Entity>) access.getValues();
		return list;

	}

	public abstract HashMap<Long, Double> getWeeklyTotals();

//	public HashMap<Long, Times> splitDates(Period swPeriod) {
//
//		switch (swPeriod) {
//		case FULL_TIME: {
//			for(Entity entity : entities) {
//				if (entity.getDate().getTime() > lastDate.getTime()) lastDate = entity.getDate();
//				if (entity.getDate().getTime() < firstDate.getTime()) firstDate = entity.getDate();
//				if (!totals.containsKey(entity.getTankId())) totals.put(entity.getTankId(), (double) 0);
//			}
//		}
//		}
//
//		return null;
//	}

}
