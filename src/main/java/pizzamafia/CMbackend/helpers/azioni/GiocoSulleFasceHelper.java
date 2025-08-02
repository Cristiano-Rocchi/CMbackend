package pizzamafia.CMbackend.helpers.azioni;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.eventi.ColpoDiTestaHelper;
import pizzamafia.CMbackend.helpers.eventi.CrossHelper;
import pizzamafia.CMbackend.helpers.eventi.DribblingHelper;
import pizzamafia.CMbackend.helpers.eventi.PassaggioCortoHelper;
import pizzamafia.CMbackend.helpers.utility.TempoPartitaManager;

import java.util.ArrayList;
import java.util.List;

public class GiocoSulleFasceHelper {

    // Azione fascia 1:
// Passaggio corto (difensore centrale → centrocampista centrale) →
// Passaggio corto (centrocampista centrale → attaccante esterno) →
// Dribbling (attaccante esterno) →
// Cross (attaccante esterno → bomber) →
// Colpo di testa (bomber)

    public static List<EventoPartita> eseguiFascia1(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio corto (difensore centrale → centrocampista centrale) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.DIFENSORE_CENTRALE, Ruolo.CENTROCAMPISTA_CENTRALE);
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio corto (centrocampista centrale → attaccante esterno) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_CENTRALE, Ruolo.ATTACCANTE_ESTERNO);
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 3. Dribbling (attaccante esterno) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.ATTACCANTE_ESTERNO);
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 4. Cross (attaccante esterno → bomber) =====
        EventoPartita c1 = CrossHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.ATTACCANTE_ESTERNO, Ruolo.BOMBER);
        eventi.add(c1);
        if (!c1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(3);

        // ===== 5. Colpo di testa (bomber) =====
        Giocatore colpitore = c1.getGiocatoreSecondario(); // chi riceve il cross
        EventoPartita testa = ColpoDiTestaHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                colpitore, titolariDifesa);
        eventi.add(testa);
        tempo.avanzaDi(testa.getDurataStimata());

        return eventi;
    }

}
