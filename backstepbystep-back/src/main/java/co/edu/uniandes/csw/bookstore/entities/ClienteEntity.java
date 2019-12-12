/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import uk.co.jemos.podam.common.PodamExclude;

/**
 *Clase que representa a un cliente de la libreria
 * 
 * @author na.tobo
 */
public class ClienteEntity extends BaseEntity
{
    /**
     * Nombre del cliente
     */
    private String nombre;
    /**
     * correo 
     */
    private String clave;
    /**
     * Saldo que tiene un cliente 
     */
    private Double saldo;
    /**
     * Direccion de la residencia del cliente
     */
    private String direccion;
    /**
     * Correo electronico del cliente
     */
    private String correoElectronico;
    
    @PodamExclude
    @ManyToMany(mappedBy = "clientes")
    private List<BookEntity> librosComprados = new ArrayList<>();
    /**
     * Retorna el nombre del cliente
     * @return 
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * Define el nombre del cliente
     * @param nombre 
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
     /**
     * Retorna la clave del cliente
     * @return clave
     */
    public String getClave() {
        return clave;
    }
     /**
      * Define la clave del cliente
      * @param clave 
      */
    public void setClave(String clave) {
        this.clave = clave;
    }
    /**
     * Retorna el saldo del cliente
     * @return 
     */
    public Double getSaldo() {
        return saldo;
    }
    /**
     * Define el saldo de un cliente
     * @param saldo 
     */
    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
    /**
     * Retorna la direccion del cliente
     * @return 
     */
    public String getDireccion() {
        return direccion;
    }
    /**
     * Define la direccion del cliente
     * @param direccion 
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    /**
     * Retorna le correo electronico del cliente
     * @return 
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    } 
    /**
     * Define el corrreo electronico del cliente
     * @param correoElectronico 
     */
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
    /**
     * Retorna la lista de libros comprados del cliente
     * @return 
     */
    public List<BookEntity> getLibrosComprados() {
        return librosComprados;
    }
    /**
     * Define la lista de libros comprados
     * @param librosComprados 
     */
    public void setLibrosComprados(List<BookEntity> librosComprados) {
        this.librosComprados = librosComprados;
    }
}
