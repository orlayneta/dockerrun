package com.demorestapi.repository;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import com.demorestapi.config.FakeMongo;
import com.docker.demorestapi.model.Student;
import com.docker.demorestapi.repository.StudentRepository;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;


@RunWith(SpringRunner.class)
@Import(value = { FakeMongo.class })
@EnableMongoRepositories(basePackageClasses = { StudentRepository.class })
public class StudentRepositoryTest {

	@SuppressWarnings("unused")
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private StudentRepository studentRepository;

	@Rule
	public MongoDbRule embeddedMongoDbRule = newMongoDbRule().defaultSpringMongoDb("mockDB");

	@Test
	@UsingDataSet(loadStrategy = LoadStrategyEnum.DELETE_ALL)
	public void noStudentsTest() {
		List<Student> students = studentRepository.findAll();
		assertTrue("Returned book list should be empty", students.isEmpty());
	}
	
	@Test
	@UsingDataSet(loadStrategy = LoadStrategyEnum.DELETE_ALL)
	public void saveStudentsTest() {
		int qt = 10;
		for (int i = 0; i < qt; i++) {
			studentRepository.save(new Student());		
		}
		List<Student> students = studentRepository.findAll();
		assertEquals(qt, students.size());
	}
	
	@Test
	@UsingDataSet(loadStrategy = LoadStrategyEnum.DELETE_ALL)
	public void findById() {
		studentRepository.save(new Student("student01"));
		List<Student> students = studentRepository.findAll();
		Optional<Student> st = studentRepository.findById(students.get(0).getId());
		assertTrue(st.isPresent());
		assertEquals("student01", st.get().getName());
	}

}