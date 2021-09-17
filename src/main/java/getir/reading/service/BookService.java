package getir.reading.service;

import getir.reading.Exception.BooksNotFoundException;
import getir.reading.model.FindBookResponse;
import getir.reading.model.SaveBookRequest;
import getir.reading.model.TrackBookCountRequest;
import getir.reading.model.TrackBookCountResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BookService {

    String addNewBook(SaveBookRequest saveBookRequest);

    List<FindBookResponse> findByAuthor(String author) throws BooksNotFoundException;

    List<FindBookResponse> findByTitle(String title);

    List<FindBookResponse> findAll();

    TrackBookCountResponse findBookCount(TrackBookCountRequest trackBookCountRequest);
}
