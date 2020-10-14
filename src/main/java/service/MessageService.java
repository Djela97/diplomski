package service;

import domain.Client;
import domain.Folder;
import domain.Message;
import domain.MessageInput;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Set;

@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
public class MessageService {

    @Inject
    EntityManager em;

    @Transactional
    public Boolean sendMessage(MessageInput messageInput){
        try{
            Client sender = (Client) em.createQuery("SELECT t FROM Client t WHERE t.mail = :value1")
                    .setParameter("value1", messageInput.getSenderMail().trim())
                    .getSingleResult();
            Client receiver = (Client) em.createQuery("SELECT t FROM Client t WHERE t.mail = :value1")
                    .setParameter("value1", messageInput.getReceiverMail().trim())
                    .getSingleResult();

            Set<Folder> folderSet = sender.getFolders();
            Set<Folder> folderSet1 = receiver.getFolders();

            Message m = new Message();
            m.setContent(messageInput.getContent());
            m.setSubject(messageInput.getSubject());
            m.setSender(sender);
            m.setReceiver(receiver);
            m.setDeleted(false);
            m.setImportant(false);
            m.setRead(false);
            em.persist(m);

            Message mes = (Message) em.createQuery("SELECT m FROM Message m WHERE m.subject = :value1" +
                    " AND m.receiver.mail = :value2 AND m.content = :value3 AND m.sender.mail = :value4")
                    .setParameter("value1", m.getSubject())
                    .setParameter("value2", m.getReceiver().getMail())
                    .setParameter("value3", m.getContent())
                    .setParameter("value4", m.getSender().getMail())
                    .getSingleResult();

            for(Folder f : folderSet){
                if(f.folderName.equals("sent")){
                    f.messageList.add(mes);
                    em.persist(f);
                }
            }
            for(Folder f : folderSet1) {
                if (f.folderName.equals("inbox")) {
                    f.messageList.add(mes);
                    em.persist(f);
                }
            }
            return true;
        } catch (NoResultException nre){
            return false;
        }
    }

    @Consumes("application/text")
    public List<Message> list(String username){
        System.out.println(username);
        Client c = (Client) em.createQuery("SELECT t FROM Client t WHERE t.username = :value1")
                .setParameter("value1", username)
                .getSingleResult();
        List<Message> messages = (List<Message>)em.createQuery("SELECT m FROM Message m WHERE m.receiver = " +
                ":value1 OR m.sender = :value1")
                .setParameter("value1", c)
                .getResultList();
        return messages;
    }

    @Transactional
    public void markAsReadMessage(MessageInput messageInput) {
        Message mes = (Message) em.createQuery("SELECT m FROM Message m WHERE m.subject = :value1" +
                " AND m.receiver.mail = :value2 AND m.content = :value3 AND m.sender.mail = :value4")
                .setParameter("value1", messageInput.getSubject())
                .setParameter("value2", messageInput.getReceiverMail())
                .setParameter("value3", messageInput.getContent())
                .setParameter("value4", messageInput.getSenderMail())
                .getSingleResult();
        mes.setRead(true);
        em.persist(mes);
    }

    @Transactional
    public void markAsDeletedMessage(MessageInput messageInput) {
        Message mes = (Message) em.createQuery("SELECT m FROM Message m WHERE m.subject = :value1" +
                " AND m.receiver.mail = :value2 AND m.content = :value3 AND m.sender.mail = :value4")
                .setParameter("value1", messageInput.getSubject())
                .setParameter("value2", messageInput.getReceiverMail())
                .setParameter("value3", messageInput.getContent())
                .setParameter("value4", messageInput.getSenderMail())
                .getSingleResult();
        mes.setDeleted(!mes.getDeleted());
        em.persist(mes);
    }

    @Transactional
    public void markAsUnreadMessage(MessageInput messageInput) {
        Message mes = (Message) em.createQuery("SELECT m FROM Message m WHERE m.subject = :value1" +
                " AND m.receiver.mail = :value2 AND m.content = :value3 AND m.sender.mail = :value4")
                .setParameter("value1", messageInput.getSubject())
                .setParameter("value2", messageInput.getReceiverMail())
                .setParameter("value3", messageInput.getContent())
                .setParameter("value4", messageInput.getSenderMail())
                .getSingleResult();
        mes.setRead(false);
        em.persist(mes);
    }

    @Transactional
    public void markAsImportantMessage(MessageInput messageInput) {
        Message mes = (Message) em.createQuery("SELECT m FROM Message m WHERE m.subject = :value1" +
                " AND m.receiver.mail = :value2 AND m.content = :value3 AND m.sender.mail = :value4")
                .setParameter("value1", messageInput.getSubject())
                .setParameter("value2", messageInput.getReceiverMail())
                .setParameter("value3", messageInput.getContent())
                .setParameter("value4", messageInput.getSenderMail())
                .getSingleResult();
        mes.setImportant(true);
        em.persist(mes);
    }

    @Transactional
    public void markAsUnimportantMessage(MessageInput messageInput) {
        Message mes = (Message) em.createQuery("SELECT m FROM Message m WHERE m.subject = :value1" +
                " AND m.receiver.mail = :value2 AND m.content = :value3 AND m.sender.mail = :value4")
                .setParameter("value1", messageInput.getSubject())
                .setParameter("value2", messageInput.getReceiverMail())
                .setParameter("value3", messageInput.getContent())
                .setParameter("value4", messageInput.getSenderMail())
                .getSingleResult();
        mes.setImportant(false);
        em.persist(mes);
    }

    @Transactional
    public void moveMessage(String username, String folderName, MessageInput messageInput){
        Client c = (Client) em.createQuery("SELECT t FROM Client t WHERE t.username = :value1")
                .setParameter("value1", username)
                .getSingleResult();
        Message mes = (Message) em.createQuery("SELECT m FROM Message m WHERE m.subject = :value1" +
                " AND m.receiver.mail = :value2 AND m.content = :value3 AND m.sender.mail = :value4")
                .setParameter("value1", messageInput.getSubject())
                .setParameter("value2", messageInput.getReceiverMail())
                .setParameter("value3", messageInput.getContent())
                .setParameter("value4", messageInput.getSenderMail())
                .getSingleResult();
        Folder folder = null;
        for(Folder f : c.getFolders()){
            if (f.getMessageList().contains(mes)){
                folder = f;
            }
        }

        folder.getMessageList().remove(mes);
        em.persist(folder);

        for(Folder f : c.getFolders()){
            if(f.folderName.equals(folderName)){
                f.messageList.add(mes);
                em.persist(f);
            }
        }
    }
}
