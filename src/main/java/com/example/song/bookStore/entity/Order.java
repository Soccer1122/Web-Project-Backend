package com.example.song.bookStore.entity;

public class Order {
	private int id;
	private String name;
	private String address;
	private String phone;
	private String city;
	private String ward;
	private String typeOfAddress;
	private int idUser;
	private int total;
	private String status;
	public Order() {
		// TODO Auto-generated constructor stub
	}
	
	

	



	public Order(int id, String name, String address, String phone, String city, String ward, String typeOfAddress,
			int idUser, int total, String status) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.city = city;
		this.ward = ward;
		this.typeOfAddress = typeOfAddress;
		this.idUser = idUser;
		this.total = total;
		this.status = status;
	}







	public String getStatus() {
		return status;
	}







	public void setStatus(String status) {
		this.status = status;
	}







	public int getTotal() {
		return total;
	}



	public void setTotal(int total) {
		this.total = total;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getWard() {
		return ward;
	}
	public void setWard(String ward) {
		this.ward = ward;
	}
	public String getTypeOfAddress() {
		return typeOfAddress;
	}
	public void setTypeOfAddress(String typeOfAddress) {
		this.typeOfAddress = typeOfAddress;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	
}
