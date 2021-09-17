//package getir.reading.security.login;
//
//import getir.reading.dao.CustomerDetails;
//import getir.reading.repository.CustomerDetailsRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
// TODO - Service for Spring security for login
//import java.util.Collection;
//import java.util.Optional;
//@Slf4j
//@Service
//public class FetchUserDetails implements UserDetailsService {
//    @Autowired
//    CustomerDetailsRepository customerDetailsRepository;
//    UserDetailsMapp
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
////
////        Optional<CustomerDetails> customerDetails = customerDetailsRepository.
////                findByUserName(username);
////        if (customerDetails.isPresent()) {
////            log.info("Customer details found for customer Id: {}", username);
////            return new UserDetails() customerDetails.get();
////        }
//
//    }
//}
