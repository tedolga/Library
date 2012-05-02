package edu.exigen.server.storage;

import edu.exigen.client.entities.Entity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author O. Tedikova
 * @version 1.0
 */
public class StorageUtils {

    public static <T extends Entity> void createStorage(OutputStream outputStream, Storage<T> storage) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Storage.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(storage, outputStream);
    }

    public static <T extends Entity> Storage<T> retrieveStorage(InputStream inputStream) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Storage.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Storage<T>) unmarshaller.unmarshal(inputStream);
    }
}
