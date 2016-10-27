/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import entity.Messages;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author rflpazini
 */
@Stateless
@Path("messages")
public class MessagesFacadeREST extends AbstractFacade<Messages> {

    @PersistenceContext(unitName = "MessagesAPIPU")
    private EntityManager em;

    public MessagesFacadeREST() {
        super(Messages.class);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(String entity) {
        try {
            Gson g = new Gson();
            Messages msg = g.fromJson(entity, Messages.class);
        } catch (JsonSyntaxException e) {
            System.out.println(e);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Messages entity) {
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
        Messages msg = super.find(id);
        Gson g = new Gson();
        return g.toJson(msg);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listAll() {
        List<Messages> l = super.findAll();
        Gson g = new Gson();
        return g.toJson(l);
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Messages> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    
}
