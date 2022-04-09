package ict.bean;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String type;
    private String email;
    private String username;
    private String password;
    private String status;
    private String message;
    
    public User() {  }
    
    public void setUserId(String userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setUserType(String type) { this.type = type; }
    public void setUserEmail(String email) { this.email = email; }
    public void setUserStatus(String status) { this.status = status; }
    public void setMessage(String message) { this.message = message; }
    public String getUserStatus() { return status; }
    public String getUserEmail() { return email; }
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getUserType() { return type; }
    public String getMessage() { return message; }
}
