package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.enums.TipoEvento;

import java.util.List;
import java.util.Random;
public class PassaggioLungoHelper {

    private static final Random random = new Random();

    // === Versione standard (retrocompatibile) ===
    public static EventoPartita genera(
            int minuto,
            int secondo,
            Partita partita,
            Squadra squadraAttaccante,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        return genera(minuto, secondo, partita, squadraAttaccante, titolariAttacco, titolariDifesa, null, null);
    }

    // === Versione con filtro per ruoli ===
    public static EventoPartita genera(
            int minuto,
            int secondo,
            Partita partita,
            Squadra squadraAttaccante,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa,
            Ruolo ruoloPassatore,
            Ruolo ruoloDestinatario
    ) {
        List<Giocatore> possibiliPassatori = (ruoloPassatore == null)
                ? titolariAttacco.stream().map(Titolari::getGiocatore).toList()
                : titolariAttacco.stream()
                .filter(t -> t.getRuolo() == ruoloPassatore)
                .map(Titolari::getGiocatore)
                .toList();

        List<Giocatore> possibiliDestinatari = (ruoloDestinatario == null)
                ? titolariAttacco.stream().map(Titolari::getGiocatore).toList()
                : titolariAttacco.stream()
                .filter(t -> t.getRuolo() == ruoloDestinatario)
                .map(Titolari::getGiocatore)
                .toList();

        if (possibiliPassatori.isEmpty() || possibiliDestinatari.isEmpty()) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .durataStimata(6)
                    .tipoEvento(TipoEvento.ERRORE_PASSAGGIO)
                    .giocatorePrincipale(null)
                    .giocatoreSecondario(null)
                    .esito("NESSUN GIOCATORE DISPONIBILE")
                    .note("Passaggio lungo non eseguito: ruoli non trovati")
                    .partita(partita)
                    .squadra(squadraAttaccante)
                    .build();
        }

        Giocatore passatore = possibiliPassatori.get(random.nextInt(possibiliPassatori.size()));
        Giocatore destinatario = possibiliDestinatari.get(random.nextInt(possibiliDestinatari.size()));
        Giocatore difensore = titolariDifesa.get(random.nextInt(titolariDifesa.size())).getGiocatore();

        // ===== 2. Statistiche =====
        StatisticheTecnicheGiocatore sp = passatore.getStatistiche();
        StatisticheTecnicheGiocatore sd = difensore.getStatistiche();

        // ===== 3. Calcola punteggi =====
        double punteggioPassatore = sp.getTecnica() * 0.4 +
                sp.getCreativita() * 0.3 +
                sp.getGiocoDiSquadra() * 0.15 +
                sp.getCarisma() * 0.15 +
                random.nextInt(11);

        double punteggioDifensore = sd.getPosizione() * 0.4 +
                sd.getIntuito() * 0.3 +
                sd.getAggressivita() * 0.2 +
                sd.getContrasti() * 0.1 +
                random.nextInt(11);

        // ===== 4. Esito: RIUSCITO =====
        if (punteggioPassatore > punteggioDifensore) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .durataStimata(6)
                    .tipoEvento(TipoEvento.PASSAGGIO)
                    .giocatorePrincipale(passatore)
                    .giocatoreSecondario(destinatario)
                    .esito("RIUSCITO")
                    .note("Passaggio lungo riuscito")
                    .partita(partita)
                    .squadra(squadraAttaccante)
                    .build();
        }

        // ===== 5. Esito: INTERCETTO =====
        if (punteggioDifensore - punteggioPassatore >= 10) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .durataStimata(6)
                    .tipoEvento(TipoEvento.INTERCETTO)
                    .giocatorePrincipale(difensore)
                    .giocatoreSecondario(passatore)
                    .esito("PALLA RECUPERATA")
                    .note("Intercetto su passaggio lungo")
                    .partita(partita)
                    .squadra(difensore.getSquadra())
                    .build();
        }

        // ===== 6. Esito: ERRORE =====
        return EventoPartita.builder()
                .minuto(minuto)
                .secondo(secondo)
                .durataStimata(6)
                .tipoEvento(TipoEvento.ERRORE_PASSAGGIO)
                .giocatorePrincipale(passatore)
                .giocatoreSecondario(destinatario)
                .esito("FUORI MISURA")
                .note("Passaggio lungo sbagliato")
                .partita(partita)
                .squadra(squadraAttaccante)
                .build();
    }
}
