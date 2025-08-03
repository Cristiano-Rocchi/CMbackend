package pizzamafia.CMbackend.helpers.utility;

import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.exceptions.BadRequestException;
import pizzamafia.CMbackend.payloads.partita.NewTitolareDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModuloUtils {

    public static Map<Ruolo, Integer> getRuoliPerModulo(String modulo) {
        return switch (modulo) {
            //1.-------442--------
            //1.1 442(1) centrocampo con 2 ali
            //1.2 442(2) centrocampo a rombo
            //2--------433--------
            //2.1 433(1) offensivo
            //2.2 433(2) difensivo


            //1.-------------442-----------------
            //1.442(1) centrocampo con 2 ali
            case "_4_4_2_1" -> Map.of(
                    Ruolo.PORTIERE, 1,
                    Ruolo.DIFENSORE_CENTRALE, 2,
                    Ruolo.TERZINO, 2,
                    Ruolo.CENTROCAMPISTA_CENTRALE, 2,
                    Ruolo.ALA, 2,
                    Ruolo.SECONDA_PUNTA, 1,
                    Ruolo.BOMBER, 1
            );
            //1.2 442(2)centrocampo a rombo
            case "_4_4_2_2" -> Map.of(
                    Ruolo.PORTIERE, 1,
                    Ruolo.DIFENSORE_CENTRALE, 2,
                    Ruolo.TERZINO, 2,
                    Ruolo.CENTROCAMPISTA_DIFENSIVO, 1,
                    Ruolo.CENTROCAMPISTA_OFFENSIVO, 1,
                    Ruolo.ALA, 2,
                    Ruolo.SECONDA_PUNTA, 1,
                    Ruolo.BOMBER, 1
            );

            //2.-------------433------------------
            //2.1 433(1) offensivo
            case "_4_3_3_1" -> Map.of(
                    Ruolo.PORTIERE, 1,
                    Ruolo.DIFENSORE_CENTRALE, 2,
                    Ruolo.TERZINO, 2,
                    Ruolo.CENTROCAMPISTA_CENTRALE, 1,
                    Ruolo.CENTROCAMPISTA_OFFENSIVO, 2,
                    Ruolo.ATTACCANTE_ESTERNO, 2,
                    Ruolo.BOMBER, 1
            );

            //2.1 433(2) difensivo
            case "_4_3_3_2" -> Map.of(
                    Ruolo.PORTIERE, 1,
                    Ruolo.DIFENSORE_CENTRALE, 2,
                    Ruolo.TERZINO, 2,
                    Ruolo.CENTROCAMPISTA_DIFENSIVO, 1,
                    Ruolo.CENTROCAMPISTA_CENTRALE, 2,
                    Ruolo.ATTACCANTE_ESTERNO, 2,
                    Ruolo.BOMBER, 1
            );

            default -> throw new UnsupportedOperationException("Modulo non supportato: " + modulo);
        };
    }


    public static void validaTitolariPerModulo(String modulo, List<NewTitolareDTO> titolari) {
        Map<Ruolo, Integer> ruoliPrevisti = getRuoliPerModulo(modulo);
        Map<Ruolo, Long> ruoliEffettivi = titolari.stream()
                .collect(Collectors.groupingBy(NewTitolareDTO::ruolo, Collectors.counting()));

        // verifica se il numero totale di giocatori è corretto
        int totaleRichiesto = ruoliPrevisti.values().stream().mapToInt(Integer::intValue).sum();
        if (titolari.size() != totaleRichiesto) {
            throw new BadRequestException("Il modulo " + modulo + " richiede " + totaleRichiesto + " titolari, ma ne hai inviati " + titolari.size());
        }

        // verifica che per ogni ruolo ci sia il numero giusto di giocatori
        for (Map.Entry<Ruolo, Integer> entry : ruoliPrevisti.entrySet()) {
            Ruolo ruoloRichiesto = entry.getKey();
            int numeroRichiesto = entry.getValue();
            long effettivo = ruoliEffettivi.getOrDefault(ruoloRichiesto, 0L);

            if (effettivo != numeroRichiesto) {
                throw new BadRequestException("Per il ruolo " + ruoloRichiesto + " servono " + numeroRichiesto + " giocatori, ma ne hai messi " + effettivo);
            }
        }
    }
}

