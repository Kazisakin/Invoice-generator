package org.invoice.domain;

public class User {
    private Long id;
    private String username;
    private String password;
    private String role; // e.g., ADMIN or USER

    public User() {}
    public User(Long i, String u, String p, String r) {
        id = i; username = u; password = p; role = r;
    }

    public Long getId() { return id; }
    public void setId(Long i) { id = i; }

    public String getUsername() { return username; }
    public void setUsername(String u) { username = u; }

    public String getPassword() { return password; }
    public void setPassword(String p) { password = p; }

    public String getRole() { return role; }
    public void setRole(String r) { role = r; }
}
