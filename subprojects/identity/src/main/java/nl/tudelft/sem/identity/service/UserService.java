package nl.tudelft.sem.identity.service;

import java.util.ArrayList;
import java.util.Collection;
import nl.tudelft.sem.identity.entity.User;
import nl.tudelft.sem.identity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String netid) throws UsernameNotFoundException {

        //fetch user object from database
        User user = userRepository.findByNetid(netid);

        if (user == null) {
            throw new UsernameNotFoundException("User with netid " + netid + " not Found");
        }

        //provide it to spring security to validate if the netid and password are valid or not
        return new org.springframework.security.core
                .userdetails.User(user.getNetid(), "{bcrypt}" + user
                .getPassword(), getGrantedAuthorities(user));
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(User user) {

        Collection<GrantedAuthority> grantedAuthority = new ArrayList<>();
        if (user.getRole().equals("teacher")) {
            grantedAuthority.add(new SimpleGrantedAuthority("ROLE_TEACHER"));
        } else if (user.getRole().equals("student")) {
            grantedAuthority.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
        }
        return grantedAuthority;
    }

}
