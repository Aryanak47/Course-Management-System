package courseManagement.entities;

public class Module{
	
	private static long count = 0;
	private  long id;
	private boolean option = false;
	private String name;
	private long teacherId;
	private long courseId;
	private int level;
	private int sem;
	static final float PASS_MARK = 40;
	static final float FULL_MASK = 100;
	
	public Module( String name, long courseId, int level, int sem, Boolean option ) {
		this.name = name;
		this.courseId = courseId;
		this.level = level;
		this.sem = sem;
		this.option = option;
		count++;
		id = count;
	}

	public  long getId() {
		return id;
	}
	public boolean isOption() {
		return option;
	}
	public void setOption(boolean option) {
		this.option = option;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTeacher(long teacher) {
		this.teacherId = teacher;
	}

	public long getTeacherId() {
		return teacherId;
	}

	public long getCourseId() {
		return courseId;
	}

	public int getLevel() {
		return level;
	}

	public int getSem() {
		return sem;
	}

	@Override
	public String toString() {
		return "Name = " + name ;
	}
	
}
