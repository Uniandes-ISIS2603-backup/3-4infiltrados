/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.dtos;

import co.edu.uniandes.csw.bookstore.adapters.DateAdapter;
import co.edu.uniandes.csw.bookstore.entities.ClienteEntity;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ClienteDTO Objeto de transferencia de datos de Cliente. Los DTO contienen las
 * representaciones de los JSON que se transfieren entre el cliente y el
 * servidor.
 *
 * Al serializarse como JSON esta clase implementa el siguiente modelo: <br>
 * <pre>
 *   {
 *      "id":number,
 *      "saldo": number,
 *      "clave": string,
 *      "nombre": string,
 *      "direccion": string,
 *      "correoElectronico": string
 *   }

 * @author na.tobo
 */
public class ClienteDTO implements Serializable
{
    private Long id;
    private Double saldo;
    private String nombre;
    private String clave;
    private String direccion;
    private String correoElectronico;
    
    
     /**
     * Constructor vacio
     */
    public ClienteDTO() {
    }

    /**
     * Crea un objeto ClienteDTO a partir de un objeto ClienteEntity.
     *
     * @param clienteEntity Entidad ClienteEntity desde la cual se va a crear el
     * nuevo objeto.
     *
     */
    public ClienteDTO(ClienteEntity clienteEntity) 
    {
        if (clienteEntity != null) {
            this.id = clienteEntity.getId();
            this.clave=clienteEntity.getClave();
            this.correoElectronico=clienteEntity.getCorreoElectronico();
            this.nombre=clienteEntity.getNombre();
            this.direccion=clienteEntity.getDireccion();
            this.saldo=clienteEntity.getSaldo();

        }
    }

   
     /**
     * Convierte un objeto ClienteDTO a ClienteEntity.
     *
     * @return Nueva objeto ClienteEntity.
     *
     */
    public ClienteEntity toEntity() {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setId(this.getId());
        clienteEntity.setNombre(this.getNombre());
        clienteEntity.setClave(this.clave);
        clienteEntity.setDireccion(this.direccion);
        clienteEntity.setSaldo(this.saldo);
        clienteEntity.setDireccion(this.correoElectronico);
        return clienteEntity;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getCorreoElectronico() {
        return correoElectronico;
    }
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
    
     @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
