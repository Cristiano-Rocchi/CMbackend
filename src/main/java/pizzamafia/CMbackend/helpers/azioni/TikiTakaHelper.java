package pizzamafia.CMbackend.helpers.azioni;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.helpers.eventi.DribblingHelper;
import pizzamafia.CMbackend.helpers.eventi.PassaggioHelper;
import pizzamafia.CMbackend.helpers.eventi.TiroHelper;

import java.util.ArrayList;
import java.util.List;

public class TikiTakaHelper {

    // Azione tiki-taka 1:
// Passaggio (difensore → difensore) → Passaggio (difensore → centrocampista) → Passaggio (centrocampista → centrocampista) → Dribbling → Passaggio → Tiro

    public static List<EventoPartita> eseguiTikiTaka1(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        int minutoCorrente = minuto;

        // =================== 1. Passaggio difensore → difensore ===================
        EventoPartita p1 = PassaggioHelper.genera(minutoCorrente, partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;

        // =================== 2. Passaggio difensore → centrocampista ===================
        minutoCorrente++; // leggero avanzamento temporale
        EventoPartita p2 = PassaggioHelper.genera(minutoCorrente, partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        // =================== 3. Passaggio centrocampista → centrocampista ===================
        minutoCorrente++;
        EventoPartita p3 = PassaggioHelper.genera(minutoCorrente, partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;

        // =================== 4. Dribbling ===================
        minutoCorrente++;
        EventoPartita d1 = DribblingHelper.genera(minutoCorrente, partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        // =================== 5. Passaggio centrocampista → attaccante ===================
        minutoCorrente++;
        EventoPartita p4 = PassaggioHelper.genera(minutoCorrente, partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;

        // =================== 6. Tiro ===================
        minutoCorrente++;
        Giocatore tiratore = p4.getGiocatorePrincipale();
        EventoPartita t1 = TiroHelper.genera(minutoCorrente, partita, squadraAttaccante, squadraDifendente, tiratore, titolariDifesa);
        eventi.add(t1);

        return eventi;
    }



    // Azione tiki-taka 2:
// Passaggio (difensore → centrocampista) →
// Passaggio (centrocampista → difensore) →
// Passaggio (difensore → centrocampista) →
// Passaggio (centrocampista → attaccante) →
// Passaggio (attaccante → centrocampista) →
// Tiro

    public static List<EventoPartita> eseguiTikiTaka2(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        int minutoCorrente = minuto;

        // ===== 1. Passaggio difensore → centrocampista =====
        EventoPartita p1 = PassaggioHelper.genera(minutoCorrente++, partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;

        // ===== 2. Passaggio centrocampista → difensore =====
        EventoPartita p2 = PassaggioHelper.genera(minutoCorrente++, partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;

        // ===== 3. Passaggio difensore → centrocampista =====
        EventoPartita p3 = PassaggioHelper.genera(minutoCorrente++, partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;

        // ===== 4. Passaggio centrocampista → attaccante =====
        EventoPartita p4 = PassaggioHelper.genera(minutoCorrente++, partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;

        // ===== 5. Passaggio attaccante → centrocampista =====
        EventoPartita p5 = PassaggioHelper.genera(minutoCorrente++, partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p5);
        if (!p5.getEsito().equals("RIUSCITO")) return eventi;

        // ===== 6. Tiro =====
        Giocatore tiratore = p5.getGiocatorePrincipale();
        EventoPartita t1 = TiroHelper.genera(minutoCorrente++, partita, squadraAttaccante, squadraDifendente, tiratore, titolariDifesa);
        eventi.add(t1);

        return eventi;
    }



}
