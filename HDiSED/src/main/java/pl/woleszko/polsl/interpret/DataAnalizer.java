package pl.woleszko.polsl.interpret;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import pl.woleszko.polsl.maths.impl.NozzleDataExtractor;
import pl.woleszko.polsl.maths.impl.RefuelDataExtractor;
import pl.woleszko.polsl.maths.impl.TankDataExtractor;
import pl.woleszko.polsl.maths.objects.Period;
import pl.woleszko.polsl.maths.objects.Times;
import pl.woleszko.polsl.model.entities.Entity;

public class DataAnalizer {

	NozzleDataExtractor nozzles = new NozzleDataExtractor();
	RefuelDataExtractor refuels = new RefuelDataExtractor();
	TankDataExtractor tanks = new TankDataExtractor();

	public HashMap<Long, Double> detect() {
		ArrayList<Times> nozzleTimes = nozzles.splitDates(Period.FULL_TIME);
		HashMap<Times, HashMap<Long, Double>> nozzleVolumes = nozzles.getVolumeTotals(nozzleTimes);
		ArrayList<Times> refuelTimes = refuels.splitDates(Period.FULL_TIME);
		HashMap<Times, HashMap<Long, Double>> refuelVolumes = refuels.getVolumeTotals(refuelTimes);
		ArrayList<Times> tankTimes = tanks.splitDates(Period.FULL_TIME);
		HashMap<Times, HashMap<Long, Double>> tankVolumes = tanks.getVolumeTotals(tankTimes);

		Set<Long> tanksList = tanks.getTanksIndexes();
		Set<Times> periodKeys = tankVolumes.keySet();

		HashMap<Long, Double> variances = new HashMap<Long, Double>();
		HashMap<Long, Double> refuelTotals = null;
		HashMap<Long, Double> tankTotals = null;
		HashMap<Long, Double> nozzleTotals = null;
		HashMap<Long, Double> nozzleVolumesPerPistol = null;

		for (Times key : periodKeys) {
			System.out.println(key.toString());
			// Totals initialization for tanks in periods (if not in period sets zeroes)

			for (Times nozzleVolumesTime : nozzleVolumes.keySet()) {
				//if (key.dateInPeriod(nozzleVolumesTime.getFrom()) && key.dateInPeriod(nozzleVolumesTime.getTo()))
					nozzleVolumesPerPistol = nozzleVolumes.get(nozzleVolumesTime);
				nozzleTotals = new HashMap<Long, Double>();
				for (Long tank : tanksList) {
					nozzleTotals.put(tank, new Double(0));
				}
				HashMap<Long, Long> nozzlesTankMapping = nozzles.getNozzlesAssign();
				for (Long pistol : nozzleVolumesPerPistol.keySet()) {
					double sum = nozzleTotals.get(nozzlesTankMapping.get(pistol));
					sum = sum + nozzleVolumesPerPistol.get(pistol);
					nozzleTotals.put(nozzlesTankMapping.get(pistol), sum);
				}

				break;
			}

			if (nozzleTotals.equals(null)) {
				nozzleTotals = new HashMap<Long, Double>();
				for (Long tank : tanksList) {
					nozzleTotals.put(tank, new Double(0));
				}
			}
			// =======================================
			if (tankVolumes.containsKey(key))
				tankTotals = tankVolumes.get(key);
			else {
				tankTotals = new HashMap<Long, Double>();
				for (Long tank : tanksList) {
					tankTotals.put(tank, new Double(0));
				}
			}

			// --------------------------------------

			for (Times refuelVolumesTime : refuelVolumes.keySet()) {
				if (key.dateInPeriod(refuelVolumesTime.getFrom()) && key.dateInPeriod(refuelVolumesTime.getTo()))
					refuelTotals = refuelVolumes.get(refuelVolumesTime);

			}
			if (refuelTotals.equals(null)) {
				refuelTotals = new HashMap<Long, Double>();
				for (Long tank : tanksList) {
					refuelTotals.put(tank, new Double(0));
				}
			}

			// ==========================================

			Double variance = new Double(0);
			for (Long tank : tanksList) {
				variance = nozzleTotals.get(tank);
				variance -= tankTotals.get(tank);
				variance -= refuelTotals.get(tank);
				System.out.println("Variance for tank " + tank + " = " + variance);
			}

			// variance = nozzleVolumes.get(key);
			// variance -= tankVolumes.get(key);
			// variance -= refuelVolumes.get(key);
			// variances.put(key, variance);
			// System.out.println("Variance for tank nr " + key.toString() + " = " +
			// variance);
		}

		return variances;
	}

	// 10%
	public void checkTank(Long tankID) {
		HashMap<Integer, HashMap<Long, Double>> hoursAvg = tanks.getHoursTrend();

		HashMap<Times, HashMap<Long, Double>> hoursVol = tanks.getVolumeTotals(tanks.splitDates(Period.HOUR));

		for (Integer hour : hoursAvg.keySet()) {
			HashMap<Long, Double> tanksValues = hoursAvg.get(hour);
			System.out.println("Variance for tank 1 for hour " + hour + " equals " + tanksValues.get(1L));
		}

		for (Integer hour : hoursAvg.keySet()) {
			HashMap<Long, Double> tanksValues = hoursAvg.get(hour);
			System.out.println("Variance for tank 2 for hour " + hour + " equals " + tanksValues.get(2L));
		}

		for (Integer hour : hoursAvg.keySet()) {
			HashMap<Long, Double> tanksValues = hoursAvg.get(hour);
			System.out.println("Variance for tank 3 for hour " + hour + " equals " + tanksValues.get(3L));
		}

		for (Integer hour : hoursAvg.keySet()) {
			HashMap<Long, Double> tanksValues = hoursAvg.get(hour);
			System.out.println("Variance for tank 4 for hour " + hour + " equals " + tanksValues.get(4L));
		}

	}

	public HashMap<Long, HashMap<Times, Double>> checkNozzles() {
		HashMap<Times, Long> nozzleUsageMap = nozzles.getUsagePeriods();

		ArrayList<Times> nozzleUsageTimes = new ArrayList<Times>();
		for (Times period : nozzleUsageMap.keySet()) {
			nozzleUsageTimes.add(period);
		}

		ArrayList<Times> tankPeriods = tanks.splitDates(300000L);

		// Sorting lists by daytime
		Collections.sort(nozzleUsageTimes, new Comparator<Times>() {
			@Override
			public int compare(Times t1, Times t2) {
				// TODO Auto-generated method stub
				Long obj1 = t1.getFrom().getTime();
				Long obj2 = t2.getFrom().getTime();
				return obj1.compareTo(obj2);
			}
		});

		Collections.sort(tankPeriods, new Comparator<Times>() {
			@Override
			public int compare(Times t1, Times t2) {
				// TODO Auto-generated method stubhg
				Long obj1 = t1.getFrom().getTime();
				Long obj2 = t2.getFrom().getTime();
				return obj1.compareTo(obj2);
			}
		});

		HashMap<Times, HashMap<Long, Double>> tankVolumesTotals = tanks.getVolumeTotals(tankPeriods);
		HashMap<Times, HashMap<Long, Double>> nozzleVolumesTotals = nozzles.getVolumeTotals(nozzleUsageTimes);

		HashMap<Long, HashMap<Times, Double>> nozzlesAnomalies = new HashMap<Long, HashMap<Times, Double>>();

		Long refueledTank = new Long(0);
		HashMap<Times, Long> refuelPeriods = refuels.getRefuelPeriods();
		HashMap<Long, Long> tankMapping = nozzles.getNozzlesAssign();
		//Przechodzenie po 5-minutowych okienkach odczytow ze zbiornikow 
		for (int i = 0; i < tankPeriods.size(); i++) {
			HashMap<Long, Double> nozzleSums = new HashMap<Long, Double>();
			Set<Times> keys = refuelPeriods.keySet();
			//Sprawdzanie czy ktorys zbiornik jest tankowany, jesli tak zapamietuje
			for (Times period : keys) {
				if ((period.dateInPeriod(tankPeriods.get(i).getFrom()))	|| (period.dateInPeriod(tankPeriods.get(i).getTo()))) {
					Long temp = refuelPeriods.get(period);
					refueledTank  = temp;
				} 
			}
			//Przechodzenie po okresach uzycia pistoletow
			for (int j = 0; j < nozzleUsageTimes.size(); j++) {
				//sprawdzenie czy tankowanie odbywa sie w danym okresie odczytow zbiornika
				if (tankPeriods.get(i).dateInPeriod(nozzleUsageTimes.get(j).getFrom())) {
					Long currentNozzle = nozzleUsageMap.get(nozzleUsageTimes.get(j));
					//sprawdzenie czy dany zbiornik (z ktorego pobierane jest paliwo) nie jest aktualnie napelniany
					if (!(tankMapping.get(currentNozzle).equals(refueledTank))) {
						if (nozzleSums.containsKey(currentNozzle)) {
							Double sum = nozzleSums.get(currentNozzle);
							sum = sum + nozzleVolumesTotals.get(nozzleUsageTimes.get(j))
									.get(currentNozzle);
							nozzleSums.put(currentNozzle, sum);
						}

						if (!nozzleSums.containsKey(currentNozzle)) {
							Double sum = nozzleVolumesTotals.get(nozzleUsageTimes.get(j))
									.get(currentNozzle);
							nozzleSums.put(currentNozzle, sum);
						}
					}
				}
			}
			//pobranie calkowitych odczytow ze zbiornika dla danego okresu
			HashMap<Long, Double> tankDeltas = tankVolumesTotals.get(tankPeriods.get(i));
			
			for(Long tank : tankDeltas.keySet()) {
				Double delta = tankDeltas.get(tank);
				Double nozzSum = new Double(0);
				for(Long nozz : nozzleSums.keySet()) {
					if(tankMapping.get(nozz).equals(tank)) {
						nozzSum = nozzSum + nozzleSums.get(nozz);
					}
				}
				Double sub = delta - nozzSum;
				if (sub > 5.0 ) {
//					 System.out.println( "Error detected at " + tankPeriods.get(i) + " on tank #" + tank + ". Delta= " + sub);
					if (!nozzlesAnomalies.containsKey(tank)) {
						HashMap<Times, Double> anomalies = new HashMap<Times, Double>();
						nozzlesAnomalies.put(tank, anomalies);
					}
					HashMap<Times, Double> anomalies = nozzlesAnomalies.get(tank);
					anomalies.put(tankPeriods.get(i), sub);
					nozzlesAnomalies.put(tank, anomalies);
				}
				
				
			}
			
//			for (Long nozz : nozzleSums.keySet()) {
//				Double delta = tankDeltas.get(tankMapping.get(nozz));
//				Double sub = delta - nozzleSums.get(nozz);
//				if (sub > 5.0 ) {
//					// System.out.println(
//					// "Error detected at " + tankPeriods.get(i) + " on nozzle #" + nozz + ". Delta
//					// = " + sub);
//					if (!nozzlesAnomalies.containsKey(nozz)) {
//						HashMap<Times, Double> anomalies = new HashMap<Times, Double>();
//						nozzlesAnomalies.put(nozz, anomalies);
//					}
//					HashMap<Times, Double> anomalies = nozzlesAnomalies.get(nozz);
//					anomalies.put(tankPeriods.get(i), sub);
//					nozzlesAnomalies.put(nozz, anomalies);
//				}
//			}
			refueledTank = 0L;
		}

		return nozzlesAnomalies;
	}

}
