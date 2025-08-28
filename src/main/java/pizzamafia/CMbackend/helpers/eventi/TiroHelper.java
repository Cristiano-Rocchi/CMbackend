package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.enums.TipoEvento;
import pizzamafia.CMbackend.helpers.utility.DefensiveMatchup;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TiroHelper {

    private static final Random random = new Random();

    public static EventoPartita genera(
            int minuto,
            int secondo,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            Giocatore tiratore,
            List<Titolari> titolariDifesa
    ) {
        // ============================================================
        // 0) Difendente plausibile che esce sul tiratore (muro/contrasto)
        //    - NON modifichiamo i tuoi pesi di tiro/parata.
        //    - Aggiungiamo solo la possibilità che il tiro venga MURATO prima.
        // ============================================================
        Ruolo ruoloTiratore = tiratore.getRuolo();
        Giocatore difensore = DefensiveMatchup.scegliPressatoreSuPortatore(ruoloTiratore, titolariDifesa, random);

        StatisticheTecnicheGiocatore st = tiratore.getStatistiche();
        StatisticheTecnicheGiocatore sd = difensore.getStatistiche();

        // Confronto "pre-tiro": capacità di creare spazio vs capacità di contrasto
        double stabilitaTiratore =   st.getTecnica()   * 0.30
                + st.getPosizione() * 0.30
                + st.getIntuito()   * 0.20
                + st.getDribbling() * 0.20
                + random.nextInt(11);

        double contestDifensore =    sd.getContrasti()   * 0.35
                + sd.getMarcatura()   * 0.25
                + sd.getPosizione()   * 0.20
                + sd.getAggressivita()* 0.20
                + random.nextInt(11);

        // Se il difensore vince chiaramente il duello, il tiro viene murato (intercetto/contrasto)
        if (contestDifensore - stabilitaTiratore >= 8) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .durataStimata(3)
                    .tipoEvento(TipoEvento.INTERCETTO)
                    .giocatorePrincipale(difensore)
                    .giocatoreSecondario(tiratore)
                    .esito("TIRO MURATO")
                    .note("Tiro contrastato prima della conclusione")
                    .partita(partita)
                    .squadra(squadraDifendente)
                    .build();
        }

        // ============================================================
        // 1) Calcolo punteggio del tiro (INVARIATO)
        // ============================================================
        double punteggioTiro = st.getFinalizzazione() * 0.5 +
                st.getIntuito() * 0.2 +
                st.getTiriDaLontano() * 0.15 +
                st.getPosizione() * 0.15 +
                random.nextInt(11); // +0–10

        // ============================================================
        // 2) Tiro fuori (INVARIATO)
        // ============================================================
        if (punteggioTiro < 60) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .durataStimata(3)
                    .tipoEvento(TipoEvento.TIRO)
                    .giocatorePrincipale(tiratore)
                    .giocatoreSecondario(null)
                    .esito("FUORI")
                    .note("Tiro fuori")
                    .partita(partita)
                    .squadra(squadraAttaccante)
                    .build();
        }

        // ============================================================
        // 3) Portiere avversario (INVARIATO)
        // ============================================================
        Giocatore portiere = titolariDifesa.stream()
                .map(Titolari::getGiocatore)
                .filter(g -> g.getRuolo() == Ruolo.PORTIERE)
                .findFirst()
                .orElse(titolariDifesa.get(0).getGiocatore()); // fallback

        StatisticheTecnicheGiocatore sp = portiere.getStatistiche();

        // ============================================================
        // 4) Punteggio parata (INVARIATO)
        // ============================================================
        double parata = sp.getRiflessi() * 0.45 +
                sp.getIntuito() * 0.25 +
                sp.getPosizione() * 0.2 +
                sp.getAgilita() * 0.1 +
                random.nextInt(11); // +0–10

        boolean parato = parata > punteggioTiro;

        // ============================================================
        // 5) Esito finale (INVARIATO): PARATA o GOL
        // ============================================================
        return EventoPartita.builder()
                .minuto(minuto)
                .secondo(secondo)
                .durataStimata(3)
                .tipoEvento(parato ? TipoEvento.PARATA : TipoEvento.GOL)
                .giocatorePrincipale(parato ? portiere : tiratore)
                .giocatoreSecondario(parato ? tiratore : null)
                .esito(parato ? "RIUSCITA" : "RETE")
                .note(parato ? "Tiro parato dal portiere" : "Rete su conclusione")
                .partita(partita)
                .squadra(parato ? squadraDifendente : squadraAttaccante)
                .build();
    }
}
