package pl.woleszko.polsl.maths.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

	// Returns Hashmap<Periods, Hashmap<TankID, VolTot>>
	public HashMap<Times, HashMap<Long, Double>> getVolumeTotals(Period period) {
		HashMap<Times, HashMap<Long, Double>> totals = new HashMap<Times, HashMap<Long, Double>>();
		HashMap<Long, Times> times = this.splitDates(period);
		Set<Long> periodKeys = times.keySet();
		Double startVol;
		Double endVol;
		Double curr = new Double(0);

		for (Long key : periodKeys) {
			HashMap<Long, Double> tanksVolume = new HashMap<Long, Double>();
			for (TankMeasuresEntity entity : entities) {
				// Jezeli nie ma tanka to dodaj
				if (!tanksVolume.containsKey(entity.getTankId()))
					tanksVolume.put(entity.getTankId(), (double) 0);

				// Szuka czasu zgodnego z koncem okresu, jezeli tak to dla konkretnego zbiornika
				// wylicza delte
				if (entity.getDate().getTime() == times.get(key).getTo().getTime()) {
					curr = tanksVolume.get(entity.getTankId());
					endVol = entity.getFuelVol();
					curr = curr - endVol;
					tanksVolume.put(entity.getTankId(), curr);
				}

				if (entity.getDate().getTime() == times.get(key).getFrom().getTime()) {
					curr = tanksVolume.get(entity.getTankId());
					startVol = entity.getFuelVol();
					curr = curr + startVol;
					tanksVolume.put(entity.getTankId(), curr);
				}
			}
			totals.put(times.get(key), tanksVolume);

		}

		return totals;
	}
	//Srednie dzienne,godzinowe itp
	public HashMap<Long, Double> getAverageDeltasOf(Period period) {
		
		HashMap<Long, Double> avg = new HashMap<Long, Double>();
		for (Long tank : this.getTanksIndexes()) {
			avg.put(tank, 0.0);
		}
		
		HashMap<Times, HashMap<Long, Double>> totals = this.getVolumeTotals(period);
		
		for (Times timePeriod : totals.keySet()) {
			HashMap<Long, Double> tanksTotals = totals.get(timePeriod);
			for(Long tank : tanksTotals.keySet()) {
				Double value = avg.get(tank);
				value = tanksTotals.get(tank) + value;
				avg.put(tank, value);
			}	
		}
		
		for(Long tank : avg.keySet()) {
			
			Double value = avg.get(tank);
			value = value/totals.size();
			avg.put(tank, value);
		}
		
		return avg;
	}
	
	public HashMap<Integer, HashMap<Long, Double>> getHoursTrend() {
		
		HashMap<Times, HashMap<Long, Double>> totals = this.getVolumeTotals(Period.HOUR);
		HashMap<Integer, HashMap<Long, Double>> avg = new HashMap<Integer, HashMap<Long, Double>>();
		
				
		for(Times timePeriod : totals.keySet()) {
			
			if(!avg.containsKey(timePeriod.getFrom().getHours())) {
				System.out.println("New hour!");
				avg.put(timePeriod.getFrom().getHours(), 0.0);
				for(Times currTimePeriod : totals.keySet()) {
					Double value = 0.0;
					if (currTimePeriod.getFrom().getHours() == timePeriod.getFrom().getHours()) {
						//1. Odczytanie danych totals z Hashmapy
						//2. Dla kazdego zbiornika wyliczyc sume 
						
					}
				}
			}
		}
		
		//Obliczyc srednia 
		
		return avg;
	}
	
	public Integer getTanksCount() {
		HashMap<Long, Double> tanksVolume = new HashMap<Long, Double>();
		Integer count = new Integer(0);

		for (TankMeasuresEntity entity : entities) {
			if (!tanksVolume.containsKey(entity.getTankId())) {
				tanksVolume.put(entity.getTankId(), (double) 0);
				count++;
			}
		}
		return count;
	}

	public Set<Long> getTanksIndexes() {
		Set<Long> keys = new HashSet<Long>();

		for (TankMeasuresEntity entity : entities) {
			if (!keys.contains(entity.getTankId())) {
				keys.add(entity.getTankId());
			}
		}

		return keys;
	}

}
