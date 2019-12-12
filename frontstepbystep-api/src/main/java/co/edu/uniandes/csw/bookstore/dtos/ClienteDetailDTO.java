/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.dtos;

import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.ClienteEntity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Estudiante
 */
public class ClienteDetailDTO extends ClienteDTO{
    
    private List<BookDTO> librosComprados;
    
    private List<BookDTO> carrito;
    
    public ClienteDetailDTO(){
        super();
    }
    
    public ClienteDetailDTO(ClienteEntity entity){
        super(entity);
        if(entity!=null){
            librosComprados = new ArrayList<>();
            carrito = new ArrayList<>();
            if(entity.getLibrosComprados() != null && entity.getLibrosComprados().size()>0){
                for(BookEntity book: entity.getLibrosComprados()){
                    this.librosComprados.add(new BookDTO(book));
                }
            }
        }
    }
    
    @Override
    public ClienteEntity toEntity() {
        ClienteEntity clienteEntity = super.toEntity();
        if (librosComprados != null) {
            List<BookEntity> booksEntity = new ArrayList<>();
            for (BookDTO dtoBook : librosComprados) {
                booksEntity.add(dtoBook.toEntity());
            }
            clienteEntity.setLibrosComprados(booksEntity);
        }
        return clienteEntity;
    }

    /**
     * @return the librosComprados
     */
    public List<BookDTO> getLibrosComprados() {
        return librosComprados;
    }

    /**
     * @param librosComprados the librosComprados to set
     */
    public void setLibrosComprados(List<BookDTO> librosComprados) {
        this.librosComprados = librosComprados;
    }

    /**
     * @return the carrito
     */
    public List<BookDTO> getCarrito() {
        return carrito;
    }

    /**
     * @param carrito the carrito to set
     */
    public void setCarrito(List<BookDTO> carrito) {
        this.carrito = carrito;
    }
    
}
