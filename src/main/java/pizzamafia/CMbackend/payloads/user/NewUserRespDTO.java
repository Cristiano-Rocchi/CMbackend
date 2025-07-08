package pizzamafia.CMbackend.payloads.user;

import java.util.UUID;

public record NewUserRespDTO(
        UUID id,
        String username,
        String email,
        String role
) {
}
