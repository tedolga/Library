package edu.exigen.server.storage;

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

    public static <T> void createStorage(OutputStream outputStream, T storage) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(BookStorage.class, ReaderStorage.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(storage, outputStream);
    }

    public static <T> T retrieveStorage(InputStream inputStream) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(BookStorage.class, ReaderStorage.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(inputStream);
    }
}
