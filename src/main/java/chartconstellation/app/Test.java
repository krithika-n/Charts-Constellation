package chartconstellation.app;

import java.util.ArrayList;
import java.util.List;

import chartconstellation.app.engine.ObjectMappingUtil;
import chartconstellation.app.entities.Chart;

public class Test {


	
	public static void main(String[] args) {
		ObjectMappingUtil util = new ObjectMappingUtil();
		List<Chart> list = new ArrayList<>();
		String path = "/Users/ripu/Documents/projects/Workspace_DV/chartconstellation/src/main/resources/Data_Images";
		util.convertToObject(path, Chart.class, list);
		System.out.println(list.size());

	}

}