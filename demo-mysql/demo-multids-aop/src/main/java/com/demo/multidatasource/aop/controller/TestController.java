package com.demo.multidatasource.aop.controller;

import com.demo.multidatasource.aop.entity.Student;
import com.demo.multidatasource.aop.entity.Teacher;
import com.demo.multidatasource.aop.service.IStudentService;
import com.demo.multidatasource.aop.service.ITeacherService;
import com.demo.practise.common.resp.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@CrossOrigin
public class TestController {
	@Autowired
	private IStudentService studentService;
	@Autowired
	private ITeacherService teacherService;

	@GetMapping("/student/all")
	public Message<List<Student>> getAllStudent() throws Exception {
		List<Student> all = studentService.getAll();
		return new Message<>(all);
	}

	@GetMapping("/teacher/all")
	public Message<List<Teacher>> getOneTeacher() throws Exception {
		List<Teacher> all = teacherService.getAll();
		return new Message<>(all);
	}

	@PostMapping("/student/add")
	public Message addStudent(@RequestBody Student student) throws Exception {
		studentService.addStudent(student);
		return new Message();
	}

	@PostMapping("/teacher/add")
	public Message addTeacher(@RequestBody Teacher teacher) throws Exception {
		teacherService.addTeacher(teacher);
		return new Message();
	}
}
