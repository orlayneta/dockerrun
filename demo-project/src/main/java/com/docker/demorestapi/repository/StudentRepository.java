package com.docker.demorestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.docker.demorestapi.model.Student;

public interface StudentRepository extends MongoRepository<Student, String> {


}
