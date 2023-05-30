package com.example.song.bookStore.entity;

import java.sql.Date;

public class Book {
	private int id;
	private String title;
	private String author;
	private String category;
	private Date releaseDate;
	private int numberOfPages;
	private String image;
	private int sold;
	private int price;
	private String des;

	public Book() {
	}

	
	public Book( int id, String title, String author, String category, Date releaseDate, int numberOfPages, String image,
			int sold, int price, String des) {
		super();
		this.id=id;
		this.title = title;
		this.author = author;
		this.category = category;
		this.releaseDate = releaseDate;
		this.numberOfPages = numberOfPages;
		this.image = image;
		this.sold = sold;
		this.price = price;
		this.des = des;
	}
	public Book(  String title, String author, String category, Date releaseDate, int numberOfPages, String image,
			int sold, int price, String des) {
		super();
		this.title = title;
		this.author = author;
		this.category = category;
		this.releaseDate = releaseDate;
		this.numberOfPages = numberOfPages;
		this.image = image;
		this.sold = sold;
		this.price = price;
		this.des = des;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getSold() {
		return sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	
}
