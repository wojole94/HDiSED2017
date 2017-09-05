package pl.woleszko.polsl.interpret;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import pl.woleszko.polsl.maths.impl.NozzleDataExtractor;
import pl.woleszko.polsl.maths.impl.RefuelDataExtractor;
import pl.woleszko.polsl.maths.impl.TankDataExtractor;
import pl.woleszko.polsl.maths.objects.Times;
import pl.woleszko.polsl.model.entities.NozzleMeasuresEntity;
import pl.woleszko.polsl.model.entities.RefuelEntity;
import pl.woleszko.polsl.model.entities.TankMeasuresEntity;
import pl.woleszko.polsl.model.impl.FileAccessor;
import pl.woleszko.polsl.model.impl.FileAccessorCSV;

public class DataAnalizer {

	NozzleDataExtractor nozzles;
	RefuelDataExtractor refuels;
	TankDataExtractor tanks;

	public DataAnalizer() {

		FileAccessor<RefuelEntity> refuelData;
		FileAccessor<TankMeasuresEntity> tankData;
		FileAccessor<NozzleMeasuresEntity> nozzleData;

		refuelData = new FileAccessorCSV<>(RefuelEntity.class, "D:/Zestaw 1/refuel.csv");
		tankData = new FileAccessorCSV<>(TankMeasuresEntity.class, "D:/Zestaw 1/tankMeasures.csv");
		nozzleData = new FileAccessorCSV<>(NozzleMeasuresEntity.class, "D:/Zestaw 1/nozzleMeasures.csv");

		refuels = new RefuelDataExtractor(refuelData);
		nozzles = new NozzleDataExtractor(nozzleData);
		tanks = new TankDataExtractor(tankData);
	}

	public HashMap<Long, Double> detect() {

		Set<Integer> tanksList = tanks.getTanksIndexes();

		Map<Integer, Double> nozzleVolumes = this.nozzles.getVolumeTotals();
		Map<Integer, Double> tankVolumes = this.tanks.getVolumeTotals();
		Map<Integer, Double> refuelVolumes = this.refuels.getVolumeTotals();
		HashMap<Long, Double> variances = new HashMap<>();

		Double variance = new Double(0);
		for (Integer tank : tanksList) {
			variance = nozzleVolumes.get(tank);
			variance -= tankVolumes.get(tank);
			variance -= refuelVolumes.get(tank);
			System.out.println("Variance for tank " + tank + " = " + variance);
		}

		return variances;
	}

	// 10%
	public void checkTank() {

		Map<Integer, Map<Date, Double>> leakages = new HashMap<>();
		Map<Integer, List<Times>> usagePeriods = nozzles.getTankUsagePeriods();
		Map<Integer, HashMap<Date, Double>> volumeChanges = tanks.getVolumeChanges();

		for (Integer tankID : tanks.getTanksIndexes()) {
			System.out.println("Checking tank #" + tankID + "...");
			Map<Date, Double> forTank = new HashMap<>();
			
			for (Date changeDate : volumeChanges.get(tankID).keySet()) {
				// Sprawdzenie czy nie trwa dostawa
				Boolean okFlag = false;
				if (!dateInRefuelPeriod(changeDate, tankID)) {
					// Sprawdzenie czy zmiana nie znajduje sie w okresie tankowania
					for (Times usagePeriod : usagePeriods.get(tankID)) {
						if (changeDate.after(usagePeriod.getFrom())
								&& (changeDate.getTime() <= usagePeriod.getTo().getTime() + 300000)) {
							okFlag = true;
							break;
						}
					}
					if(!okFlag) {
						System.out.println("Anomaly detection at " + changeDate +" on tank #"+ tankID);
						forTank.put(changeDate, volumeChanges.get(tankID).get(changeDate));
					}
				}

			}
			leakages.put(tankID, forTank);
		}
		System.out.println("finito");;

		// HashMap<Integer, HashMap<Long, Double>> hoursAvg = tanks.getHoursTrend();
		//
		// HashMap<Times, HashMap<Long, Double>> hoursVol =
		// tanks.getVolumeTotals(tanks.splitDates(Period.HOUR));
		//
		// for(Integer hour : hoursAvg.keySet()) {
		// HashMap<Long, Double> tanksValues = hoursAvg.get(hour);
		// System.out.println("Variance for tank 1 for hour " +hour+ " equals " +
		// tanksValues.get(1L));
		// }
		//
		// for(Integer hour : hoursAvg.keySet()) {
		// HashMap<Long, Double> tanksValues = hoursAvg.get(hour);
		// System.out.println("Variance for tank 2 for hour " +hour+ " equals " +
		// tanksValues.get(2L));
		// }
		//
		// for(Integer hour : hoursAvg.keySet()) {
		// HashMap<Long, Double> tanksValues = hoursAvg.get(hour);
		// System.out.println("Variance for tank 3 for hour " +hour+ " equals " +
		// tanksValues.get(3L));
		// }
		//
		// for(Integer hour : hoursAvg.keySet()) {
		// HashMap<Long, Double> tanksValues = hoursAvg.get(hour);
		// System.out.println("Variance for tank 4 for hour " +hour+ " equals " +
		// tanksValues.get(4L));
		// }
		//
		// Integer counter = 0;
		//
		// for (Times time : hoursVol.keySet()) {
		//
		// for (Integer hour : hoursAvg.keySet()) {
		// if (time.getFrom().getHours() == hour) {
		//
		// HashMap<Long, Double> testers = hoursAvg.get(hour);
		// HashMap<Long, Double> actuals = hoursVol.get(time);
		//
		// Double standard = testers.get(tankID);
		// Double actual = actuals.get(tankID);
		// Double value = new Double(0);
		// if (!actual.equals(0D))
		// value = actual / standard; // uzyskujemy jaki procent odchyłu
		// value = Math.abs(value);
		//
		// if (value > 2.50) {
		// System.out.println("Probability of leakage on tank #" + tankID + " at " +
		// time.toString());
		// System.out.println("Delta: " + value + "%");
		// counter++;
		// }
		//
		// }
		// }
		//
		// }
		// System.out.println("COunter: " + counter);

	}

	public void checkNozzles() {
		// HashMap<Long, ArrayList<Times>> nozzleUsageMap = nozzles.getUsagePeriods();
		// for(Long nozzle : nozzleUsageMap.keySet()) {
		// System.out.println("Checking nozzle #" + nozzle);
		// ArrayList<Times> nozzleUsageTimes = nozzleUsageMap.get(nozzle);
		// ArrayList<Times> tankPeriods = tanks.splitDates(300000L);
		//
		// HashMap<Times, HashMap<Long, Double>> tankVolumesTotals =
		// tanks.getVolumeTotals(tankPeriods); //tutaj powinien zbierac totale z okresow
		// w ktorych nastapilo tankowanie
		//
		//
		// HashMap<Times, HashMap<Long, Double>> nozzleVolumesTotals =
		// nozzles.getTransactionTotals(nozzleUsageTimes); //tutaj powinien zbierac
		// calkowite licznik transakcji
		//
		//
		//
		// for(Times period : nozzleUsageTimes) {
		// HashMap<Long, Double> tanksCounters = null;
		//
		// for(Times tankPeriod : tankVolumesTotals.keySet()) {
		// if(tankPeriod.dateInPeriod(period.getTo())) {
		// tanksCounters = tankVolumesTotals.get(tankPeriod);
		// }
		// }
		//
		//
		// if(tanksCounters != null) {
		// HashMap<Long, Double> nozzleCounters = nozzleVolumesTotals.get(period);
		//
		// for(Long tank : tanks.getTanksIndexes()) {
		// Double delta = nozzleCounters.get(tank) - tanksCounters.get(tank);
		// Math.abs(delta);
		// if(delta > 5.0) System.out.println("Error detected at " + period.getFrom() +
		// " related to tank " + tank + ". Delta = " + delta);
		// }
		// } else {
		// System.out.println("Error in checking nozzle.. Abort..");
		// }
		//
		// }
		//
		// }
		//
		Map<Integer, HashMap<Times, Double>> nozzleUsages = nozzles.getTransactionTotals();
		Map<Integer, HashMap<Date, Double>> tankEntities = tanks.getVolumeChanges();

		for (Integer nozzID : nozzles.getNozzlesAssign().keySet()) {
			Map<Times, Double> tankMeasuresFromNozzles = nozzleUsages.get(nozzID);
			HashMap<Date, Double> tankMeasuresFromTanks = tankEntities.get(nozzles.getNozzlesAssign().get(nozzID));

			tankMeasuresFromNozzles = tankMeasuresFromNozzles.entrySet().stream().filter(e1 -> {

				Times period = e1.getKey();
				List<Date> multipleUsages = nozzles.getMultipleUsagePeriods();
				for (Date multipleUsage : multipleUsages)
					if (period.containsDate(multipleUsage))
						return false;

				return true;
			}).collect(Collectors.toMap(e1 -> e1.getKey(), e1 -> e1.getValue()));

			for (Times usageDate : tankMeasuresFromNozzles.keySet()) {
				Double val2 = 0D;
				if (!dateInRefuelPeriod(usageDate.getTo(), nozzles.getNozzlesAssign().get(nozzID))) {
					for (Date changeDate : tankMeasuresFromTanks.keySet()) {
						if (/*
							 * changeDate.after(usageDate.getFrom()) && changeDate.getTime() <
							 * (usageDate.getFrom()).getTime() + 300000L
							 */ changeDate.after(usageDate.getFrom())
								&& (changeDate.getTime() <= usageDate.getTo().getTime() + 300000)) {
							val2 = val2 + tankMeasuresFromTanks.get(changeDate);
						}
					}
					Double val1 = tankMeasuresFromNozzles.get(usageDate);
					Double delta = val1 - val2;/*
												 * tankMeasuresFromNozzles.get(usageDate) -
												 * tankMeasuresFromTanks.get(changeDate);
												 */
					// Math.abs(delta);

					if (delta < -5.0)
						System.out.println("Anomaly detected at " + usageDate + " related to nozzle " + nozzID
								+ ". Delta = " + delta);
				}
			}
		}
	}

	public Boolean dateInRefuelPeriod(Date date, Integer tankID) {
		Map<Times, Integer> refuelsPeriods = refuels.getRefuelPeriods();
		List<Times> periods = refuelsPeriods.entrySet().stream().filter(e1 -> e1.getValue().equals(tankID))
				.map(Map.Entry::getKey).collect(Collectors.toList());

		for (Times period : periods) {
			if (period.containsDate(date)) {
				return true;
			}
		}
		return false;
	}

	// ListIterator<NozzleMeasuresEntity> nozzlesIterator =
	// tankMeasuresFromNozzles.listIterator();
	// ListIterator<TankMeasuresEntity> tanksIterator =
	// tankMeasuresFromTanks.listIterator();
	//
	// HashMap<Date, Double> deltas = new HashMap<>();
	// while(nozzlesIterator.hasNext()) {
	// NozzleMeasuresEntity nozzleEntity = nozzlesIterator.next();
	// while(tanksIterator.hasNext()) {
	// TankMeasuresEntity tankEntity = tanksIterator.next();
	// if(tankEntity.getDate().getTime() >= nozzleEntity.getDate().getTime()) {
	// Double delta = tanksIterator.previous().getFuelVol() -
	// tankEntity.getFuelVol();
	//
	// }
	// }
	//
	//
	//
	// }
	//
	// }
	//
}

// public double getAvgVariancePerHour(Double variance) {
//
// double result = 0;
// HashMap<Long, Times> list = tanks.splitDates(Period.HOUR);
// Integer hoursCount = list.size();
// result = variance / hoursCount;
//
// return result;
// }
