package getir.reading.repository;

import getir.reading.dao.BookDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookDetailsRepository extends JpaRepository<BookDetails, Integer> {

    Optional<List<BookDetails>> findByAuthor(@Param("author") String author);

    Optional<List<BookDetails>> findByTitle(@Param("title") String title);

    Optional<BookDetails>
    findByTitleAndAuthorAndAvailableCopiesGreaterThan(@Param("title") String title,
                                                      @Param("author") String author,
                                                      @Param("availableCopies") int availableCopies);

    Optional<BookDetails> findByTitleAndAuthor(@Param("title") String title,
                                               @Param("author") String author);
}
