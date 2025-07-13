package pizzamafia.CMbackend.helpers;

import pizzamafia.CMbackend.entities.Giocatore;

import java.util.Comparator;
import java.util.List;

public class ValutazioneSquadraHelper {

    // Questo metodo calcola il valore tecnico totale della squadra.
    // Viene presa la media dei 15 giocatori con valore tecnico più alto.
    // Se la squadra ha meno di 15 giocatori, si fa la media di tutti.
    public static int calcolaValoreSquadra(List<Giocatore> rosa) {
        return (int) Math.round(
                rosa.stream()
                        .sorted(Comparator.comparingInt(Giocatore::getValoreTecnico).reversed())
                        .limit(15)
                        .mapToInt(Giocatore::getValoreTecnico)
                        .average()
                        .orElse(0)
        );
    }

}
