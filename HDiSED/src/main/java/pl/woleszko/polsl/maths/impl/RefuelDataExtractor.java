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
	
	public HashMap<Times, Long> getRefuelPeriods(){
		HashMap<Times, Long> result = new HashMap<Times, Long>();

		for(RefuelEntity entity : entities) {
			Date startDate = new Date();
			Date endDate = new Date();
			startDate = entity.getDate();
			
			Double equals =  (entity.getFuelVol() / (entity.getSpeed() / 60000));
			Long duration = equals.longValue();
			
			duration++;
			endDate.setTime(startDate.getTime() + duration);
			
			Times period = new Times(startDate, endDate);
			result.put(period, entity.getTankId());
		}
		return result;
	}

	@Override
	public HashMap<Times, HashMap<Long, Double>> getVolumeTotals(ArrayList<Times> times) {

//		Date date = entities.get(entities.size() - 1).getDate();

		HashMap<Times, HashMap<Long, Double>> totals = new HashMap<Times, HashMap<Long, Double>>();

//		HashMap<Long, Times> times = splitDates(period);
		//Set<Long> periodKeys = times.keySet();

		for (Times key : times) {
			HashMap<Long, Double> tanksVolume = new HashMap<Long, Double>();
			for (RefuelEntity entity : entities) {
				
				if (!tanksVolume.containsKey(entity.getTankId()))
					tanksVolume.put(entity.getTankId(), (double) 0);
				
				//Szuka czasu zawartego w okresie, jezeli tak to dla konkretnego zbiornika wylicza sume wplywow 
				if (key.dateInPeriod(entity.getDate())) {
					Double curr = tanksVolume.get(entity.getTankId());
					Double totalVol = entity.getFuelVol();						
					totalVol = totalVol + curr;
					tanksVolume.put(entity.getTankId(), totalVol);
				}
			}
			totals.put(key, tanksVolume);
		}
		return totals;
		
		

	}
}