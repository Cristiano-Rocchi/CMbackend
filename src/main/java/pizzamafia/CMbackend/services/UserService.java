package pizzamafia.CMbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pizzamafia.CMbackend.entities.User;
import pizzamafia.CMbackend.enums.Role;
import pizzamafia.CMbackend.exceptions.NotFoundException;
import pizzamafia.CMbackend.payloads.user.NewUserDTO;
import pizzamafia.CMbackend.payloads.user.NewUserRespDTO;
import pizzamafia.CMbackend.repositories.UserRepository;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // CREA e salva un utente
    public NewUserRespDTO save(NewUserDTO newUserDTO) {

        userRepository.findByUsername(newUserDTO.username()).ifPresent(u -> {
            throw new RuntimeException("Username già in uso!");
        });

        userRepository.findByEmail(newUserDTO.email()).ifPresent(u -> {
            throw new RuntimeException("Email già in uso!");
        });

        User user = new User();
        user.setUsername(newUserDTO.username());
        user.setEmail(newUserDTO.email());
        user.setPassword(passwordEncoder.encode(newUserDTO.password()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return new NewUserRespDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().toString()
        );
    }
    //FINDBYID
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente con ID " + id + " non trovato"));
    }

}
