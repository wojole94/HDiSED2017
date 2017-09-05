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
import pl.woleszko.polsl.model.impl.FileAccessor;

public class NozzleDataExtractor extends DataExtractor<NozzleMeasuresEntity> {
	// this is not needed anyway. Parametrized class DataHarverster,
	// returns elements already mapped to the proper Entity derived class
	// ArrayList<NozzleMeasuresEntity> entities = new ArrayList<>();
	
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
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

		// Map<Integer, List<NozzleMeasuresEntity>> splitedByTankID =
		// endMoments.stream().sorted().collect(Collectors.groupingBy(entity ->
		// entity.getTankId()));

		//
		// // HashMap<Long, Times> times = this.splitDates(period);
		//// Set<Long> periodKeys = times.keySet();
		//
		// for (Times key : times) {
		// HashMap<Long, Double> tanksVolume = new HashMap<Long, Double>();
		// for (NozzleMeasuresEntity entity : getEntities()) {
		//
		// if (!tanksVolume.containsKey(entity.getTankId()))
		// tanksVolume.put(entity.getTankId(), (double) 0);
		//
		// // Szuka czasu zgodnego z koncem okresu, jezeli tak to dla konkretnego
		// zbiornika
		// // wylicza delte
		// if (entity.getDate().getTime() == key.getTo().getTime()) {
		// endVol = entity.getLiterCounter();
		// tanksVolume.put(entity.getTankId(), endVol);
		// }
		//
		// }
		// totals.put(key, tanksVolume);
		// }
		return dateValues;
	}
	
	public List<Date> getMultipleUsagePeriods(){
		
		List<Date> usageDates = new ArrayList<>();
		List<NozzleMeasuresEntity> usageMoments = list.stream().filter(e1 -> e1.getStatus().equals(0)).sorted().collect(Collectors.toList());
		Date currDate = new Date();
		for(NozzleMeasuresEntity usageMoment : usageMoments) {
			if (!currDate.equals(usageMoment.getDate())) 
				currDate = usageMoment.getDate();
			else usageDates.add(usageMoment.getDate());
		}
		return usageDates;
	}	

	/**
	 * 1. Long - nozzleId 2. Long index
	 */
	// public HashMap<Long, ArrayList<Times>> getUsagePeriods() {
	// HashMap<Long, ArrayList<Times>> usagePeriods = new HashMap<Long,
	// ArrayList<Times>>();
	//
	// List<NozzleMeasuresEntity> entities = getEntities();
	//
	// for (int i = 0; i < entities.size(); i++) {
	// NozzleMeasuresEntity currEntity = entities.get(i);
	// if (entities.get(i).getStatus().equals(0)) {
	//
	// Date startDate = entities.get(i).getDate();
	// Date endDate = null;
	// Long currentNozzle = entities.get(i).getNozId();
	// Date previous = startDate;
	//
	// int j = i + 1;
	// do {
	// if ((entities.get(j).getNozId().equals(currentNozzle))
	// && (entities.get(j).getStatus().equals(0)))
	// previous = entities.get(j).getDate();
	//
	// if ((entities.get(j).getStatus().equals(1))
	// && (entities.get(j).getNozId().equals(currentNozzle)))
	// endDate = previous;
	//
	// j++;
	// } while (endDate == null && j < entities.size());
	//
	// if (endDate != null) {
	// Times period = new Times(startDate, endDate);
	// if (!usagePeriods.containsKey(currentNozzle)) {
	// ArrayList<Times> timesList = new ArrayList<Times>();
	// usagePeriods.put(currentNozzle, timesList);
	// }
	// ArrayList<Times> timesList = usagePeriods.get(currentNozzle);
	// timesList.add(period);
	// usagePeriods.put(currentNozzle, timesList);
	// } else {
	// System.out.println("Error getting usage periods! Abort... ");
	// return usagePeriods;
	// }
	// }
	//
	// }
	//
	// return usagePeriods;
	// }

	public Map<Integer, List<Times>> getTankUsagePeriods() {


		Map<Integer, List<NozzleMeasuresEntity>> splitedByNozzID = list.stream()
				.collect(Collectors.groupingBy(entity -> entity.getNozId()));
//		
//		
//		
////		Map<Integer, List<NozzleMeasuresEntity>> result = entitiesPerTank.entrySet().stream().collect(Collectors.toMap(e1 -> e1.getKey(),e1 -> {
////			
////			List<NozzleMeasuresEntity> entitiesList = e1.getValue();
////			List<Date> resultList = entitiesList.stream().map(entity -> entity.getDate()).collect(Collectors.toList());
////			
////			return (List<Date>) resultList;
////		}));
//		
//		Map<Integer, List<Date>> result = new HashMap<>(); 
//		for(Integer tankId : entitiesPerTank.keySet()) {
//			List<NozzleMeasuresEntity> entitiesList = entitiesPerTank.get(tankId);
//			entitiesList = entitiesList.stream().filter(distinctByKey(e1 -> e1.getDate())).sorted().collect(Collectors.toList());
//			List<Date> datesList = new ArrayList<>();
//			for(NozzleMeasuresEntity entity : entitiesList) {
//				datesList.add(entity.getDate());
//			}
//			result.put(tankId, datesList);
//		}
//		
//		return result;
//		
//		Map<Integer, List<NozzleMeasuresEntity>> splitedByNozzID = list.stream()
//				.collect(Collectors.groupingBy(entity -> entity.getNozId()));

		// List<NozzleMeasuresEntity> endMoments = new ArrayList<>();

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
				if (entity.getStatus().equals(0)) {
					tankEnd = false;
					startEntity = entity;
					while (!tankEnd && iter.hasNext()) {
						NozzleMeasuresEntity nextEntity = iter.next();
						if (nextEntity.getStatus().equals(1)) {
							
							// Date date = startEntity.getDate();
							Times period = new Times(startEntity.getDate(), nextEntity.getDate());
							values.add(period);
							tankEnd = true;
						}
					}
				}
			}
			if(dateValuesPerTank.containsKey(this.getNozzlesAssign().get(nozzleID))) {
					List<Times> timesList = dateValuesPerTank.get(this.getNozzlesAssign().get(nozzleID));
					timesList.addAll(values);
					dateValuesPerTank.put(this.getNozzlesAssign().get(nozzleID),timesList);
			}
			else {
				dateValuesPerTank.put(this.getNozzlesAssign().get(nozzleID), values);
			}
				
			
			
		}
		
		return dateValuesPerTank;
		
		
				
		
		
		
		
		
		
		// entitiesPerNozzle.entrySet().stream()
		// .forEach(nozzleData -> {
		//
		// nozzleData.getValue().stream().filter(elem ->
		// elem.getStatus().equals(0)).collect();
		//
		//
		//
		//
		// });
		// forEach(this::getTransactionSums(e ->
		// e.rowNum)).collect(Collectors.toList());

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
