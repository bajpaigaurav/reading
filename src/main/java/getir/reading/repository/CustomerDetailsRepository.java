package getir.reading.repository;

import getir.reading.dao.CustomerDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Integer> {

    Optional<CustomerDetails> findByEmailOrPhoneNumber(@Param("email") String email,
                                                       @Param("phoneNumber") String phoneNumber);
    Optional<CustomerDetails> findByUserName(@Param("userName") String userName);
}
