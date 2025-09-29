package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.enums.TipoEvento;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class RigoreHelper {

    private static final Random random = new Random();

    /**
     * Genera l'evento RIGORE in stile TiroHelper:
     * - seleziona rigorista (liste -> fallback su titolari con calciPiazzati, tie-break finalizzazione)
     * - seleziona portiere titolare avversario
     * - calcola punteggi con pesi specificati e momentum
     * - esito: GOL o PARATA (durataStimata 3, note coerenti)
     */
    public static EventoPartita genera(
            int minuto,
            int secondo,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            Formazione formazioneAttaccante,
            Formazione formazioneDifendente
    ) {
        // ===== 1) Selezione protagonisti =====
        Giocatore rigorista = selezionaRigorista(formazioneAttaccante);
        Giocatore portiere = selezionaPortiere(formazioneDifendente);

        StatisticheTecnicheGiocatore sa = rigorista.getStatistiche();
        StatisticheTecnicheGiocatore sp = portiere.getStatistiche();

        // ===== 2) Punteggio "tiro dal dischetto" (rigorista) =====
        // Attributi richiesti: calciPiazzati, finalizzazione, freddezza, carisma
        double punteggioRigore =
                safe(sa.getCalciPiazzati()) * 0.40 +
                        safe(sa.getFinalizzazione()) * 0.30 +
                        safe(sa.getFreddezza()) * 0.20 +
                        safe(sa.getCarisma()) * 0.10 +
                        random.nextInt(11); // random


        // ===== 3) Punteggio "parata" (portiere) =====
        // Attributi richiesti: riflessi, posizione, tuffo
        double parata =
                safe(sp.getRiflessi()) * 0.45 +
                        safe(sp.getPosizione()) * 0.30 +
                        safe(sp.getTuffo()) * 0.25 +
                        random.nextInt(11); // random

        boolean parato = parata > punteggioRigore;

        // ===== 4) Evento rigore =====
        return EventoPartita.builder()
                .minuto(minuto)
                .secondo(secondo)
                .durataStimata(3)
                .tipoEvento(TipoEvento.RIGORE)
                .giocatorePrincipale(rigorista)
                .giocatoreSecondario(portiere)
                .esito(parato ? "PARATO" : "GOL")
                .note(parato ? "Rigore parato dal portiere" : "Rete su calcio di rigore")
                .partita(partita)
                .squadra(parato ? squadraDifendente : squadraAttaccante)
                .build();
    }

        // ===================== Selezione rigorista & portiere =====================

    /**
     * Regole:
     * 1) Usa la lista "rigoristi" in ordine; prendi il primo che è in campo (tra i titolari della formazione).
     * 2) Fallback: tra i titolari, max su calciPiazzati; tie-break su finalizzazione.
     */
    private static Giocatore selezionaRigorista(Formazione formazioneAttaccante) {
        // Prova lista
        List<UUID> lista = formazioneAttaccante.getRigoristi();
        if (lista != null && !lista.isEmpty()) {
            for (UUID id : lista) {
                Optional<Giocatore> inCampo = getTitolariInCampo(formazioneAttaccante).stream()
                        .filter(g -> g.getId().equals(id))
                        .findFirst();
                if (inCampo.isPresent()) return inCampo.get();
            }
        }
        // Fallback
        return getTitolariInCampo(formazioneAttaccante).stream()
                .max(Comparator
                        .comparingInt((Giocatore g) -> safe(g.getStatistiche().getCalciPiazzati()))
                        .thenComparingInt(g -> safe(g.getStatistiche().getFinalizzazione()))
                )
                .orElseThrow(() -> new IllegalStateException("Nessun titolare disponibile per battere il rigore"));
    }

    /**
     * Portiere = titolare con ruolo PORTIERE.
     * Fallback raro: migliore profilo-portiere tra i titolari (riflessi+posizione+tuffo).
     */
    private static Giocatore selezionaPortiere(Formazione formazioneDifendente) {
        for (Titolari t : formazioneDifendente.getTitolari()) {
            if (t.getRuolo() == Ruolo.PORTIERE) return t.getGiocatore();
        }
        return getTitolariInCampo(formazioneDifendente).stream()
                .max(Comparator.comparingInt(RigoreHelper::scoreProfiloPortiere))
                .orElseThrow(() -> new IllegalStateException("Nessun giocatore disponibile come portiere"));
    }

    private static List<Giocatore> getTitolariInCampo(Formazione f) {
        return f.getTitolari().stream()
                .map(Titolari::getGiocatore)
                .toList();
    }

    private static int scoreProfiloPortiere(Giocatore g) {
        StatisticheTecnicheGiocatore s = g.getStatistiche();
        return safe(s.getRiflessi()) + safe(s.getPosizione()) + safe(s.getTuffo());
    }

    private static int safe(Integer v) {
        return v == null ? 0 : v;
    }
}
