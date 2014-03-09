package edu.sjsu.cmpe.library.repository;

import java.util.Map;

import edu.sjsu.cmpe.library.domain.Book;

/**
 * Book repository interface.
 * 
 * What is repository pattern?
 * 
 * @see http://martinfowler.com/eaaCatalog/repository.html
 */
public interface BookRepositoryInterface {
    /**
     * Save a new book in the repository
     * 
     * @param newBook
     *            a book instance to be create in the repository
     * @return a newly created book instance with auto-generated ISBN
     */
    Book saveBook(Book newBook);

    /**
     * Retrieve an existing book by ISBN
     * 
     * @param isbn
     *            a valid ISBN
     * @return a book instance
     */
    Book getBookByISBN(Long isbn);
    boolean deleteBook(Long isbn);
    boolean changeStatus(Book book, String status);
    Book createReview(Book book, Map review);
    Map getBookReviewById(Long isbn,int reviewid);
    Map getBookAuthorById(Long isbn,int authorid);
    Map getAllReviews(Book book);
    Map viewAllAuthors(Book book);

    // TODO: add other operations here!
}
