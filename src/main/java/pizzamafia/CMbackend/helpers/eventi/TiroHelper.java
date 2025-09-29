package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.enums.TipoEvento;
import pizzamafia.CMbackend.helpers.utility.DefensiveMatchup;
import pizzamafia.CMbackend.helpers.utility.MomentumBonusManager;

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
        double stabilitaTiratore =   st.getLetturaDelGioco()   * 0.50
                + st.getTecnica() * 0.30
                + st.getEquilibrio() * 0.20
                + random.nextInt(11);
        // Momentum cumulativo per l'azione
        stabilitaTiratore += MomentumBonusManager.peek(partita, squadraAttaccante);

        double contestDifensore =    sd.getMarcatura()   * 0.50
                + sd.getContrasti()   * 0.30
                + sd.getLetturaDelGioco() * 0.20
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
        // 1) Calcolo punteggio del tiro
        //        // ============================================================
        double punteggioTiro = st.getFinalizzazione() * 0.4 +
                st.getTiro() * 0.20 +
                st.getLetturaDelGioco() * 0.20 +
                st.getFreddezza() * 0.20 +
                random.nextInt(11); // +0–10
        // Momentum cumulativo per l'azione
        punteggioTiro += MomentumBonusManager.peek(partita, squadraAttaccante);

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
        double parata = sp.getTuffo() * 0.50 +
                sp.getRiflessi() * 0.30 +
                sp.getPosizione() * 0.20 +
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
