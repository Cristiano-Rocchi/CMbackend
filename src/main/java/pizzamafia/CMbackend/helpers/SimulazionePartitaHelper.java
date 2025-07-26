package pizzamafia.CMbackend.helpers;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.helpers.eventi.DribblingHelper;
import pizzamafia.CMbackend.helpers.eventi.TiroHelper;

import java.util.*;

public class SimulazionePartitaHelper {

    private static final Random random = new Random();

    public static List<EventoPartita> simulaPartita(
            Partita partita,
            List<Titolari> titolariCasa,
            List<Titolari> titolariTrasferta
    ) {
        List<EventoPartita> eventi = new ArrayList<>();

        Squadra squadraCasa = partita.getSquadraCasa();
        Squadra squadraTrasferta = partita.getSquadraTrasferta();

        for (int minuto = 1; minuto <= 90; minuto += 5) {
            List<EventoPartita> eventiBlocco = simulaBlocco(
                    minuto,
                    minuto + 4,
                    partita,
                    titolariCasa,
                    titolariTrasferta,
                    squadraCasa,
                    squadraTrasferta
            );
            eventi.addAll(eventiBlocco);
        }

        eventi.sort(Comparator.comparingInt(EventoPartita::getMinuto));
        return eventi;
    }

    private static List<EventoPartita> simulaBlocco(
            int minutoInizio,
            int minutoFine,
            Partita partita,
            List<Titolari> titolariCasa,
            List<Titolari> titolariTrasferta,
            Squadra squadraCasa,
            Squadra squadraTrasferta
    ) {
        List<EventoPartita> eventi = new ArrayList<>();

        // =================== 1. Calcola la forza media effettiva ===================
        double forzaCasa = titolariCasa.stream()
                .mapToDouble(Titolari::getValoreEffettivo)
                .average().orElse(0);

        double forzaTrasferta = titolariTrasferta.stream()
                .mapToDouble(Titolari::getValoreEffettivo)
                .average().orElse(0);

        // Applichiamo un piccolo bonus casa
        forzaCasa += 2.0;

        // =================== 2. Probabilità di attacco pesata ===================
        double totale = forzaCasa + forzaTrasferta;
        double probabilitaAttaccoCasa = forzaCasa / totale;
        boolean attaccaCasa = random.nextDouble() < probabilitaAttaccoCasa;

        Squadra squadraAttaccante = attaccaCasa ? squadraCasa : squadraTrasferta;
        Squadra squadraDifendente = attaccaCasa ? squadraTrasferta : squadraCasa;

        List<Titolari> titolariAttacco = attaccaCasa ? titolariCasa : titolariTrasferta;
        List<Titolari> titolariDifesa = attaccaCasa ? titolariTrasferta : titolariCasa;

        // =================== 3. Numero dinamico di azioni ===================
        // Squadra forte = più probabilità di fare 2 azioni
        int numeroAzioni;
        double dominance = Math.abs(forzaCasa - forzaTrasferta);

        if (dominance > 20) {
            numeroAzioni = attaccaCasa ? (random.nextDouble() < 0.7 ? 2 : 1) : (random.nextDouble() < 0.7 ? 1 : 0);
        } else if (dominance > 10) {
            numeroAzioni = random.nextDouble() < 0.6 ? 1 : 0;
        } else {
            numeroAzioni = random.nextDouble() < 0.4 ? 1 : 0;
        }

        // =================== 4. Genera le azioni ===================

        //azione 1 dribbling-tiro
        for (int i = 0; i < numeroAzioni; i++) {
            int minutoEvento = minutoInizio + random.nextInt(minutoFine - minutoInizio + 1);

            EventoPartita eventoDribbling = DribblingHelper.genera(minutoEvento, partita, squadraAttaccante, titolariAttacco, titolariDifesa);
            eventi.add(eventoDribbling);


            if (eventoDribbling.getEsito().equals("RIUSCITO")) {
                EventoPartita eventoTiro = TiroHelper.genera(minutoEvento, partita, squadraAttaccante, squadraDifendente, eventoDribbling.getGiocatorePrincipale(), titolariDifesa);
                eventi.add(eventoTiro);
            }

        }

        return eventi;

    }


}
