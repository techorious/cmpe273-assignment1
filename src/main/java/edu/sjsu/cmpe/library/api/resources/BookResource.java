package edu.sjsu.cmpe.library.api.resources;

import java.util.*;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.dto.BookDto;
import edu.sjsu.cmpe.library.dto.LinkDto;
import edu.sjsu.cmpe.library.dto.LinksDto;
import edu.sjsu.cmpe.library.repository.BookRepository;
import edu.sjsu.cmpe.library.repository.BookRepositoryInterface;

@Path("/v1/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    /** bookRepository instance */
    private final BookRepositoryInterface bookRepository;
    /**
     * BookResource constructor
     * 
     * @param bookRepository
     *            a BookRepository instance
     */
    public BookResource(BookRepositoryInterface bookRepository) {
	this.bookRepository = bookRepository;
    }

    @GET
    @Path("/{isbn}")
    @Timed(name = "view-book")
    public BookDto getBookByIsbn(@PathParam("isbn") LongParam isbn) {
	Book book = bookRepository.getBookByISBN(isbn.get());
	BookDto bookResponse = new BookDto(book);
	bookResponse.addLink(new LinkDto("view-book", "/books/" + book.getIsbn(),"GET"));
	bookResponse.addLink(new LinkDto("update-book", "/books/" + book.getIsbn(), "PUT"));
	bookResponse.addLink(new LinkDto("delete-book", "/books/" + book.getIsbn(), "DELETE"));
	bookResponse.addLink(new LinkDto("create-review", "/books/" + book.getIsbn() +"/reviews", "POST"));
	if(!bookResponse.getBook().getReview().isEmpty()){
		bookResponse.addLink(new LinkDto("view-all-review", "/books/" + book.getIsbn() +"/reviews", "GET"));
	}
	
	List authors_links = new ArrayList();
	int j = 0;
	System.out.println("Size of Author List" + bookResponse.getBook().getAuthors().size());
	for(int i = 0; i < bookResponse.getBook().getAuthors().size(); i++){
		j = i+1;
		authors_links.add(new LinkDto("view-author", "/books/" + book.getIsbn() +"/authors/" + j, "GET"));
		System.out.println("i" + i + "links" + authors_links.get(i));
	}
	bookResponse.getBook().setAuthors(authors_links);
	// add more links

	return bookResponse;
    }

    @POST
    @Timed(name = "create-book")
    public Response createBook(@Valid Book request) {
	// Store the new book in the BookRepository so that we can retrieve it.
	Book savedBook = bookRepository.saveBook(request);
	String location = "/books/" + savedBook.getIsbn();
	LinksDto links = new LinksDto();
	links.addLink(new LinkDto("view-book", location, "GET"));
	links.addLink(new LinkDto("update-book", location, "POST"));
	links.addLink(new LinkDto("delete-book", location, "DELETE"));
	links.addLink(new LinkDto("create-review", location, "POST"));
	// Add other links if needed

	//return Response.status(201).entity(bookResponse).build();
	return Response.status(201).entity(links).build();
    }
    
    @DELETE
    @Path("/{isbn}")
    @Timed(name = "delete-book")
    public Response deleteBook(@PathParam("isbn") LongParam isbn){
    // Delete the book the same isbn from the repository
    boolean value = bookRepository.deleteBook(isbn.get());
    LinksDto links = new LinksDto();
    links.addLink(new LinkDto("create-book", "/books", "POST"));
    
    return Response.ok(links).build();
    }
    
    @PUT
    @Path("{isbn}")
    public Response updateBook(@PathParam("isbn") LongParam isbn, @QueryParam("status") String status){
    	Book book = bookRepository.getBookByISBN(isbn.get());
    	bookRepository.changeStatus(book, status);
    	System.out.println("New Status :" + book.getStatus());
    	LinksDto links = new LinksDto();
    	links.addLink(new LinkDto("view-book", "/books/" + book.getIsbn(), "GET"));
    	links.addLink(new LinkDto("update-book", "/books/" + book.getIsbn(), "PUT"));
    	links.addLink(new LinkDto("delete-book", "/books/" + book.getIsbn(), "DELETE"));
    	links.addLink(new LinkDto("create-review", "/books/" + book.getIsbn() + "/reviews", "POST"));
    	if(!book.getReview().isEmpty()){
    		links.addLink(new LinkDto("view-all-review", "/books/" + book.getIsbn() +"/reviews", "GET"));
    	}
    	return Response.status(200).entity(links).build();
    }
    
    @POST
    @Path("/{isbn}/reviews")
    public Response createReview(Map review, @PathParam("isbn") LongParam isbn){
    	Book book = bookRepository.getBookByISBN(isbn.get());
    	Book reviewBook = bookRepository.createReview(book, review);
    	String location = "/books/" +book.getIsbn() + "/reviews/" + book.getReview().size();
    	LinksDto links = new LinksDto();
    	links.addLink(new LinkDto("view review", location , "GET"));
    	return Response.status(201).entity(links).build();
    }
    
    @GET
    @Path("/{isbn}/reviews/{reviewId}")
    public Map viewReview(@PathParam("isbn") LongParam isbn, @PathParam("reviewId") int reviewId){
    	Book book = bookRepository.getBookByISBN(isbn.get());
    	return (bookRepository.getBookReviewById(isbn.get(), reviewId));
    	//BookDto bookRespose = (BookDto)book.getReview().get(reviewId);
    	//bookRespose.addLink(new LinkDto("view-review", "/books/" + book.getIsbn() + "/reviews/" + reviewId, "GET"));
    	//return bookRespose;
    }
    
    @GET
    @Path("/{isbn}/reviews")
    @Timed(name = "view-all-reviews")
    public Map viewAllReview(@PathParam("isbn") LongParam isbn){
    	Book book = bookRepository.getBookByISBN(isbn.get());
    	return (bookRepository.getAllReviews(book));
    }
    
    
    @GET
    @Path("/{isbn}/authors")
    @Timed(name = "view-all-authors")
    public Map viewAllAuthors(@PathParam("isbn") LongParam isbn){
    	Book book = bookRepository.getBookByISBN(isbn.get());
    	return (bookRepository.viewAllAuthors(book));
    		
    }
    
    @GET
    @Path("/{isbn}/authors/{authorId}")
    public Map viewAuthor(@PathParam("isbn") LongParam isbn, @PathParam("authorId") int authorId){
    	Book book = bookRepository.getBookByISBN(isbn.get());
    	return (bookRepository.getBookAuthorById(isbn.get(), authorId));
    	//BookDto bookRespose = (BookDto)book.getReview().get(reviewId);
    	//bookRespose.addLink(new LinkDto("view-review", "/books/" + book.getIsbn() + "/reviews/" + reviewId, "GET"));
    	//return bookRespose;
    }
    
    
    
    
    
}

