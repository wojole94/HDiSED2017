package pl.polsl.woleszko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import pl.woleszko.polsl.interpret.DataAnalizer;
import pl.woleszko.polsl.maths.impl.DataExtractor;
//import pl.woleszko.polsl.interpret.LeakageDetector;
import pl.woleszko.polsl.maths.impl.NozzleDataExtractor;
import pl.woleszko.polsl.maths.impl.RefuelDataExtractor;
import pl.woleszko.polsl.maths.impl.TankDataExtractor;
import pl.woleszko.polsl.maths.objects.Period;
import pl.woleszko.polsl.maths.objects.Times;
import pl.woleszko.polsl.model.entities.Entity;
import pl.woleszko.polsl.model.entities.NozzleMeasuresEntity;
import pl.woleszko.polsl.model.impl.FileAccessorCSV;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DataAnalizer extractor = new DataAnalizer();
		extractor.detect();
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
		
//		DataAnalizer analise = new DataAnalizer();
//		analise.checkTank();
//				analise.detect();
//		analise.splitDates(Period.DAY);
//		analise.splitDates((long) 86400000);
//		analise.checkTank((long) 3);

	}


}
