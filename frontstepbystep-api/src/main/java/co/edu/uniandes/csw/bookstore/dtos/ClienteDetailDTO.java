/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.dtos;

import co.edu.uniandes.csw.bookstore.entities.AuthorEntity;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.ClienteEntity;
import co.edu.uniandes.csw.bookstore.entities.PrizeEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author na.tobo
 */
public class ClienteDetailDTO extends ClienteDTO implements Serializable
{
    // relación  cero o muchos libros
    private List<BookDTO> books;


    public ClienteDetailDTO() {
        super();
    }

    /**
     * Crea un objeto ClienteDetailDTO a partir de un objeto ClienteEntity
     * incluyendo los atributos de ClienteDTO.
     *
     * @param clienteEntity Entidad ClienteEntity desde la cual se va a crear el
     * nuevo objeto.
     *
     */
    public ClienteDetailDTO(ClienteEntity clienteEntity) {
        super(clienteEntity);
        if (clienteEntity != null) {
            books = new ArrayList<>();
            for (BookEntity entityBooks : clienteEntity.getLibrosComprados()) {
                books.add(new BookDTO(entityBooks));
            }
        }
    }

    /**
     * Convierte un objeto ClienteDetailDTO a ClienteEntity incluyendo los
     * atributos de ClienteDTO.
     *
     * @return Nueva objeto ClienteEntity.
     *
     */
    @Override
    public ClienteEntity toEntity() {
        ClienteEntity clienteEntity = super.toEntity();
        if (books != null) {
            List<BookEntity> booksEntity = new ArrayList<>();
            for (BookDTO dtoBook : books) {
                booksEntity.add(dtoBook.toEntity());
            }
            clienteEntity.setLibrosComprados(booksEntity);
        }
        return clienteEntity;
    }

    /**
     * Obtiene la lista de libros del autor
     *
     * @return the books
     */
    public List<BookDTO> getBooks() {
        return books;
    }

    /**
     * Modifica la lista de libros para el autor
     *
     * @param books the books to set
     */
    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }

   
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
