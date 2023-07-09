package ca.mayambabokambmbu.controllers;

import java.net.URI;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ca.mayambabokambmbu.beans.Book;
import ca.mayambabokambmbu.beans.ErrorMessage;
import ca.mayambabokambmbu.beans.Review;
import ca.mayambabokambmbu.database.DatabaseAccess;

@RestController
@RequestMapping("/books")
public class RestBookController {

private DatabaseAccess database;
	
	////////////////////////////////////////////////////////////////////////

		public RestBookController(DatabaseAccess database) {
			this.database = database;
		}

		 @GetMapping
		 public List <Book> getBooks(){
			 List<Book> books = database.getBook();
			 
			 for(Book i: books) {
				i.setReviews(database.getReviewList(i.getId()));
			 }
			 return books;
		 }
		 
		
		 
		 @SuppressWarnings("unused")
		@GetMapping("/{id}")
		 public ResponseEntity<?> getBook(@PathVariable Long id ) {
			 
			 Book book = database.getBook(id);
			 List<Review> reviews = database.getReviewList(id);
			 
			 book.setReviews(reviews);
			
			 if(book != null) {
				 
				 return ResponseEntity.ok(book);
			 } else {
				 return ResponseEntity.status(HttpStatus.NOT_FOUND)
						 .body(new ErrorMessage("No such Record"));
			 }
			 
			 
		 }
		 
	
		 
		@PostMapping(consumes="application/json")
		public ResponseEntity<?> postBook(@RequestBody Book book) {
			
			try {
				
				Long id = database.addRestBook(book);
				
				
				book.setId(id);
				
				
				URI location = ServletUriComponentsBuilder.fromCurrentServletMapping()
						.path("/{id}").buildAndExpand(id).toUri();
				
				return ResponseEntity.created(location).body(book);
				
			} catch (Exception e) {
				
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body(new ErrorMessage("Name already exists."));
			}
			
		}
}
