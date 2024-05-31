package com.docker.demorestapi.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.docker.demorestapi.model.Student;
import com.docker.demorestapi.repository.StudentRepository;

@RestController
@RequestMapping("/student")
public class StudentController {

    Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    StudentRepository studentRepository;

    @GetMapping
    public List<Student> student() {
        logger.info("FIND ALL");

        return studentRepository.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Student create(@RequestBody Student student) {
        logger.info("SAVE STUDENT");
        return studentRepository.save(student);
    }

    @GetMapping(value = "/{id}")
    public Optional<Student> read(@PathVariable String id) {
        logger.info("GET STUDENT BY ID");
        return studentRepository.findById(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody Student student) {
        logger.info("UPDATE STUDENT");
        studentRepository.save(student);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable String id) {
        logger.info("DELETE STUDENT");
        Student st = new Student();
        st.setId(id);
        studentRepository.delete(st);
    }
}
