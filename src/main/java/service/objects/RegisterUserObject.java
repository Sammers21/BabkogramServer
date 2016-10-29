package service.objects;


public class RegisterUserObject {

    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {

        return username;
    }

    public RegisterUserObject(String username, String password) {

        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "RegisterUserObject{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public RegisterUserObject() {

    }
}
