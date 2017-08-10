package pl.woleszko.polsl.maths.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import pl.woleszko.polsl.maths.objects.Period;
import pl.woleszko.polsl.maths.objects.Times;
import pl.woleszko.polsl.model.entities.Entity;
import pl.woleszko.polsl.model.entities.NozzleMeasuresEntity;
import pl.woleszko.polsl.model.entities.RefuelEntity;
import pl.woleszko.polsl.model.entities.TankMeasuresEntity;

public class RefuelDataExtractor extends DataExtractor {
	ArrayList<RefuelEntity> entities = new ArrayList<RefuelEntity>();

	public RefuelDataExtractor() {
		list = getEntities("refuel.csv");
		for (Entity ent : list) {
			entities.add((RefuelEntity) ent);
		}
	}

	@Override
	public HashMap<Long, Double> getVolumeTotals(Period period) {

		Date date = entities.get(entities.size() - 1).getDate();

		HashMap<Long, Double> totals = new HashMap<Long, Double>();

		HashMap<Integer, Times> times = splitDates(period);
		Set<Integer> keys = times.keySet();

		for (Integer key : keys) {
			for (RefuelEntity entity : entities) {
				if (!totals.containsKey(entity.getTankId()))
					totals.put(entity.getTankId(), (double) 0);
				if (entity.getDate().getTime() <= times.get(key).getTo().getTime() && 
						entity.getDate().getTime() >= times.get(key).getFrom().getTime()) {
					Double tempSum = totals.get(entity.getTankId());
					tempSum += entity.getFuelVol();
					totals.put(entity.getTankId(), tempSum);
				}

			}
		}
		return totals;

	}
}