/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.persistence;

import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.ClienteEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * Clase que maneja la persistencia de cliente.Se conecta a través del Entity
 * Manager de javax.persistance con la base de datos SQL.
 * @author na.tobo
 */
@Stateless
public class ClientePersistence 
{
    private static final Logger LOGGER = Logger.getLogger(ClientePersistence.class.getName());
    
    @PersistenceContext(unitName = "BookStorePU")
    protected EntityManager em;
    
     /**
     * Método para persistir la entidad en la base de datos.
     *
     * @param clienteEntity objeto cliente que se creará en la base de datos
     * @return devuelve la entidad creada con un id dado por la base de datos.
     */
    public ClienteEntity create(ClienteEntity clienteEntity) {
        LOGGER.log(Level.INFO, "Creando un cliente nuevo");
        em.persist(clienteEntity);
        LOGGER.log(Level.INFO, "cliente creado");
        return clienteEntity;
    }

    /**
     * Devuelve todos los clientes de la base de datos.
     *
     * @return una lista con todos los clientes que encuentre en la base de datos,
     * "select u from ClienteEntity u" es como un "select * from ClienteEntity;" -
     * "SELECT * FROM table_name" en SQL.
     */
    public List<ClienteEntity> findAll() {
        LOGGER.log(Level.INFO, "Consultando todos los clientes");
        Query q = em.createQuery("select u from ClienteEntity u");
        return q.getResultList();
    }

    /**
     * Busca si hay algun cliente con el id que se envía de argumento
     *
     * @param clientesId: id correspondiente al libro buscado.
     * @return un libro.
     */
    public ClienteEntity find(Long clientesId) {
        LOGGER.log(Level.INFO, "Consultando el cliente con id={0}", clientesId);
        return em.find(ClienteEntity.class, clientesId);
    }

    /**
     * Actualiza un cliente.
     *
     * @param clienteEntity: el cliente que viene con los nuevos cambios. Por ejemplo
     * el nombre pudo cambiar. En ese caso, se haria uso del método update.
     * @return un libro con los cambios aplicados.
     */
    public ClienteEntity update(ClienteEntity clienteEntity) {
        LOGGER.log(Level.INFO, "Actualizando el cliente con id={0}", clienteEntity.getId());
        return em.merge(clienteEntity);
    }

    /**
     *
     * Borra un cliente de la base de datos recibiendo como argumento el id del
     * cliente
     *
     * @param clientesId: id correspondiente al cliente a borrar.
     */
    public void delete(Long clientesId) {
        LOGGER.log(Level.INFO, "Borrando el cliente con id={0}", clientesId);
        ClienteEntity clienteEntity = em.find(ClienteEntity.class, clientesId);
        em.remove(clienteEntity);
    }
    /**
     * Busca si hay algun cliente con el Nombre que se envía de argumento
     *
     * @param nombre: nombre del cliente que se está buscando
     * @return null si no existe ningun cliente con el nombre del argumento. Si
     * existe alguno devuelve el primero.
     */
    public ClienteEntity findByNombre(String nombre) {
        LOGGER.log(Level.INFO, "Consultando clientes por nombre ", nombre);
        // Se crea un query para buscar clientes con el isbn que recibe el método como argumento. ":isbn" es un placeholder que debe ser remplazado
        TypedQuery query = em.createQuery("Select e From ClienteEntity e where e.nombre = :nombre", ClienteEntity.class);
        // Se remplaza el placeholder ":nombre" con el valor del argumento 
        query = query.setParameter("nombre", nombre);
        // Se invoca el query se obtiene la lista resultado
        List<ClienteEntity> sameNombre = query.getResultList();
        ClienteEntity result;
        if (sameNombre  == null) {
            result = null;
        } else if (sameNombre.isEmpty()) {
            result = null;
        } else {
            result = sameNombre.get(0);
        }
        LOGGER.log(Level.INFO, "Saliendo de consultar clientes por nombre ", nombre);
        return result;
    }
}
