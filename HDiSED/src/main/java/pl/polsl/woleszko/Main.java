package pl.polsl.woleszko;

import java.util.Date;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import pl.woleszko.polsl.interpret.AnomaliesDetector;

public class Main {

	public static void main(String[] args) {

	    //Logback configuration - package level grained
        ((Logger) LoggerFactory.getLogger("pl.woleszko")).setLevel(Level.DEBUG);
        ((Logger) LoggerFactory.getLogger("pl.woleszko.polsl.model.utils")).setLevel(Level.INFO);
//	    FileAccessor<TankMeasuresEntity> tankData;
//        tankData = new FileAccessorCSV<>(TankMeasuresEntity.class, "D:/Zestaw 1/tankMeasures.csv");
//        
//		TankDataExtractor extractor = new TankDataExtractor(tankData);
//		extractor.getVolumeChanges();
//		
//		HashMap<Long, HashMap<Long, Times>> list = extractor.getUsagePeriods();
//		HashMap<Long, Long> assignments = extractor.getNozzlesAssign();
//		HashMap<Integer, HashMap<Long, Double>> list = extractor.getHoursTrend();
//		
//		
////		Set<Long> keys = variances.keySet();
////		System.out.println();
////		for(Long key : keys) {System.out.println("Avg leakage per hour = " + extractor.getAvgPerHour(variances.get(key)));}
//		
////		HashMap<Long, Double> list = extractor.getVolumeTotals(Period.FULL_TIME);
////		
//		Integer counter = 0;
//		for(Long key : list.keySet()) {
//			System.out.println("Nozzle #" + key);
//			for(Long time : list.get(key).keySet())
//			System.out.println("Usage period: " + list.get(key).get(time).getFrom().toString() + " --> " + list.get(key).get(time).getTo().toString()+ " on nozzle #" + key);
//		}
//		
//		for(Long nozzle : assignments.keySet()) {
//			System.out.println("Nozzle #" + nozzle + " is attached to tank #" + assignments.get(nozzle));			
//		}
//			HashMap<Long, Double> tanksTotals = list.get(key);
//			// odczyt z tankow
//			for(Long tank : tanksTotals.keySet()) {
//				System.out.println("Tank " + tank + "avg = " +tanksTotals.get(tank));
//				
//			}
//		}
////		
////		System.out.println("counter: " + counter);
		
		AnomaliesDetector analise = new AnomaliesDetector("");
//		analise.detect();
//		analise.checkTank();
		analise.checkPipes();
		analise.checkNozzles();

//		analise.splitDates(Period.DAY);
//		analise.splitDates((long) 86400000);
		

	}


}
