package pizzamafia.CMbackend.payloads.user;

public record UserLoginRespDTO(
        String token,
        NewUserRespDTO user
) {
}
