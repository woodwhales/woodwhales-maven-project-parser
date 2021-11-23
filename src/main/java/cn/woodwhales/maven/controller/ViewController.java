package cn.woodwhales.maven.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

	@GetMapping({"/index", "/"})
	public String index() {
		return "index";
	}
	
}
