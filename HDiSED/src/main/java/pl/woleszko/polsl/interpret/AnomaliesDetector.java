package pl.woleszko.polsl.interpret;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
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
import pl.woleszko.polsl.model.utils.StreamUtils;

public class AnomaliesDetector {

	NozzleDataExtractor nozzles;
	RefuelDataExtractor refuels;
	TankDataExtractor tanks;

	PrintWriter systemOutputFile;
	PrintWriter nozzleOutputFile;

	public AnomaliesDetector(String folderPath) {

		FileAccessor<RefuelEntity> refuelData;
		FileAccessor<TankMeasuresEntity> tankData;
		FileAccessor<NozzleMeasuresEntity> nozzleData;

		refuelData = new FileAccessorCSV<>(RefuelEntity.class, folderPath+"/refuel.csv");
		tankData = new FileAccessorCSV<>(TankMeasuresEntity.class, folderPath+"/tankMeasures.csv");
		nozzleData = new FileAccessorCSV<>(NozzleMeasuresEntity.class, folderPath+"/nozzleMeasures.csv");

		refuels = new RefuelDataExtractor(refuelData);
		nozzles = new NozzleDataExtractor(nozzleData);
		tanks = new TankDataExtractor(tankData);
		
		String systemOutputFileName = folderPath+"/systemOutput.log";
		String nozzleOutputFileName = folderPath+"/nozzleOutput.log";
		try {
			systemOutputFile = new PrintWriter(new FileWriter(systemOutputFileName));
			nozzleOutputFile = new PrintWriter(new FileWriter(nozzleOutputFileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
			 if(refuelVolumes.get(tank) != null) variance += refuelVolumes.get(tank);
			System.out.println("Variance for tank " + tank + " = " + variance);
		}

		return variances;
	}

	// 10%

	/**
	 * Returns List of anomalies when 1. There is no usage of nozzles 2. There is no
	 * refuels on tanks. 3. There is no delta on TotalCounter 4. There is delta on
	 * tank measures
	 */

	public Map<Date, Double> checkSingleTank(Integer tankID) {
		Map<Integer, HashMap<Date, Double>> volumeChanges = tanks.getVolumeChanges();
		System.out.println("Checking tank #" + tankID + "...");
		Map<Date, Double> forTank = new HashMap<>();

		for (Date changeDate : volumeChanges.get(tankID).keySet()) {
			if (!refuels.dateInRefuelPeriod(changeDate, tankID) && !nozzles.dateInUsagePeriod(changeDate, tankID)) {
				System.out.println("Anomaly detection at " + changeDate + " on tank #" + tankID);
				forTank.put(changeDate, volumeChanges.get(tankID).get(changeDate));
			}
		}

		return forTank;
	}

	public Map<Integer, Map<Date, Double>> checkTanks() {
		System.out.println("*************************************");
		System.out.println("Checking tank leakages...");
		System.out.println("*************************************");
		Map<Integer, Map<Date, Double>> leakages = new HashMap<>();

		for (Integer tankID : tanks.getTanksIndexes()) {
			Map<Date, Double> forTank = checkSingleTank(tankID);
			leakages.put(tankID, forTank);
		}
		return leakages;
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

		systemOutputFile.println("*************************************");
		systemOutputFile.println("Checking system leakages...");
		systemOutputFile.println("*************************************");
		Map<Integer, HashMap<Date, Double>> totalCounterVolumeChanges = nozzles.getVolumeChanges();
		Map<Integer, HashMap<Date, Double>> tankVolumeChanges = tanks.getVolumeChanges();
		Map<Integer, Map<Date, Double>> anomalies = new HashMap<>();
		for (Integer tankID : tanks.getTanksIndexes()) {
			systemOutputFile.println("Checking pipelines system related to tank #" + tankID + "...");
			Map<Date, Double> forTank = new HashMap<>();
			HashMap<Date, Double> volumeChangesOnSystem = totalCounterVolumeChanges.get(tankID);
			HashMap<Date, Double> volumeChangesOnTank = tankVolumeChanges.get(tankID);
			for (Date changeDate : volumeChangesOnSystem.keySet()) {
				if (!nozzles.dateInUsagePeriod(changeDate, tankID) && !refuels.dateInRefuelPeriod(changeDate, tankID)) {
					Date tankMeasureDate = tanks.getNextTankMeasureAfter(changeDate, tankID);
					Double elem1 = volumeChangesOnTank.get(tankMeasureDate);
					Double elem2 = volumeChangesOnSystem.get(changeDate);
					Double delta = volumeChangesOnTank.get(tankMeasureDate) - volumeChangesOnSystem.get(changeDate);
					if (delta.equals(0D)) {
						systemOutputFile.println("Leakage on pipes system detected at " + changeDate
								+ ". System related to tank #" + tankID
								+ ". \nPipeline leakage: "+ volumeChangesOnSystem.get(changeDate));

					} else if(delta > 0  && !delta.equals(volumeChangesOnTank.get(tankMeasureDate))) {
						systemOutputFile.println("Leakage on pipes system & tank detected at " + changeDate
								+ ". System related to tank #" + tankID+". \nPipeline leakage: "+ delta
								+ " \nTank leakage: "+ (volumeChangesOnTank.get(tankMeasureDate) - delta));

					} else if(delta.equals(volumeChangesOnTank.get(tankMeasureDate))) {
						systemOutputFile.println("Leakage on tank detected at " + changeDate
								+ ". System related to tank #" + tankID+". \nTank leakage: "+volumeChangesOnTank.get(tankMeasureDate));
					}
					forTank.put(changeDate, volumeChangesOnSystem.get(changeDate));
				}
			}
			anomalies.put(tankID, forTank);
		}
		
//		System.out.println("*************************************");
//		System.out.println("Checking tank leakages...");
//		System.out.println("*************************************");
//		for(Integer tankID : anomalies.keySet()) {
//			if(anomalies.get(tankID).isEmpty()) {
//				//czyli wyciek jest tylko na orurowaniu bo tankMeasures == totalCounter
//				anomalies.put(tankID, checkSingleTank(tankID));
//			} else {
//				//obsluzyc sytuacje, gdy delta < 0 - bo wtedy wyciek jest i tu i tu
//				
//				
//			}
//				
//		}
		systemOutputFile.close();
		return anomalies;
	}

	/**
	 * Returns List of anomalies when 1. There is usage of nozzles and 2. There is
	 * no refuels on tanks. 3. There is delta on TotalCounter 4. There is delta on
	 * tan kmeasures 5. if tankDelta != totalCounterDelta
	 */
	public Map<Integer, Map<Times, Double>> checkNozzles() {
		nozzleOutputFile.println("*************************************");
		nozzleOutputFile.println("Checking nozzles");
		nozzleOutputFile.println("*************************************");
		Map<Integer, HashMap<Times, Double>> nozzleUsages = nozzles.getTransactionTotals();
		Map<Integer, HashMap<Date, Double>> tankEntities = tanks.getVolumeChanges();
		Map<Integer, Map<Times, Double>> anomalies = new HashMap<>();

		for (Integer nozzID : nozzleUsages.keySet()) {
			nozzleOutputFile.println("Checking nozzle #" + nozzID + "... (tank #" + nozzles.getNozzleAssign(nozzID) + ")");
			Map<Times, Double> tankMeasuresFromNozzles = nozzleUsages.get(nozzID);
			HashMap<Date, Double> tankMeasuresFromTanks = tankEntities.get(nozzles.getNozzleAssign(nozzID));
			HashMap<Times, Double> forNozzle = new HashMap<>();

			tankMeasuresFromNozzles = tankMeasuresFromNozzles.entrySet().stream()
					.filter(e1 -> nozzles.dateInMultipleUsagePeriod(e1.getKey()))
					.collect(Collectors.toMap(e1 -> e1.getKey(), e1 -> e1.getValue()));
			Double val2 = 0D;
			for (Times usageDate : tankMeasuresFromNozzles.keySet()) {
				for (Date changeDate : tankMeasuresFromTanks.keySet()) {
					if (!refuels.dateInRefuelPeriod(usageDate.getTo(), nozzles.getNozzleAssign(nozzID))
							&& nozzles.dateInUsagePeriod(changeDate, nozzles.getNozzleAssign(nozzID))) {
						val2 = val2 + tankMeasuresFromTanks.get(changeDate);
					}
					Double val1 = tankMeasuresFromNozzles.get(usageDate);
					Double delta = val1 - val2;
					if (delta != 0.0) {;
						forNozzle.put(usageDate, delta);
					}
				}
			}
			Map<Times, Double> newMap = forNozzle.entrySet().stream().filter(StreamUtils.distinctByKey(e1 -> e1.getKey().getFrom())).collect(Collectors.toMap(e1 -> e1.getKey(), e1 -> e1.getValue()));
			for(Times period : newMap.keySet()) {
				if (newMap.get(period) != 0.0) {
					nozzleOutputFile.println("Anomaly detected at " + period + " related to nozzle " + nozzID
							+" . Delta = " + newMap.get(period));
				}
			}
			anomalies.put(nozzID, forNozzle);
		}
		nozzleOutputFile.close();
		return anomalies;
	}

}
