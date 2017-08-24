package pl.woleszko.polsl.maths.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

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
		this.sortEntityListByDate(list);
		for (Entity ent : list) {
			entities.add((NozzleMeasuresEntity) ent);
		}
	}

	public HashMap<Times, HashMap<Long, Double>> getVolumeTotals(HashMap<Long, Times> times) {

		Double curr = new Double(0);
		Double endVol = new Double(0);
		Double startVol = new Double(0);

		HashMap<Times, HashMap<Long, Double>> totals = new HashMap<Times, HashMap<Long, Double>>();

//		HashMap<Long, Times> times = this.splitDates(period);
		Set<Long> periodKeys = times.keySet();

		for (Long key : periodKeys) {
			HashMap<Long, Double> tanksVolume = new HashMap<Long, Double>();
			for (NozzleMeasuresEntity entity : entities) {

				if (!tanksVolume.containsKey(entity.getTankId()))
					tanksVolume.put(entity.getTankId(), (double) 0);

				// Szuka czasu zgodnego z koncem okresu, jezeli tak to dla konkretnego zbiornika
				// wylicza delte
				if (entity.getDate().getTime() == times.get(key).getTo().getTime()) {
					curr = tanksVolume.get(entity.getTankId());
					endVol = entity.getTotalCounter();
					curr = endVol + curr;
					tanksVolume.put(entity.getTankId(), curr);
				}

				if (entity.getDate().getTime() == times.get(key).getFrom().getTime()) {
					curr = tanksVolume.get(entity.getTankId());
					startVol = entity.getTotalCounter();
					curr = curr - startVol;
					tanksVolume.put(entity.getTankId(), curr);
				}
			}
			totals.put(times.get(key), tanksVolume);
		}
		return totals;
	}

	/**
	 * Long - nozzleId
	 * 
	 */
	public HashMap<Long, HashMap<Long, Times>> getUsagePeriods() {
		HashMap<Long, HashMap<Long, Times>> usagePeriods = new HashMap<Long, HashMap<Long, Times>>();

		for (int i = 0; i < entities.size(); i++) {
			NozzleMeasuresEntity currEntity = entities.get(i);
			if (entities.get(i).getStatus().equals(0)) {

				Date startDate = entities.get(i).getDate();
				Date endDate = null;
				Long currentNozzle = entities.get(i).getNozId();

				int j = i;
				while(entities.get(j).getStatus().equals(0) && !(entities.get(j).getNozId().equals(currentNozzle))) {
					j++;
				}
				endDate = entities.get(j).getDate();
								
				Times period = new Times(startDate, endDate);
				
				if(!usagePeriods.containsKey(currentNozzle)) {
					HashMap<Long, Times> timesList = new HashMap<Long, Times>();
					usagePeriods.put(currentNozzle, timesList);
				}
				
				HashMap<Long, Times> timesList = usagePeriods.get(currentNozzle);
				timesList.put((long) i, period);
				
			}
		}

		return usagePeriods;
	}

	/**
	 * Returns the list which specify which nozzle correspond to which tank
	 * HashMap elems:
	 * 		1. Long - nozzle
	 * 		2. Long - tank
	 */
	public HashMap<Long, Long> getNozzlesAssign() {
		
		HashMap<Long, Long> assigns = new HashMap<Long, Long>();
		
		
		for(NozzleMeasuresEntity entity : entities) {
			if(!assigns.containsKey(entity.getNozId())) {
				assigns.put(entity.getNozId(), entity.getTankId());
			}
		}
		return assigns;
	}
}
