package ca.mayambabokambmbu.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.mayambabokambmbu.beans.Book;
import ca.mayambabokambmbu.database.DatabaseAccess;

// 8034 4308

@Controller
public class HomeController {

	private DatabaseAccess database;
	
	public HomeController(DatabaseAccess database) {
		this.database = database;
	}
	
	@Autowired
	@Lazy
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	@Lazy
	private UserDetailsManager jdbcUserDetailsManager;
	
	// index to view books
	@GetMapping("/")
	public String home(Model model) {
		
		List<Book> books = database.getBook();
		
		model.addAttribute("bookList", books );
		
		return "index";
	}
	
	// Login page
	@GetMapping("/login")
	public String loginPage() {
		
		return "login";
	}
	
	
	  @GetMapping("/secured") 
	  public String loginAction() {
	  
	  return "redirect:/"; 
	  }
	 


	@GetMapping("/newUser")
	public String registerUser(Model model) {
		
		List<String> authorities = database.getAuthorities();
		
		model.addAttribute("authorities", authorities);
		
		return "register";
	}
	
	// Register new user
	@PostMapping("/register")
	public String register(@RequestParam String userName, @RequestParam String password,
							@RequestParam String[] authorities, Model model) {
		
		List<GrantedAuthority> authorityList = new ArrayList<>();
		
		for (String authority: authorities) {
			authorityList.add(new SimpleGrantedAuthority(authority));
		}
		
		String encodedPassword = passwordEncoder.encode(password);
		User user = new User(userName, encodedPassword, authorityList);
		
		jdbcUserDetailsManager.createUser(user);
		model.addAttribute("message", "We have a new user");
		
		return "login";
	}
	
	
	
	
//	@GetMapping("/admin/addBook") 
//	public String addBook() {
//	  
//	  return "/admin/add-book"; 
//	}
//	
	
	
	
	// add review to book
//		@PostMapping("/user/addReview")
//		public String addReview() {
//			
//			return "";
//		}
	
	
}
