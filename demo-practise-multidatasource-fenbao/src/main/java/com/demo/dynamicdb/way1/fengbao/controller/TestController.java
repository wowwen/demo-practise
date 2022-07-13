package com.demo.dynamicdb.way1.fengbao.controller;

import com.demo.dynamicdb.way1.fengbao.entity.Student;
import com.demo.dynamicdb.way1.fengbao.entity.Teacher;
import com.demo.dynamicdb.way1.fengbao.service.IStudentService;
import com.demo.dynamicdb.way1.fengbao.service.ITeacherService;
import com.demo.practise.common.resp.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@CrossOrigin
public class TestController {
	@Autowired
	private IStudentService studentService;
	@Autowired
	private ITeacherService teacherService;

	@GetMapping("/student/{id}")
	public Message<Student> getOneStudent(@PathVariable(value = "id") String id) throws Exception {
		Student student = studentService.getById(id);
		return new Message<Student>(student);
	}

	@GetMapping("/teacher/{id}")
	public Message<Teacher> getOneTeacher(@PathVariable(value = "id") String id) throws Exception {
		Teacher teacher = teacherService.getById(id);
		return new Message<Teacher>(teacher);
	}


}
