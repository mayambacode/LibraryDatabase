package ca.mayambabokambmbu.database;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ca.mayambabokambmbu.beans.Book;
import ca.mayambabokambmbu.beans.Review;

@Repository
public class DatabaseAccess {

	private NamedParameterJdbcTemplate jdbc;

	public DatabaseAccess(DataSource dataSource) {
		this.jdbc = new NamedParameterJdbcTemplate(dataSource);
	}

	// retrieve list of books
	public List<Book> getBook() {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "SELECT * FROM books";

		BeanPropertyRowMapper<Book> bookMapper = new BeanPropertyRowMapper<Book>(Book.class);

		List<Book> books = jdbc.query(query, namedParameters, bookMapper);

		return books;
	}

	// add a new books to the list
	public int addBook(Book book) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "INSERT INTO books(title, author) VALUES (:title, :author)";

		namedParameters.addValue("title", book.getTitle()).addValue("author", book.getAuthor());

		int returnValue = jdbc.update(query, namedParameters);

		return returnValue;
	}

	// get book based on id
	public Book getBook(Long id) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "SELECT * FROM books WHERE id = :id";

		namedParameters.addValue("id", id);

		BeanPropertyRowMapper<Book> bookMapper = new BeanPropertyRowMapper<>(Book.class);

		List<Book> books = jdbc.query(query, namedParameters, bookMapper);

		if (books.isEmpty()) {
			System.out.println("There are no books here");
			return null;
		} else {
			return books.get(0);
		}

	}

	// retrieve reviews list
	public List<Review> getReview() {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "SELECT * FROM reviews";

		BeanPropertyRowMapper<Review> reviewMapper = new BeanPropertyRowMapper<Review>(Review.class);

		List<Review> reviews = jdbc.query(query, namedParameters, reviewMapper);

		return reviews;
	}

	// Add review to books
	public int addReview(Review review) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "INSERT INTO reviews(text, bookId) VALUES (:text, :bookId)";

		namedParameters.addValue("text", review.getText()).addValue("bookId", review.getBookId());

		int returnValue = jdbc.update(query, namedParameters);

		return returnValue;
	}

	// list of reviews based on bookId
	public List<Review> getReviewList(Long bookId) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "SELECT * FROM reviews WHERE bookId = :bookId";

		namedParameters.addValue("bookId", bookId);
		BeanPropertyRowMapper<Review> reviewMapper = new BeanPropertyRowMapper<Review>(Review.class);

		List<Review> reviews = jdbc.query(query, namedParameters, reviewMapper);

		if (reviews.isEmpty()) {
			System.out.println("There's no review here");
			return null;
		} else {
			return reviews;
		}

	}

	public Review getReview(Long id) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "SELECT * FROM reviews WHERE id = :id";

		namedParameters.addValue("id", id);

		BeanPropertyRowMapper<Review> reviewMapper = new BeanPropertyRowMapper<Review>(Review.class);

		List<Review> reviews = jdbc.query(query, namedParameters, reviewMapper);

		if (reviews.isEmpty()) {
			System.out.println("There's no review here");
			return null;
		} else {
			return reviews.get(0);
		}

	}

	// For security
	public List<String> getAuthorities() {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "SELECT DISTINCT authority FROM authorities";

		List<String> authorities = jdbc.queryForList(query, namedParameters, String.class);

		return authorities;
	}

	// For RESTful API
	  public Long addRestBook(Book book) throws Exception{
		  MapSqlParameterSource namedParameters = new MapSqlParameterSource();

			String query = "INSERT INTO books(title, author) VALUES (:title, :author)";

			namedParameters.addValue("title", book.getTitle()).addValue("author", book.getAuthor());
			
			KeyHolder generatedKey = new GeneratedKeyHolder();
			
			int returnValue = jdbc.update(query, namedParameters);
			
			Long bookId = (Long) generatedKey.getKey();
			
			return (returnValue > 0) ? bookId: 0;
	  }
	  
	   //for RESTful API 
	   public Long addRestReview (Review review) {
		   
		   MapSqlParameterSource namedParameters = new MapSqlParameterSource();

			String query = "INSERT INTO reviews(text, bookId) VALUES (:text, :bookId)";

			namedParameters.addValue("text", review.getText()).addValue("bookId", review.getBookId());

			KeyHolder generatedKey = new GeneratedKeyHolder();
			
			int returnValue = jdbc.update(query, namedParameters);
			
			Long reviewId = (Long) generatedKey.getKey();
			
			return (returnValue > 0) ? reviewId: 0;
	  }
	 

}
