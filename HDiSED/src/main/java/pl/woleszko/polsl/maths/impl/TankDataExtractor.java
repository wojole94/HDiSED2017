package pl.woleszko.polsl.maths.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import pl.woleszko.polsl.maths.objects.Period;
import pl.woleszko.polsl.maths.objects.Times;
import pl.woleszko.polsl.model.entities.Entity;
import pl.woleszko.polsl.model.entities.NozzleMeasuresEntity;
import pl.woleszko.polsl.model.entities.TankMeasuresEntity;
import pl.woleszko.polsl.model.impl.FileAccessorCSV;

public class TankDataExtractor extends DataExtractor {
	ArrayList<TankMeasuresEntity> entities = new ArrayList<TankMeasuresEntity>();

	public TankDataExtractor() {
		getEntities("tankMeasures.csv");
		for (Entity ent : list) {
			entities.add((TankMeasuresEntity) ent);
		}
	}

	public HashMap<Long, Double> getVolumeTotals(Period period) {
		HashMap<Long, Double> totals = new HashMap<Long, Double>();
		HashMap<Long, Times> times = splitDates(period);
		Set<Long> keys = times.keySet();
		
		for (Long key : keys) {
			for (TankMeasuresEntity entity : entities) {
				if (!totals.containsKey(entity.getTankId()))
					totals.put(entity.getTankId(), (double) 0);
				if (entity.getDate().getTime() == times.get(key).getTo().getTime()) {
					Double tempSum = totals.get(entity.getTankId());
					tempSum -= entity.getFuelVol();
					totals.put(entity.getTankId(), tempSum);
				}
				if (entity.getDate().getTime() == times.get(key).getFrom().getTime()) {
					Double tempSum = totals.get(entity.getTankId());
					tempSum += entity.getFuelVol();
					totals.put(entity.getTankId(), tempSum);
				}

			}
		}

		return totals;

	}
}
