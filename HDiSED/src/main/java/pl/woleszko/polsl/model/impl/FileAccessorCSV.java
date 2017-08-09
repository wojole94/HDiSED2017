package pl.woleszko.polsl.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Main;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.spi.DataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.woleszko.polsl.model.entities.Entity;
import pl.woleszko.polsl.model.entities.NozzleMeasuresEntity;
import pl.woleszko.polsl.model.entities.RefuelEntity;
import pl.woleszko.polsl.model.entities.TankMeasuresEntity;


public class FileAccessorCSV implements FileAccessor {
	
	private Main mainContext;


	private CSVHandler csvHandler;
	private String fileName;
	private Logger log = LoggerFactory.getLogger(FileAccessorCSV.class);
	
	public FileAccessorCSV(String fileName){
		csvHandler = new CSVHandler();
		this.fileName = fileName;
		configure();
	}
	
	@Override
	public ArrayList<Entity> getValues() {
		
		return csvHandler.getList();
		
	}

	@Override
	public void configure() {
		
		log.debug("||--------------------||");
		log.debug("||---Configuring...---||");
		log.debug("||--------------------||");
		
		// create a Main instance
		mainContext = new Main();
		// bind MyBean into the registry
		mainContext.bind("csvHandlerBean", csvHandler);
		// add routes
		mainContext.addRouteBuilder(new RouteBuilder() {

			@Override
			public void configure() throws Exception {

				BindyCsvDataFormat bindy = null;
				
				if (fileName.equals("nozzleMeasures.csv"))
				bindy = new BindyCsvDataFormat(NozzleMeasuresEntity.class);
				if (fileName.equals("refuel.csv"))
				bindy = new BindyCsvDataFormat(RefuelEntity.class);
				if (fileName.equals("tankMeasures.csv"))
				bindy = new BindyCsvDataFormat(TankMeasuresEntity.class);
				
				from("file:D:/?fileName="+fileName+"&noop=true")						
						.unmarshal(bindy)
						.log("pol route'a")
						.log("za procesem")
						.to("bean:csvHandlerBean?method=csvHandler");

			}

		});	
	
//	// add event listener
//	mainContext.addMainListener(new Events());
	// run until you terminate the JVM
	System.out.println("Starting Camel. Use ctrl + c to terminate the JVM.\n");
	try {
		mainContext.start();	
		mainContext.run();

	
	} catch (Exception e) {
		e.printStackTrace();
	}
	log.debug("||------------------------||");
	log.debug("||---Context started...---||");
	log.debug("||------------------------||");
	}
	
	public void close() {
		try {
			mainContext.stop();
			mainContext.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
