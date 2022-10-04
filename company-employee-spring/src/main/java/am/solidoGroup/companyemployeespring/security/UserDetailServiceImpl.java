package am.solidoGroup.companyemployeespring.security;

import am.solidoGroup.companyemployeespring.repository.UserRepository;
import am.solidoGroup.companyemployeespring.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byEmail = userRepository.findUserByEmail(username);
        if(byEmail.isEmpty()){
            throw new UsernameNotFoundException("Username does not exist");
        }
        return new CurrentUser(byEmail.get());
    }
}
