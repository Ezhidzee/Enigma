package su.ezhidze.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import su.ezhidze.entity.InputMessage;
import su.ezhidze.entity.User;
import su.ezhidze.exception.AuthenticationFailException;
import su.ezhidze.exception.DuplicateEntryException;
import su.ezhidze.exception.RecordNotFoundException;
import su.ezhidze.model.UserRegistrationModel;
import su.ezhidze.model.UserResponseModel;
import su.ezhidze.repository.UserRepository;
import su.ezhidze.validator.Validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseModel addNewUser(final UserRegistrationModel userRegistrationModel) {
        if (userRepository.findByNickname(userRegistrationModel.getNickname()).isPresent()) {
            throw new DuplicateEntryException("This nickname is already associated with an account.");
        }

        Validator.validate(userRegistrationModel);

        User newUser = new User(userRegistrationModel);
        newUser.setPassword(passwordEncoder.encode(userRegistrationModel.getPassword()));

        return new UserResponseModel(userRepository.save(newUser));
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        User user = userRepository.findByNickname(nickname).orElseThrow(() -> new AuthenticationFailException("User not found"));
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder().username(user.getNickname()).password(user.getPassword()).roles("USER").build();
        return userDetails;
    }

    public User loadUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(() -> new AuthenticationFailException("User not found"));
    }

    public ArrayList<UserResponseModel> getUsers() {
        return new ArrayList<>(((Collection<User>) userRepository.findAll()).stream().map(UserResponseModel::new).toList());
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("User not found"));
    }

    public void delete(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("User not found"));
        userRepository.delete(user);
    }

    public User patchUser(User user, Map<String, Object> fields) {
        if (fields.get("nickname") != null) {
            if (userRepository.findByNickname((String) fields.get("nickname")).isPresent()) {
                throw new DuplicateEntryException("This nickname is already associated with an account.");
            }
            user.setNickname((String) fields.get("email"));
        }
        if (fields.get("phoneNumber") != null) {
            if (userRepository.findByPhoneNumber((String) fields.get("phoneNumber")).isPresent()) {
                throw new DuplicateEntryException("This phoneNumber is already associated with an account.");
            }
            user.setPhoneNumber((String) fields.get("phoneNumber"));
        }
        if (fields.get("password") != null) user.setPassword((String) fields.get("password"));
        userRepository.save(user);
        return user;
    }

    public User patchUser(Integer id, Map<String, Object> fields) {
        User user = getUserById(id);
        return patchUser(user, fields);
    }

    public void setPublicKey(String nickname, String publicKey) {
        User user = userRepository.findByNickname(nickname).orElseThrow(() -> new AuthenticationFailException("User not found"));
        user.setPublicKey(publicKey);
        userRepository.save(user);
    }

    public void addUnreadMessage(Integer id, InputMessage message) {
        User user = getUserById(id);
        user.getUnreadMessages().add(message);
        userRepository.save(user);
    }
}
