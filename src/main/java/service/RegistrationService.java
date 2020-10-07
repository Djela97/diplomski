package service;

import domain.Client;
import domain.ClientInput;
import domain.Folder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class RegistrationService {

    @Inject
    EntityManager em;

    @Transactional
    public Client register(ClientInput clientInput){
        Client c = null;
        try {
            c = (Client) em.createQuery("SELECT t FROM Client t WHERE t.mail = :value1")
                    .setParameter("value1", clientInput.getMail())
                    .getSingleResult();
        } catch (NoResultException nre){
            Client client = new Client();
            client.setMail(clientInput.getMail());
            client.setUsername(clientInput.getUsername());
            client.setPassword(clientInput.getPassword());
            Folder inbox = new Folder();
            inbox.setFolderName("inbox");
            inbox.setClient(client);
            Folder sent = new Folder();
            sent.setFolderName("sent");
            sent.setClient(client);
            Folder important = new Folder();
            important.setFolderName("important");
            important.setClient(client);
            Folder deleted = new Folder();
            deleted.setFolderName("trash");
            deleted.setClient(client);
            Set<Folder> folders = new HashSet<>();
            folders.add(inbox);
            folders.add(sent);
            folders.add(important);
            folders.add(deleted);
            client.setFolders(folders);
            em.persist(inbox);
            em.persist(important);
            em.persist(sent);
            em.persist(deleted);
            em.persist(client);
            return em.find(Client.class, client.getId());
        }
        return null;
    }

}
