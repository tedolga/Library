package edu.exigen.server.provider;

import edu.exigen.client.entities.Reader;
import edu.exigen.server.dao.ReaderDAO;
import org.junit.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class ReaderProviderTest {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final String FILE_NAME = "readerProvided.xml";
    private ReaderDAO readerDAO = new ReaderDAO(FILE_NAME);
    private ReaderProvider provider = new ReaderProvider(readerDAO, reservationRecordProvider);

    @Before
    public void setUp() throws Exception {
        provider.loadData();
    }

    @BeforeClass
    @AfterClass
    public static void clear() {
        File file = new File(FILE_NAME);
        file.delete();
    }

    @Test
    public void testCreateReader() throws Exception {
        Assert.assertEquals(0, provider.readAll().size());
        Reader reader = new Reader();
        reader.setFirstName("Ivan");
        reader.setLastName("Petrov");
        reader.setAddress("34/5 Nevsky pr.");
        reader.setDateOfBirth(new Date());
        provider.createReader(reader);
        Assert.assertEquals(1, provider.readAll().size());
        Assert.assertEquals(1, provider.readAll().get(0).getId());
        Reader newReader = reader.copy();
        newReader.setId(0);
        newReader.setFirstName("Petr");
        provider.createReader(newReader);
        Assert.assertEquals(2, provider.readAll().size());
        Assert.assertEquals(2, provider.searchReaders("Petr").get(0).getId());
    }

    @Test
    public void testSearchReaders() throws Exception {
        Assert.assertEquals(1, provider.searchReaders("Petr").size());
        Assert.assertEquals(2, provider.searchReaders("Petrov").size());
        Assert.assertEquals(2, provider.searchReaders("Petr Petrov").size());
        Assert.assertEquals(2, provider.searchReaders(dateFormat.format(new Date())).size());
    }

    @Test
    public void testUpdateReader() throws Exception {
        Reader oldReader = provider.searchReaders("1").get(0);
        Reader reader = new Reader();
        reader.setId(oldReader.getId());
        reader.setFirstName("Ivan");
        reader.setLastName("Petrov");
        reader.setAddress("39/5 pr. Veteranov");
        reader.setDateOfBirth(new Date());
        provider.updateReader(oldReader, reader);
        Assert.assertEquals("39/5 pr. Veteranov", provider.searchReaders("1").get(0).getAddress());
        Assert.assertEquals(2, provider.readAll().size());
        Assert.assertEquals(1, provider.searchReaders("Veteranov").size());
    }

    @Test
    public void testDeleteReader() throws Exception {
        Reader oldReader = provider.searchReaders("2").get(0);
        provider.deleteReader(oldReader);
        Assert.assertEquals(1, provider.readAll().size());
        Assert.assertEquals(0, provider.searchReaders("Petr").size());
        Assert.assertEquals(1, provider.searchReaders("Petr Petrov").size());
    }
}
