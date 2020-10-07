package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Folder {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    public String folderName;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "folder_message",
            joinColumns = @JoinColumn(name = "folder_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "message_id", unique = false)
    )

    public List<Message> messageList;

    @OneToOne
    @JsonIgnore
    public Client client;

    public Folder() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
