package pl.woleszko.polsl.interpret;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import pl.woleszko.polsl.maths.impl.NozzleDataExtractor;
import pl.woleszko.polsl.maths.impl.RefuelDataExtractor;
import pl.woleszko.polsl.maths.impl.TankDataExtractor;
import pl.woleszko.polsl.maths.objects.Period;
import pl.woleszko.polsl.maths.objects.Times;

public class DataAnalizer {

	NozzleDataExtractor nozzles;
	RefuelDataExtractor refuels;
	TankDataExtractor tanks;
	
	public DataAnalizer() {        
        nozzles = new NozzleDataExtractor("nozzleMeasures.csv");
        refuels = new RefuelDataExtractor("refuel.csv");
        tanks = new TankDataExtractor("tankMeasures.csv");
	}

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

		for (Times key : periodKeys) {
			System.out.println(key.toString());
			// Totals initialization for tanks in periods (if not in period sets zeroes)

			for (Times nozzleVolumesTime : nozzleVolumes.keySet()) {
				if (key.dateInPeriod(nozzleVolumesTime.getFrom()) && key.dateInPeriod(nozzleVolumesTime.getTo()))
					nozzleTotals = nozzleVolumes.get(nozzleVolumesTime);
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
		
		for(Integer hour : hoursAvg.keySet()) {
			HashMap<Long, Double> tanksValues = hoursAvg.get(hour);
			System.out.println("Variance for tank 1 for hour " +hour+ " equals " + tanksValues.get(1L));					
		}
		
		for(Integer hour : hoursAvg.keySet()) {
			HashMap<Long, Double> tanksValues = hoursAvg.get(hour);
			System.out.println("Variance for tank 2 for hour " +hour+ " equals " + tanksValues.get(2L));			
		}
		
		for(Integer hour : hoursAvg.keySet()) {
			HashMap<Long, Double> tanksValues = hoursAvg.get(hour);
			System.out.println("Variance for tank 3 for hour " +hour+ " equals " + tanksValues.get(3L));			
		}
		
		for(Integer hour : hoursAvg.keySet()) {
			HashMap<Long, Double> tanksValues = hoursAvg.get(hour);
			System.out.println("Variance for tank 4 for hour " +hour+ " equals " + tanksValues.get(4L));			
		}
		
//		Integer counter = 0;
//
//		for (Times time : hoursVol.keySet()) {
//			
//			for (Integer hour : hoursAvg.keySet()) {
//				if (time.getFrom().getHours() == hour) {
//
//					HashMap<Long, Double> testers = hoursAvg.get(hour);
//					HashMap<Long, Double> actuals = hoursVol.get(time);
//
//					Double standard = testers.get(tankID);
//					Double actual = actuals.get(tankID);
//					Double value = new Double(0);
//					if (!actual.equals(0D))
//						value = actual / standard; // uzyskujemy jaki procent odchyłu
//					value = Math.abs(value);
//
//					if (value > 2.50) {
//						System.out.println("Probability of leakage on tank #" + tankID + " at " + time.toString());
//						System.out.println("Delta: " + value + "%");
//						counter++;
//					}
//
//				}
//			}
//
//		}
//		System.out.println("COunter: " + counter);

	}
	
	public void checkNozzles() {
		HashMap<Long, ArrayList<Times>> nozzleUsageMap = nozzles.getUsagePeriods();
		for(Long nozzle : nozzleUsageMap.keySet()) {
			System.out.println("Checking nozzle #" + nozzle);
			ArrayList<Times> nozzleUsageTimes = nozzleUsageMap.get(nozzle);
			ArrayList<Times> tankPeriods = tanks.splitDates(300000L);
			
			HashMap<Times, HashMap<Long, Double>> tankVolumesTotals = tanks.getVolumeTotals(tankPeriods);	//tutaj powinien zbierac totale z okresow w ktorych nastapilo tankowanie
			
			
			HashMap<Times, HashMap<Long, Double>> nozzleVolumesTotals = nozzles.getTransactionTotals(nozzleUsageTimes); //tutaj powinien zbierac calkowite licznik transakcji
			
			
			
			for(Times period : nozzleUsageTimes) {
				HashMap<Long, Double> tanksCounters = null;
				
				for(Times tankPeriod : tankVolumesTotals.keySet()) {
					if(tankPeriod.dateInPeriod(period.getTo())) {
						tanksCounters = tankVolumesTotals.get(tankPeriod);
					}
				}
				
				
				if(tanksCounters != null) {
				HashMap<Long, Double> nozzleCounters = nozzleVolumesTotals.get(period);
				
				for(Long tank : tanks.getTanksIndexes()) {
					Double delta = nozzleCounters.get(tank) - tanksCounters.get(tank);
					Math.abs(delta);
					if(delta > 5.0) System.out.println("Error detected at " + period.getFrom() + " related to tank " + tank + ". Delta = " + delta);
				}
				} else {
					System.out.println("Error in checking nozzle.. Abort..");
				}
				
			}
			
		}
		
	}

//	public double getAvgVariancePerHour(Double variance) {
//
//		double result = 0;
//		HashMap<Long, Times> list = tanks.splitDates(Period.HOUR);
//		Integer hoursCount = list.size();
//		result = variance / hoursCount;
//
//		return result;
//	}

}
