package edu.exigen.server.storage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author O. Tedikova
 * @version 1.0
 */
public class StorageUtils<T extends Storage> {

    public static <T> void createStorage(OutputStream outputStream, T storage) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(BookStorage.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(storage, outputStream);
    }

    public static <T> T retrieveStorage(InputStream inputStream) {
        return (T) new Object();
    }
}
