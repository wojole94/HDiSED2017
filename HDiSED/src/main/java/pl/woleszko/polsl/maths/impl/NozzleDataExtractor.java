package pl.woleszko.polsl.maths.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import pl.woleszko.polsl.maths.objects.Period;
import pl.woleszko.polsl.maths.objects.Times;
import pl.woleszko.polsl.model.entities.Entity;
import pl.woleszko.polsl.model.entities.NozzleMeasuresEntity;
import pl.woleszko.polsl.model.impl.FileAccessor;
import pl.woleszko.polsl.model.impl.FileAccessorCSV;

public class NozzleDataExtractor extends DataExtractor {
	ArrayList<NozzleMeasuresEntity> entities = new ArrayList<NozzleMeasuresEntity>();

	public NozzleDataExtractor() {
		list = getEntities("nozzleMeasures.csv");
		for (Entity ent : list) {
			entities.add((NozzleMeasuresEntity) ent);
		}
	}

	public HashMap<Long, Double> getVolumeTotals(Period period) {

		Double tempSum = (double) 0;


		HashMap<Long, Double> totals = new HashMap<Long, Double>();

		HashMap<Long, Times> times = splitDates(period);
		Set<Long> keys = times.keySet();

		for (Long key : keys) {
			for (NozzleMeasuresEntity entity : entities) {
				if (!totals.containsKey(entity.getTankId()))
					totals.put(entity.getTankId(), (double) 0);
				if (entity.getDate().getTime() == times.get(key).getTo().getTime()) {
					tempSum = totals.get(entity.getTankId());
					tempSum += entity.getTotalCounter();
					totals.put(entity.getTankId(), tempSum);
				}
			}
		}
		return totals;
	}
}
