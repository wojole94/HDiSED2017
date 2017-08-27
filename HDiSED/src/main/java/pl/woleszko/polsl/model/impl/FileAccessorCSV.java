package pl.woleszko.polsl.model.impl;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.main.Main;

import lombok.extern.slf4j.Slf4j;
import pl.woleszko.polsl.model.entities.Entity;

@Slf4j
public class FileAccessorCSV<T extends Entity> implements FileAccessor<T> {

	private Main mainContext;
	private CSVHandler<T> csvHandler;
	private String directoryName;
	private String fileName;

	public FileAccessorCSV(Class<T> type, String fileName) {

	    File file = new File(fileName);

	    //try to find in resources if there is no in default path
	    file = file.exists() ? file : getFileFromResources(fileName);
	    
        csvHandler = new CSVHandler<>();
        
        this.fileName = file.getName();
        this.directoryName = file.getParentFile().getAbsolutePath();
        configure(type);	    
	}

    @Override
	public List<T> getValues() {
        return csvHandler.getList();
	}

    @Override
    public void configure(Class<T> type) {

        //  log.debug("||--------------------||");
        //	log.debug("||---Configuring...---||");
        //	log.debug("||--------------------||");

        // create a Main instance
        mainContext = new Main();
        
        // bind MyBean into the registry
        mainContext.bind("csvHandlerBean", csvHandler);
        mainContext.bind("terminate", this);
        
        // add routes
        mainContext.addRouteBuilder(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                BindyCsvDataFormat bindy = new BindyCsvDataFormat(type);

                from("file:" + directoryName + "?fileName=" + fileName + "&consumer.delay=1000&noop=true")
                    .unmarshal(bindy)
		            .to("bean:csvHandlerBean?method=csvHandler")
		            .to("bean:terminate?method=close");
            }

        });

        //	// add event listener
        //	mainContext.addMainListener(new Events());
        // run until you terminate the JVM
        System.out.println("Starting Camel. Use ctrl + c to terminate the JVM.\n");
        try {

            mainContext.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    //	log.debug("||------------------------||");
    //	log.debug("||---Context started...---||");
    //	log.debug("||------------------------||");
    }

	public void close() {
		mainContext.completed();
	}

	/** Tries to load file from resources
	 * @param fileName
	 * @return
	 */
	private File getFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(fileName);
        
        if (url == null) {
            log.error("File {} does not exists!", fileName);
        }
        
        return new File(url.getFile());
	}
}
