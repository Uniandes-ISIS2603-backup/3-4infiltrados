package co.edu.uniandes.csw.bookstore.test.logic;

import co.edu.uniandes.csw.bookstore.ejb.AuthorBooksLogic;
import co.edu.uniandes.csw.bookstore.ejb.BookLogic;
import co.edu.uniandes.csw.bookstore.ejb.ClienteBooksLogic;
import co.edu.uniandes.csw.bookstore.ejb.EditorialLogic;
import co.edu.uniandes.csw.bookstore.entities.AuthorEntity;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.ClienteEntity;
import co.edu.uniandes.csw.bookstore.entities.EditorialEntity;
import co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.bookstore.persistence.AuthorPersistence;
import co.edu.uniandes.csw.bookstore.persistence.ClientePersistence;
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
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.List;

/**
 * Pruebas de logica de la relacion Author - Books
 *
 * @author ISIS2603
 */
@RunWith(Arquillian.class)
public class ClienteBooksLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private ClienteBooksLogic clienteBookLogic;

    @Inject
    private BookLogic bookLogic;
    
    @Inject 
    private EditorialLogic editorialLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;
    
    private ClienteEntity cliente = new ClienteEntity();
    private EditorialEntity editorial = new EditorialEntity();
    private List<BookEntity> data = new ArrayList<>();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(ClienteEntity.class.getPackage())
                .addPackage(BookEntity.class.getPackage())
                .addPackage(ClienteBooksLogic.class.getPackage())
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
        em.createQuery("delete from ClienteEntity").executeUpdate();
        em.createQuery("delete from BookEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        
        cliente = factory.manufacturePojo(ClienteEntity.class);
        cliente.setId(1L);
        cliente.setLibrosComprados(new ArrayList<>());
        em.persist(cliente);

        for (int i = 0; i < 3; i++) {
            BookEntity entity = factory.manufacturePojo(BookEntity.class);
            entity.setClientes(new ArrayList<>());
            entity.getClientes().add(cliente);
            em.persist(entity);
            data.add(entity);
            cliente.getLibrosComprados().add(entity);
        }
    }

    /**
     * Prueba para asociar un cliente a un libro.
     *
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test
    public void addBookTest() throws BusinessLogicException {
        BookEntity newBook = factory.manufacturePojo(BookEntity.class);
        editorialLogic.createEditorial(editorial);
        newBook.setEditorial(editorial);
        bookLogic.createBook(newBook);
        BookEntity bookEntity = clienteBookLogic.addBook(cliente.getId(), newBook.getId());
        Assert.assertNotNull(bookEntity);

        Assert.assertEquals(bookEntity.getId(), newBook.getId());
        Assert.assertEquals(bookEntity.getName(), newBook.getName());
        Assert.assertEquals(bookEntity.getDescription(), newBook.getDescription());
        Assert.assertEquals(bookEntity.getIsbn(), newBook.getIsbn());
        Assert.assertEquals(bookEntity.getImage(), newBook.getImage());

        BookEntity lastBook = clienteBookLogic.getBook(cliente.getId(), newBook.getId());

        Assert.assertEquals(lastBook.getId(), newBook.getId());
        Assert.assertEquals(lastBook.getName(), newBook.getName());
        Assert.assertEquals(lastBook.getDescription(), newBook.getDescription());
        Assert.assertEquals(lastBook.getIsbn(), newBook.getIsbn());
        Assert.assertEquals(lastBook.getImage(), newBook.getImage());
    }

    /**
     * Prueba para consultar la lista de Books de un cliente.
     */
    @Test
    public void getBooksTest() {
        List<BookEntity> bookEntities = clienteBookLogic.getBooks(cliente.getId());

        Assert.assertEquals(data.size(), bookEntities.size());

        for (int i = 0; i < data.size(); i++) {
            Assert.assertTrue(bookEntities.contains(data.get(0)));
        }
    }

    /**
     * Prueba para cpnsultar un libro de un cliente.
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test
    public void getBookTest() throws BusinessLogicException {
        BookEntity bookEntity = data.get(0);
        BookEntity book = clienteBookLogic.getBook(cliente.getId(), bookEntity.getId());
        Assert.assertNotNull(book);

        Assert.assertEquals(bookEntity.getId(), book.getId());
        Assert.assertEquals(bookEntity.getName(), book.getName());
        Assert.assertEquals(bookEntity.getDescription(), book.getDescription());
        Assert.assertEquals(bookEntity.getIsbn(), book.getIsbn());
        Assert.assertEquals(bookEntity.getImage(), book.getImage());
    }

    /**
     * Prueba para actualizar los libros de un cliente.
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test
    public void replaceBooksTest() throws BusinessLogicException {
        List<BookEntity> nuevaLista = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            BookEntity entity = factory.manufacturePojo(BookEntity.class);
            entity.setClientes(new ArrayList<>());
            entity.getClientes().add(cliente);
            editorialLogic.createEditorial(editorial);
            entity.setEditorial(editorial);
            bookLogic.createBook(entity);
            nuevaLista.add(entity);
        }
        clienteBookLogic.replaceBooks(cliente.getId(), nuevaLista);
        List<BookEntity> bookEntities = clienteBookLogic.getBooks(cliente.getId());
        for (BookEntity aNuevaLista : nuevaLista) {
            Assert.assertTrue(bookEntities.contains(aNuevaLista));
        }
    }

    /**
     * Prueba desasociar un libro con un cliente.
     *
     */
    @Test
    public void removeBookTest() {
        for (BookEntity book : data) {
            clienteBookLogic.removeBook(cliente.getId(), book.getId());
        }
        Assert.assertTrue(clienteBookLogic.getBooks(cliente.getId()).isEmpty());
    }
}
