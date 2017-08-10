package pl.woleszko.polsl.maths.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import pl.woleszko.polsl.maths.objects.Period;
import pl.woleszko.polsl.maths.objects.Times;
import pl.woleszko.polsl.model.entities.Entity;
import pl.woleszko.polsl.model.entities.RefuelEntity;
import pl.woleszko.polsl.model.entities.TankMeasuresEntity;
import pl.woleszko.polsl.model.impl.FileAccessorCSV;

public abstract class DataExtractor {
	protected ArrayList<Entity> list;
	
	protected ArrayList<Entity> getEntities(String fileName) {
		// TODO Auto-generated method stub
		FileAccessorCSV access = new FileAccessorCSV(fileName);
		list = (ArrayList<Entity>) access.getValues();
		return list;

	}

	public HashMap<Integer, Times> splitDates(Period swPeriod) {

		switch (swPeriod) {
		case FULL_TIME: {
			Date lastDate = list.get(list.size() - 1).getDate();
			Date firstDate = list.get(0).getDate();
			for (Entity entity : list) {
				if (entity.getDate().getTime() > lastDate.getTime())
					lastDate = entity.getDate();
				if (entity.getDate().getTime() < firstDate.getTime())
					firstDate = entity.getDate();
			}
			Times dates = new Times(firstDate, lastDate);
			HashMap<Integer, Times> result = new HashMap<Integer, Times>();
			result.put(new Integer(0), dates);
			return result;
		}
		case WEEK:{
			
		}
		}
		return null;
	}

	public abstract HashMap<Long, Double> getVolumeTotals(Period period);

}
