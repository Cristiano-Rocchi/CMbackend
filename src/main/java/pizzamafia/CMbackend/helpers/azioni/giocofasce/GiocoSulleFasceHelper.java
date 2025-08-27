package pizzamafia.CMbackend.helpers.azioni.giocofasce;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.eventi.*;
import pizzamafia.CMbackend.helpers.utility.TempoPartitaManager;

import java.util.ArrayList;
import java.util.List;

public class GiocoSulleFasceHelper {

    // Azione fascia 1:
// Passaggio corto (difensore → centrocampista) →
// Passaggio corto (centrocampista → esterno(centrocampista o attaccante)) →
// Dribbling (esterno(centrocampista o attaccante)) →
// Cross (esterno(centrocampista o attaccante) → bomber) →
// Colpo di testa (bomber)

    public static List<EventoPartita> eseguiFascia1(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa,
            List<Ruolo> ruoli
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio corto (difensore → centrocampista) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(0), ruoli.get(1));
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio corto (centrocampista → esterno) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1), ruoli.get(2));
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 3. Dribbling (esterno) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2));
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 4. Cross (esterno → bomber) =====
        EventoPartita c1 = CrossHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2), ruoli.get(3));
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

    // Azione fascia 2:
// Passaggio corto (difensore → terzino) →
// Passaggio corto (terzino → centrocampista) →
// Passaggio lungo (centrocampista → terzino) →
// Dribbling (terzino) →
// Cross (terzino → bomber) →
// Colpo di testa (bomber)

    public static List<EventoPartita> eseguiFascia2(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa,
            List<Ruolo> ruoli
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio corto (difensore → terzino) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(0), ruoli.get(1));
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio corto (terzino → centrocampista centrale) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1), ruoli.get(2));
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 3. Passaggio lungo (centrocampista centrale → terzino) =====
        EventoPartita p3 = PassaggioLungoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2), ruoli.get(1));
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(6);

        // ===== 4. Dribbling (terzino) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1));
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 5. Cross (terzino → bomber) =====
        EventoPartita c1 = CrossHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1), ruoli.get(3));
        eventi.add(c1);
        if (!c1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(3);

        // ===== 6. Colpo di testa (bomber) =====
        Giocatore colpitore = c1.getGiocatoreSecondario();
        EventoPartita testa = ColpoDiTestaHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                colpitore, titolariDifesa);
        eventi.add(testa);
        tempo.avanzaDi(testa.getDurataStimata());

        return eventi;
    }

    // Azione fascia 3:
    // Passaggio corto (difensore → terzino) →
    // Dribbling (terzino) →
    // Passaggio corto (terzino → esterno (centrocampo o attacco)) →
    // Dribbling esterno(centrocampo o attacco) →
    // Cross (esterno(centrocampo o attacco) → bomber) →
    // Colpo di testa (bomber)

    public static List<EventoPartita> eseguiFascia3(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa,
            List<Ruolo> ruoli
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio corto (difensore → terzino) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(0), ruoli.get(1));
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Dribbling (terzino) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1));
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 3. Passaggio corto (terzino → esterno (centrocampo o attacco)) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1), ruoli.get(2));
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 4. Dribbling (esterno (centrocampo o attacco)) =====
        EventoPartita d2 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2));
        eventi.add(d2);
        if (!d2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 5. Cross (esterno (centrocampo o attacco) → bomber) =====
        EventoPartita cross = CrossHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2), ruoli.get(3));
        eventi.add(cross);
        if (!cross.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(3);

        // ===== 6. Colpo di testa (bomber) =====
        Giocatore colpitore = cross.getGiocatoreSecondario();
        EventoPartita testa = ColpoDiTestaHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                colpitore, titolariDifesa);
        eventi.add(testa);
        tempo.avanzaDi(testa.getDurataStimata());

        return eventi;
    }

    // Azione fascia 4:
    // Passaggio corto (terzino → centrocampista centrale) →
    // Passaggio corto (centrocampista centrale → esterno (ala o terzino o att. esterno)) →
    // Dribbling (esterno) →
    // Passaggio corto (esterno → seconda punta) →
    // Passaggio corto (seconda punta → esterno) →
    // Tiro (esterno)


    public static List<EventoPartita> eseguiFascia4(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa,
            List<Ruolo> ruoli
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio corto (terzino → centrocampista centrale) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(0), ruoli.get(1));
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio corto (centrocampista centrale → esterno (ala o terzino o att. esterno)) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1), ruoli.get(2));
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 3. Dribbling (esterno (ala o terzino o att. esterno)) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2));
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 4. Passaggio corto (esterno (ala o terzino o att. esterno) → seconda punta) =====
        EventoPartita p3 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2), ruoli.get(3));
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 5. Passaggio corto (seconda punta → esterno (ala o terzino o att. esterno)) =====
        EventoPartita p4 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(3), ruoli.get(2));
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 6. Tiro (esterno (ala o terzino o att. esterno)) =====
        Giocatore tiratore = p4.getGiocatoreSecondario();
        EventoPartita tiro = TiroHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                tiratore, titolariDifesa);
        eventi.add(tiro);
        tempo.avanzaDi(tiro.getDurataStimata());

        return eventi;
    }

    // Azione fascia 5:
// Passaggio corto (centrocampista centrale → centrocampista centrale) →
// Passaggio corto (centrocampista centrale → esterno (ala o terzino o att.esterno)) →
// Dribbling (esterno) →
// Dribbling (esterno) →
// Passaggio corto (esterno → seconda punta) →
// Tiro (seconda punta)

    public static List<EventoPartita> eseguiFascia5(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa,
            List<Ruolo> ruoli
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio corto (centrocampista centrale → centrocampista centrale) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(0), ruoli.get(1));
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio corto (centrocampista centrale → esterno (ala o terzino o att.esterno)) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1), ruoli.get(2));
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 3. Dribbling (esterno (ala o terzino o att.esterno)) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2));
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 4. Dribbling (esterno (ala o terzino o att.esterno)) =====
        EventoPartita d2 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2));
        eventi.add(d2);
        if (!d2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 5. Passaggio corto (esterno (ala o terzino o att.esterno) → seconda punta) =====
        EventoPartita p3 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2), ruoli.get(3));
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 6. Tiro (seconda punta) =====
        Giocatore tiratore = p3.getGiocatoreSecondario();
        EventoPartita tiro = TiroHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                tiratore, titolariDifesa);
        eventi.add(tiro);
        tempo.avanzaDi(tiro.getDurataStimata());

        return eventi;
    }







}
