package controller;

import domain.Message;
import domain.MessageInput;
import service.MessageService;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.PostLoad;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageController {

    @Inject
    MessageService service;

    public MessageController(){}

    @POST
    @Path("/send")
    public Response sendMessage(MessageInput messageInput){
        Boolean messageSent = service.sendMessage(messageInput);
        if (messageSent){
            return Response.status(200).entity("Message sent").build();
        }
        return Response.status(404).entity("Message wasn't sent. Receiver mail not found").build();
    }

    @GET
    @Path("/list")
    public List<Message> messageList(@QueryParam("username") String username){
        try{
            service.list(username);
            return service.list(username);
        } catch (NoResultException nre){
            return new ArrayList<>();
        }
    }

    @POST
    @Path("/read")
    public Response markAsReadMessage(MessageInput messageInput){
        service.markAsReadMessage(messageInput);
        return Response.ok().status(200).build();
    }

    @POST
    @Path("/unread")
    public Response markAsUnreadMessage(MessageInput messageInput){
        service.markAsUnreadMessage(messageInput);
        return Response.ok().status(200).build();
    }

    @POST
    @Path("/delete")
    public Response markAsDeletedMessage(MessageInput messageInput){
        service.markAsDeletedMessage(messageInput);
        return Response.ok().status(200).build();
    }

    @POST
    @Path("/move")
    public Response moveMessage(@QueryParam("username") String username,@QueryParam("foldername") String folderName,
                                MessageInput messageInput){
        service.moveMessage(username, folderName, messageInput);
        return Response.ok().status(200).build();
    }
}

