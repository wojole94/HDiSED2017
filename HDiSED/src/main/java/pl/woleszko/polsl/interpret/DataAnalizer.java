package pl.woleszko.polsl.interpret;

import java.util.HashMap;
import java.util.Set;

import pl.woleszko.polsl.maths.impl.NozzleDataExtractor;
import pl.woleszko.polsl.maths.impl.RefuelDataExtractor;
import pl.woleszko.polsl.maths.impl.TankDataExtractor;
import pl.woleszko.polsl.maths.objects.Period;

public class DataAnalizer {

	NozzleDataExtractor nozzles = new NozzleDataExtractor();
	RefuelDataExtractor refuels = new RefuelDataExtractor();
	TankDataExtractor tanks = new TankDataExtractor();
	
	public void detect() {
		HashMap<Long, Double> nozzleVolumes = nozzles.getVolumeTotals(Period.FULL_TIME);
		HashMap<Long, Double> refuelVolumes = refuels.getVolumeTotals(Period.FULL_TIME);
		HashMap<Long, Double> tankVolumes = tanks.getVolumeTotals(Period.FULL_TIME);
		
		Set<Long> keys = tankVolumes.keySet();
		
		HashMap<Long, Double> variances = new HashMap<Long, Double>();
		
		for(Long key : keys) {
			Double variance = new Double(0);
			variance = nozzleVolumes.get(key);
			variance -= tankVolumes.get(key);
			variance -= refuelVolumes.get(key);
			//variances.put(key, variance);
			System.out.println("Variance for tank nr "+ key.toString() +" = " + variance);
		}
		
		
		

		
	}
	
}
