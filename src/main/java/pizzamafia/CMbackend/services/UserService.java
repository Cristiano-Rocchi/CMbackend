package pizzamafia.CMbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pizzamafia.CMbackend.entities.User;
import pizzamafia.CMbackend.enums.Role;
import pizzamafia.CMbackend.exceptions.NotFoundException;
import pizzamafia.CMbackend.exceptions.UnauthorizedException;
import pizzamafia.CMbackend.payloads.user.NewUserDTO;
import pizzamafia.CMbackend.payloads.user.NewUserRespDTO;
import pizzamafia.CMbackend.payloads.user.UserLoginDTO;
import pizzamafia.CMbackend.payloads.user.UserLoginRespDTO;
import pizzamafia.CMbackend.repositories.UserRepository;
import pizzamafia.CMbackend.security.JWTTools;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTTools jwtTools;

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

    //LOGIN
    public UserLoginRespDTO login(UserLoginDTO credentials) {
        User user = userRepository
                .findByUsernameOrEmail(credentials.identifier(), credentials.identifier())
                .orElseThrow(() -> new UnauthorizedException("Username o email non validi."));

        if (!passwordEncoder.matches(credentials.password(), user.getPassword())) {
            throw new UnauthorizedException("Password non valida.");
        }

        String token = jwtTools.createToken(user);
        NewUserRespDTO userDTO = new NewUserRespDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().toString()
        );

        return new UserLoginRespDTO(token, userDTO);
    }




}
