/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.ejb;

import co.edu.uniandes.csw.bookstore.entities.AuthorEntity;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.ClienteEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.bookstore.persistence.AuthorPersistence;
import co.edu.uniandes.csw.bookstore.persistence.BookPersistence;
import co.edu.uniandes.csw.bookstore.persistence.ClientePersistence;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *Clase que representa la realcion entre clientes y libros 
 * @author na.tobo
 */
@Stateless
public class ClienteBooksLogic 
{
    private static final Logger LOGGER = Logger.getLogger(ClienteBooksLogic.class.getName());

    @Inject
    private BookPersistence bookPersistence;

    @Inject
    private ClientePersistence clientePersistence;

    /**
     * Asocia un Book existente a un Cliente
     *
     * @param clientesId Identificador de la instancia de Cliente
     * @param booksId Identificador de la instancia de Book
     * @return Instancia de BookEntity que fue asociada a Cliente
     */
    public BookEntity addBook(Long clientesId, Long booksId) {
        LOGGER.log(Level.INFO, "Inicia proceso de asociarle un libro al autor con id = {0}", clientesId);
        ClienteEntity clienteEntity = clientePersistence.find(clientesId);
        BookEntity bookEntity = bookPersistence.find(booksId);
        bookEntity.getClientes().add(clienteEntity);
        LOGGER.log(Level.INFO, "Termina proceso de asociarle un libro al autor con id = {0}", clientesId);
        return bookPersistence.find(booksId);
    }

    /**
     * Obtiene una colección de instancias de BookEntity asociadas a una
     * instancia de Cliente
     *
     * @param clientesId Identificador de la instancia de Cliente
     * @return Colección de instancias de BookEntity asociadas a la instancia de
     * Cliente
     */
    public List<BookEntity> getBooks(Long clientesId) {
        LOGGER.log(Level.INFO, "Inicia proceso de consultar todos los libros del autor con id = {0}", clientesId);
        return clientePersistence.find(clientesId).getLibrosComprados();
    }

    /**
     * Obtiene una instancia de BookEntity asociada a una instancia de Cliente
     *
     * @param clientesId Identificador de la instancia de Cliente
     * @param booksId Identificador de la instancia de Book
     * @return La entidadd de Libro del autor
     * @throws BusinessLogicException Si el libro no está asociado al autor
     */
    public BookEntity getBook(Long clientesId, Long booksId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Inicia proceso de consultar el libro con id = {0} del autor con id = " + clientesId, booksId);
        List<BookEntity> books = clientePersistence.find(clientesId).getLibrosComprados();
        BookEntity bookEntity = bookPersistence.find(booksId);
        int index = books.indexOf(bookEntity);
        LOGGER.log(Level.INFO, "Termina proceso de consultar el libro con id = {0} del autor con id = " + clientesId, booksId);
        if (index >= 0) {
            return books.get(index);
        }
        throw new BusinessLogicException("El libro no está asociado al autor");
    }

    /**
     * Remplaza las instancias de Book asociadas a una instancia de Cliente
     *
     * @param clienteId Identificador de la instancia de Cliente
     * @param books Colección de instancias de BookEntity a asociar a instancia
     * de Cliente
     * @return Nueva colección de BookEntity asociada a la instancia de Cliente
     */
    public List<BookEntity> replaceBooks(Long clienteId, List<BookEntity> books) {
        LOGGER.log(Level.INFO, "Inicia proceso de reemplazar los libros asocidos al cliente con id = {0}", clienteId);
        ClienteEntity clienteEntity = clientePersistence.find(clienteId);
        List<BookEntity> bookList = bookPersistence.findAll();
        for (BookEntity book : bookList) {
            if (books.contains(book)) {
                if (!book.getClientes().contains(clienteEntity)) {
                    book.getClientes().add(clienteEntity);
                }
            } else {
                book.getClientes().remove(clienteEntity);
            }
        }
        clienteEntity.setLibrosComprados(books);
        LOGGER.log(Level.INFO, "Termina proceso de reemplazar los libros asocidos al cliente con id = {0}", clienteId);
        return clienteEntity.getLibrosComprados();
    }

    /**
     * Desasocia un Book existente de un Cliente existente
     *
     * @param clientesId Identificador de la instancia de Cliente
     * @param booksId Identificador de la instancia de Book
     */
    public void removeBook(Long clientesId, Long booksId) {
        LOGGER.log(Level.INFO, "Inicia proceso de borrar un libro del cliente con id = {0}", clientesId);
        ClienteEntity clienteEntity = clientePersistence.find(clientesId);
        BookEntity bookEntity = bookPersistence.find(booksId);
        bookEntity.getClientes().remove(clienteEntity);
        LOGGER.log(Level.INFO, "Termina proceso de borrar un libro del cliente con id = {0}", clientesId);
    }
}
