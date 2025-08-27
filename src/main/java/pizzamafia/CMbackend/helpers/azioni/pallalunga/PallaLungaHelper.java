package pizzamafia.CMbackend.helpers.azioni.pallalunga;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.eventi.*;
import pizzamafia.CMbackend.helpers.utility.TempoPartitaManager;

import java.util.ArrayList;
import java.util.List;

public class PallaLungaHelper {

    //1=DIFESA
    //2=CENTROCAMPO

    // Azione palla lunga 1:
// Passaggio corto (difensore → centrocampista) →
// Passaggio lungo (centrocampista → attaccante) →
// Dribbling (attaccante) →
// Dribbling (attaccante) →
// Tiro (attaccante)

    public static List<EventoPartita> eseguiPallaLunga1(
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

        // ===== 2. Passaggio lungo (centrocampista → attaccante) =====
        EventoPartita p2 = PassaggioLungoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1), ruoli.get(2));
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(6);

        // ===== 3. Dribbling (attaccante) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2));
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 4. Dribbling (attaccante) =====
        EventoPartita d2 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2));
        eventi.add(d2);
        if (!d2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 5. Tiro (attaccante) =====
        Giocatore tiratore = d2.getGiocatorePrincipale();
        EventoPartita tiro = TiroHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                tiratore, titolariDifesa);
        eventi.add(tiro);
        tempo.avanzaDi(tiro.getDurataStimata());

        return eventi;
    }




    // Azione palla lunga 2:
// Passaggio corto (difensore → centrocampista) →
// Passaggio lungo (centrocampista → attaccante) →
// Dribbling (attaccante) →
// Cross (attaccante → bomber) →
// Colpo di testa (bomber)

    public static List<EventoPartita> eseguiPallaLunga2(
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

        // ===== 2. Passaggio lungo (centrocampista → attaccante) =====
        EventoPartita p2 = PassaggioLungoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1), ruoli.get(2));
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(6);

        // ===== 3. Dribbling (attaccante) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2));
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 4. Cross (attaccante → bomber) =====
        EventoPartita cross = CrossHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2), ruoli.get(3));
        eventi.add(cross);
        if (!cross.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(3);

        // ===== 5. Colpo di testa (bomber) =====
        Giocatore colpitore = cross.getGiocatoreSecondario();
        EventoPartita testa = ColpoDiTestaHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                colpitore, titolariDifesa);
        eventi.add(testa);
        tempo.avanzaDi(testa.getDurataStimata());

        return eventi;
    }



    // Azione palla lunga 3:
    // Passaggio corto (difensore → centrocampista) →
    // Dribbling (centrocampista ) →
    // Passaggio lungo (centrocampista → attaccante) →
    // Dribbling (attaccante) →
    // Tiro (attaccante)

    public static List<EventoPartita> eseguiPallaLunga3(
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

        // ===== 2. Dribbling (centrocampista) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1));
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 3. Passaggio lungo (centrocampista → attaccante) =====
        EventoPartita p2 = PassaggioLungoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1), ruoli.get(2));
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(6);

        // ===== 4. Dribbling (attaccante) =====
        EventoPartita d2 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2));
        eventi.add(d2);
        if (!d2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 5. Tiro (attaccante) =====
        Giocatore tiratore = d2.getGiocatorePrincipale();
        EventoPartita tiro = TiroHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                tiratore, titolariDifesa);
        eventi.add(tiro);
        tempo.avanzaDi(tiro.getDurataStimata());

        return eventi;
    }



    // Azione palla lunga 4:
// Dribbling (centrocampista) →
// Passaggio lungo (centrocampista → attaccante) →
// Dribbling (attaccante) →
// Dribbling (attaccante) →
// Tiro (attaccante)

    public static List<EventoPartita> eseguiPallaLunga4(
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

        // ===== 1. Dribbling (centrocampista) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(0));
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 2. Passaggio lungo (centrocampista → attaccante) =====
        EventoPartita p1 = PassaggioLungoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(0), ruoli.get(1));
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(6);

        // ===== 3. Dribbling (attaccante) =====
        EventoPartita d2 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1));
        eventi.add(d2);
        if (!d2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 4. Dribbling (attaccante) =====
        EventoPartita d3 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1));
        eventi.add(d3);
        if (!d3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 5. Tiro (attaccante) =====
        Giocatore tiratore = d3.getGiocatorePrincipale();
        EventoPartita tiro = TiroHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                tiratore, titolariDifesa);
        eventi.add(tiro);
        tempo.avanzaDi(tiro.getDurataStimata());

        return eventi;
    }









}
