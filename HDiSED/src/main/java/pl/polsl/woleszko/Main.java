package pl.polsl.woleszko;

import java.util.ArrayList;
import java.util.List;

import pl.woleszko.polsl.model.entities.Entity;
import pl.woleszko.polsl.model.entities.NozzleMeasuresEntity;
import pl.woleszko.polsl.model.impl.FileAccessorCSV;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileAccessorCSV access = new FileAccessorCSV("nozzleMeasures.csv");
		ArrayList<Entity> list = access.getValues();
		for(Entity ent : list) {
			System.out.println(ent.toString());
		}
		access.close();
	}


}
