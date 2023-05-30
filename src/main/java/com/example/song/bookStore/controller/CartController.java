package com.example.song.bookStore.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.song.bookStore.entity.Book;
import com.example.song.bookStore.entity.Cart;

@RestController
@CrossOrigin
public class CartController {
	
	@GetMapping("/carts/{userid}")
	public List<Cart> getCarts(Model model, @PathVariable int userid) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		List<Cart> carts = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");
			ps = connection.prepareStatement("select id,bookid,quantity from cart where userid = ?");
			ps.setInt(1, userid);
			result = ps.executeQuery();
			while (result.next()) {
				int id = result.getInt("id");
				int bookid = result.getInt("bookid");
				int quantity = result.getInt("quantity");
				carts.add(new Cart(id,userid, bookid, quantity));
				
			}
			connection.close();
		} // End of try block
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(carts);
		return carts;
	}
	
	
	@PostMapping("/addToCart")
	public void addToCart(@RequestBody Cart cart) {
	    int id = cart.getBookid();
	    int quantity = cart.getQuantity();	    	
	    int userid = cart.getUserid();
	    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002")) {
	        try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO cart(userid,bookid,quantity)  VALUES (?, ?, ?)")) {
	            insertStatement.setInt(1, userid);
	            insertStatement.setInt(2, id);
	            insertStatement.setInt(3, quantity);
	            int rowsInserted = insertStatement.executeUpdate();
	            // Kiểm tra số hàng bị ảnh hưởng bởi truy vấn INSERT

	            // Cập nhật số lượng sách đã bán
	            try (PreparedStatement selectStatement = connection.prepareStatement("SELECT sold FROM book WHERE id = ?")) {
	                selectStatement.setInt(1, id);
	                try (ResultSet resultSet = selectStatement.executeQuery()) {
	                    int sold = 0;
	                    if (resultSet.next()) {
	                        sold = resultSet.getInt("sold");
	                    }
	                    try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE book SET sold = ? WHERE id = ?")) {
	                        updateStatement.setInt(1, sold + quantity);
	                        updateStatement.setInt(2, id);
	                        int rowsUpdated = updateStatement.executeUpdate();
	                        // Kiểm tra số hàng bị ảnh hưởng bởi truy vấn UPDATE
	                    }
	                }
	            }
	            connection.close();
	        }
	        System.out.println("thành công");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	@DeleteMapping("/cart/delete/{id}")
	public void deleteCart(@PathVariable("id") int id) {
		Connection connection = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");
			ps = connection.prepareStatement("delete from cart where id= ?");
			ps.setInt(1, id);
			result = ps.executeUpdate();

			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
