package com.example.song.bookStore.entity;

public class Comment {
	private int id;
	private String content;
	private String author;
	private int rating;
	private int bookid;
	public Comment() {
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public int getBookid() {
		return bookid;
	}
	public void setBookid(int bookid) {
		this.bookid = bookid;
	}
	public Comment(int id, String content, String author, int rating, int bookid) {
		super();
		this.id = id;
		this.content = content;
		this.author = author;
		this.rating = rating;
		this.bookid = bookid;
	}
	
}
