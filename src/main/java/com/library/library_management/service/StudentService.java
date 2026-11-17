
package com.library.library_management.service;

import com.library.library_management.entity.Student;
import com.library.library_management.exception.DuplicateResourceException;
import com.library.library_management.exception.ResourceNotFoundException;
import com.library.library_management.repository.StudentRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student registerStudent(Student student) {

        if (studentRepository.findByEmail(student.getEmail())
                .isPresent()) {
            throw new DuplicateResourceException(
                "Email already registered"
            );
        }

        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Student not found: " + id
                )
            );
    }

    public Student updateStudent(Long id, Student updatedStudent) {

        Student student = getStudentById(id);

        studentRepository.findByEmail(updatedStudent.getEmail())
            .filter(existing ->
                !existing.getStudentId().equals(id))
            .ifPresent(existing -> {
                throw new DuplicateResourceException(
                    "Email already registered"
                );
            });

        student.setStudentName(updatedStudent.getStudentName());
        student.setEmail(updatedStudent.getEmail());
        student.setDepartment(updatedStudent.getDepartment());
        student.setPhoneNumber(updatedStudent.getPhoneNumber());

        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }
}