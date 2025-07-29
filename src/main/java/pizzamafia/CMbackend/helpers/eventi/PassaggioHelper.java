package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.TipoEvento;

import java.util.List;
import java.util.Random;

public class PassaggioHelper {

    private static final Random random = new Random();

    public static EventoPartita genera(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        // =================== 1. Estrai i giocatori coinvolti ===================
        Giocatore passatore = titolariAttacco.get(random.nextInt(titolariAttacco.size())).getGiocatore();
        Giocatore difensore = titolariDifesa.get(random.nextInt(titolariDifesa.size())).getGiocatore();

        // =================== 2. Ottieni le statistiche ===================
        StatisticheTecnicheGiocatore sp = passatore.getStatistiche();
        StatisticheTecnicheGiocatore sd = difensore.getStatistiche();

        // =================== 3. Calcola i punteggi (con random) ===================
        double punteggioPassatore = sp.getTecnica() * 0.45 +
                sp.getCreativita() * 0.25 +
                sp.getGiocoDiSquadra() * 0.2 +
                sp.getCarisma() * 0.1 +
                random.nextInt(11); // bonus casuale

        double punteggioDifensore = sd.getPosizione() * 0.4 +
                sd.getIntuito() * 0.3 +
                sd.getAggressivita() * 0.2 +
                sd.getContrasti() * 0.1 +
                random.nextInt(11); // bonus casuale

        // =================== 4. Determina l’esito con logica a soglia ===================
        if (punteggioPassatore > punteggioDifensore) {
            // PASSAGGIO riuscito
            return EventoPartita.builder()
                    .minuto(minuto)
                    .tipoEvento(TipoEvento.PASSAGGIO)
                    .giocatorePrincipale(passatore)
                    .giocatoreSecondario(null)
                    .esito("RIUSCITO")
                    .note("Passaggio completato con successo")
                    .partita(partita)
                    .squadra(squadraAttaccante)
                    .build();

        } else if (punteggioDifensore - punteggioPassatore >= 10) {
            // INTERCETTO (chiaro vantaggio del difensore)
            return EventoPartita.builder()
                    .minuto(minuto)
                    .tipoEvento(TipoEvento.INTERCETTO)
                    .giocatorePrincipale(difensore)
                    .giocatoreSecondario(passatore)
                    .esito("PALLA RECUPERATA")
                    .note("Intercetto netto su passaggio sbagliato")
                    .partita(partita)
                    .squadra(difensore.getSquadra())
                    .build();

        } else {
            // ERRORE PASSAGGIO (equilibrio)
            return EventoPartita.builder()
                    .minuto(minuto)
                    .tipoEvento(TipoEvento.ERRORE_PASSAGGIO)
                    .giocatorePrincipale(passatore)
                    .giocatoreSecondario(null)
                    .esito("FUORI MISURA")
                    .note("Passaggio impreciso, palla persa")
                    .partita(partita)
                    .squadra(squadraAttaccante)
                    .build();
        }
    }
}
