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

	// Long is a nozzleID
	public HashMap<Times, HashMap<Long, Double>> getVolumeTotals(ArrayList<Times> times) {

		Double curr = new Double(0);
		Double endVol = new Double(0);
		Double startVol = new Double(0);

		HashMap<Times, HashMap<Long, Double>> totals = new HashMap<Times, HashMap<Long, Double>>();

		// HashMap<Long, Times> times = this.splitDates(period);
		// Set<Long> periodKeys = times.keySet();

		for (Times key : times) {
			HashMap<Long, Double> tanksVolume = new HashMap<Long, Double>();
			for (NozzleMeasuresEntity entity : entities) {

				if (!tanksVolume.containsKey(entity.getNozId()))
					tanksVolume.put(entity.getNozId(), (double) 0);

				// Szuka czasu zgodnego z koncem okresu, jezeli tak to dla konkretnego zbiornika
				// wylicza delte
				if (entity.getDate().getTime() == key.getTo().getTime()) {
					curr = tanksVolume.get(entity.getNozId());
					endVol = entity.getTotalCounter();
					curr = endVol + curr;
					tanksVolume.put(entity.getNozId(), curr);
				}

				if (entity.getDate().getTime() == key.getFrom().getTime()) {
					curr = tanksVolume.get(entity.getNozId());
					startVol = entity.getTotalCounter();
					curr = curr - startVol;
					tanksVolume.put(entity.getNozId(), curr);
				}
			}
			totals.put(key, tanksVolume);
		}
		return totals;
	}

	public HashMap<Times, Double> getTransactionTotals(ArrayList<Times> times) {

		Double curr = new Double(0);
		Double endVol = new Double(0);
		Double startVol = new Double(0);

		HashMap<Times, Double> totals = new HashMap<Times, Double>();

		// HashMap<Long, Times> times = this.splitDates(period);
		// Set<Long> periodKeys = times.keySet();

		for (Times key : times) {
			HashMap<Long, Double> tanksVolume = new HashMap<Long, Double>();
			for (NozzleMeasuresEntity entity : entities) {

				if (!tanksVolume.containsKey(entity.getTankId()))
					tanksVolume.put(entity.getTankId(), (double) 0);

				// Szuka czasu zgodnego z koncem okresu, jezeli tak to dla konkretnego zbiornika
				// wylicza delte
				if (entity.getDate().getTime() == key.getTo().getTime()) {
					endVol = entity.getLiterCounter();

					// tanksVolume.put(entity.getTankId(), endVol); - wariant z podzialem na
					// zbiorniki
				}

			}
			totals.put(key, endVol);
		}
		return totals;
	}

	/**
	 * 1. Long - nozzleId
	 */

	public HashMap<Times, Long> getUsagePeriods() {

		HashMap<Times, Long> usagePeriods = new HashMap<Times, Long>();
		Boolean stopFlag = false;
		for (int i = 0; i < entities.size(); i++) {
			NozzleMeasuresEntity currEntity = entities.get(i);
			stopFlag = false;
			if (entities.get(i).getStatus().equals(0)) {

				for (Times period : usagePeriods.keySet()) {
					if (period.dateInPeriod(entities.get(i).getDate())
							&& usagePeriods.get(period).equals(entities.get(i).getNozId())) {
						stopFlag = true;
					}
				}

				if (!stopFlag) {
					Date startDate = entities.get(i).getDate();
					Date endDate = null;
					Long currentNozzle = entities.get(i).getNozId();
					// Date previous = startDate;

					int j = i + 1;
					do {
						// if ((entities.get(j).getNozId().equals(currentNozzle))
						// && (entities.get(j).getStatus().equals(0)))
						// previous = entities.get(j).getDate();

						if ((entities.get(j).getStatus().equals(1))
								&& (entities.get(j).getNozId().equals(currentNozzle)))
							endDate = /* previous */ entities.get(j).getDate();

						j++;
					} while (endDate == null && j < entities.size());

					if (endDate != null) {

						Times period = new Times(startDate, endDate);
						if (!usagePeriods.containsKey(period)) {
							usagePeriods.put(period, currentNozzle);
						} else {
							startDate.setTime(startDate.getTime() + 1);
							period = new Times(startDate, endDate);
							usagePeriods.put(period, currentNozzle);
						}
					} else {
						System.out.println("Error getting usage periods! Abort... ");
						return usagePeriods;
					}
				}
			}

		}

		return usagePeriods;
	}

	/**
	 * Returns the list which specify which nozzle correspond to which tank HashMap
	 * elems: 1. Long - nozzle 2. Long - tank
	 */
	public HashMap<Long, Long> getNozzlesAssign() {

		HashMap<Long, Long> assigns = new HashMap<Long, Long>();

		for (NozzleMeasuresEntity entity : entities) {
			if (!assigns.containsKey(entity.getNozId())) {
				assigns.put(entity.getNozId(), entity.getTankId());
			}
		}
		return assigns;
	}
}
