package pl.woleszko.polsl.maths.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import pl.woleszko.polsl.maths.objects.DateValue;
import pl.woleszko.polsl.maths.objects.Period;
import pl.woleszko.polsl.model.entities.NozzleMeasuresEntity;
import pl.woleszko.polsl.model.entities.TankMeasuresEntity;
import pl.woleszko.polsl.model.impl.FileAccessor;

public class TankDataExtractor extends DataExtractor<TankMeasuresEntity> {

	public TankDataExtractor(FileAccessor<TankMeasuresEntity> accessor) {
		super(accessor);
	}

	// Returns Hashmap<Periods, Hashmap<TankID, VolTot>>
	public HashMap<Integer, Double> getVolumeTotals() {
		HashMap<Integer, Double> totals = new HashMap<>();
		Map<Integer, List<TankMeasuresEntity>> splitedByTankID = list.stream()
				.collect(Collectors.groupingBy(TankMeasuresEntity::getTankId));

		Map<Integer, Double> firstValues = splitedByTankID.entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> {
					TankMeasuresEntity entity = entry.getValue().stream().sorted().findFirst().get();
					return entity.getFuelVol();
				}));

		Map<Integer, Double> lastValues = splitedByTankID.entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> {
					TankMeasuresEntity entity = entry.getValue().stream().sorted(Comparator.reverseOrder()).findFirst()
							.get();
					return entity.getFuelVol();
				}));

		for (Integer tank : firstValues.keySet()) {
			Double delta = firstValues.get(tank) - lastValues.get(tank);
			totals.put(tank, delta);
		}

		return totals;
	}

	public Map<Integer, HashMap<Date, Double>> getVolumeChanges() {
		Map<Integer, List<TankMeasuresEntity>> splitedByTankID = list.stream()
				.collect(Collectors.groupingBy(entity -> entity.getTankId()));

		Map<Integer, HashMap<Date, Double>> dateValues = new HashMap<>();
		for (Integer tankID : splitedByTankID.keySet()) {
			
			List<TankMeasuresEntity> tankList = splitedByTankID.get(tankID).stream().sorted().collect(Collectors.toList());
			ListIterator<TankMeasuresEntity> iter = tankList.listIterator();
			HashMap<Date, Double> values = new HashMap<>();
			
			TankMeasuresEntity entity = iter.next();
			while (iter.hasNext()) {
				TankMeasuresEntity nextEntity = iter.next();
				if (!nextEntity.getFuelVol().equals(entity.getFuelVol())) {
					Double value = entity.getFuelVol() - nextEntity.getFuelVol();
					Date date = nextEntity.getDate();
					values.put(date, value);
				}
				entity = nextEntity;

			}
			dateValues.put(tankID, values);
		}
		return dateValues;
	}
	
	/**
	 * 
	 * @param date - date after which you need tank measure date 
	 * @param tankID - selected tank
	 * @return Date of nearly tank measure. Returns null when there is not such one;
	 */
	
	public Date getNextTankMeasureAfter(Date date, Integer tankID) {
		List<TankMeasuresEntity> tankMeasuresForTank = list.stream().filter(e1 -> e1.getTankId().equals(tankID)).sorted().collect(Collectors.toList());
		for(TankMeasuresEntity entity : tankMeasuresForTank) {
			Date currentDate = entity.getDate();
			if (currentDate.getTime() >= date.getTime()
					&& (currentDate.getTime() <= date.getTime() + 300000)) {
				return currentDate;
			}
		}
		
		return null;
	}

	public Integer getTanksCount() {
		HashMap<Integer, Double> tanksVolume = new HashMap<>();
		Integer count = new Integer(0);

		for (TankMeasuresEntity entity : getEntities()) {
			if (!tanksVolume.containsKey(entity.getTankId())) {
				tanksVolume.put(entity.getTankId(), (double) 0);
				count++;
			}
		}
		return count;
	}

	public Set<Integer> getTanksIndexes() {
		Set<Integer> keys = new HashSet<>();

		for (TankMeasuresEntity entity : getEntities()) {
			if (!keys.contains(entity.getTankId())) {
				keys.add(entity.getTankId());
			}
		}

		return keys;
	}

}
