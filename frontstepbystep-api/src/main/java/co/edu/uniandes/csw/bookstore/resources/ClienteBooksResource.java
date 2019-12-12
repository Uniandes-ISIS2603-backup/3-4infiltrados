/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.resources;

import co.edu.uniandes.csw.bookstore.dtos.BookDetailDTO;
import co.edu.uniandes.csw.bookstore.ejb.AuthorBooksLogic;
import co.edu.uniandes.csw.bookstore.ejb.BookLogic;
import co.edu.uniandes.csw.bookstore.ejb.ClienteBooksLogic;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @author na.tobo
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteBooksResource 
{
    private static final Logger LOGGER = Logger.getLogger(ClienteBooksResource.class.getName());

    @Inject
    private ClienteBooksLogic clienteBookLogic;

    @Inject
    private BookLogic bookLogic; // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.

    /**
     * Asocia un libro existente con un cliente existente
     *
     * @param clientesId El ID del cliente al cual se le va a asociar el libro
     * @param booksId El ID del libro que se asocia
     * @return JSON {@link BookDetailDTO} - El libro asociado.
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro.
     */
    @POST
    @Path("{booksId: \\d+}")
    public BookDetailDTO addBook(@PathParam("clientesId") Long clientesId, @PathParam("booksId") Long booksId) {
        LOGGER.log(Level.INFO, "ClienteBooksResource addBook: input: clientesId {0} , booksId {1}", new Object[]{clientesId, booksId});
        if (bookLogic.getBook(booksId) == null) {
            throw new WebApplicationException("El recurso /books/" + booksId + " no existe.", 404);
        }
        BookDetailDTO detailDTO = new BookDetailDTO(clienteBookLogic.addBook(clientesId, booksId));
        LOGGER.log(Level.INFO, "ClienteBooksResource addBook: output: {0}", detailDTO);
        return detailDTO;
    }

    /**
     * Busca y devuelve todos los libros que existen en un cliente.
     *
     * @param clientesId El ID del cliente del cual se buscan los libros
     * @return JSONArray {@link BookDetailDTO} - Los libros encontrados en el
     * cliente. Si no hay ninguno retorna una lista vacía.
     */
    @GET
    public List<BookDetailDTO> getBooks(@PathParam("clientesId") Long clientesId) {
        LOGGER.log(Level.INFO, "ClienteBooksResource getBooks: input: {0}", clientesId);
        List<BookDetailDTO> lista = booksListEntity2DTO(clienteBookLogic.getBooks(clientesId));
        LOGGER.log(Level.INFO, "ClienteBooksResource getBooks: output: {0}", lista);
        return lista;
    }

    /**
     * Busca y devuelve el libro con el ID recibido en la URL, relativo a un
     * cliente.
     *
     * @param clientesId El ID del cliente del cual se busca el libro
     * @param booksId El ID del libro que se busca
     * @return {@link BookDetailDTO} - El libro encontrado en el cliente.
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     * si el libro no está asociado al cliente
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro.
     */
    @GET
    @Path("{booksId: \\d+}")
    public BookDetailDTO getBook(@PathParam("clientesId") Long clientesId, @PathParam("booksId") Long booksId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "ClienteBooksResource getBook: input: clientesId {0} , booksId {1}", new Object[]{clientesId, booksId});
        if (bookLogic.getBook(booksId) == null) {
            throw new WebApplicationException("El recurso /books/" + booksId + " no existe.", 404);
        }
        BookDetailDTO detailDTO = new BookDetailDTO(clienteBookLogic.getBook(clientesId, booksId));
        LOGGER.log(Level.INFO, "ClienteBooksResource getBook: output: {0}", detailDTO);
        return detailDTO;
    }

    /**
     * Actualiza la lista de libros de un cliente con la lista que se recibe en el
     * cuerpo
     *
     * @param clientesId El ID del cliente al cual se le va a asociar el libro
     * @param books JSONArray {@link BookDetailDTO} - La lista de libros que se
     * desea guardar.
     * @return JSONArray {@link BookDetailDTO} - La lista actualizada.
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro.
     */
    @PUT
    public List<BookDetailDTO> replaceBooks(@PathParam("clientesId") Long clientesId, List<BookDetailDTO> books) {
        LOGGER.log(Level.INFO, "ClienteBooksResource replaceBooks: input: clientesId {0} , books {1}", new Object[]{clientesId, books});
        for (BookDetailDTO book : books) {
            if (bookLogic.getBook(book.getId()) == null) {
                throw new WebApplicationException("El recurso /books/" + book.getId() + " no existe.", 404);
            }
        }
        List<BookDetailDTO> lista = booksListEntity2DTO(clienteBookLogic.replaceBooks(clientesId, booksListDTO2Entity(books)));
        LOGGER.log(Level.INFO, "ClienteBooksResource replaceBooks: output: {0}", lista);
        return lista;
    }

    /**
     * Elimina la conexión entre el libro y e cliente recibidos en la URL.
     *
     * @param clientesId El ID del cliente al cual se le va a desasociar el libro
     * @param booksId El ID del libro que se desasocia
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro.
     */
    @DELETE
    @Path("{booksId: \\d+}")
    public void removeBook(@PathParam("clientesId") Long clientesId, @PathParam("booksId") Long booksId) {
        LOGGER.log(Level.INFO, "ClienteBooksResource deleteBook: input: clientesId {0} , booksId {1}", new Object[]{clientesId, booksId});
        if (bookLogic.getBook(booksId) == null) {
            throw new WebApplicationException("El recurso /books/" + booksId + " no existe.", 404);
        }
        clienteBookLogic.removeBook(clientesId, booksId);
        LOGGER.info("ClienteBooksResource deleteBook: output: void");
    }

    /**
     * Convierte una lista de BookEntity a una lista de BookDetailDTO.
     *
     * @param entityList Lista de BookEntity a convertir.
     * @return Lista de BookDetailDTO convertida.
     */
    private List<BookDetailDTO> booksListEntity2DTO(List<BookEntity> entityList) {
        List<BookDetailDTO> list = new ArrayList<>();
        for (BookEntity entity : entityList) {
            list.add(new BookDetailDTO(entity));
        }
        return list;
    }

    /**
     * Convierte una lista de BookDetailDTO a una lista de BookEntity.
     *
     * @param dtos Lista de BookDetailDTO a convertir.
     * @return Lista de BookEntity convertida.
     */
    private List<BookEntity> booksListDTO2Entity(List<BookDetailDTO> dtos) {
        List<BookEntity> list = new ArrayList<>();
        for (BookDetailDTO dto : dtos) {
            list.add(dto.toEntity());
        }
        return list;
    }
}
