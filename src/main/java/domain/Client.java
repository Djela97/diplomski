package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Set;

@Entity
public class Client {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    public String mail;
    @Column(unique = true)
    public String username;
    private String password;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "client_folders",
//    joinColumns = @JoinColumn(name = "client_id"),
//    inverseJoinColumns = @JoinColumn(name = "folder_id"))
//    @JsonIgnore
//    public Set<Folder> folders;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    public Set<Folder> folders;

    public Client() {
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Transactional
    public Set<Folder> getFolders() {
        return folders;
    }

    public void setFolders(Set<Folder> folders) {
        this.folders = folders;
    }
}
