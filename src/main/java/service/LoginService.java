package service;

import domain.Client;
import domain.ClientInput;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class LoginService {

    @Inject
    EntityManager em;

    @Transactional
    public Client login(ClientInput clientInput){
        Client c = null;
        try {
            c = (Client) em.createQuery("SELECT t FROM Client t WHERE (t.mail = :value1 OR t.username = :value2) and " +
                    "t.password = :value3")
                    .setParameter("value1", clientInput.getMail())
                    .setParameter("value2", clientInput.getUsername())
                    .setParameter("value3", clientInput.getPassword())
                    .getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
        return c;
    }

}
