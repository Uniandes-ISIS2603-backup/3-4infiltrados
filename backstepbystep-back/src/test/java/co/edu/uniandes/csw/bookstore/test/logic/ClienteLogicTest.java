/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.bookstore.test.logic;

import co.edu.uniandes.csw.bookstore.ejb.BookLogic;
import co.edu.uniandes.csw.bookstore.ejb.ClienteLogic;
import co.edu.uniandes.csw.bookstore.entities.AuthorEntity;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.ClienteEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.bookstore.persistence.ClientePersistence;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de Cliente
 *
 * @author na.tobo
 */
@RunWith(Arquillian.class)
public class ClienteLogicTest 
{
    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private ClienteLogic clienteLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;
        
    private List<ClienteEntity> data = new ArrayList<ClienteEntity>();
    
    
    
    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(ClienteEntity.class.getPackage())
                .addPackage(ClienteLogic.class.getPackage())
                .addPackage(ClientePersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    /**
     * Configuración inicial de la prueba.
     */
    @Before
    public void configTest() {
        try {
            utx.begin();
            clearData();
            insertData();
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        em.createQuery("delete from BookEntity").executeUpdate();
        em.createQuery("delete from ClienteEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ClienteEntity entity = factory.manufacturePojo(ClienteEntity.class);
            em.persist(entity);
            entity.setLibrosComprados(new ArrayList<>());
            data.add(entity);
        }
        ClienteEntity cliente = data.get(2);
        BookEntity entity = factory.manufacturePojo(BookEntity.class);
        entity.getClientes().add(cliente);
        em.persist(entity);
        cliente.getLibrosComprados().add(entity);
    }
     /**
     * Prueba para crear un Cliente.
     */
    @Test
    public void createClienteTest() throws BusinessLogicException {
        ClienteEntity newEntity = factory.manufacturePojo(ClienteEntity.class);
        ClienteEntity result = clienteLogic.createCliente(newEntity);
        Assert.assertNotNull(result);
        
        ClienteEntity entity = em.find(ClienteEntity.class, result.getId());
        
        Assert.assertEquals(newEntity.getId(), entity.getId());
        Assert.assertEquals(newEntity.getSaldo(), entity.getSaldo());
        Assert.assertEquals(newEntity.getNombre(), entity.getNombre());
        Assert.assertEquals(newEntity.getCorreoElectronico(), entity.getCorreoElectronico());
        Assert.assertEquals(newEntity.getDireccion(), entity.getDireccion());
        Assert.assertEquals(newEntity.getClave(), entity.getClave());
    }

    /**
     * Prueba para consultar la lista de Clientes.
     */
    @Test
    public void getClientesTest() {
        List<ClienteEntity> list = clienteLogic.getClientes();
        Assert.assertEquals(data.size(), list.size());
        for (ClienteEntity entity : list) {
            boolean found = false;
            for (ClienteEntity storedEntity : data) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    /**
     * Prueba para consultar un Cliente.
     */
    @Test
    public void getClienteTest() {
        ClienteEntity entity = data.get(0);
        ClienteEntity resultEntity = clienteLogic.getCliente(entity.getId());
        Assert.assertNotNull(resultEntity);
        Assert.assertEquals(resultEntity.getId(), entity.getId());
        Assert.assertEquals(resultEntity.getSaldo(), entity.getSaldo());
        Assert.assertEquals(resultEntity.getNombre(), entity.getNombre());
        Assert.assertEquals(resultEntity.getCorreoElectronico(), entity.getCorreoElectronico());
        Assert.assertEquals(resultEntity.getDireccion(), entity.getDireccion());
        Assert.assertEquals(resultEntity.getClave(), entity.getClave());
    }

    /**
     * Prueba para actualizar un Cliente.
     */
    @Test
    public void updateClienteTest() throws BusinessLogicException 
    {
        ClienteEntity entity = data.get(0);
        ClienteEntity pojoEntity = factory.manufacturePojo(ClienteEntity.class);

        pojoEntity.setId(entity.getId());

        clienteLogic.updateCliente(pojoEntity.getId(),pojoEntity);

        ClienteEntity resp = em.find(ClienteEntity.class, entity.getId());

        Assert.assertEquals( pojoEntity.getId(), resp.getId());
        Assert.assertEquals( pojoEntity.getSaldo(), resp.getSaldo());
        Assert.assertEquals( pojoEntity.getNombre(), resp.getNombre());
        Assert.assertEquals( pojoEntity.getCorreoElectronico(), resp.getCorreoElectronico());
        Assert.assertEquals( pojoEntity.getDireccion(), resp.getDireccion());
        Assert.assertEquals( pojoEntity.getClave(), resp.getClave());
    }

    /**
     * Prueba para eliminar un Cliente
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test
    public void deleteClienteTest() throws BusinessLogicException {
        ClienteEntity entity = data.get(0);
        clienteLogic.deleteCliente(entity.getId());
        ClienteEntity deleted = em.find(ClienteEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    /**
     * Prueba para eliminar un Cliente asociado a un libro
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void deleteClienteConLibroTest() throws BusinessLogicException 
    {
        clienteLogic.deleteCliente(data.get(2).getId());
    }
}
