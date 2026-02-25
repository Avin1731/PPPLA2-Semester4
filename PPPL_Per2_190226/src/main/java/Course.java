import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseCode;
    private String courseName;
    private List<Student>students;

    public Course(String courseCode, String courseName){
        this.courseCode=courseCode;
        this.courseName=courseName;
        this.students=new ArrayList<>();
    }
    public void enrollStudent(Student student){
        if (!students.contains(student)){
            students.add(student);
        }
    }
    public void unenrollStudent(Student student){
        students.remove(student);
    }
    public int getStudentCount(){
        return students.size();
    }
}
