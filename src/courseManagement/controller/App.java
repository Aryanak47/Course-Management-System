package courseManagement.controller;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import courseManagement.entities.*;
import courseManagement.entities.Module;
import courseManagement.utils.FileHandler;
import courseManagement.DataStore;
public class App {

	public static void main(String[] args) {
		// loading data
		DataStore.loadData();
		showMenu();
   

	}
	
	private  static  void showMenu() {
		try {
			User u = null;
			System.out.println("");
			System.out.println("************************************************************");
			System.out.println("***************************MENUS****************************");
			System.out.println("");
			System.out.println("************************************************************");
			System.out.println("| 1.Enter 1 to Register course                             |");
			System.out.println("| 2.Enter 2 to Login as Admin                              |");
			System.out.println("| 3.Enter 3 to Login as Student                            |");
			System.out.println("| 4.Enter 4 to Login as Teacher                            |");
			System.out.println("| 5.Enter 5 to Exit                                        |");
			System.out.println("************************************************************");
			System.out.println(" \n");
			System.out.print("Enter : ");
			Scanner sc = new Scanner(System.in);
			int selected = sc.nextInt();
			if ( selected == 1 ) {
				u = new Student().register();
				if ( u == null ) {
					System.out.println("\nRegistration failed !\n");
					return;
				}
				Student std =(Student) u;
				std.loadModules();
			}else if ( selected == 2 ){
				 u = DataStore.getAdmin();
			}else if ( selected == 3 ){
				displayTable(DataStore.getStudents());
				u = authenticate();	
			}else if ( selected == 4 ){
				displayTable(DataStore.getTeachers());
				u = authenticate();
			}else if ( selected == 5 ){
				return;
			}else {
				System.out.println("Invalid selection !");
				return;
			}
			if ( u == null ) {
				System.out.println("Login failed ! Please enter correct id.\n");
				return;
			}
			while ( !u.isLogout() ) {
				u.display();
			}
			u.setLogout(false);
			showMenu();
		}catch(InputMismatchException e) {
			System.out.println("Invalid input ,please enter number\n");
			showMenu();
		}
	}

	private static User authenticate() {
		Scanner sc = new Scanner(System.in);
		System.out.println("\nEnter your id number : ");
		int id = sc.nextInt();
		System.out.println("\n");
		List<User> users = DataStore.getUsers();
		User u = null;
		for ( User user : users ) {
			if ( user.getUserId() ==  id ) {
				u = user;
				break;
			}
		}
		return u;
	}
	private static void displayTable(List<? extends User> lists) {
		System.out.format("+-----------+----------------------+%n");
		System.out.format("|%-11s|%-22s|%n","ID","NAME");
		System.out.format("+-----------+----------------------+%n");
		for ( User u : lists ) {
			System.out.format("|%-11s|%-22s|%n", u.getUserId(),u.getName());		
		}
		System.out.format("+-----------+----------------------+%n");
	}

}
