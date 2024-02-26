package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.Students;
import com.example.demo.models.StudentsRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Controller
public class StudentController {

    @Autowired
    private StudentsRepository studentsRepo;

    @GetMapping("/students")
    public String getAllStudents(Model model) {
        System.out.println("Getting all students");
        List<Students> students = studentsRepo.findAll();
        model.addAttribute("students", students);
        return "users/displayStudent";
    }

    @GetMapping("/")
    public RedirectView process(){
        return new RedirectView("/students");
    }

    @GetMapping("/students/add")
    public String showAddStudentForm() {
        return "users/addStudent";
    }

    @PostMapping("/students/add")
    public String addStudent(@RequestParam Map<String, String> newstudent, HttpServletResponse response){
        System.out.println("ADD student");
        String newName = newstudent.get("name");
        int newAge = Integer.parseInt(newstudent.get("age"));
        int newWeight = Integer.parseInt(newstudent.get("weight"));
        int newHeight = Integer.parseInt(newstudent.get("height"));
        String newHairColor = newstudent.get("hairColor");
        double newGPA = Double.parseDouble(newstudent.get("gpa"));
        studentsRepo.save(new Students(newName,newAge,newWeight,newHeight,newHairColor,newGPA));
        return "redirect:/students";
    }

    @GetMapping("/student/update/{uid}")
public String showUpdateStudentForm(@PathVariable("uid") String uidString, Model model) {
    Long uid;
    try {
        uid = Long.parseLong(uidString);
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid UID format");
    }

    List<Students> existingStudents = studentsRepo.findByUid(uid);
    if (existingStudents.isEmpty()) {
        throw new RuntimeException("Student not found");
    }
    Students existingStudent = existingStudents.get(0);
    model.addAttribute("student", existingStudent);
    return "users/editStudent";
}

    @PostMapping("/student/update/{uid}")
    public String updateStudent(@PathVariable("uid") Long uid, @RequestParam Map<String, String> updatedStudent) {
        System.out.println("UPDATE student");
        List<Students> existingStudents = studentsRepo.findByUid(uid);
        if (existingStudents.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        Students existingStudent = existingStudents.get(0);
        String newName = updatedStudent.get("name");
        int newAge = Integer.parseInt(updatedStudent.get("age"));
        int newWeight = Integer.parseInt(updatedStudent.get("weight"));
        int newHeight = Integer.parseInt(updatedStudent.get("height"));
        String newHairColor = updatedStudent.get("hairColor");
        double newGPA = Double.parseDouble(updatedStudent.get("gpa"));

        existingStudent.setName(newName);
        existingStudent.setAge(newAge);
        existingStudent.setWeight(newWeight);
        existingStudent.setHeight(newHeight);
        existingStudent.setHairColor(newHairColor);
        existingStudent.setGpa(newGPA);

        studentsRepo.save(existingStudent);
        return "redirect:/students";
    }

    @Transactional
    @GetMapping("/student/delete/{uid}")
    public String deleteStudent(@PathVariable("uid") Long uid) {
        System.out.println("DELETE student");
        studentsRepo.deleteByUid(uid);
        return "redirect:/students";
    }
    
}
