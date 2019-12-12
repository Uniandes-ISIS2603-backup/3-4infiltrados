/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.resources;

import co.edu.uniandes.csw.bookstore.dtos.ClienteDTO;
import co.edu.uniandes.csw.bookstore.dtos.ClienteDetailDTO;
import co.edu.uniandes.csw.bookstore.ejb.ClienteLogic;
import co.edu.uniandes.csw.bookstore.entities.ClienteEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author JuanDavidSerrano
 */
@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class ClienteResource {
    
    private static final Logger LOGGER = Logger.getLogger(ClienteResource.class.getName());
    
    @Inject
    private ClienteLogic clienteLogic;
    
    @POST
    public ClienteDTO createCliente(ClienteDTO cliente) throws BusinessLogicException{
        LOGGER.log(Level.INFO, "ClienteResource createCliente: input: {0}", cliente);
        ClienteDTO clienteDTO = new ClienteDTO(clienteLogic.createCliente(cliente.toEntity()));
        LOGGER.log(Level.INFO, "ClienteResource createCliente: output: {0}", clienteDTO);
        return clienteDTO;
    }
    
    @GET
    public List<ClienteDetailDTO> getClientes() {
        LOGGER.info("ClienteResource getCliente: input: void");
        List<ClienteDetailDTO> listaClientes = listEntity2DTO(clienteLogic.getClientes());
        LOGGER.log(Level.INFO, "ClienteResource getClientes: output: {0}", listaClientes);
        return listaClientes;
    }
    
    @PUT
    @Path("{clientesId: \\d+}")
    public ClienteDetailDTO updateCliente(@PathParam("clientesId") Long clientesId, ClienteDetailDTO cliente) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "ClienteResource updateCliente: input: clientesId: {0} , cliente: {1}", new Object[]{clientesId, cliente});
        cliente.setId(clientesId);
        if (clienteLogic.getCliente(clientesId) == null) {
            throw new WebApplicationException("El recurso /clientes/" + clientesId + " no existe.", 404);
        }
        ClienteDetailDTO detailDTO = new ClienteDetailDTO(clienteLogic.updateCliente(clientesId, cliente.toEntity()));
        LOGGER.log(Level.INFO, "AuthorResource updateAuthor: output: {0}", detailDTO);
        return detailDTO;
    }
    
    @DELETE
    @Path("{clientesId: \\d+}")
    public void deleteCliente(@PathParam("clientesId") Long clientesId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "ClienteResource deleteCliente: input: {0}", clientesId);
        if (clienteLogic.getCliente(clientesId) == null) {
            throw new WebApplicationException("El recurso /clientes/" + clientesId + " no existe.", 404);
        }
        clienteLogic.deleteCliente(clientesId);
        LOGGER.info("ClienteResource deleteCliente: output: void");
    }
    
    /**
     * Conexión con el servicio de libros para un cliente.
     * {@link ClienteBooksResource}
     *
     * Este método conecta la ruta de /clientes con las rutas de /books que
     * dependen del cliente, es una redirección al servicio que maneja el segmento
     * de la URL que se encarga de los libros.
     *
     * @param clientesId El ID del cliente con respecto al cual se accede al
     * servicio.
     * @return El servicio de Libros para ese cliente en paricular.
     */
    @Path("{clientesId: \\d+}/books")
    public Class<ClienteBooksResource> getClienteBooksResource(@PathParam("clientesId") Long clientesId) {
        if (clienteLogic.getCliente(clientesId) == null) {
            throw new WebApplicationException("El recurso /clientes/" + clientesId + " no existe.", 404);
        }
        return ClienteBooksResource.class;
    }
    
    
    private List<ClienteDetailDTO> listEntity2DTO(List<ClienteEntity> entityList) {
        List<ClienteDetailDTO> list = new ArrayList<>();
        for (ClienteEntity entity : entityList) {
            list.add(new ClienteDetailDTO(entity));
        }
        return list;
    }
    
}
