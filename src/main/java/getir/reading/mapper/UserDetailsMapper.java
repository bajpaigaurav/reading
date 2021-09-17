//package getir.reading.mapper;
//
//import getir.reading.dao.CustomerDetails;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//@Component
//class UserDetailsMapper {
//// TODO - thinking to having role based authentication where admin or different set of users can perform different house keeping operation
//    UserDetails toUserDetails(CustomerDetails customerDetails) {
//
//        return User.withUsername(customerDetails.getUserName())
//                .password(customerDetails.getPassword())
//                .build();
//    }
//}