package com.example.song.bookStore.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.song.bookStore.entity.Order;
import com.example.song.bookStore.entity.OrderedItem;
import com.example.song.bookStore.entity.Cart;

@RestController
@CrossOrigin
public class OrderController {

	@GetMapping("/orders")
	public List<Order> getAllOrders(Model model) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<Order> orders = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from orders ORDER BY id DESC");
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String address = resultSet.getString("address");
				String phone = resultSet.getString("phone");
				String city = resultSet.getString("city");
				String ward = resultSet.getString("ward");
				String typeOfAddress = resultSet.getString("typeOfAddress");
				int userid = resultSet.getInt("idUser");
				int total = resultSet.getInt("total");
				String status = resultSet.getString("status");
				orders.add(new Order(id, name, address, phone, city, ward, typeOfAddress, userid, total, status));

			}
			connection.close();
		} // End of try block
		catch (Exception e) {
			e.printStackTrace();
		}
		return orders;
	}

	@GetMapping("/orders/{userid}")
	public List<Order> getOrders(Model model, @PathVariable int userid) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		List<Order> orders = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");
			ps = connection.prepareStatement(
					"select id,name,address,city,ward,typeOfAddress,total,status from orders where idUser = ? ORDER BY id DESC");
			ps.setInt(1, userid);
			result = ps.executeQuery();
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				String address = result.getString("address");
				String city = result.getString("city");
				String ward = result.getString("ward");
				String typeOfAddress = result.getString("typeOfAddress");
				int total = result.getInt("total");
				String status = result.getString("status");
				orders.add(
						new Order(id, name, typeOfAddress, address, city, ward, typeOfAddress, userid, total, status));

			}
			connection.close();
		} // End of try block
		catch (Exception e) {
			e.printStackTrace();
		}
		return orders;
	}

	@PostMapping("/addOrder")
	public ResponseEntity<ResponseObject> addOrder(@RequestBody Order order) {
		String name = order.getName();
		String phone = order.getPhone();
		String address = order.getAddress();
		String city = order.getCity();
		String ward = order.getWard();
		String typeOfAddress = order.getTypeOfAddress();
		int idUser = order.getIdUser();
		int total = order.getTotal();
		if (phone.matches(
				"^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$") == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ResponseObject("failed", "Số điện thoại không đúng định dạng", ""));
		} else {
			int rowsInserted = 0;
			try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root",
					"11102002")) {
				// Thêm thông tin giỏ hàng vào cơ sở dữ liệu
				try (PreparedStatement insertStatement = connection.prepareStatement(
						"INSERT INTO orders(name, phone, address, city, ward, typeOfAddress, idUser, total,status)  VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)")) {
					insertStatement.setString(1, name);
					insertStatement.setString(2, phone);
					insertStatement.setString(3, address);
					insertStatement.setString(4, city);
					insertStatement.setString(5, ward);
					insertStatement.setString(6, typeOfAddress);
					insertStatement.setInt(7, idUser);
					insertStatement.setInt(8, total);
					insertStatement.setString(9, "Chờ xác nhận");
					rowsInserted = insertStatement.executeUpdate();
				}
				// Lấy ra giỏ hàng của khách hàn
				ResultSet result = null;
				List<Cart> carts = new ArrayList<>();
				try (PreparedStatement getStatement = connection
						.prepareStatement("select id,bookid,quantity from cart where userid = ?")) {
					getStatement.setInt(1, idUser);
					result = getStatement.executeQuery();
					while (result.next()) {
						int id = result.getInt("id");
						int bookid = result.getInt("bookid");
						int quantity = result.getInt("quantity");
						carts.add(new Cart(id, idUser, bookid, quantity));

					}
				}
				// Thêm mặt hàng trong giỏ hàng vào OrderedItem
				int idOder = 0;
				try (PreparedStatement getidStatement = connection
						.prepareStatement("select id from orders ORDER BY id desc LIMIT 1")) {
					result = getidStatement.executeQuery();
					while (result.next()) {
						idOder = result.getInt("id");
					}
				}
				for (Cart cart : carts) {
					try (PreparedStatement insertStatement = connection.prepareStatement(
							"INSERT INTO orderedItem(idBook, quantity, idUser, idOrder)  VALUES (?, ?, ?, ?)")) {
						insertStatement.setInt(1, cart.getBookid());
						insertStatement.setInt(2, cart.getQuantity());
						insertStatement.setInt(3, cart.getUserid());
						insertStatement.setInt(4, idOder);
						rowsInserted = insertStatement.executeUpdate();
					}
					// Cập nhật số lượng sách đã bán
					try (PreparedStatement selectStatement = connection
							.prepareStatement("SELECT sold FROM book WHERE id = ?")) {
						selectStatement.setInt(1, cart.getBookid());
						try (ResultSet resultSet2 = selectStatement.executeQuery()) {
							int sold = 0;
							if (resultSet2.next()) {
								sold = resultSet2.getInt("sold");
							}
							try (PreparedStatement updateStatement = connection
									.prepareStatement("UPDATE book SET sold = ? WHERE id = ?")) {
								updateStatement.setInt(1, sold + cart.getQuantity());
								updateStatement.setInt(2, cart.getBookid());
								int rowsUpdated = updateStatement.executeUpdate();
								// Kiểm tra số hàng bị ảnh hưởng bởi truy vấn UPDATE
							}
						}
					}
				}
				int result1 = 0;
				try (PreparedStatement deleteStatement = connection
						.prepareStatement("delete from cart where userid= ?")) {
					deleteStatement.setInt(1, idUser);
					result1 = deleteStatement.executeUpdate();
				}
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (rowsInserted != 0) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Đặt hàng thành công", "")

				);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new ResponseObject("failed", "Đặt hàng thành công", ""));
			}
		}
	}

	@PutMapping("/orders/confirm/{id}")
	public void addToCart(@PathVariable int id) {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root",
				"11102002")) {

			try (PreparedStatement insertStatement = connection
					.prepareStatement("UPDATE orders SET status = ? WHERE id = ?")) {
				insertStatement.setString(1,"Đã xác nhận");
				insertStatement.setInt(2, id);
				int rowsInserted = insertStatement.executeUpdate();
				// Kiểm tra số hàng bị ảnh hưởng bởi truy vấn INSERT
			}
			connection.close();
			System.out.println("thành công");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@DeleteMapping("/orders/delete/{id}")
	public void deleteOrders(@PathVariable("id") int id) {
		Connection connection = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");

			ResultSet result1 = null;
			List<OrderedItem> orderedItem = new ArrayList<>();
			try (PreparedStatement getStatement = connection
					.prepareStatement("select idBook,quantity from orderedItem where idOrder = ?")) {
				getStatement.setInt(1, id);
				result1 = getStatement.executeQuery();
				while (result1.next()) {
					int bookid = result1.getInt("idBook");
					int quantity = result1.getInt("quantity");
					try (PreparedStatement updateStatement = connection
							.prepareStatement("UPDATE book SET sold = sold-? WHERE id = ?")) {
						updateStatement.setInt(1, quantity);
						updateStatement.setInt(2, bookid);
						int rowsUpdated = updateStatement.executeUpdate();
						// Kiểm tra số hàng bị ảnh hưởng bởi truy vấn UPDATE
					}
				}
			}
			ps = connection.prepareStatement("delete from ordereditem where idOrder= ?");
			ps.setInt(1, id);
			result = ps.executeUpdate();

			ps = connection.prepareStatement("delete from orders where id= ?");
			ps.setInt(1, id);
			result = ps.executeUpdate();
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
