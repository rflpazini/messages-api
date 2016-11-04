/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import entity.Users;
import java.util.List;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author rflpazini
 */
@Stateless
@Path("users")
public class UsersFacadeREST extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "MessagesAPIPU")
    private EntityManager em;

    public UsersFacadeREST() {
        super(Users.class);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(String entity) {
        try {
            Gson gson = new Gson();
            Users user = gson.fromJson(entity, Users.class);
            super.create(user);
        } catch (JsonSyntaxException e) {
            System.out.println(e);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void edit(@PathParam("id") Integer id, Users entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String find(@PathParam("id") Integer id) {
        Users user = super.find(id);
        Gson g = new Gson();
        return g.toJson(user);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listAll() {
        List<Users> l = super.findAll();
        Gson g = new Gson();
        return g.toJson(l);
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Users> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @POST
    @Path("auth/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authUser(@PathParam("username") String userName) {
        Gson g = new Gson();
        Query q = em.createQuery("SELECT u FROM Users u WHERE u.userName = :userName");
        q.setParameter("userName", userName);
        try {
            Users user = (Users) q.getSingleResult();
            return Response.ok(g.toJson(user)).build();
        } catch (Exception e) {
            registerNewUser(userName);  
            return Response.status(Response.Status.ACCEPTED).build();
        }
    }

    private void registerNewUser(String userName) {
        Users user = new Users();
        user.setUserName(userName);
        user.setUserIp(getToken());
        super.create(user);
    }

    private String getToken() {
        UUID token = UUID.randomUUID();
        return token.toString();
    }
}
