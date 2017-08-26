package pl.woleszko.polsl.model.impl;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.woleszko.polsl.model.entities.Entity;

@Slf4j
public class CSVHandler<T extends Entity> {
	private List<T> list;
	
	public void csvHandler(List<T> body) throws Exception {
		log.debug("||------------------------||");
		log.debug("||--List Initialization---||");
		log.debug("||------------------------||");
		this.list = body;
	}
	
	public List<T> getList(){
		if (list.equals(null)) {
		    throw new NullPointerException();
		}
		return list;
	}
}
