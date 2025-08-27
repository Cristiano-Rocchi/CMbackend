package pizzamafia.CMbackend.helpers.utility;

import pizzamafia.CMbackend.enums.Modulo;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.exceptions.BadRequestException;
import pizzamafia.CMbackend.payloads.partita.NewTitolareDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModuloUtils {

    // Restituisce i ruoli previsti per ogni modulo (tipizzato con enum)
    public static Map<Ruolo, Integer> getRuoliPerModulo(Modulo modulo) {
        return switch (modulo) {
            //1.-------------442-----------------
            case _4_4_2_1 -> Map.of(
                    Ruolo.PORTIERE, 1,
                    Ruolo.DIFENSORE_CENTRALE, 2,
                    Ruolo.TERZINO_DX, 1,
                    Ruolo.TERZINO_SX, 1,
                    Ruolo.CENTROCAMPISTA_CENTRALE, 2,
                    Ruolo.ALA_DX, 1,
                    Ruolo.ALA_SX, 1,
                    Ruolo.SECONDA_PUNTA, 1,
                    Ruolo.BOMBER, 1
            );
            case _4_4_2_2 -> Map.of(
                    Ruolo.PORTIERE, 1,
                    Ruolo.DIFENSORE_CENTRALE, 2,
                    Ruolo.TERZINO_DX, 1,
                    Ruolo.TERZINO_SX, 1,
                    Ruolo.CENTROCAMPISTA_DIFENSIVO, 1,
                    Ruolo.CENTROCAMPISTA_OFFENSIVO, 1,
                    Ruolo.CENTROCAMPISTA_CENTRALE, 2,
                    Ruolo.SECONDA_PUNTA, 1,
                    Ruolo.BOMBER, 1
            );

            //2.-------------433------------------
            case _4_3_3_1 -> Map.of(
                    Ruolo.PORTIERE, 1,
                    Ruolo.DIFENSORE_CENTRALE, 2,
                    Ruolo.TERZINO_DX, 1,
                    Ruolo.TERZINO_SX, 1,
                    Ruolo.CENTROCAMPISTA_CENTRALE, 2,
                    Ruolo.CENTROCAMPISTA_OFFENSIVO, 1,
                    Ruolo.ATTACCANTE_ESTERNO_DX, 1,
                    Ruolo.ATTACCANTE_ESTERNO_SX, 1,
                    Ruolo.BOMBER, 1
            );
            case _4_3_3_2 -> Map.of(
                    Ruolo.PORTIERE, 1,
                    Ruolo.DIFENSORE_CENTRALE, 2,
                    Ruolo.TERZINO_DX, 1,
                    Ruolo.TERZINO_SX, 1,
                    Ruolo.CENTROCAMPISTA_DIFENSIVO, 1,
                    Ruolo.CENTROCAMPISTA_CENTRALE, 2,
                    Ruolo.ATTACCANTE_ESTERNO_DX, 1,
                    Ruolo.ATTACCANTE_ESTERNO_SX, 1,
                    Ruolo.BOMBER, 1
            );
        };
    }

    // Valida che i titolari siano coerenti col modulo scelto
    public static void validaTitolariPerModulo(Modulo modulo, List<NewTitolareDTO> titolari) {
        Map<Ruolo, Integer> ruoliPrevisti = getRuoliPerModulo(modulo);
        Map<Ruolo, Long> ruoliEffettivi = titolari.stream()
                .collect(Collectors.groupingBy(NewTitolareDTO::ruolo, Collectors.counting()));

        // verifica numero totale di giocatori
        int totaleRichiesto = ruoliPrevisti.values().stream().mapToInt(Integer::intValue).sum();
        if (titolari.size() != totaleRichiesto) {
            throw new BadRequestException("Il modulo " + modulo.name() + " richiede "
                    + totaleRichiesto + " titolari, ma ne hai inviati " + titolari.size());
        }

        // verifica che per ogni ruolo ci sia il numero giusto
        for (Map.Entry<Ruolo, Integer> entry : ruoliPrevisti.entrySet()) {
            Ruolo ruoloRichiesto = entry.getKey();
            int numeroRichiesto = entry.getValue();
            long effettivo = ruoliEffettivi.getOrDefault(ruoloRichiesto, 0L);

            if (effettivo != numeroRichiesto) {
                throw new BadRequestException("Per il ruolo " + ruoloRichiesto
                        + " servono " + numeroRichiesto + " giocatori, ma ne hai messi " + effettivo);
            }
        }
    }
}
