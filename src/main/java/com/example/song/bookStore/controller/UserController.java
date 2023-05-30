package com.example.song.bookStore.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.song.bookStore.entity.User;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class UserController {
	
	@PostMapping("/login")
	public ResponseEntity<ResponseObject> login(@RequestParam("email") String email,
	                                            @RequestParam("password") String password,
	                                            HttpSession session) {
	    Connection connection = null;
	    PreparedStatement ps = null;
	    ResultSet result = null;
	    User user = new User();
	    boolean haveUser = false;
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");
	        ps = connection.prepareStatement("select * from user where email = ?");
	        ps.setString(1, email);
	        result = ps.executeQuery();
	        while (result.next()) {
	            haveUser = true;
	            user.setId(result.getInt("id"));
	            user.setName(result.getString("name"));
	            user.setPassword(result.getString("password"));
	            user.setEmail(result.getString("email"));
	            user.setRole(result.getString("role"));
	        }
	        connection.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    if (haveUser) {
	        if (user.getRole().equalsIgnoreCase("admin")) {
	            if (password.equals(user.getPassword())) {
	                session.setAttribute("email", email);
	                return ResponseEntity.status(HttpStatus.OK).body(
	                        new ResponseObject("ok", "Đăng nhập thành công với role là admin", user)
	                );
	            } else {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
	                        new ResponseObject("failed", "Sai email hoặc mật khẩu", "")
	                );
	            }
	        } else {
	            if (password.equals(user.getPassword())) {
	                session.setAttribute("email", email);
	                return ResponseEntity.status(HttpStatus.OK).body(
	                        new ResponseObject("ok", "Đăng nhập thành công với role là user", user)
	                );
	            } else {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
	                        new ResponseObject("failed", "Sai email hoặc mật khẩu", "")
	                );
	            }
	        }
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
	                new ResponseObject("failed", "Sai email hoặc mật khẩu", "")
	        );
	    }
	}
	@PostMapping("/sign-up")
	public ResponseEntity<ResponseObject> signUp(@RequestBody User user) {
		
	    String name = user.getName();
	    String password = user.getPassword();
	    String email = user.getEmail();
	    String role = user.getRole();
	    Connection connection = null;
	    PreparedStatement ps = null;
	    ResultSet result = null;
	    boolean haveUser= false;
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "11102002");
	        ps = connection.prepareStatement("select * from user where email = ?");
	        ps.setString(1, email);
	        result = ps.executeQuery();
	        while(result.next()) {
	        	haveUser=true;
	        }
	     // Thêm người dùng vào CSDL
	        if(haveUser) {
	        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
		                new ResponseObject("failed", "email đã tồn tại", ""));
	        }
	        try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO user(name,password,email,role)  VALUES (?, ?, ?, ?)")) {
	            insertStatement.setString(1, name);
	            insertStatement.setString(2, password);
	            insertStatement.setString(3, email);
	            insertStatement.setString(4, role);
	            int rowsInserted = insertStatement.executeUpdate();
	            
	            
	        }
	        connection.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	        System.out.println("thành công");
	        return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Đăng kí thành công", user)
            );    
	}
}
