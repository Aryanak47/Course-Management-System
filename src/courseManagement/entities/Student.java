package courseManagement.entities;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import courseManagement.*;
import courseManagement.utils.FileHandler;

public class Student extends User{
	
	private int level;
	private int sem;
	private long courseId;
	private  List<Module> myModules = new ArrayList<>();
	private Map<Module, Float> marks =  new HashMap<Module, Float>();
	
	public Student() {
		super( null, null );
	}
	
	public Student ( String name, int sem , int level, long courseId ) {
		super( name, UserType.STUDENT );
		this.sem = sem;
		this.level = level;
		this.courseId = courseId;
	}
	
	@Override
	public void display() {
		System.out.println("");
		System.out.println("************************************************************");
		System.out.println("***************************STUDENT**************************");
		System.out.println("");
		System.out.println("************************************************************");
		System.out.println("| 1.Enter 1 to  Display Teachers                           |");
		System.out.println("| 2.Enter 2 to Display Modules                             |");
		System.out.println("| 3.Enter 3 to Proceed new level                           |");
		System.out.println("| 4.Enter 4 to logout                                      |");
		System.out.println("************************************************************");
		System.out.println("\nEnter  : ");
		try {
			Scanner scanner = new Scanner(System.in);
			int option = scanner.nextInt();
			switch(option) {
			case 1:
				displayTeachers();
				break;
			case 2:
				displayModules();
				break;
			case 3:
				proceedToNewLevel();
				break;
			default:
				setLogout(true);
			}
			
		}catch(InputMismatchException e) {
			System.out.println("Invalid input! Please enter number\n");
		}
	}

	private void displayTeachers() {
		System.out.println("\nTEACHERS :" );
		System.out.println("--------\n");
		System.out.format("+-----------+----------------------+----------------------------------------------------+%n");
		System.out.format("|%-11s|%-22s|%-52s|%n","ID","NAME","MODULE");
		System.out.format("+-----------+----------------------+----------------------------------------------------+%n");
		for ( Module module : myModules ) {
			for ( Teacher teacher : DataStore.getTeachers() ) {
				if ( teacher.getUserId() == module.getTeacherId() ) {
					System.out.format("|%-11s|%-22s|%-52s|%n", teacher.getUserId(),teacher.getName(),module.getName());
					break;
				}
			}
		}
		System.out.format("+-----------+----------------------+----------------------------------------------------+%n");
		
	}
	


	public List<Module> getMyModules() {
		return myModules;
	}

	private void displayModules() {
		System.out.println("\nMODULES :" );
		System.out.println("-------");
		System.out.format("+-----------+----------------------------------------------------+%n");
		System.out.format("|%-11s|%-52s|%n","ID","NAME");
		System.out.format("+-----------+----------------------------------------------------+%n");
		for ( Module module : myModules ) {
			System.out.format("|%-11s|%-52s|%n",module.getId(),module.getName());
			
		}
		System.out.format("+-----------+----------------------------------------------------+%n");
	}

	public long getCourseId() {
		return courseId;
	}
	public Course getCourse() {
		Course course = null;
		for ( Course c: DataStore.getCourses() ) {
			if ( courseId == c.getId() ) {
				course = c;
				break;
			}
		}
		return course;
	}

	public void setCourse(long courseId) {
		this.courseId = courseId;
	}
	
	public void loadModules() {
		myModules.clear();
		for ( Module module : DataStore.getModules() ) {
			if ( module.getCourseId() == courseId &&
					module.getLevel() == level && !module.isOption()) {
				myModules.add(module);
			}
		}
		if ( this.level == 6 ) {
			addFromFile();
		}
	}

	private void addFromFile() {
		List<String> data = FileHandler.readData("studentModules.txt");
			for ( String row : data ) {
				String[] info = row.split("\t\t");
				if ( this.getUserId() == Long.parseLong(info[0]) ) {
					long moduleId = Long.parseLong(info[1]);
					for ( Module module : DataStore.getModules() ) {
						if ( module.getId() == moduleId ) {
							this.getMyModules().add(module);
						}
					}
				}
			}
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSem() {
		return sem;
	}

	public void setSem(int sem) {
		this.sem = sem;
	}
	
	public  Student  register() {
		Student std = null;
		try {
			Scanner sc = new Scanner(System.in);		
			System.out.println("\nEnter your full name :");
			String userName = sc.nextLine();
			System.out.println("\nCourse :");
			System.out.println("------\n");
			boolean courseAvailable = false;
			System.out.println("\nCOURSE :" );
			System.out.println("-------");
			System.out.format("+-----------+-----------+%n");
			System.out.format("|%-11s|%-11s|%n","ID","NAME");
			System.out.format("+-----------+-----------+%n");
			for ( Course course : DataStore.getCourses() ) {
				if ( course.isActivate() ) {
					System.out.format("|%-11s|%-11s|%n",course.getId(),course.getName());
					courseAvailable = true;
				}
			}
			System.out.format("+-----------+-----------+%n");
			if (!courseAvailable) {
				System.out.println("\nThere is no any course available for now !");
				return null;
			}
			System.out.println("Enter course id :");
			long courseId  = sc.nextLong();
			System.out.println("\nLevel :");
			int level = sc.nextInt();
			Module m = null;
			if ( level == 6 ) {
				m = getModule(courseId);	
				
			}
			if ( level < 4 || level > 6 ) {
				level = 4;
			}
			System.out.println("\nSemester :");
			int sem = sc.nextInt();
			if ( sem < 1 || sem > 2 ) {
				sem = 1;
			}
			std = new Student( userName, sem, level, courseId );
			if (m != null ) {
				String d = std.getUserId()+"\t\t"+m.getId();
				FileHandler.writeData("studentModules.txt", d);
				std.getMyModules().add(m);
			}
			DataStore.getUsers().add(std);
			String data =std.getUserId()+"\t\t"+
						std.getName()+"\t\t"+
						std.getSem()+"\t\t"+
						std.getLevel()+"\t\t"+
						std.getCourseId();
			FileHandler.writeData("student.txt", data);
			
			System.out.println("\nCongratulations you are sucessfully registered ! Your id is "+std.getUserId()+"\n");
			return std;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
			
	}
	
	private  Module getModule(long courseId) {
		Module m = null;
		List<Module> optional = new ArrayList<>();
		for ( Module module: DataStore.getModules() ) {
			if ( module.getCourseId() == courseId &&
				 module.getLevel() == 6 &&
				 module.isOption() ) {
				
				System.out.println("\n"+module.getId()+"\t\t"+module.getName());
				optional.add(module);
			}
		}
		if ( optional.size() > 0 ) {
			System.out.print("\nChoose any one module.Enter number to select, for example enter 1 to select "+optional.get(0).getName()+" : ");
			 Scanner sc = new Scanner(System.in);
			int select =sc.nextInt();
			if ( select < 0 || select > optional.size() ) {
				m = optional.get(0);
				return m;
			}
			m = optional.get(select -1);
			System.out.println(m.getName());
		}
		return m;
	}
	
	public Map<Module, Float> getMarks() {
		return marks;
	}
	
	public void setMarks(Map<Module, Float> marks) {
		this.marks = marks;
	}
	
	public void proceedToNewLevel() {
		int half = myModules.size()/2;
		int totalPass = 0;
		for ( Module module: myModules ) {
			float mark = marks.getOrDefault( module, (float) 0.0 );		
			if ( mark >= 40 ) {
				totalPass++;
				
			}
		}
		// checking whether student has passed half of the modules for the current level or not
		if ( totalPass < half ) {
			System.out.println("\nSorry you cannot proceed to new level !\n");
			return;
		}
		if ( level < 6 ) {
			System.out.println("old level"+ this.level);
			this.level++;
			System.out.println("new level"+ this.level);
			this.sem = 1;
			myModules.clear();
			marks.clear();
			if ( level == 6 ) this.myModules.add(getModule(this.courseId));
			loadModules();
			System.out.println("\nCongratulations ! You are in level "+this.level+"\n");
			return;
		}
		System.out.println("\nYou are in level 6, which is last year of your course.\n");

	}
	

	
}