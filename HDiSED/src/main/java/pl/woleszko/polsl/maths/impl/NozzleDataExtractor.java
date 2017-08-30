package pl.woleszko.polsl.maths.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pl.woleszko.polsl.maths.objects.Times;
import pl.woleszko.polsl.model.entities.NozzleMeasuresEntity;
import pl.woleszko.polsl.model.impl.FileAccessor;

public class NozzleDataExtractor extends DataExtractor<NozzleMeasuresEntity> {
    //  this is not needed anyway. Parametrized class DataHarverster, 
    //  returns elements already mapped to the proper Entity derived class
    //  ArrayList<NozzleMeasuresEntity> entities = new ArrayList<>();

    public NozzleDataExtractor(FileAccessor<NozzleMeasuresEntity> accessor) {
        super(accessor);

    //    NOT NEEDED ANYMORE
    //      for (Entity ent : getEntities()) {
    //          entities.add((NozzleMeasuresEntity) ent);
    //      }     
    }

	public HashMap<Times, HashMap<Long, Double>> getVolumeTotals(ArrayList<Times> times) {

		Double curr = new Double(0);
		Double endVol = new Double(0);
		Double startVol = new Double(0);

		HashMap<Times, HashMap<Long, Double>> totals = new HashMap<Times, HashMap<Long, Double>>();

		// HashMap<Long, Times> times = this.splitDates(period);
		//Set<Long> periodKeys = times.keySet();

		for (Times key : times) {
			HashMap<Long, Double> tanksVolume = new HashMap<Long, Double>();
			for (NozzleMeasuresEntity entity : getEntities()) {

				if (!tanksVolume.containsKey(entity.getTankId()))
					tanksVolume.put(entity.getTankId(), (double) 0);

				// Szuka czasu zgodnego z koncem okresu, jezeli tak to dla konkretnego zbiornika
				// wylicza delte
				if (entity.getDate().getTime() == key.getTo().getTime()) {
					curr = tanksVolume.get(entity.getTankId());
					endVol = entity.getTotalCounter();
					curr = endVol + curr;
					tanksVolume.put(entity.getTankId(), curr);
				}

				if (entity.getDate().getTime() == key.getFrom().getTime()) {
					curr = tanksVolume.get(entity.getTankId());
					startVol = entity.getTotalCounter();
					curr = curr - startVol;
					tanksVolume.put(entity.getTankId(), curr);
				}
			}
			totals.put(key, tanksVolume);
		}
		return totals;
	}

	public HashMap<Times, HashMap<Long, Double>> getTransactionTotals(ArrayList<Times> times) {

		Double curr = new Double(0);
		Double endVol = new Double(0);
		Double startVol = new Double(0);

		HashMap<Times, HashMap<Long, Double>> totals = new HashMap<Times, HashMap<Long, Double>>();

		// HashMap<Long, Times> times = this.splitDates(period);
//		Set<Long> periodKeys = times.keySet();

		for (Times key : times) {
			HashMap<Long, Double> tanksVolume = new HashMap<Long, Double>();
			for (NozzleMeasuresEntity entity : getEntities()) {

				if (!tanksVolume.containsKey(entity.getTankId()))
					tanksVolume.put(entity.getTankId(), (double) 0);

				// Szuka czasu zgodnego z koncem okresu, jezeli tak to dla konkretnego zbiornika
				// wylicza delte
				if (entity.getDate().getTime() == key.getTo().getTime()) {
					endVol = entity.getLiterCounter();
					tanksVolume.put(entity.getTankId(), endVol);
				}

			}
			totals.put(key, tanksVolume);
		}
		return totals;
	}

	/**
	 * 1. Long - nozzleId 2. Long index
	 */
	public HashMap<Long, ArrayList<Times>> getUsagePeriods() {
		HashMap<Long, ArrayList<Times>> usagePeriods = new HashMap<Long, ArrayList<Times>>();
		
		List<NozzleMeasuresEntity> entities = getEntities(); 

		for (int i = 0; i < entities.size(); i++) {
			NozzleMeasuresEntity currEntity = entities.get(i);
			if (entities.get(i).getStatus().equals(0)) {

				Date startDate = entities.get(i).getDate();
				Date endDate = null;
				Long currentNozzle = entities.get(i).getNozId();
				Date previous = startDate;
				
				int j = i + 1;
				do {
					if ((entities.get(j).getNozId().equals(currentNozzle))
						&& (entities.get(j).getStatus().equals(0)))
						previous = entities.get(j).getDate();
					
					if ((entities.get(j).getStatus().equals(1)) 
						&& (entities.get(j).getNozId().equals(currentNozzle))) 
						endDate = previous;
										
					j++;
				} while (endDate == null && j < entities.size());

				if (endDate != null) {
					Times period = new Times(startDate, endDate);
					if (!usagePeriods.containsKey(currentNozzle)) {
						ArrayList<Times> timesList = new ArrayList<Times>();
						usagePeriods.put(currentNozzle, timesList);
					}
					ArrayList<Times> timesList = usagePeriods.get(currentNozzle);
					timesList.add(period);
					usagePeriods.put(currentNozzle, timesList);
				} else {
					System.out.println("Error getting usage periods! Abort... ");
					return usagePeriods;
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

		for (NozzleMeasuresEntity entity : getEntities()) {
			if (!assigns.containsKey(entity.getNozId())) {
				assigns.put(entity.getNozId(), entity.getTankId());
			}
		}
		return assigns;
	}
}
