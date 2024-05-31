package resources;

import com.docker.demorestapi.model.Student;

public class TestDatabuild {

    public Student addStudentPayload(String name) {
	Student student = new Student();
	student.setName(name);
	return student;
    }

}