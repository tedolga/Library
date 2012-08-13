package edu.exigen.server;

import edu.exigen.server.provider.BookProvider;
import edu.exigen.server.provider.ReaderProvider;
import edu.exigen.server.provider.ReservationRecordProvider;

/**
 * Class for holding all required providers.
 *
 * @author O. Tedikova
 * @version 1.0
 */
public class ProvidersHolder {
    private BookProvider bookProvider;
    private ReaderProvider readerProvider;
    private ReservationRecordProvider recordProvider;

    public ProvidersHolder(BookProvider bookProvider, ReaderProvider readerProvider, ReservationRecordProvider recordProvider) {
        this.bookProvider = bookProvider;
        this.readerProvider = readerProvider;
        this.recordProvider = recordProvider;
    }

    public BookProvider getBookProvider() {
        return bookProvider;
    }

    public void setBookProvider(BookProvider bookProvider) {
        this.bookProvider = bookProvider;
    }

    public ReaderProvider getReaderProvider() {
        return readerProvider;
    }

    public void setReaderProvider(ReaderProvider readerProvider) {
        this.readerProvider = readerProvider;
    }

    public ReservationRecordProvider getRecordProvider() {
        return recordProvider;
    }

    public void setRecordProvider(ReservationRecordProvider recordProvider) {
        this.recordProvider = recordProvider;
    }
}
