package getir.reading.service.impl;

import getir.reading.Exception.BookAlreadyExistsException;
import getir.reading.Exception.BooksNotFoundException;
import getir.reading.dao.BookDetails;
import getir.reading.model.FindBookResponse;
import getir.reading.model.SaveBookRequest;
import getir.reading.model.TrackBookCountRequest;
import getir.reading.model.TrackBookCountResponse;
import getir.reading.repository.BookDetailsRepository;
import getir.reading.service.BookService;
import getir.reading.util.ReadingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    BookDetailsRepository bookDetailsRepository;

    @Override
    public String addNewBook(SaveBookRequest saveBookRequest) {

        Optional<BookDetails> bookDetails = bookDetailsRepository
                .findByTitleAndAuthor(saveBookRequest.getTitle(), saveBookRequest.getAuthor());
        if (bookDetails.isPresent()) {
            log.info("Book with title: {} and author: {} already exists",
                    saveBookRequest.getTitle(), saveBookRequest.getAuthor());
            throw BookAlreadyExistsException.builder().build();
        }

        BookDetails bookDetail = BookDetails.builder()
                .title(saveBookRequest.getTitle())
                .author(saveBookRequest.getAuthor())
                .isbn(saveBookRequest.getIsbn())
                .lastUpdatedOn(ReadingUtils.getCurrentTimestamp())
                .availableCopies(Integer.parseInt(saveBookRequest.getAvailableCopies()))
                .price(Double.parseDouble(saveBookRequest.getPrice()))
                .build();

        bookDetailsRepository.save(bookDetail);
        log.info("Book saved in DB successfully");
        return String.valueOf(bookDetail.getId());
    }

    @Override
    public List<FindBookResponse> findByAuthor(String author) throws BooksNotFoundException {

        Optional<List<BookDetails>> books = bookDetailsRepository.findByAuthor(author);
        if (!books.get().isEmpty()) {
            log.info("Book by author {} found in the DB", author);
            List<FindBookResponse> booksFound =
                    books.get().stream().map(e ->
                                    FindBookResponse.builder()
                                            .title(e.getTitle())
                                            .author(e.getAuthor())
                                            .availableCopies(e.getAvailableCopies())
                                            .build())
                            .collect(Collectors.toList());
            return booksFound;
        }
        log.info("Book by author {} not found in the DB", author);
        throw BooksNotFoundException.builder().build();
    }

    @Override
    public List<FindBookResponse> findByTitle(String title) {
        Optional<List<BookDetails>> books = bookDetailsRepository.findByTitle(title);
        if (!books.get().isEmpty()) {
            log.info("Book with title {} found in the DB", title);
            List<FindBookResponse> booksFound =
                    books.get().stream().map(e ->
                                    FindBookResponse.builder()
                                            .title(e.getTitle())
                                            .author(e.getAuthor())
                                            .availableCopies(e.getAvailableCopies())
                                            .build())
                            .collect(Collectors.toList());
            return booksFound;
        }
        log.info("Book with title {} not found in the DB", title);
        throw BooksNotFoundException.builder().build();
    }

    @Override
    public List<FindBookResponse> findAll() {

        Optional<List<BookDetails>> books = Optional.of(bookDetailsRepository.findAll());
        if (!books.get().isEmpty()) {
            log.info("Total number of books found in DB: {}", books.get().size());
            List<FindBookResponse> booksFound =
                    books.get().stream().map(e ->
                                    FindBookResponse.builder()
                                            .title(e.getTitle())
                                            .author(e.getAuthor())
                                            .availableCopies(e.getAvailableCopies())
                                            .build())
                            .collect(Collectors.toList());
            return booksFound;
        }
        log.info("No books found in the DB");
        throw BooksNotFoundException.builder().build();
    }

    @Override
    public TrackBookCountResponse findBookCount(TrackBookCountRequest trackBookCountRequest) {
        Optional<BookDetails> bookDetails = bookDetailsRepository
                .findByTitleAndAuthor(trackBookCountRequest.getTitle(), trackBookCountRequest.getAuthor());
        if (bookDetails.isEmpty()) {
            log.info("No books found in the DB with title:{} and author: {}",
                    trackBookCountRequest.getTitle(), trackBookCountRequest.getAuthor());
            return TrackBookCountResponse.builder()
                    .availableCopies(String.valueOf(0))
                    .build();
        }

        log.info("Number of books: {} found in the DB with title:{} and author: {}",
                bookDetails.get().getAvailableCopies(), trackBookCountRequest.getTitle(),
                trackBookCountRequest.getAuthor());
        return TrackBookCountResponse.builder()
                .availableCopies(String.valueOf(bookDetails.get().getAvailableCopies()))
                .build();
    }
}
