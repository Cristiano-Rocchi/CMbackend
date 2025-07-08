package pizzamafia.CMbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pizzamafia.CMbackend.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // Cerca un utente per username (usato per login)
    Optional<User> findByUsername(String username);

    //Cerca un utente per email (utile per registrazione o recupero)
    Optional<User> findByEmail(String email);
}


