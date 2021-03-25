package courseManagement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import courseManagement.entities.Admin;
import courseManagement.entities.Course;
import courseManagement.entities.Student;
import courseManagement.entities.Teacher;
import courseManagement.entities.User;
import courseManagement.utils.FileHandler;
import courseManagement.entities.Module;


public class DataStore {
	
	private static List<User> users = new ArrayList<User>();
	private static List<Teacher> teachers = new ArrayList<>();
	private static List<Student> students = new ArrayList<>();
	private static List<Course> courses = new ArrayList<>();
	private static List<Module> modules = new ArrayList<Module>();

	public static void loadData() {
		// loading data
		addTeacher();
		addAdmin();
		getTeachers();
		loadCourses();
		loadModules();
		addStudent();
	}
	
	private static void addStudent() {
		List<String> data = FileHandler.readData("student.txt");
		for ( String row : data ) {
			String info[] = row.split("\t\t");
			Student std =  new Student( info[1], Integer.parseInt(info[2]), Integer.parseInt(info[3]), Integer.parseInt(info[4]) );
			std.setUserId(Long.parseLong(info[0]));
			std.loadModules();
			users.add(std);
		}
	}
	
	

	private static void loadModules() {
		List<String> data = FileHandler.readData("modules.txt");
		for ( String row :data ) {
			String info[] = row.split("\t\t");
			Module module = new Module( info[0], Long.parseLong(info[1]), Integer.parseInt(info[2]), Integer.parseInt(info[3]), Boolean.parseBoolean(info[4] ));
			Admin admin = getAdmin();
			if ( getTeachers().size() > 0 ) {
				long teacherId = getTeacherId();
				admin.assignModuleToTeacher( module, teacherId );
			}
			modules.add(module);
		}
	}

	private static long getTeacherId() {
		int total = getTeachers().size();
		int random = (int) (Math.random()*total);
		Teacher teacher = getTeachers().get(random);
		return teacher.getUserId();
	}

	private static void loadCourses() {
		List<String> data = FileHandler.readData("course.txt");
		for ( String row : data ) {
			String info[] = row.split("\t\t");
			courses.add( new Course( info[0], Boolean.parseBoolean( info[1] )));
		}         
	}

	private static void addTeacher() {
		List<String> data = FileHandler.readData("teacher.txt");
		for ( String row : data ) {
			String info[] = row.split("\t\t");
			Teacher teacher = new Teacher( info[1], info[2] );
			teacher.setUserId(Long.parseLong(info[0]));
			users.add(teacher);
		}	
	}
	
	private static void addAdmin() {
		Admin admin = new Admin( "Aryan Bimali", "aryanbimali34@gmail.com" );
		users.add(admin);
		
	}
	
	public static Admin getAdmin() {
		Admin admin = null;
		for ( User user : getUsers() ) {
			if ( user instanceof Admin ) {
				admin = (Admin) user;
				break;
			}
		}
		return admin;
	}
	
	public static  List<User> getUsers() {
		return users;
	}

	public static List<Course> getCourses() {
		return courses;
	}

	public static List<Module> getModules() {
		return modules;
	}
	
	public static List<Teacher> getTeachers() {
		teachers.clear();
		for ( User user : getUsers() ) {
			if (user instanceof Teacher) {
				Teacher teacher = (Teacher) user;
				teachers.add(teacher);
			}
		}
		return teachers;
	}

	public static List<Student> getStudents() {
		students.clear();
		for ( User user : users  ) {
			if ( user instanceof Student ) {
				students.add((Student) user);
			} 
		}
		return students;
	}

}
