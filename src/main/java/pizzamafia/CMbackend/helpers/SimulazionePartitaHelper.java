package pizzamafia.CMbackend.helpers;

import pizzamafia.CMbackend.entities.MarcatorePartita;
import pizzamafia.CMbackend.entities.Partita;
import pizzamafia.CMbackend.entities.Squadra;
import pizzamafia.CMbackend.entities.Titolari;

import java.util.*;
import java.util.stream.Collectors;

public class SimulazionePartitaHelper {

    private static final Random random = new Random();

    /**
     * Calcola il valore medio della formazione basato sui titolari schierati.
     * @param titolari Lista di titolari effettivi della formazione (11)
     * @return valore medio squadra
     */
    public static double calcolaValoreFormazione(List<Titolari> titolari) {
        if (titolari == null || titolari.isEmpty()) return 0;

        double somma = titolari.stream()
                .mapToDouble(Titolari::getValoreEffettivo)
                .sum();

        return somma / titolari.size(); // dovrebbe essere sempre 11
    }

    /**
     * Applica un piccolo bonus alla squadra di casa (fattore campo).
     * @param valore originale
     * @return valore con bonus
     */
    public static double applicaFattoreCasa(double valore) {
        return valore + 2; // Bonus casa
    }

    /**
     * Calcola il numero di goal da assegnare in base al rapporto tra i valori delle due squadre.
     * @param forzaCasa valore squadra casa
     * @param forzaTrasferta valore squadra trasferta
     * @return array con goals: [goalCasa, goalTrasferta]
     */
    public static int[] generaRisultato(double forzaCasa, double forzaTrasferta) {
        double diff = forzaCasa - forzaTrasferta;

        double probCasa = 0.5 + (diff / 40.0); // scala realistica
        probCasa = Math.min(0.9, Math.max(0.1, probCasa)); // clamp

        double esito = random.nextDouble();

        int goalCasa = 0;
        int goalTrasferta = 0;

        if (esito < probCasa - 0.1) {
            // Vince trasferta
            goalTrasferta = generaGoalRandom();
            goalCasa = Math.max(0, goalTrasferta - random.nextInt(3));
        } else if (esito <= probCasa + 0.1) {
            // Pareggio
            int gol = random.nextInt(3); // 0–2
            goalCasa = gol;
            goalTrasferta = gol;
        } else {
            // Vince casa
            goalCasa = generaGoalRandom();
            goalTrasferta = Math.max(0, goalCasa - random.nextInt(3));
        }

        return new int[]{goalCasa, goalTrasferta};
    }

    /**
     * Genera un numero realistico di goal (1–4)
     */
    private static int generaGoalRandom() {
        return 1 + random.nextInt(4);
    }



    /**
     * Genera la lista di marcatori per una partita.
     * @param goalCasa numero di goal segnati dalla squadra di casa
     * @param goalTrasferta numero di goal segnati dalla squadra in trasferta
     * @param titolariCasa lista dei titolari della squadra di casa
     * @param titolariTrasferta lista dei titolari della squadra in trasferta
     * @param partita oggetto Partita a cui i marcatori sono legati
     * @return lista completa di marcatori
     */
    public static List<MarcatorePartita> generaMarcatori(
            int goalCasa,
            int goalTrasferta,
            List<Titolari> titolariCasa,
            List<Titolari> titolariTrasferta,
            Partita partita
    ) {
        List<MarcatorePartita> marcatori = new ArrayList<>();

        marcatori.addAll(
                generaMarcatoriPerSquadra(goalCasa, titolariCasa, partita, partita.getSquadraCasa())
        );

        marcatori.addAll(
                generaMarcatoriPerSquadra(goalTrasferta, titolariTrasferta, partita, partita.getSquadraTrasferta())
        );

        //marcatori restituiti in ordine di minuto
        return marcatori.stream()
                .sorted(Comparator.comparingInt(MarcatorePartita::getMinuto))
                .collect(Collectors.toList());

    }

    private static List<MarcatorePartita> generaMarcatoriPerSquadra(
            int goal,
            List<Titolari> titolari,
            Partita partita,
            Squadra squadra
    ) {
        List<MarcatorePartita> marcatori = new ArrayList<>();
        Set<Integer> minutiUsati = new HashSet<>();

        for (int i = 0; i < goal; i++) {
            Titolari marcatore = titolari.get(random.nextInt(titolari.size()));

            int minuto;
            do {
                minuto = 1 + random.nextInt(90);
            } while (minutiUsati.contains(minuto));
            minutiUsati.add(minuto);

            MarcatorePartita m = new MarcatorePartita();
            m.setPartita(partita);
            m.setGiocatore(marcatore.getGiocatore());
            m.setSquadra(squadra);
            m.setMinuto(minuto);

            marcatori.add(m);
        }

        return marcatori;
    }


}
