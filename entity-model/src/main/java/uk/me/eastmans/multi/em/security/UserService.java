package uk.me.eastmans.multi.em.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.me.eastmans.multi.em.kafka.PostKafkaEvent;

import java.util.List;
import java.util.Optional;

@Service
@PreAuthorize("isAuthenticated()")
public class UserService {

    private final UserRepository userRepository;
    private final PostKafkaEvent postKafkaEvent;

    @Autowired

    UserService(PostKafkaEvent postKafkaEvent, UserRepository userRepository) {
        this.postKafkaEvent = postKafkaEvent;
        this.userRepository = userRepository;
    }

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
        userRepository.flush();
        postKafkaEvent.postDelete("User", user.getId() );
    }

    @Transactional(readOnly = true)
    public List<User> list(Pageable pageable) {
        return userRepository.findAllBy(pageable).toList();
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void saveOrCreate(User user) {
        boolean create = user.getId() == null;
        userRepository.saveAndFlush(user);
        if (create) {
            postKafkaEvent.postCreate("User", user.getId() );
        } else {
            postKafkaEvent.postUpdate("User", user.getId() );
        }
    }

    @Transactional
    public Optional<User> getUser(long id) { return userRepository.findById(String.valueOf(id));}
}