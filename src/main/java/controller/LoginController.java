package controller;

import domain.Client;
import domain.ClientInput;
import service.LoginService;
import service.RegistrationService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginController {

    @Inject
    LoginService service;

    public LoginController(){}

    @POST
    public Response loginClient(ClientInput clientInput){
        Client c = service.login(clientInput);
        if (c == null){
            return Response.status(404).entity("Credentials are bad").build();
        }
        return Response.status(200).entity(c).build();
    }

}
