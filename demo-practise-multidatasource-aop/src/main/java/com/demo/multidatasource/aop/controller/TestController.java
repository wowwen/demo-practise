package com.demo.multidatasource.aop.controller;

import com.demo.multidatasource.aop.entity.Student;
import com.demo.multidatasource.aop.entity.Teacher;
import com.demo.multidatasource.aop.service.IStudentService;
import com.demo.multidatasource.aop.service.ITeacherService;
import com.demo.practise.common.resp.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
		return new Message<List<Student>>(all);
	}

	@GetMapping("/teacher/all")
	public Message<List<Teacher>> getOneTeacher() throws Exception {
		List<Teacher> all = teacherService.getAll();
		return new Message<List<Teacher>>(all);
	}


}
