package domain;

public class ClientInput {

    public String mail;
    public String username;
    public String password;

    public ClientInput(String mail, String username, String password) {
        this.mail = mail;
        this.username = username;
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
