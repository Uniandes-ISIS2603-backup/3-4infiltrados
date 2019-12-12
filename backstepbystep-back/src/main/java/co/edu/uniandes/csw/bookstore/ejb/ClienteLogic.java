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
import co.edu.uniandes.csw.bookstore.persistence.BookPersistence;
import co.edu.uniandes.csw.bookstore.persistence.ClientePersistence;
import co.edu.uniandes.csw.bookstore.persistence.EditorialPersistence;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Logica que maneja un cliente
 *
 * @author na.tobo
 */
@Stateless
public class ClienteLogic {

    private static final Logger LOGGER = Logger.getLogger(ClienteLogic.class.getName());

    @Inject
    private ClientePersistence persistence;

    /**
     * Guardar un nuevo cliente
     *
     * @param clienteEntity La entidad de tipo cliente del nuevo cliente a
     * persistir.
     * @return La entidad luego de persistirla
     * @throws BusinessLogicException Si el ISBN es inválido o ya existe en la
     * persistencia.
     */
    public ClienteEntity createCliente(ClienteEntity clienteEntity) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Inicia proceso de creación del cliente");
        if (clienteEntity.getNombre() == null) {
            throw new BusinessLogicException("El nombre del cliente es inválido");
        }
        if (clienteEntity.getClave() == null) {
            throw new BusinessLogicException("La clave del cliente es inválida");
        }
        if (clienteEntity.getDireccion() == null) {
            throw new BusinessLogicException("La direccion del cliente es inválida");
        }
        if (clienteEntity.getCorreoElectronico()== null) {
            throw new BusinessLogicException("El correo electronico del cliente es inválido");
        }
        if (!validateSaldo(clienteEntity.getSaldo())) {
            throw new BusinessLogicException("El Saldo es inválido");
        }
        if (persistence.findByNombre(clienteEntity.getNombre()) != null) {
            throw new BusinessLogicException("El nombre del cliente ya esta registrado.");
        }
        if (clienteEntity.getId() != null) {
            throw new BusinessLogicException("El id del cliente ya esta registrado.");
        }

        persistence.create(clienteEntity);
        LOGGER.log(Level.INFO, "Termina proceso de creación del cliente");
        return clienteEntity;
    }

    /**
     * Devuelve todos los clientes que hay en la base de datos.
     *
     * @return Lista de entidades de tipo cliente.
     */
    public List<ClienteEntity> getClientes() {
        LOGGER.log(Level.INFO, "Inicia proceso de consultar todos los clientes");
        List<ClienteEntity> clientes = persistence.findAll();
        LOGGER.log(Level.INFO, "Termina proceso de consultar todos los clientes");
        return clientes;
    }

    /**
     * Busca un cliente por ID
     *
     * @param clientesId El id del cliente a buscar
     * @return El cliente encontrado, null si no lo encuentra.
     */
    public ClienteEntity getCliente(Long clientesId) {
        LOGGER.log(Level.INFO, "Inicia proceso de consultar el cliente con id = {0}", clientesId);
        ClienteEntity clienteEntity = persistence.find(clientesId);
        if (clienteEntity == null) {
            LOGGER.log(Level.SEVERE, "El cliente con el id = {0} no existe", clientesId);
        }
        LOGGER.log(Level.INFO, "Termina proceso de consultar el cliente con id = {0}", clientesId);
        return clienteEntity;
    }

    /**
     * Actualizar un cliente por ID
     *
     * @param clientesId El ID del cliente a actualizar
     * @param clienteEntity La entidad del cliente con los cambios deseados
     * @return La entidad del cliente luego de actualizarla
     * @throws BusinessLogicException Si el IBN de la actualización es inválido
     */
    public ClienteEntity updateCliente(Long clientesId, ClienteEntity clienteEntity) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Inicia proceso de actualizar el cliente con id = {0}", clientesId);
      if (!validateSaldo(clienteEntity.getSaldo())) {
            throw new BusinessLogicException("El Saldo es inválido");
        }
        if (persistence.findByNombre(clienteEntity.getNombre()) != null) {
            throw new BusinessLogicException("El nombre del cliente ya esta registrado.");
        }
        if (persistence.find(clienteEntity.getId()) == null) {
            throw new BusinessLogicException("El id del cliente no esta registrado.");
        }
        ClienteEntity newEntity = persistence.update(clienteEntity);
        LOGGER.log(Level.INFO, "Termina proceso de actualizar el cliente con id = {0}", clienteEntity.getId());
        return newEntity;
    }

    /**
     * Eliminar un cliente por ID
     *
     * @param clientesId El ID del cliente a eliminar
     * @throws BusinessLogicException si el cliente tiene autores asociados
     */
    public void deleteCliente(Long clientesId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Inicia proceso de borrar el cliente con id = {0}", clientesId);
        List<BookEntity> books = getCliente(clientesId).getLibrosComprados();
        if (books != null && !books.isEmpty()) {
            throw new BusinessLogicException("No se puede borrar el cliente con id = " + clientesId + " porque tiene libros asociados");
        }
        persistence.delete(clientesId);
        LOGGER.log(Level.INFO, "Termina proceso de borrar el cliente con id = {0}", clientesId);
    }

    /**
     * Verifica que el saldo no sea invalido.
     *
     * @param saldo a verificar
     * @return true si el saldo es valido.
     */
    private boolean validateSaldo(Double saldo) {
        return !(saldo == null || saldo < 0.0);
    }

}
