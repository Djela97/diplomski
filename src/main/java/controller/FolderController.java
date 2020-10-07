package controller;

import domain.Folder;
import domain.FolderInput;
import domain.Message;
import domain.MessageInput;
import service.FolderService;
import service.MessageService;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/folders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FolderController {

    @Inject
    FolderService service;

    public FolderController(){};

    @GET
    @Path("/list")
    public List<Folder> messageList(@QueryParam("username") String username){
        try{
            service.list(username);
            return service.list(username);
        } catch (NoResultException nre){
            return new ArrayList<>();
        }
    }

    @POST
    @Path("/create")
    public Response createFolder(@QueryParam("username") String username, FolderInput folderInput){
        Boolean folderCreated = service.createFolder(username, folderInput);
        if (folderCreated){
            return Response.status(200).entity("Folder created").build();
        }
        return Response.status(404).entity("Folder wasn't created. Folder with same name already exists").build();
    }

    @POST
    @Path("/delete")
    public Response sendMessage(@QueryParam("username") String username, FolderInput folderInput){
        service.deleteFolder(username, folderInput);
        return Response.status(200).build();
    }
}
