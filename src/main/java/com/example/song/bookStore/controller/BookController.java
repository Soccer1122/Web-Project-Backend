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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.song.bookStore.entity.Book;


@RestController
@CrossOrigin
public class BookController {
		@GetMapping("/books")
		public List<Book> getBooks(Model model) throws IOException {
			Connection connection = null;
			Statement statement = null;
			ResultSet resultSet = null;
			List<Book> Books = new ArrayList<>();
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");
				statement = connection.createStatement();
				resultSet = statement.executeQuery("select * from book ");
				while (resultSet.next()) {
					int id = resultSet.getInt("id");
					String title = resultSet.getString("title");
					String author = resultSet.getString("author");
					String category = resultSet.getString("category");				
					Date releaseDate = resultSet.getDate("releaseDate");
					int numberOfPages = resultSet.getInt("numberOfPages");
					String image = resultSet.getString("image");
					int sold = resultSet.getInt("sold");
					int price = resultSet.getInt("price");
					String des = resultSet.getString("des");
					Books.add(new Book(id, title, author, category, releaseDate, numberOfPages, image, sold,price, des));
				}
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Books;
		}
//		@GetMapping("/Books/search/{searchTerm}")
//		public List<Book> searchBook(Model model, @PathVariable String searchTerm) throws IOException {
//			Connection connection = null;
//			PreparedStatement ps = null;
//			ResultSet resultSet = null;
//			List<Book> Books = new ArrayList<>();
//			try {
//				Class.forName("com.mysql.cj.jdbc.Driver");
//				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_demo", "root", "11102002");
//				ps = connection.prepareStatement("SELECT * FROM jdbc_demo.Book WHERE name LIKE ? OR brand LIKE ?; ");
//				ps.setString(1,"%"+searchTerm+"%");
//				ps.setString(2,"%"+searchTerm+"%");
//				resultSet=ps.executeQuery();
//				while (resultSet.next()) {
//					String id = resultSet.getString("id");
//					String name = resultSet.getString("name");
//					int price = resultSet.getInt("price");
//					String brand = resultSet.getString("brand");
//					int sold = resultSet.getInt("sold");
//					Date Ngaysx = resultSet.getDate("ngaySX");
//					Books.add(new Book(id, name, price, brand, sold == 0 ? false : true, Ngaysx));
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return Books;
//		}
//
		@GetMapping("/books/{id}")
		public Book getBook(Model model, @PathVariable int id) {
			model.addAttribute("id", id);
			Connection connection = null;
			PreparedStatement ps = null;
			ResultSet result = null;
			Book Book = new Book();
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");
				ps = connection.prepareStatement("select * from book where id = ?");
				ps.setInt(1, id);
				result = ps.executeQuery();
				while (result.next()) {
					Book.setId(result.getInt("id"));
					Book.setTitle(result.getString("title"));
					Book.setAuthor(result.getString("author"));
					Book.setCategory(result.getString("category"));
					Book.setReleaseDate(result.getDate("releaseDate"));
					Book.setNumberOfPages(result.getInt("numberOfPages"));
					Book.setImage(result.getString("image"));
					Book.setSold(result.getInt("sold"));
					Book.setPrice(result.getInt("price"));
					Book.setDes(result.getString("des"));
				}
				connection.close();
			} // End of try block
			catch (Exception e) {
				e.printStackTrace();
			}
			model.addAttribute("Book", Book);
			return Book;
		}
//
//		@PostMapping("/Books/save")
//		public void addStudent(@RequestBody Book Book, Model model) {
//			Connection connection = null;
//			PreparedStatement ps = null;
//			int result = 0;
////				if (isDuplicateStudent(student)) {
////					model.addAttribute("errorMessage", "Sinh viên đã tồn tại trong CSDL");
////			        return "student-information";
////			    }
////				if(isDuplicateIdStudent(student.getId())) {
////					model.addAttribute("errorMessage", "Id sinh viên đã tồn tại trong CSDL");
////			        return "student-information";
////				}
//			try {
//				Class.forName("com.mysql.cj.jdbc.Driver");
//				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_demo", "root", "11102002");
//				ps = connection.prepareStatement("INSERT INTO Book VALUES (?, ?, ?, ?, ?,?)");
//				ps.setString(1, Book.getId());
//				ps.setString(2, Book.getName());
//				ps.setInt(3, Book.getPrice());
//				ps.setString(4, Book.getBrand());
//				ps.setInt(5, Book.isSold() ? 1 : 0);
//				ps.setDate(6, Book.getNgaySX());
//				result = ps.executeUpdate();
//
//				ps.close();
//				connection.close();
//
//				// Redirect the response to success page
//			} // End of try block
//			catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//	//
//		@PutMapping("/Books/save/{id}")
//		public void updateStudent(@RequestBody Book Book, @PathVariable String id) {
//			Connection connection = null;
//			PreparedStatement ps = null;
//			int result = 0;
//			try {
//				Class.forName("com.mysql.cj.jdbc.Driver");
//				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_demo", "root", "11102002");
//				ps = connection.prepareStatement("UPDATE Book SET name=?,price=?,brand=?,sold=?,ngaySX=? WHERE id=?");
//				ps.setString(1, Book.getName());
//				ps.setInt(2, Book.getPrice());
//				ps.setString(3, Book.getBrand());
//				ps.setInt(4, Book.isSold() ? 1 : 0);
//				ps.setDate(5, Book.getNgaySX());
//				ps.setString(6, id);
//				result = ps.executeUpdate();
//
//				ps.close();
//				connection.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		@DeleteMapping("/delete/{id}")
//		public void deleteBook(@PathVariable("id") String id) {
//			Connection connection = null;
//			PreparedStatement ps = null;
//			int result = 0;
//			try {
//				Class.forName("com.mysql.cj.jdbc.Driver");
//				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_demo", "root", "11102002");
//				ps = connection.prepareStatement("delete from Book where id= ?");
//				ps.setString(1, id);
//				result = ps.executeUpdate();
//
//				ps.close();
//				connection.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}

//			private boolean isDuplicateStudent(Student student) {
//				Connection connection = null;
//				Statement statement = null;
//				ResultSet resultSet = null;
//				List<Student> students = new ArrayList<>();
//				try {
//					Class.forName("com.mysql.cj.jdbc.Driver");
//					connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_demo", "root", "11102002");
//					statement = connection.createStatement();
//					resultSet = statement.executeQuery("select * from student ");
//					while (resultSet.next()) {
//						String id = resultSet.getString("id");
//						String name = resultSet.getString("name");
//						Date dob = resultSet.getDate("dob");
//						String major = resultSet.getString("major");
//						int vaccinated = resultSet.getInt("vaccinated");
//						students.add(new Student(id, name, dob, major, vaccinated == 0 ? false : true));
//					}
	//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				for (Student s : students) {
//					if (s.getName().equalsIgnoreCase(student.getName())
//							&& s.getDob().equals(student.getDob())) {
//						return true;
//					}
//				}
//				return false;
//			}
	//
//			private boolean isDuplicateIdStudent(String idChecked) {
//				Connection connection = null;
//				Statement statement = null;
//				ResultSet resultSet = null;
//				List<Student> students = new ArrayList<>();
//				try {
//					Class.forName("com.mysql.cj.jdbc.Driver");
//					connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_demo", "root", "11102002");
//					statement = connection.createStatement();
//					resultSet = statement.executeQuery("select * from student ");
//					while (resultSet.next()) {
//						String id = resultSet.getString("id");
//						String name = resultSet.getString("name");
//						Date dob = resultSet.getDate("dob");
//						String major = resultSet.getString("major");
//						int vaccinated = resultSet.getInt("vaccinated");
//						students.add(new Student(id, name, dob, major, vaccinated == 0 ? false : true));
//					}
	//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				for (Student s : students) {
//					if (s.getId().equalsIgnoreCase(idChecked)) {
//						return true;
//					}
//				}
//				return false;
//			}
}
