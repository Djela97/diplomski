package service;

import domain.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
public class FolderService {

    @Inject
    EntityManager em;

    @Consumes("application/text")
    public List<Folder> list(String username){
        System.out.println(username);
        Client c = (Client) em.createQuery("SELECT t FROM Client t WHERE t.username = :value1")
                .setParameter("value1", username)
                .getSingleResult();
        return new ArrayList<>(c.getFolders());
    }

    @Transactional
    public Boolean createFolder(String username, FolderInput folderInput) {
        Client c = (Client) em.createQuery("SELECT t FROM Client t WHERE t.username = :value1")
                .setParameter("value1", username)
                .getSingleResult();
        for (Folder f : c.getFolders()){
            if(f.getFolderName().equals(folderInput.getFolderName()))
                return false;
        }
        Folder f = new Folder();
        f.setClient(c);
        f.setFolderName(folderInput.getFolderName());
        f.setMessageList(new ArrayList<>());
        em.persist(f);
        c.getFolders().add(f);
        em.persist(c);
        return true;
    }

    @Transactional
    public void deleteFolder(String username, FolderInput folderInput) {
        Client c = (Client) em.createQuery("SELECT t FROM Client t WHERE t.username = :value1")
                .setParameter("value1", username)
                .getSingleResult();
        Folder toDelete = null;
        for (Folder f : c.getFolders()){
            if(f.getFolderName().equals(folderInput.getFolderName()))
                toDelete = f;
        }
        c.getFolders().remove(toDelete);
        em.remove(toDelete);
        em.persist(c);
    }
}
