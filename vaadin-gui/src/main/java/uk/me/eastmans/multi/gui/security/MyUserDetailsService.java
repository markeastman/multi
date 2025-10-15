package uk.me.eastmans.multi.gui.security;

import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.me.eastmans.multi.em.security.Persona;
import uk.me.eastmans.multi.em.security.User;
import uk.me.eastmans.multi.em.security.UserRepository;

@Service
@PreAuthorize("isAuthenticated()")
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    MyUserDetailsService(UserRepository repository) { this.userRepository = repository; }

    @Override
    public UserDetails loadUserByUsername(String username) {
        LoggerFactory.getLogger(MyUserDetailsService.class)
                .warn("loadUserByUsername " + username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        LoggerFactory.getLogger(MyUserDetailsService.class)
                .warn("Persona " + user.getPersonas());
        return new MyUserPrincipal(user);
    }

    @Transactional
    public void switchPersonaForUser(User user, String name) {
         Persona persona = user.getPersonaWithName(name);
        if (persona != null) {
            user.setDefaultPersona(persona);
            userRepository.saveAndFlush(user);
        }
    }
}

