package courseManagement.entities;

import java.util.ArrayList;
import java.util.List;

import courseManagement.DataStore;

public class Course{
	
	private static long count = 200;
	private  long id;
	private String name;
	private boolean activate;
	List<Module> modules = new ArrayList<>();
	
	public Course ( String name, boolean activate ) {
		this.setName(name);
		this.activate = activate;
		count++;
		id = count;
	}
	
	public boolean isActivate() {
		return activate;
	}
	
	public void setActivate(boolean activate) {
		this.activate = activate;
	}
	
	public  long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	public List<Module> getModules() {
		for ( Module module : DataStore.getModules() ) {
			if ( module.getCourseId() == this.id) {
				modules.add(module);
			}
		}
		return modules;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}