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

	public HashMap<Times, HashMap<Long, Double>> getVolumeTotals(Period period) {

		
		Double curr = new Double(0);
		Double endVol = new Double(0);
		Double startVol = new Double(0);
		
		HashMap<Times, HashMap<Long, Double>> totals = new HashMap<Times, HashMap<Long, Double>>();

		HashMap<Long, Times> times = this.splitDates(period);
		Set<Long> periodKeys = times.keySet();

		for (Long key : periodKeys) {
			HashMap<Long, Double> tanksVolume = new HashMap<Long, Double>();
			for (NozzleMeasuresEntity entity : entities) {

				if (!tanksVolume.containsKey(entity.getTankId()))
					tanksVolume.put(entity.getTankId(), (double) 0);
				
				//Szuka czasu zgodnego z koncem okresu, jezeli tak to dla konkretnego zbiornika wylicza delte 
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
}
