package com.example.song.bookStore.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.song.bookStore.entity.Category;

@RestController
@CrossOrigin
public class CategoryController {
	@GetMapping("/categories")
	public List<Category> getCategories(Model model) throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<Category> categories = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from category");
			while (resultSet.next()) {
				int id = resultSet.getInt("id");		
				String name = resultSet.getString("name");
				categories.add(new Category(id, name));
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categories;
	}
}
