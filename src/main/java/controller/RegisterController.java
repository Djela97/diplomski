package controller;

import domain.Client;
import domain.ClientInput;
import service.RegistrationService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegisterController {

    @Inject
    RegistrationService service;

    public RegisterController(){}
    @POST
    public Response registerClient(ClientInput clientInput){
        Client c = service.register(clientInput);
        if (c == null){
            return Response.status(409).entity("User already exists").build();
        }
        return Response.status(201).entity(c).build();
    }

}
