package pl.woleszko.polsl.maths.impl;

import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pl.woleszko.polsl.maths.objects.Times;
import pl.woleszko.polsl.model.entities.RefuelEntity;
import pl.woleszko.polsl.model.impl.FileAccessor;

public class RefuelDataExtractor extends DataExtractor<RefuelEntity> {

    public RefuelDataExtractor(FileAccessor<RefuelEntity> accessor) {
        super(accessor);
	}

	@Override
	public Map<Integer, Double> getVolumeTotals() {
		
		 Map<Integer, List<RefuelEntity>> splitedByTankID = list.stream().collect(Collectors.groupingBy(RefuelEntity::getTankId));
			
		 
		 Map<Integer, Double> totals = splitedByTankID.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> {
			 DoubleSummaryStatistics sum = entry.getValue().stream().collect(Collectors.summarizingDouble(entity -> entity.getFuelVol()));
			 return sum.getSum();
			 }
		 ));	
		
		 
		return totals;

	}
	
	public HashMap<Times, Integer> getRefuelPeriods(){
		
		HashMap<Times, Integer> result = new HashMap<Times, Integer>();

		for(RefuelEntity entity : list) {
			Date startDate = new Date();
			Date endDate = new Date();
			startDate = entity.getDate();
			
			Double equals =  (entity.getFuelVol() / (entity.getSpeed() / 60000));
			Long duration = equals.longValue();
			
			duration++;
			endDate.setTime(startDate.getTime() + duration);
			
			Times period = new Times(startDate, endDate);
			result.put(period, entity.getTankId());
		}
		return result;
	}
}