package courseManagement.entities;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import courseManagement.Addable;
import courseManagement.DataStore;
import courseManagement.utils.FileHandler;

public class Admin extends User implements Addable{
	
	private String email;
	
	public Admin (String name, String email ) {
		super ( name, UserType.ADMINISTRATOR );
		this.email = email;
	}

	@Override
	public void display() {
		System.out.println("");
		System.out.println("************************************************************");
		System.out.println("***************************ADMIN****************************");
		System.out.println("");
		System.out.println("************************************************************");
		System.out.println("| 1.Enter 1 to Display Student Results                     |");
		System.out.println("| 2.Enter 2 to Add Course                                  |");
		System.out.println("| 3.Enter 3 to Add Module                                  |");
		System.out.println("| 4.Enter 4 to Delete Course                               |");
		System.out.println("| 5.Enter 5 to Update Course name                          |");
		System.out.println("| 6.Enter 6 to Update Module name                          |");
		System.out.println("| 7.Enter 7 to Delete Module                               |");
		System.out.println("| 8.Enter 8 to Activate Course                             |");
		System.out.println("| 9.Enter 9 to Cancell Course                              |");
		System.out.println("| 10.Enter 10 to Display All Modules                       |");
		System.out.println("| 11.Enter 11 to Display All Courses                       |");
		System.out.println("| 12.Enter 12 to to add new  teacher                       |");
		System.out.println("| 13.Enter 13 to assign teacher to module                  |");
		System.out.println("| 14.Enter 14 to Logout                                    |");
		System.out.println("************************************************************");
		System.out.println("\nEnter  : ");
		try {
			Scanner scanner = new Scanner( System.in );
			int option = scanner.nextInt();
			long id;
			switch(option) {
			case 1:
				displayResult();
				break;
			case 2:
				addCourse();
				break;
			case 3:
				addModules();
				break;
			case 4:
				System.out.print("Enter course id : ");
				id = scanner.nextLong();
				deleteCourse( id );
				break;
			case 5:
				System.out.print("Enter course id : ");
				id = scanner.nextLong();
				System.out.print("\nEnter new name of course : ");
				scanner.nextLine();
				String name = scanner.nextLine();
				updateCourseName( name, id );
				break;
			case 6:
				System.out.print("Enter module id : ");
				id = scanner.nextLong();
				System.out.print("\nEnter new name of module : ");
				scanner.nextLine();
				String m = scanner.nextLine();
				updateModuleName( m, id );
				break;
			case 7:
				System.out.print("Enter module id : ");
				id = scanner.nextLong();
				deleteModule( id );
				break;
			case 8:
				System.out.print("Enter course id : ");
				id = scanner.nextLong();
				reactivateOrCancellCourse( id, true );
				break;
			case 9:
				System.out.print("Enter course id : ");
				id = scanner.nextLong();
				reactivateOrCancellCourse( id, false );
				break;
			case 10:
				viewAllModules();
				break;
			case 11:
				viewCourses();
				break;
			case 12:
				addTeacher();
				break;
			case 13:
				assignModuleToTeacher();
				break;
			default:
				setLogout(true);
			}
		}catch(InputMismatchException e) {
			System.out.println("Invalid input ,please enter number\n");
		}
	}
	
	private void addTeacher() {
		System.out.print("Enter teacher name : ");
		Scanner sc = new Scanner(System.in);
		String name = sc.nextLine();
		System.out.print("\nEnter teacher email : ");
		String email = sc.nextLine();
		Teacher teacher = new Teacher(name, email);
		String data = teacher.getUserId()+"\t\t"+teacher.getName()+"\t\t"+teacher.getEmail();
		FileHandler.writeData("teacher.txt", data);
		DataStore.getUsers().add(teacher);
		System.out.println("\nSucessfull added ! Teacher id is "+teacher.getUserId());
	}

	public void displayResult() {
		Scanner sc = new Scanner( System.in );
		System.out.print("Enter student id : ");
		Student std = null;
		long id = sc.nextLong();
		for ( User u : DataStore.getUsers() ) {
			if ( u instanceof Student ) {
				Student student = ( Student ) u;
				if ( student.getUserId() == id ) {
					std = student;
					break;
				}
			}
		}
		if ( std == null ) {
			System.out.println("\nThere is no any student with this id "+id+"\n");
			return;
		}
		Map<Module,Float> marks = std.getMarks();
		if ( marks.size() == 0 ) {
			System.out.println("\nTeachers have not given marks to the student whose  id is "+id+"\n");
			return;
		}
		float sum = 0;
		System.out.println("\n\nStudent Name : "+std.getName());
		System.out.println("\nLevel : "+std.getLevel());
		System.out.println("\nid :"+ std.getUserId());
		System.out.println("\nCourse id : "+std.getCourseId()+"\n");
		
		System.out.format("+----------------------------------------------------+-----------+-----------+-----------+-----------+%n");
		System.out.format("|%-52s|%-11s|%-11s|%-11s|%-11s|%n","Modules","Full marks","Pass marks","Marks","Grade");
		System.out.format("+----------------------------------------------------+-----------+-----------+-----------+-----------+%n");
	
		String grade;
		for ( Module module : marks.keySet() ) {
			float mark =  marks.get(module);
			sum += mark;
			grade = getGrade(mark);
			System.out.format("|%-52s|%-11s|%-11s|%-11s|%-11s|%n",
							  module.getName(),Module.FULL_MASK,Module.PASS_MARK,mark,grade);
		}
		System.out.format("+----------------------------------------------------+-----------+-----------+-----------+-----------+%n");
		float per = sum / (Module.FULL_MASK*marks.size()) * 100;
		grade = getGrade( per );
		System.out.format("+--------------+%n");
		System.out.format("|%-14s|%n","Grade : "+grade);
		System.out.format("+--------------+%n");
		System.out.format("+--------------------+%n");
		System.out.format("|%-20s|%n","Percentage : "+String.format( "%.2f", per )+"%");
		System.out.format("+--------------------+%n");
	}

	private String getGrade(float mark) {
		String grade;
		if ( mark >= 90 ){
			grade = "A";
		}
		else if ( mark >= 80 ){
			grade = "B";
		}
		else if ( mark >= 70 ){
			grade = "C";
		}
		else if ( mark >= 60 ){
			grade = "D";
		}
		else if ( mark >= 40 ){
			grade = "E";
		}
		else {
			grade = "fail";
		} 
		return grade;
		
	}
	
	@Override
	public void addCourse() {
		String courseName;
		Scanner sc = new Scanner( System.in );
		System.out.println("Enter the  course name : ");
		courseName = sc.nextLine();
		Course course = new Course( courseName, true );
		String courseData = course.getName()+"\t\t"+course.isActivate();
		int i = 0;
		int moduleLimit = 8;
		while ( i < 3 ) {
			// add only four modules in level 6
			if ( i == 2 ) {
				moduleLimit = 4;
			}
			// loop for adding modules in different semester
			for ( int j = 0 ; j < moduleLimit ; j++ ) {
				try {
					int sem = 1;
					if ( j >= 4 ) {
						sem = 2;
					}
					String msg = i != 2 ? "semester: "+sem :"";
					System.out.println("\nEnter the modules number "+(j+1)+ " for level: "+(i+4)+" "+msg+"\n");
					String moduleName = sc.nextLine();
					boolean option = false;
//					in level 6 asking for optinal modules
					if( i == 2 ) {
						System.out.println("\nIs it optional module ?(1 for yes, 0 for no)\n");
						int o = sc.nextInt();
						if ( o == 1 ) {
							option = true;
						}
					}
					Module module = new Module( moduleName, course.getId(), (i+4), sem, option );
					String moduleData = module.getName()+"\t\t"+
										course.getId()+"\t\t"+
										module.getLevel()+"\t\t"+
										module.getSem()+"\t\t"+
										module.isOption();
					displayTeacher();
					System.out.print("Enter teacher id :");
					long id = sc.nextLong();
					assignModuleToTeacher( module, id );
					DataStore.getModules().add(module);
					FileHandler.writeData("modules.txt", moduleData);
				}catch(InputMismatchException e) {
					System.out.println("Please enter (1 for yes, 0 for no)\n");
					j--;
				}
				sc.nextLine();
			}
			i++;
		}
		DataStore.getCourses().add(course);
		FileHandler.writeData("course.txt", courseData);
	}
	
	private void displayTeacher() {
		System.out.println("\nTEACHERS :" );
		System.out.println("--------\n");
		System.out.format("+-----------+----------------------+%n");
		System.out.format("|%-11s|%-22s|%n","ID","NAME");
		System.out.format("+-----------+----------------------+%n");
		for ( Teacher teacher : DataStore.getTeachers() ) {
			System.out.format("|%-11s|%-22s|%n",teacher.getUserId(), teacher.getName());
		}
		System.out.format("+-----------+----------------------+%n");
	}

	public void reactivateOrCancellCourse(long courseId,Boolean activate) {
		boolean found = false;
		for ( Course c : DataStore.getCourses() ) {
			if ( c.getId() == courseId ) {
				found = true;
				c.setActivate(activate);
				System.out.println("\nSucessfully updated ! ");
				break;
			}
		}
		if(!found) {
			System.out.println("There is no course with this id : "+ courseId);	
			return;
		}
		// update in file
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir")+"\\src\\course.txt"), "UTF-8"))){
			for (  Course co : DataStore.getCourses() ) {
				writer.write(co.getName()+"\t\t"+co.isActivate()+"\n");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void assignModuleToTeacher(Module module,long teacherId) {
		module.setTeacher(teacherId);
		
	}
	public void assignModuleToTeacher() {
		System.out.print("Enter module id : ");
		Scanner sc = new Scanner(System.in);
		long moduleId = sc.nextLong();
		displayTeacher();
		System.out.print("Enter teacher id : ");
		long teacherId = sc.nextLong();
		for ( Module module : DataStore.getModules() ) {
			if ( module.getId() == moduleId ) {
				module.setTeacher(teacherId);
				System.out.println("\nSucessfully assigned !");
				return;
	
			}
		}
		System.out.println("\nModule not found with this id "+moduleId);
		
		
		
	}
	
	@Override
	public void addModules() {
		try {
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter the module name : ");
			String name = sc.nextLine();
			System.out.println("\nIs it optional module ?(1 for yes, 0 for no)\n");
			int o = sc.nextInt();
			boolean option = false;
			if ( o == 1 ) {
				option = true;
			}
			System.out.println("\nEnter Level : ");
			int level = sc.nextInt();
			System.out.println("\nEnter semester : ");
			int sem = sc.nextInt();
			System.out.println("\nEnter course id : ");
			int course = sc.nextInt();
			for ( Course c : DataStore.getCourses() ) {
				if ( c.getId() == course ){
					Module module = new Module( name, course, level, sem, option );
					String moduleData = module.getName()+"\t\t"+
							c.getId()+"\t\t"+
							module.getLevel()+"\t\t"+
							module.getSem()+"\t\t"+
							module.isOption();
					displayTeacher();
					System.out.print("Enter teacher id :");
					long id = sc.nextLong();
					assignModuleToTeacher( module, id );
					DataStore.getModules().add(module);
					FileHandler.writeData("modules.txt", moduleData);
					return;
				}
			}
			System.out.println("\nThere is no any course with this id :  "+ course);
		}catch(InputMismatchException e) {
			System.out.println("\nInvalid input ,please enter number\n");
			addModules();
		}
	}
	
	public void deleteCourse(long id) {
		boolean found = false;
		for ( Course c : DataStore.getCourses() ) {
			if ( c.getId() == id ) {
				found = true;
				DataStore.getCourses().remove(c);
				System.out.println("\nSucessfully deleted course : "+ c.getId());
				break;
			}
			
		}
		if (!found) {
			System.out.println("\nThere is no course with this id : "+ id);	
			return;
		}
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir")+"\\src\\course.txt"), "UTF-8"))){
			for(Course m : DataStore.getCourses()) {
				writer.write(m.getName()+
							"\t\t"+m.isActivate()+"\n");
			}
//			delete all students who are registered in deleted course
			deleteStudents(id);
//			delete all modules of deleted course
			deleteModules(id);
			
			
			

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void deleteModules(long id) {
		List<Module> modulesToRemove = new ArrayList<>();
		boolean found = false;
		for ( Module m : DataStore.getModules()  ) {
			if ( m.getCourseId() == id ) {
				found = true;
				modulesToRemove.add(m);
			}
		}
		if(!found) {
			System.out.println("\nThere is no modules with this id : "+ id);	
			return;
		}
		DataStore.getModules().removeAll(modulesToRemove);
		try( BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(System.getProperty("user.dir")+"\\src\\modules.txt"), "UTF-8" ))){
			for ( Module m : DataStore.getModules() ) {
				writer.write(m.getName()+
						"\t\t"+m.getCourseId()+
						"\t\t"+m.getLevel()+
						"\t\t"+m.getSem()+
						"\t\t"+m.isOption()+"\n");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void deleteStudents(long id) {
		boolean found = false;
		List<User> stdToRemove = new ArrayList<>();
		for ( User user : DataStore.getUsers()  ) {
			if ( user instanceof Student ) {
				Student std = (Student) user;
				if ( std.getCourseId() == id ) {
					found = true;
					stdToRemove.add(std);
				}
			} 
		}
		if(!found) {
			System.out.println("\nThere is no students with this id : "+ id);	
			return;
		}
		DataStore.getUsers().removeAll(stdToRemove);
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir")+"\\src\\student.txt"), "UTF-8"))){
			for ( Student s : DataStore.getStudents() ) {
				writer.write(s.getName()+
						"\t\t"+s.getSem()+
						"\t\t"+s.getLevel()+
						"\t\t"+s.getCourseId()+"\n"
						);	
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	

	public void updateCourseName(String name,long id) {
		boolean found = false;
		for ( Course c : DataStore.getCourses() ) {
			if ( c.getId() == id ) {
				found = true;
				System.out.println("\nOld name : "+c.getName());
				c.setName(name);
				System.out.println("\nNew name : "+c.getName());
				System.out.println("\nSucessfully updated ! ");
				break;
			}
		}
		if(!found) {
			System.out.println("There is no course with this id : "+ id);	
			return;
		}
		// update in file
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir")+"\\src\\course.txt"), "UTF-8"))){
			for (  Course co : DataStore.getCourses() ) {
				writer.write(co.getName()+"\t\t"+co.isActivate()+"\n");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void viewAllModules(){
		System.out.println("\nMODULES :" );
		System.out.println("-------\n");
		System.out.format("+-----------+----------------------------------------------------+%n");
		System.out.format("|%-11s|%-52s|%n","ID","NAME");
		System.out.format("+-----------+----------------------------------------------------+%n");
		for ( Module module : DataStore.getModules() ) {
			System.out.format("|%-11s|%-52s|%n",module.getId(),module.getName());
		}
		System.out.format("+-----------+----------------------------------------------------+%n");
	}
	
	public void updateModuleName(String name,long id) {
		boolean found = false;
		for ( Module m : DataStore.getModules() ) {
			if ( m.getId() == id ) {
				System.out.println("\nOld name : "+m.getName());
				found = true;
				m.setName(name);
				System.out.println("\nNew name : "+m.getName()+"");
				System.out.println("\nSucessfully updated ! ");
				break;
			}
		}
		if(!found) {
			System.out.println("\nThere is no module with this id : "+ id);
			return;
		}	
		// update in file
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir")+"\\src\\modules.txt"), "UTF-8"))){
			for (  Module m : DataStore.getModules() ) {
				writer.write(m.getName()+
						"\t\t"+m.getCourseId()+
						"\t\t"+m.getLevel()+
						"\t\t"+m.getSem()+
						"\t\t"+m.isOption()+"\n");
				
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteModule(long id) {
		boolean found = false;
		for ( Module m : DataStore.getModules() ) {
			if ( m.getId() == id ) {
				found = true;
				DataStore.getModules().remove(m);
				System.out.println("\nSucessfully deleted ! \n");
				break;
			}
			
		}
		if (!found) {
			System.out.println("\nThere is no module with this id : "+ id);	
			return;
		}
		try( BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(System.getProperty("user.dir")+"\\src\\modules.txt"), "UTF-8" ))){
			for ( Module m : DataStore.getModules() ) {
				writer.write(m.getName()+
						"\t\t"+m.getCourseId()+
						"\t\t"+m.getLevel()+
						"\t\t"+m.getSem()+
						"\t\t"+m.isOption()+"\n");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void viewCourses() {
		System.out.println("\nCOURSE :" );
		System.out.println("-------");
		System.out.format("+-----------+-----------+%n");
		System.out.format("|%-11s|%-11s|%n","ID","NAME");
		System.out.format("+-----------+-----------+%n");
		for ( Course c : DataStore.getCourses() ) {
			if ( c.isActivate() ) {
				System.out.format("|%-11s|%-11s|%n",c.getId(),c.getName());
			}
		}
		System.out.format("+-----------+-----------+%n");
	}

	public String getEmail() {
		return email;
	}
	
}