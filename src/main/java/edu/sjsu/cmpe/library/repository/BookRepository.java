package edu.sjsu.cmpe.library.repository;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.dto.LinkDto;

public class BookRepository implements BookRepositoryInterface {
    /** In-memory map to store books. (Key, Value) -> (ISBN, Book) */
    private final ConcurrentHashMap<Long, Book> bookInMemoryMap;
    int authorId = 0;
    int reviewId = 0;
    /** Never access this key directly; instead use generateISBNKey() */
    private long isbnKey;
    public BookRepository(ConcurrentHashMap<Long, Book> bookMap) {
	checkNotNull(bookMap, "bookMap must not be null for BookRepository");
	bookInMemoryMap = bookMap;
	isbnKey = 0;
    }

    /**
     * This should be called if and only if you are adding new books to the
     * repository.
     * 
     * @return a new incremental ISBN number
     */
    private final Long generateISBNKey() {
	// increment existing isbnKey and return the new value
	return Long.valueOf(++isbnKey);
    }
    
    private Integer generateAuthorId(){
    	return Integer.valueOf(authorId++);
    }
    
    private Integer generateReviewId(){
    	return Integer.valueOf(++reviewId);
    }
    

    /**
     * This will auto-generate unique ISBN for new books.
     */
    @Override
    public Book saveBook(Book newBook) {
	checkNotNull(newBook, "newBook instance must not be null");
	Book temp_Book = new Book();
    List bookAuthorDetailsContainerList = new ArrayList();
	// Generate new ISBN
	Long isbn = generateISBNKey();
	newBook.setIsbn(isbn);
	temp_Book.setIsbn(newBook.getIsbn());
	temp_Book.setTitle(newBook.getTitle());
	temp_Book.setPublication_date(newBook.getPublication_date());
	temp_Book.setLanguage(newBook.getLanguage());
	temp_Book.setNum_pages(newBook.getNum_pages());
	temp_Book.setStatus(newBook.getStatus());
	for(int i = 0 ;i < newBook.getAuthors().size() ; i++){
		authorId = generateAuthorId();
		bookAuthorDetailsContainerList.add(authorId, newBook.getAuthors().get(i));
	}
	authorId = 0;
	temp_Book.setAuthors(bookAuthorDetailsContainerList);
	
	
	//temp_Book.setAuthor(newBook.getAuthor());
	
	// TODO: create and associate other fields such as author

	// Finally, save the new book into the map
	bookInMemoryMap.putIfAbsent(isbn, temp_Book);

	return temp_Book;
    }

    /**
     * @see edu.sjsu.cmpe.library.repository.BookRepositoryInterface#getBookByISBN(java.lang.Long)
     */
    @Override
    public Book getBookByISBN(Long isbn) {
	checkArgument(isbn > 0,
		"ISBN was %s but expected greater than zero value", isbn);
	return bookInMemoryMap.get(isbn);
    }
    
    @Override
    public boolean deleteBook(Long isbn){
    checkArgument(isbn > 0,
    			"ISBN was %s but expected greater than zero value", isbn);
    bookInMemoryMap.remove(isbn);
    return true;
    	
    }
    
    @Override
    public boolean changeStatus(Book book, String status){
    	book.setStatus(status);
    	return true;
    }
    
    @Override
    public Book createReview(Book book, Map review){
    		int i=0;
    		Book temp_Book = new Book();
    		Map bookReviewDetailsContainerMap = new HashMap();
    		temp_Book = book;
    		//System.out.println("The Book Passed:" + temp_Book.getReview().toString());
    		reviewId = generateReviewId();
    		//bookReviewDetailsContainerMap.clear();
    		bookReviewDetailsContainerMap.put("id", reviewId);
    		bookReviewDetailsContainerMap.put("rating", review.get("rating"));
    		bookReviewDetailsContainerMap.put("comment", review.get("comment"));
    		//System.out.println("bookReviewDetailsContainerMap : " + bookReviewDetailsContainerMap.toString());
    		temp_Book.setReview(bookReviewDetailsContainerMap);
    		return temp_Book;
    }
    
    public Map getBookReviewById(Long isbn,int reviewId){
    			checkArgument(isbn > 0, "ISBN was %s but expected greater than zero value", isbn);
    			List bookList=new ArrayList();
    			Map bookreviewmap=new HashMap();
    			Book book = bookInMemoryMap.get(isbn);
    			//TODO get the review with the reviewid received from parameter
    			bookList=book.getReview();
    			if(!bookList.isEmpty()){
    			bookreviewmap.put("review",bookList.get(reviewId-1));
    			}
    			List lst=new ArrayList();
    			String location = "/books/" +isbn + "/reviews/" + reviewId ;
    			LinkDto ldto=new LinkDto("view-review", location, "GET");
    			lst.add(ldto);	
    			bookreviewmap.put("links",lst);
    			return bookreviewmap;
    			}
    
    
    public Map getBookAuthorById(Long isbn,int authorId){
		checkArgument(isbn > 0, "ISBN was %s but expected greater than zero value", isbn);
		List bookList=new ArrayList();
		Map bookAuthorMap=new HashMap();
		Book book = bookInMemoryMap.get(isbn);
		//TODO get the review with the reviewid received from parameter
		bookList=book.getAuthors();
		if(!bookList.isEmpty()){
		bookAuthorMap.put("Author",bookList.get(authorId-1));
		}
		List lst=new ArrayList();
		String location = "/books/" + isbn +"/author/" + authorId;
		LinkDto ldto=new LinkDto("view-review", location, "GET");
		lst.add(ldto);	
		bookAuthorMap.put("links",lst);
		return bookAuthorMap;
		}
    public Map getAllReviews(Book book){
    	Map reviews = new HashMap();
    	reviews.put("reviews", book.getReview());
    	reviews.put("links", new ArrayList());
    	return reviews;    	
    }
    
    public Map viewAllAuthors(Book book){
    	Map reviews = new HashMap();
    	reviews.put("Authors", book.getAuthors());
    	reviews.put("links", new ArrayList());
    	return reviews;    
    }
}
