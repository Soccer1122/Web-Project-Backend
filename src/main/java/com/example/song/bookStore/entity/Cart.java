package com.example.song.bookStore.entity;

public class Cart {
	private int id;
	private int userid;
	private int bookid;
	private int quantity;
	public Cart() {
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getBookid() {
		return bookid;
	}
	public void setBookid(int bookid) {
		this.bookid = bookid;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Cart(int id, int userid, int bookid, int quantity) {
		super();
		this.id = id;
		this.userid = userid;
		this.bookid = bookid;
		this.quantity = quantity;
	}
	
}
