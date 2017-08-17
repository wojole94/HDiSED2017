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

	public HashMap<Long,Times> splitDates(Period swPeriod) {

		switch (swPeriod) {
		case FULL_TIME: {
			Date lastDate = list.get(list.size()-1).getDate();
			Date firstDate = list.get(0).getDate();
			Long key = new Long(0);
			for (Entity entity : list) {
				if (entity.getDate().getTime() > lastDate.getTime())
					lastDate = entity.getDate();
				if (entity.getDate().getTime() < firstDate.getTime())
					firstDate = entity.getDate();
			}
			Times dates = new Times(firstDate, lastDate);
			HashMap<Long, Times> result = new HashMap<Long, Times>();
			result.put(key,dates);
			return result;
		}
		case MONTH: {
			// Date lastDate = list.get(list.size() - 1).getDate();
			// Date firstDate = list.get(0).getDate();
		}
		case DAY: {
			HashMap<Long, Times> result = new HashMap<Long, Times>();
			for(Entity entity : list) {
				int day = (int) entity.getDate().getDate() * 31;
				int month = (int) (entity.getDate().getMonth() + 1) * 12;
				 
				Long keyValue = (long) (day+month);
				if(!result.containsKey(keyValue)) {
					Long currDay = keyValue;
					Date lastDate = entity.getDate();
					Date firstDate = entity.getDate();
					for (Entity currEntity : list) {
						int currentDay = (int) currEntity.getDate().getDate() * 31;
						int currMonth = (int) (currEntity.getDate().getMonth() + 1) * 12;
						Long checkVal = (long) (currentDay + currMonth);
						if (checkVal == currentDay) {
							if (currEntity.getDate().getTime() > lastDate.getTime())
								lastDate = entity.getDate();
							if (currEntity.getDate().getTime() < firstDate.getTime())
								firstDate = entity.getDate();
						}
					}
					Times dates = new Times(firstDate, lastDate);
					result.put(currDay, dates);
					
				}
			}
			return result;
		}
		case HOUR: {
			
			HashMap<Long, Times> result = new HashMap<Long, Times>();
			for (Entity entity : list) {
				int day = (int) entity.getDate().getDate() * 31;
				int month = (int) (entity.getDate().getMonth() + 1) * 12;
				int hour = (int) (entity.getDate().getHours() + 1) * 24;
				Long keyValue = (long) (hour+day+month);
				if (!result.containsKey(keyValue)) {
					Long currHour = keyValue;
					Date lastDate = entity.getDate();
					Date firstDate = entity.getDate();
					for (Entity currEntity : list) {
						int currDay = (int) currEntity.getDate().getDate() * 31;
						int currMonth = (int) (currEntity.getDate().getMonth() + 1) * 12;
						int currentHour = (int) (currEntity.getDate().getHours() + 1) * 24;
						Long checkVal = (long) (hour+day+month);
						if (checkVal == currHour) {
							if (currEntity.getDate().getTime() > lastDate.getTime())
								lastDate = entity.getDate();
							if (currEntity.getDate().getTime() < firstDate.getTime())
								firstDate = entity.getDate();
						}
					}
					Times dates = new Times(firstDate, lastDate);
					result.put(currHour, dates);
				}
			}
			return result;
		}

		}
		return null;
	}

	public abstract HashMap<Times, HashMap<Long, Double>> getVolumeTotals(Period period);

}
