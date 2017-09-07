package pl.woleszko.polsl.maths.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pl.woleszko.polsl.maths.objects.Times;
import pl.woleszko.polsl.model.entities.Entity;
import pl.woleszko.polsl.model.entities.NozzleMeasuresEntity;
import pl.woleszko.polsl.model.entities.TankMeasuresEntity;
import pl.woleszko.polsl.model.impl.FileAccessor;

public class NozzleDataExtractor extends DataExtractor<NozzleMeasuresEntity> {
	

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public NozzleDataExtractor(FileAccessor<NozzleMeasuresEntity> accessor) {
		super(accessor);
		
	}

	@Override
	public Map<Integer, Double> getVolumeTotals() {

		Map<Integer, List<NozzleMeasuresEntity>> splitedByTankID = list.stream()
				.collect(Collectors.groupingBy(NozzleMeasuresEntity::getNozId));

		Map<Integer, Double> nozzTotals = splitedByTankID.entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> {
					NozzleMeasuresEntity entity = entry.getValue().stream().max(Entity::compareTo).get();
					return entity.getTotalCounter();
				}));

		Double sum = 0D;
		Map<Integer, Double> tankSums = new HashMap<>();

		for (Integer nozz : nozzTotals.keySet()) {
			if (!tankSums.containsKey(getNozzlesAssign().get(nozz))) {
				tankSums.put(getNozzlesAssign().get(nozz), 0D);
			}
			sum = tankSums.get(getNozzlesAssign().get(nozz));
			sum += nozzTotals.get(nozz);
			tankSums.put(getNozzlesAssign().get(nozz), sum);
		}

		return tankSums;
	}

	// public Map<Integer, HashMap<Date, Double>> getTotalChanges(){
	//
	// Map<Integer, List<NozzleMeasuresEntity>> splitedByTankID = list.stream()
	// .collect(Collectors.groupingBy(entity -> entity.getNozId()));
	//
	// Map<Integer, HashMap<Date, Double>> dateValues = new HashMap<>();
	// for (Integer tankID : splitedByTankID.keySet()) {
	//
	// List<NozzleMeasuresEntity> tankList =
	// splitedByTankID.get(tankID).stream().sorted().collect(Collectors.toList());
	// ListIterator<NozzleMeasuresEntity> iter = tankList.listIterator();
	// HashMap<Date, Double> values = new HashMap<>();
	//
	// NozzleMeasuresEntity entity = iter.next();
	// while (iter.hasNext()) {
	// NozzleMeasuresEntity nextEntity = iter.next();
	// if (!nextEntity.getTotalCounter().equals(entity.getTotalCounter())) {
	// Double value = entity.getTotalCounter() - nextEntity.getTotalCounter();
	// Date date = nextEntity.getDate();
	// values.put(date, value);
	// }
	// entity = nextEntity;
	//
	// }
	// dateValues.put(tankID, values);
	// }
	// return dateValues;
	// }

	public Map<Integer, HashMap<Times, Double>> getTransactionTotals() {
		//
		// Double curr = new Double(0);
		// Double endVol = new Double(0);
		// Double startVol = new Double(0);
		//
		// HashMap<Integer, List<NozzleMeasuresEntity>> endMomentsPerNozzle = new
		// HashMap<>();

		Map<Integer, List<NozzleMeasuresEntity>> splitedByNozzID = list.stream()
				.collect(Collectors.groupingBy(entity -> entity.getNozId()));

		// List<NozzleMeasuresEntity> endMoments = new ArrayList<>();

		Map<Integer, HashMap<Times, Double>> dateValues = new HashMap<>();
		for (Integer nozzleID : splitedByNozzID.keySet()) {

			List<NozzleMeasuresEntity> nozzleList = splitedByNozzID.get(nozzleID);
			nozzleList = nozzleList.stream().sorted().collect(Collectors.toList());
			ListIterator<NozzleMeasuresEntity> iter = nozzleList.listIterator();
			HashMap<Times, Double> values = new HashMap<>();
			Boolean tankEnd = true;
			NozzleMeasuresEntity startEntity = new NozzleMeasuresEntity();
			while (iter.hasNext()) {
				NozzleMeasuresEntity entity = iter.next();
				if (entity.getStatus().equals(0)) {
					tankEnd = false;
					startEntity = entity;
					while (!tankEnd && iter.hasNext()) {
						NozzleMeasuresEntity nextEntity = iter.next();
						if (nextEntity.getStatus().equals(1)) {
							Double value = nextEntity.getTotalCounter() - startEntity.getTotalCounter();
							// Date date = startEntity.getDate();
							Times period = new Times(startEntity.getDate(), nextEntity.getDate());
							values.put(period, value);
							tankEnd = true;
						}
					}
				}
			}
			dateValues.put(nozzleID, values);
		}
		return dateValues;
	}

	public List<Date> getMultipleUsagePeriods() {

		List<Date> usageDates = new ArrayList<>();
		List<NozzleMeasuresEntity> usageMoments = list.stream().filter(e1 -> e1.getStatus().equals(0)).sorted()
				.collect(Collectors.toList());
		Date currDate = new Date();
		for (NozzleMeasuresEntity usageMoment : usageMoments) {
			if (!currDate.equals(usageMoment.getDate()))
				currDate = usageMoment.getDate();
			else
				usageDates.add(usageMoment.getDate());
		}
		return usageDates;
	}
	
	public Map<Integer, HashMap<Date, Double>> getVolumeChanges(){
		Map<Integer, HashMap<Date, Double>> volumeChanges = new HashMap<>();
		
		Map<Integer, List<NozzleMeasuresEntity>> splitedByNozzle = list.stream().sorted().collect(Collectors.groupingBy(e1 -> e1.getNozId()));
		
		for(Integer nozzleID : splitedByNozzle.keySet()) {
			List<NozzleMeasuresEntity> nozzleEntities = splitedByNozzle.get(nozzleID);
			ListIterator<NozzleMeasuresEntity> iter = nozzleEntities.listIterator();
			HashMap<Date, Double> values = new HashMap<>();
			
			NozzleMeasuresEntity entity = iter.next();
			while (iter.hasNext()) {
				NozzleMeasuresEntity nextEntity = iter.next();
				if (!nextEntity.getTotalCounter().equals(entity.getTotalCounter())) {
					Double value = nextEntity.getTotalCounter() - entity.getTotalCounter();
					Date date = nextEntity.getDate();
					values.put(date, value);
				}
				entity = nextEntity;
			}
			
			if (volumeChanges.containsKey(this.getNozzlesAssign().get(nozzleID))) {
				HashMap<Date, Double> timesList = volumeChanges.get(this.getNozzlesAssign().get(nozzleID));
				timesList.putAll(values);
				volumeChanges.put(this.getNozzlesAssign().get(nozzleID), timesList);
			} else {
				volumeChanges.put(this.getNozzlesAssign().get(nozzleID), values);
			}
		}		
		
		return volumeChanges;
	}
	
	

	public Map<Integer, List<Times>> getTankUsagePeriods() {

		Map<Integer, List<NozzleMeasuresEntity>> splitedByNozzID = list.stream()
				.collect(Collectors.groupingBy(entity -> entity.getNozId()));

		Map<Integer, List<Times>> dateValuesPerTank = new HashMap<>();
		for (Integer nozzleID : splitedByNozzID.keySet()) {
			List<NozzleMeasuresEntity> nozzleList = splitedByNozzID.get(nozzleID);
			nozzleList = nozzleList.stream().sorted().collect(Collectors.toList());
			ListIterator<NozzleMeasuresEntity> iter = nozzleList.listIterator();
			List<Times> values = new ArrayList<>();
			Boolean tankEnd = true;
			NozzleMeasuresEntity startEntity = new NozzleMeasuresEntity();
			while (iter.hasNext()) {
				NozzleMeasuresEntity entity = iter.next();
				if (entity.getStatus().equals(0) && tankEnd.equals(true)) {
					tankEnd = false;
					startEntity = entity;
				}

				if (entity.getStatus().equals(1) && tankEnd.equals(false)) {
					// Date date = startEntity.getDate();
					Times period = new Times(startEntity.getDate(), entity.getDate());
					values.add(period);
					tankEnd = true;
				}

			}
			if (dateValuesPerTank.containsKey(this.getNozzlesAssign().get(nozzleID))) {
				List<Times> timesList = dateValuesPerTank.get(this.getNozzlesAssign().get(nozzleID));
				timesList.addAll(values);
				dateValuesPerTank.put(this.getNozzlesAssign().get(nozzleID), timesList);
			} else {
				dateValuesPerTank.put(this.getNozzlesAssign().get(nozzleID), values);
			}

		}

		return dateValuesPerTank;

	}

	/**
	 * Returns the list which specify which nozzle correspond to which tank HashMap
	 * elems: 1. Long - nozzle 2. Long - tank
	 */
	public HashMap<Integer, Integer> getNozzlesAssign() {

		HashMap<Integer, Integer> assigns = new HashMap<>();

		for (NozzleMeasuresEntity entity : getEntities()) {
			if (!assigns.containsKey(entity.getNozId())) {
				assigns.put(entity.getNozId(), entity.getTankId());
			}
		}
		return assigns;
	}

}
