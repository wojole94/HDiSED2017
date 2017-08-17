package pl.polsl.woleszko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import pl.woleszko.polsl.interpret.DataAnalizer;
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
		
//		HashMap<Long, Times> list = 
		HashMap<Long, Double> variances = extractor.detect();
//		Set<Long> keys = variances.keySet();
//		System.out.println();
//		for(Long key : keys) {System.out.println("Avg leakage per hour = " + extractor.getAvgPerHour(variances.get(key)));}
		
//		HashMap<Long, Double> list = extractor.getVolumeTotals(Period.FULL_TIME);
//		
//		Integer counter = 0;
//		for(Long key : list.keySet()) {
//			counter++;
//			System.out.println(list.get(key));
//		}
//		
//		System.out.println("counter: " + counter);
	}


}
