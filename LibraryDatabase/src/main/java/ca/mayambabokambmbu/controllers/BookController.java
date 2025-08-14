package ca.mayambabokambmbu.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ca.mayambabokambmbu.beans.Book;
import ca.mayambabokambmbu.beans.Review;
import ca.mayambabokambmbu.database.DatabaseAccess;


@Controller
public class BookController {

private DatabaseAccess database;
	
	public BookController(DatabaseAccess database) {
		this.database = database;
	}
	
	
	@GetMapping("/bookReview/{id}")
	public String viewReviews(@PathVariable Long id, Model model) {
		
//		Book book= database.getBook(id);
		List<Review> reviews = database.getReviewList(id);
		Book book = database.getBook(id);
		
		model.addAttribute("isReviewEmpty", reviews == null);
		
		model.addAttribute("bookList", reviews);
		
		model.addAttribute(book);
		
		return "view-book";
	}
	
	

	// Get method to get to add but path
	@GetMapping("admin")
	public String goToManager(Model model) {
		
		List <Book> books =  database.getBook();
		
		model.addAttribute("book", new Book());
		
		return "/admin/add-book";
	}
	
	
//	Add a new book to the list
	@PostMapping("/addBook")
	public String bookSubmit (@ModelAttribute Book book, Model model) {
		
		database.addBook(book);

		
		System.out.println(book);
		
		return "redirect:/";
	}
	
	// Review path
		@GetMapping("/review/{bookId}")
		public String goToUser(Model model, @PathVariable Long bookId) {
			
			System.out.println(bookId);
			
			Review review = new Review();
			review.setBookId(bookId);
			
			
					
					/*
					 * model.addAttribute("review", new Review()); model.addAttribute("text",
					 * review.getText()); model.addAttribute("bookId", review.getBookId());
					 */
			model.addAttribute("review", review);
			
			return "/user/add-review";
		}
		
		// add review to book
		 @PostMapping("/addReview")
		 public String addReview(@ModelAttribute Review review) throws Exception {
			 
			 System.out.println(review);
			 
			 database.addReview(review);
			 
		
			 return "redirect:/bookReview/"+review.getBookId();
		}
}
