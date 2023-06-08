package com.example.song.bookStore.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.song.bookStore.entity.Book;
import com.example.song.bookStore.entity.Cart;


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
				resultSet = statement.executeQuery("select * from book ORDER BY id DESC");
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

		@PostMapping("/books/save")
		public ResponseEntity <ResponseObject> addBook(@RequestBody Book Book, Model model) throws ClassNotFoundException, SQLException {
			Connection connection = null;
			PreparedStatement ps = null;
			int result = 0;
			ResultSet result1=null;
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");
			ps = connection.prepareStatement("select * from book where title = ? and author = ?");
		    ps.setString(1, Book.getTitle());
		    ps.setString(2, Book.getAuthor());
		    result1 = ps.executeQuery();
		    if( result1.next() ) {
		    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
		                new ResponseObject("failed", "Sách đã có trong cơ sở dữ liệu", "")
		        );
		    }
		    else {
			try {
				ps = connection.prepareStatement("INSERT INTO book(title,author,category, releaseDate, numberOfPages,sold,price,des) VALUES (?, ?, ?, ?, ?,?,?,?)");
				ps.setString(1, Book.getTitle());
				ps.setString(2, Book.getAuthor());
				ps.setString(3, Book.getCategory());
				ps.setDate(4, Book.getReleaseDate());
				ps.setInt(5, Book.getNumberOfPages());
				ps.setInt(6, 0);
				ps.setInt(7, Book.getPrice());
				ps.setString(8, Book.getDes());
				result = ps.executeUpdate();

			} // End of try block
			catch (Exception e) {
				e.printStackTrace();
			}
			Statement statement = connection.createStatement();	   
		    result1 = statement.executeQuery("select id from book ORDER BY id DESC LIMIT 1");
		    int id = 0;
		    while(result1.next()) {
		    	id = result1.getInt("id");
		    }
		    String base64Image = Book.getImage(); // Dữ liệu ảnh từ book.image
	        String filePath = "D:\\Ki 2 Nam 3\\LapTrinhWeb\\my-project\\public\\images\\browse-books\\book"+id+".jpg"; // Đường dẫn tới file mới

	        try {
	            // Giải mã dữ liệu base64 thành byte array
	            byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);

	            // Lưu byte array thành file mới
	            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
	            fileOutputStream.write(imageBytes);
	            fileOutputStream.close();

	            System.out.println("Lưu ảnh thành công!");
	        } catch (IOException e) {
	            System.out.println("Lỗi: " + e.getMessage());
	        }
	        try {
				ps = connection.prepareStatement("UPDATE book SET image=? WHERE id=?");
				ps.setString(1, "http:\\\\localhost:3000\\images\\browse-books\\book"+id+".jpg");
				ps.setInt(2, id);
				result = ps.executeUpdate();
				ps.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
			return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "thành công", "")
            );
		}
	}

		@PutMapping("/books/save/{id}")
		public ResponseEntity<ResponseObject> updateBook(@RequestBody Book Book, @PathVariable int id) throws ClassNotFoundException, SQLException {
			Connection connection = null;
			PreparedStatement ps = null;
			int result = 0;
			ResultSet result1=null;
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");
			ps = connection.prepareStatement("select * from book where title = ? and author = ? and id != ?");
		    ps.setString(1, Book.getTitle());
		    ps.setString(2, Book.getAuthor());
		    ps.setInt(3, id);
		    result1 = ps.executeQuery();
		    if( result1.next() ) {
		    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
		                new ResponseObject("failed", "Sách đã có trong cơ sở dữ liệu", "")
		        );
		    }
		    else {
			try {
				ps = connection.prepareStatement("UPDATE book SET title=?,author=?,des=?,releaseDate=?,numberOfPages=?,category=?,price=? WHERE id=?");
				ps.setString(1, Book.getTitle());
				ps.setString(2, Book.getAuthor());
				ps.setString(3, Book.getDes());
				ps.setDate(4, Book.getReleaseDate());
				ps.setInt(5, Book.getNumberOfPages());
				ps.setString(6, Book.getCategory());
				ps.setInt(7, Book.getPrice());
				ps.setInt(8, id);
				result = ps.executeUpdate();

				ps.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	        String image = "http:\\\\localhost:3000\\images\\browse-books\\book"+id+".jpg";
			if(image.equals(Book.getImage())==false) {
			String base64Image = Book.getImage(); // Dữ liệu ảnh từ book.image
			String filePath = "D:\\Ki 2 Nam 3\\LapTrinhWeb\\my-project\\public\\images\\browse-books\\book"+id+".jpg"; // Đường dẫn đến file cần thay thế

	        try {
	            // Giải mã dữ liệu base64 thành byte array
	            byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);

	            // Tạo file tạm thời để lưu ảnh mới
	            File tempFile = File.createTempFile("temp_image", ".png");
	            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
	            fileOutputStream.write(imageBytes);
	            fileOutputStream.close();

	            // Thay thế file cũ bằng file mới
	            Files.copy(tempFile.toPath(), Path.of(filePath), StandardCopyOption.REPLACE_EXISTING);
	            tempFile.delete();

	            System.out.println("Thay thế ảnh thành công!");
	        } catch (IOException e) {
	            System.out.println("Lỗi: " + e.getMessage());
	        }
			}
	        return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "thành công", "")
            );
		}
	}

		@DeleteMapping("/delete/{id}")
		public void deleteBook(@PathVariable("id") int id) {
			Connection connection = null;
			PreparedStatement ps = null;
			int result = 0;
			ResultSet result1 = null;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");
				Book book = new Book();
				ps = connection.prepareStatement("select * from book where id = ?");
				ps.setInt(1, id);
				result1 = ps.executeQuery();
				while (result1.next()) {
					book.setId(result1.getInt("id"));
					book.setTitle(result1.getString("title"));
					book.setAuthor(result1.getString("author"));
					book.setCategory(result1.getString("category"));
					book.setReleaseDate(result1.getDate("releaseDate"));
					book.setNumberOfPages(result1.getInt("numberOfPages"));
					book.setImage(result1.getString("image"));
					book.setSold(result1.getInt("sold"));
					book.setPrice(result1.getInt("price"));
					book.setDes(result1.getString("des"));
				}

				
				ps = connection.prepareStatement("delete from cart where bookid= ?");
				ps.setInt(1, id);
				result = ps.executeUpdate();
				

				ps = connection.prepareStatement("delete from comment where bookid= ?");
				ps.setInt(1, id);
				result = ps.executeUpdate();
				List <Integer> idOrder = new ArrayList<>();
				List <Integer> quantity = new ArrayList<>();
				ps = connection.prepareStatement("select idOrder,quantity from ordereditem where idBook=?");
				ps.setInt(1, id);
				result1 = ps.executeQuery();
				while (result1.next()) {
					idOrder.add(new Integer(result1.getInt("idOrder")));
					quantity.add(new Integer(result1.getInt("quantity")));
				}
				
				ps = connection.prepareStatement("delete from ordereditem where idBook= ?");
				ps.setInt(1, id);
				result = ps.executeUpdate();


				for(int i = 0 ; i <idOrder.size() ; i++) {				
					ps = connection.prepareStatement("select * FROM ordereditem where idOrder = ?");
					ps.setInt(1,idOrder.get(i) );
					result1 = ps.executeQuery();
					if(result1.next()==false) {
						ps = connection.prepareStatement("delete from orders where id= ?");
						ps.setInt(1, idOrder.get(i));
						result = ps.executeUpdate();
					}
					else {
					try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE orders SET total = total - ? WHERE id = ?")) {
		                updateStatement.setInt(1, quantity.get(i)*book.getPrice()*105/100 );
		                updateStatement.setInt(2, idOrder.get(i));
		                int rowsUpdated = updateStatement.executeUpdate();
		                // Kiểm tra số hàng bị ảnh hưởng bởi truy vấn UPDATE
		            }
					}
				}	
				ps = connection.prepareStatement("delete from book where id= ?");
				ps.setInt(1, id);
				result = ps.executeUpdate();
				ps.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

}
