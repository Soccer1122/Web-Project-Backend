package com.example.song.bookStore.entity;

public class OrderedItem {
	private int id;
	private int idBook;
	private int quantity;
	private int idUser;
	private int idOrder;
	public OrderedItem() {
		// TODO Auto-generated constructor stub
	}
	public OrderedItem(int id ,int idBook, int quantity, int idUser, int idOrder) {
		super();
		this.id=id;
		this.idBook = idBook;
		this.quantity = quantity;
		this.idUser = idUser;
		this.idOrder = idOrder;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdBook() {
		return idBook;
	}
	public void setIdBook(int idBook) {
		this.idBook = idBook;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getIdUser() {
		return idUser;
	}
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	public int getIdOrder() {
		return idOrder;
	}
	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}
	
}
