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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.song.bookStore.entity.Cart;
import com.example.song.bookStore.entity.Comment;
@RestController
@CrossOrigin
public class CommentController {
	@GetMapping("/comments/{idbook}")
	public List<Comment> getComment(Model model, @PathVariable int idbook) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		List<Comment> comments = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");
			ps = connection.prepareStatement("select * from comment where bookid = ?");
			ps.setInt(1, idbook);
			result = ps.executeQuery();
			while (result.next()) {
				int id = result.getInt("id");
				String content = result.getString("content");
				int authorid = result.getInt("authorid");
				PreparedStatement ps2 = connection.prepareStatement("select name from user where id = ?");
				ps2.setInt(1, authorid);
				ResultSet result2 = ps2.executeQuery();
				String author=null;
				while(result2.next())
				author= result2.getString("name");
				int rating = result.getInt("rating");
				int bookid=result.getInt("bookid");
				comments.add(new Comment(id, content, author, rating, bookid));
			}
			connection.close();
		} // End of try block
		catch (Exception e) {
			e.printStackTrace();
		}
		return comments;
	}
	@PostMapping("/addComment/{authorid}")
	public void addToComment(@RequestBody Comment comment, @PathVariable int authorid) {
		String content=comment.getContent();
		int rating = comment.getRating();
	    int id = comment.getBookid();    	
	    System.out.println(content);
	    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002")) {
	        // Thêm thông tin giỏ hàng vào cơ sở dữ liệu
	        try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO comment(content,authorid,rating,bookid)  VALUES (?, ?, ?,?)")) {
	            insertStatement.setString(1,content );
	            insertStatement.setInt(2, authorid);
	            insertStatement.setInt(3, rating);
	            insertStatement.setInt(4, id);
	            int rowsInserted = insertStatement.executeUpdate();
	        }
	        System.out.println("thành công");
	        connection.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
