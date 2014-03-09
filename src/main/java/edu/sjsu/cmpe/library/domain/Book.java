package edu.sjsu.cmpe.library.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Book {
	@JsonProperty
    private long isbn;
	@JsonProperty
	@NotEmpty
    private String title;
    private String  publication_date;
	@JsonProperty
    private String language;
	@JsonProperty("num-pages")
    private int num_pages;
	@JsonProperty
    private String status;
	@NotEmpty
	@Valid
	@JsonProperty
    private List authors = new ArrayList();
	@JsonProperty
    private List<HashMap> review = new ArrayList<HashMap>();
    
    

	public List getReview() {
		return review;
	}

	public void setReview(Map review_new) {
		System.out.println("Before Changing: Inside Book :" + this.getReview().toString());
		this.getReview().add(review_new);
		System.out.println("After Changing : Inside Book :" + this.getReview().toString());
	}

	public List getAuthors() {
		return authors;
	}

	public void setAuthors(List authors) {
		this.authors = authors;
	}

	public String getPublication_date() {
		return publication_date;
	}

	public void setPublication_date(String publication_date) {
		this.publication_date = publication_date;
	}

	/**
     * @return the language
     */
	public String getLanguage() {
		return language;
	}
	
	/**
     * @param language
     *  the language to set
     */
	public void setLanguage(String language) {
		this.language = language;
	}
	
    /**
     * @return the num_pages
     */
	public int getNum_pages() {
		return num_pages;
	}
	
	/**
     * @param num_pages
     *  the num_pages to set
     */
	public void setNum_pages(int num_pages) {
		this.num_pages = num_pages;
	}
	

    /**
     * @return the status
     */
	public String getStatus() {
		return status;
	}
	
	/**
     * @param status
     *  the status to set
     */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
     * @return the isbn
     */
    public long getIsbn() {
	return isbn;
    }

    /**
     * @param isbn
     *  the isbn to set
     */
    public void setIsbn(long isbn) {
	this.isbn = isbn;
    }

	/**
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     * the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }
    
    
}
