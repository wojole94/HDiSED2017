package pl.woleszko.polsl.model;

import java.util.List;

public class FileAccessorCSV implements FileAccessor{
	List<List<String>> csvData;
	String path;
	
	FileAccessorCSV(){
		
	}
	
	FileAccessorCSV(String path){
	this.path = path;	
	}	
	
	@Override
	public void getFileData(List<List<String>> csvData) {
		this.csvData = csvData;
		
	}

	@Override
	public List<List<String>> getValues(Object type) {
		
		return csvData;
	}
	

}
