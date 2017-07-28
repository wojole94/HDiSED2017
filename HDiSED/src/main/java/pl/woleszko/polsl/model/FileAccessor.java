package pl.woleszko.polsl.model;

import java.util.List;

public interface FileAccessor {
	public void getFileData(List<List<String>> csvData);
	public List<List<String>> getValues(Object type);
	
}
