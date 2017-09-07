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

public class AnomaliesDetector {

	NozzleDataExtractor nozzles;
	RefuelDataExtractor refuels;
	TankDataExtractor tanks;

	public AnomaliesDetector() {

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
			variance = tankVolumes.get(tank);
			variance -= nozzleVolumes.get(tank);
			// if(refuelVolumes.get(tank) != null) variance += refuelVolumes.get(tank);
			System.out.println("Variance for tank " + tank + " = " + variance);
		}

		return variances;
	}

	// 10%

	/**
	 * Returns List of anomalies when 
	 * 1. There is no usage of nozzles 
	 * 2. There is no refuels on tanks. 
	 * 3. There is no delta on TotalCounter 
	 * 4. There is delta on tank measures
	 */

	public Map<Date, Double> checkTank(Integer tankID) {
//		System.out.println("*************************************");
//		System.out.println("Checking tank leakages...");
//		System.out.println("*************************************");
		Map<Integer, Map<Date, Double>> leakages = new HashMap<>();
		Map<Integer, HashMap<Date, Double>> volumeChanges = tanks.getVolumeChanges();

//		for (Integer tankID : tanks.getTanksIndexes()) {
			System.out.println("Checking tank #" + tankID + "...");
			Map<Date, Double> forTank = new HashMap<>();

			for (Date changeDate : volumeChanges.get(tankID).keySet()) {
				if (!dateInRefuelPeriod(changeDate, tankID) && !dateInUsagePeriod(changeDate, tankID)) {
					System.out.println("Anomaly detection at " + changeDate + " on tank #" + tankID);
					forTank.put(changeDate, volumeChanges.get(tankID).get(changeDate));
				}

			}
//			leakages.put(tankID, forTank);
//		}
		return forTank;
	}

	/**
	 * Returns List of anomalies when 
	 * 1. There is no usage of nozzles 
	 * 2. There is no refuels on tanks. 
	 * 3. There is delta on TotalCounter 
	 * 4. There is delta on tankmeasures 
	 * 5. if tankDelta == totalCounterDelta
	 */
	public Map<Integer, Map<Date, Double>> checkPipes() {
		System.out.println("*************************************");
		System.out.println("Checking system leakages...");
		System.out.println("*************************************");
		Map<Integer, HashMap<Date, Double>> totalCounterVolumeChanges = nozzles.getVolumeChanges();
		Map<Integer, HashMap<Date, Double>> tankVolumeChanges = tanks.getVolumeChanges();
		Map<Integer, Map<Date, Double>> anomalies = new HashMap<>();
		for (Integer tankID : tanks.getTanksIndexes()) {
			System.out.println("Checking pipelines system related to tank #" + tankID + "...");
			Map<Date, Double> forTank = new HashMap<>();
			HashMap<Date, Double> volumeChangesOnSystem = totalCounterVolumeChanges.get(tankID);
			HashMap<Date, Double> volumeChangesOnTank = tankVolumeChanges.get(tankID);
			for (Date changeDate : volumeChangesOnSystem.keySet()) {
				if (!dateInUsagePeriod(changeDate, tankID) && !dateInRefuelPeriod(changeDate, tankID)) {
					Date tankMeasureDate = tanks.getNextTankMeasureAfter(changeDate, tankID);
					if (volumeChangesOnSystem.get(changeDate).equals(volumeChangesOnTank.get(tankMeasureDate))) {
						System.out.println("Leakage on pipes system detected at " + changeDate
								+ ". System related to tank #" + tankID);
						forTank.put(changeDate, volumeChangesOnSystem.get(changeDate));
					}
//						System.out.println("Some strange things happens here (data is invalid! at " + changeDate
//								+ " on tank #" + tankID + ")");
				}
			}
			anomalies.put(tankID, forTank);
		}
		
		System.out.println("*************************************");
		System.out.println("Checking tank leakages...");
		System.out.println("*************************************");
		for(Integer tankID : anomalies.keySet()) {
			if(anomalies.get(tankID).isEmpty()) {
				anomalies.put(tankID, checkTank(tankID));
			}
		}
		
		return anomalies;
	}

	/**
	 * Returns List of anomalies when 
	 * 1. There is usage of nozzles and 
	 * 2. There is no refuels on tanks. 
	 * 3. There is delta on TotalCounter 
	 * 4. There is delta on tan kmeasures 
	 * 5. if tankDelta != totalCounterDelta
	 */
	public Map<Integer, Map<Times, Double>> checkNozzles() {
		System.out.println("*************************************");
		System.out.println("Checking nozzles");
		System.out.println("*************************************");
		Map<Integer, HashMap<Times, Double>> nozzleUsages = nozzles.getTransactionTotals();
		Map<Integer, HashMap<Date, Double>> tankEntities = tanks.getVolumeChanges();
		Map<Integer, Map<Times, Double>> anomalies = new HashMap<>();

		for (Integer nozzID : nozzles.getNozzlesAssign().keySet()) {
			System.out.println("Checking nozzle #"+nozzID+"... (tank #"+ nozzles.getNozzlesAssign().get(nozzID)+")");
			Map<Times, Double> tankMeasuresFromNozzles = nozzleUsages.get(nozzID);
			HashMap<Date, Double> tankMeasuresFromTanks = tankEntities.get(nozzles.getNozzlesAssign().get(nozzID));
			HashMap<Times, Double> forNozzle = new HashMap<>();

			tankMeasuresFromNozzles = tankMeasuresFromNozzles.entrySet().stream()
					.filter(e1 -> dateInMultipleUsagePeriod(e1.getKey()))
					.collect(Collectors.toMap(e1 -> e1.getKey(), e1 -> e1.getValue()));
			Double val2 = 0D;
			for (Times usageDate : tankMeasuresFromNozzles.keySet()) {
				for (Date changeDate : tankMeasuresFromTanks.keySet()) {
					if (!dateInRefuelPeriod(usageDate.getTo(), nozzles.getNozzlesAssign().get(nozzID))
							&& dateInUsagePeriod(changeDate, nozzles.getNozzlesAssign().get(nozzID))) {
						val2 = val2 + tankMeasuresFromTanks.get(changeDate);
					}
					Double val1 = tankMeasuresFromNozzles.get(usageDate);
					Double delta = val1 - val2;
					if (delta != 0.0) {
						System.out.println("Anomaly detected at " + usageDate + " related to nozzle " + nozzID
								+" . Delta = " + delta);
						forNozzle.put(usageDate, delta);
					}
				}
			}
			anomalies.put(nozzID, forNozzle);
		}
		return anomalies;
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

	public Boolean dateInUsagePeriod(Date changeDate, Integer tankID) {
		Map<Integer, List<Times>> usagePeriods = nozzles.getTankUsagePeriods();
		for (Times usagePeriod : usagePeriods.get(tankID)) {
			if (changeDate.after(usagePeriod.getFrom())
					&& (changeDate.getTime() <= usagePeriod.getTo().getTime() + 300000)) {
				return true;
			}
		}
		return false;
	}

	public Boolean dateInMultipleUsagePeriod(Times period) {
		List<Date> multipleUsages = nozzles.getMultipleUsagePeriods();
		for (Date multipleUsage : multipleUsages)
			if (period.containsDate(multipleUsage))
				return false;

		return true;
	}
	
	

}
