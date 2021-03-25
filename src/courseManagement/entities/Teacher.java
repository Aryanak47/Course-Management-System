package courseManagement.entities;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import courseManagement.*;
import courseManagement.*;

public class Teacher extends User{
	
	private String email;
	private List<Module> modules = new ArrayList<Module>();
	
	public Teacher ( String name, String email ) {
		super( name, UserType.INSTRUCTOR );
		this.setEmail(email);
	}

	private void loadModules() {
		for ( Module module : DataStore.getModules() ) {
			if ( module.getTeacherId() == this.getUserId() ) {
				this.modules.add(module);
			}
		} 
	}

	@Override
	public void display() {
		System.out.println("");
		System.out.println("************************************************************");
		System.out.println("***************************TEACHER**************************");
		System.out.println("");
		System.out.println("************************************************************");
		System.out.println("| 1.Enter 1 to Display Modules                             |");
		System.out.println("| 2.Enter 2 to Display Students                            |");
		System.out.println("| 3.Enter 3 to Give Marks                                  |");
		System.out.println("| 4.Enter 4 to logout                                      |");
		System.out.println("************************************************************");
		System.out.println("\nEnter  : ");
		try {
			Scanner scanner = new Scanner(System.in);
			int option = scanner.nextInt();
			switch(option) {
			case 1:
				viewModels();
				break;
			case 2:
				viewStudents();
				break;
			case 3:
				addMarks();
				break;
			default:
				setLogout(true);
			}
		}catch(InputMismatchException e) {
			System.out.println("Invalid input ,please enter number\n");
		}
	}

	public void viewModels() {
		System.out.println("\nMODULES :" );
		System.out.println("-------\n");
		System.out.format("+-----------+----------------------------------------------------+%n");
		System.out.format("|%-11s|%-52s|%n","ID","NAME");
		System.out.format("+-----------+----------------------------------------------------+%n");
		for ( Module module : DataStore.getModules() ) {
			if ( this.getUserId() == module.getTeacherId() ) {
				System.out.format("|%-11s|%-52s|%n",module.getId(),module.getName());
			}
		}
		System.out.format("+-----------+----------------------------------------------------+%n");
		
	}
	
	public void viewStudents() {
		print();
		for ( User user: DataStore.getUsers() ) {
			if ( user instanceof Student ) {
				Student std = (Student) user;
				showStudentInfo( std, new ArrayList<Module>() );
			}
		}
	}
	
	public void addMarks() {
		Scanner scanner = new Scanner(System.in);
		List<Module> modules = new ArrayList<>();
		System.out.println("Enter the student id");
		Student student = null;
		long id = scanner.nextLong();
		boolean found = false;
		for ( User user: DataStore.getUsers() ) {
			if ( user instanceof Student ) {
				Student std = (Student) user;
				if ( std.getUserId() == id ) {
					print();
					showStudentInfo( std, modules );
					if ( modules.size() == 0 ) {
						System.out.println("\nYou are not a teacher of the student whose id is "+id+".\n");
						return;
					}
					found = true;
					student = std;
					break;
				}	
			}
		}
		if ( !found ) {
			System.out.println("\nYou are not a teacher of the student whose id is "+id+".\n");
		}
		for ( Module m : modules ) {
			System.out.println("Enter marks for "+m.getName());
			float marks =  scanner.nextFloat();
			student.getMarks().put( m, marks );
			
		}	
	}
	
	private void print() {
		System.out.println("\nSTUDENTS :" );
		System.out.println("---------");
		System.out.format("+-----------+------------------------------+----------------------------------------------------+%n");
		System.out.format("|%-11s|%-30s|%-52s|%n","ID","NAME","MODULES");
		System.out.format("+-----------+------------------------------+----------------------------------------------------+%n");
		
	}
	
	private void showStudentInfo(Student std,List<Module> modules) {
		this.modules.clear();
		loadModules();
		List<Module> stdModules = std.getMyModules();
		for ( Module module: this.modules ) {
			if ( stdModules.contains(module) ) {
				modules.add(module);				
				System.out.format("|%-11s|%-30s|%-52s|%n",std.getUserId(),std.getName(),module.getName());
			}
		}
		System.out.format("+-----------+------------------------------+----------------------------------------------------+%n");
		
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public List<Module> getModules() {
		return modules;
	}
	
	
}