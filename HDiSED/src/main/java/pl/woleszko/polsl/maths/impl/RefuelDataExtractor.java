package pl.woleszko.polsl.maths.impl;

import java.util.ArrayList;
import java.util.HashMap;

import pl.woleszko.polsl.model.entities.Entity;
import pl.woleszko.polsl.model.entities.RefuelEntity;
import pl.woleszko.polsl.model.entities.TankMeasuresEntity;

public class RefuelDataExtractor extends DataExtractor {
	ArrayList<RefuelEntity> entities = new ArrayList<RefuelEntity>();

	RefuelDataExtractor() {
		ArrayList<Entity> list = getEntities("refuel.csv");
		for (Entity ent : list) {
			entities.add((RefuelEntity) ent);
		}
	}

	@Override
	public HashMap<Long, Double> getWeeklyTotals() {
		return null;
	}
}