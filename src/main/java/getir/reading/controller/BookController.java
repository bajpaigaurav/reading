package getir.reading.controller;

import getir.reading.constants.ReadingAppConstants;
import getir.reading.model.*;
import getir.reading.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Validated
@RestController
@Slf4j
@RequestMapping("/getir/reading/books")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> addBook(@Valid @RequestBody SaveBookRequest saveBookRequest) {
        log.info("In book controller for saving new book with title: {}, author: {}",
                saveBookRequest.getTitle(), saveBookRequest.getAuthor());
        String bookId = bookService.addNewBook(saveBookRequest);
        SaveBookResponse saveBookResponse = SaveBookResponse
                .builder().bookId(bookId)
                .status(ReadingAppConstants.BOOK_ADDED)
                .build();
        SuccessResponse<SaveBookResponse> successResponse = new SuccessResponse<>();
        successResponse.setData(saveBookResponse);
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
    }

    @GetMapping(value = "/find/author", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> findByAuthor(@RequestParam("author")
                                                        @NotBlank String author) {
        log.info("In book controller for finding book by author: {}", author);
        List<FindBookResponse> booksFound = bookService.findByAuthor(author);
        SuccessResponse<List<FindBookResponse>> successResponse = new SuccessResponse<>();
        successResponse.setData(booksFound);
        return new ResponseEntity<>(successResponse, HttpStatus.FOUND);
    }

    @GetMapping(value = "/find/title", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> findByTitle(@RequestParam("title")
                                                       @NotBlank String title) {
        log.info("In book controller for finding book with title: {}", title);
        List<FindBookResponse> booksFound = bookService.findByTitle(title);
        SuccessResponse<List<FindBookResponse>> successResponse = new SuccessResponse<>();
        successResponse.setData(booksFound);
        return new ResponseEntity<>(successResponse, HttpStatus.FOUND);
    }

    @GetMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FindBookResponse>> findAll() {
        log.info("In book controller for finding all books in DB");
        List<FindBookResponse> booksFound = bookService.findAll();
        return new ResponseEntity<>(booksFound, HttpStatus.FOUND);
    }

    @PostMapping(value = "/find/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> findBookCount(
            @RequestBody TrackBookCountRequest trackBookCountRequest) {
        log.info("In book controller for finding count of book with title: {} and author: {}",
                trackBookCountRequest.getTitle(), trackBookCountRequest.getAuthor());
        TrackBookCountResponse trackBookCountResponse = bookService.findBookCount(trackBookCountRequest);
        SuccessResponse<TrackBookCountResponse> successResponse = new SuccessResponse<>();
        successResponse.setData(trackBookCountResponse);
        return new ResponseEntity<>(successResponse, HttpStatus.FOUND);
    }
}
