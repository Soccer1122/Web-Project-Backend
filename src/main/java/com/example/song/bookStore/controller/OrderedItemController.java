package com.example.song.bookStore.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.song.bookStore.entity.Book;
import com.example.song.bookStore.entity.OrderedItem;
@RestController
@CrossOrigin
public class OrderedItemController {
	@GetMapping("/orderedItems")
	public List<OrderedItem> getOrderedItems(Model model) throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<OrderedItem> OrderedItems = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from ordereditem ");
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				int idBook = resultSet.getInt("idBook");
				int quantity = resultSet.getInt("quantity");
				int idUser = resultSet.getInt("idUser");
				int idOrder = resultSet.getInt("idOrder");
				OrderedItems.add(new OrderedItem(id, idBook, quantity, idUser, idOrder));
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return OrderedItems;
	}
	@DeleteMapping("/orderedItem/delete/{id}")
	public void deleteOrderedItem(@PathVariable("id") int id) {
		Connection connection = null;
		PreparedStatement ps = null;
		int result = 0;
		ResultSet result1 = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");
			ps = connection.prepareStatement("select idBook,quantity,idUser,idOrder from orderedItem where id = ?");
			ps.setInt(1, id);
			result1 = ps.executeQuery();
			int idBook = 0;
			int quantity = 0;
			int idUser=0;
			int idOrder=0;
			int price = 0;
			while(result1.next()) {
				idBook = result1.getInt("idBook");
				quantity = result1.getInt("quantity");
				idUser = result1.getInt("idUser");
				idOrder = result1.getInt("idOrder");
			}
			ps = connection.prepareStatement("delete from orderedItem where id= ?");
			ps.setInt(1, id);
			result = ps.executeUpdate();
			
			try (PreparedStatement selectStatement = connection.prepareStatement("SELECT sold,price FROM book WHERE id = ?")) {
                selectStatement.setInt(1, idBook);
                try (ResultSet resultSet2 = selectStatement.executeQuery()) {
                    int sold = 0;
                    if (resultSet2.next()) {
                        sold = resultSet2.getInt("sold");
                        price = resultSet2.getInt("price");
                    }
                    try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE book SET sold = ? WHERE id = ?")) {
                        updateStatement.setInt(1, sold - quantity);
                        updateStatement.setInt(2, idBook);
                        int rowsUpdated = updateStatement.executeUpdate();
                        // Kiểm tra số hàng bị ảnh hưởng bởi truy vấn UPDATE
                    }
                }
            }
			try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE orders SET total = total - ? WHERE id = ?")) {
                updateStatement.setInt(1, quantity*price*105/100 );
                updateStatement.setInt(2, idOrder);
                int rowsUpdated = updateStatement.executeUpdate();
                // Kiểm tra số hàng bị ảnh hưởng bởi truy vấn UPDATE
            }
			ps = connection.prepareStatement("select idBook,quantity,idUser,idOrder from orderedItem where idOrder = ?");
			ps.setInt(1, idOrder);
			result1 = ps.executeQuery();
			if(result1.next()==false) {
				ps = connection.prepareStatement("delete from orders where id= ?");
				ps.setInt(1, idOrder);
				result = ps.executeUpdate();
			}
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}