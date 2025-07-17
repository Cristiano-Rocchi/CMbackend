package pizzamafia.CMbackend.helpers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Getter
@Setter
public class UserTeamContext {

    private UUID userTeamId;

    public boolean isUserTeam(UUID idSquadra) {
        return userTeamId != null && userTeamId.equals(idSquadra);
    }

    public boolean isInitialized() {
        return userTeamId != null;
    }
}
