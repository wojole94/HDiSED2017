package pl.woleszko.polsl.model.impl;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.woleszko.polsl.model.entities.Entity;


public class CSVHandler {
	private ArrayList<Entity> list;
	
	public void csvHandler(ArrayList<Entity> body) throws Exception {
		
		Logger log = LoggerFactory.getLogger(CSVHandler.class);
		log.debug("||------------------------||");
		log.debug("||--List Initialization---||");
		log.debug("||------------------------||");

		String output = new String();
		
		for (Entity entity : body) {
			output += entity.toString() + "\n";
			log.debug(entity.toString());
		}

	}
	
	public ArrayList<Entity> getList(){
		if (list.equals(null)) throw new NullPointerException();
		return list;
	}
}
