package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.TipoEvento;

import java.util.List;
import java.util.Random;

public class DribblingHelper {

    private static final Random random = new Random();

    public static EventoPartita genera(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        // =================== 1. Estrai i giocatori coinvolti ===================
        Giocatore attaccante = titolariAttacco.get(random.nextInt(titolariAttacco.size())).getGiocatore();
        Giocatore difensore = titolariDifesa.get(random.nextInt(titolariDifesa.size())).getGiocatore();

        // =================== 2. Ottieni le statistiche ===================
        StatisticheTecnicheGiocatore sa = attaccante.getStatistiche();
        StatisticheTecnicheGiocatore sd = difensore.getStatistiche();

        // =================== 3. Calcola il punteggio di entrambi ===================
        double punteggioAttaccante = sa.getDribbling() * 0.5 +
                sa.getAgilita() * 0.2 +
                sa.getScatto() * 0.15 +
                sa.getIntuito() * 0.15 +
                random.nextInt(11); // bonus casuale

        double punteggioDifensore = sd.getMarcatura() * 0.4 +
                sd.getContrasti() * 0.3 +
                sd.getPosizione() * 0.2 +
                sd.getAggressivita() * 0.1 +
                random.nextInt(11); // bonus casuale

        // =================== 4. Determina l’esito ===================
        boolean riuscito = punteggioAttaccante > punteggioDifensore;

        // =================== 5. Costruisci l’evento ===================
        return EventoPartita.builder()
                .minuto(minuto)
                .tipoEvento(TipoEvento.DRIBBLING)
                .giocatorePrincipale(attaccante)
                .giocatoreSecondario(difensore)
                .esito(riuscito ? "RIUSCITO" : "FALLITO")
                .note("Confronto dribbling realistico")
                .partita(partita)
                .squadra(squadraAttaccante)
                .build();
    }
}

