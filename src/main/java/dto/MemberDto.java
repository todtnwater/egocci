package dto;

public class MemberDto {
    private int no;
    private String email;
    private String password;
    private String name;
    private String phone1;
    private String phone2;
    private String phone3;
    private String gender;
    private String role;
    private int email_verified;
    private String created_at;
    private String updated_at;
    private int exited;

    public MemberDto() {}

    public MemberDto(String email, String password, String name, String phone1, String phone2, String phone3, String gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.phone3 = phone3;
        this.gender = gender;
    }

    // Getters and Setters
    public int getNo() { return no; }
    public void setNo(int no) { this.no = no; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone1() { return phone1; }
    public void setPhone1(String phone1) { this.phone1 = phone1; }

    public String getPhone2() { return phone2; }
    public void setPhone2(String phone2) { this.phone2 = phone2; }

    public String getPhone3() { return phone3; }
    public void setPhone3(String phone3) { this.phone3 = phone3; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getEmail_verified() { return email_verified; }
    public void setEmail_verified(int email_verified) { this.email_verified = email_verified; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }

    public int getExited() { return exited; }
    public void setExited(int exited) { this.exited = exited; }
}