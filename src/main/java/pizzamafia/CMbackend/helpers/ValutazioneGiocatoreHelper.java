package pizzamafia.CMbackend.helpers;

import pizzamafia.CMbackend.entities.Giocatore;
import pizzamafia.CMbackend.entities.StatisticheTecnicheGiocatore;
import pizzamafia.CMbackend.enums.Ruolo;

import java.util.Map;

public class ValutazioneGiocatoreHelper {


    //===========VALORETECNICO GIOCATORE================
    // Questo metodo calcola il valore tecnico finale di un giocatore,
    // in base al ruolo assegnato e alle sue statistiche tecniche.
    public static double calcolaValoreTecnico(Ruolo ruolo, StatisticheTecnicheGiocatore s) {
        return switch (ruolo) {
            case PORTIERE -> s.getPortiere() * 0.6 + s.getVelocita() * 0.2 + s.getDifesa() * 0.1 + s.getPassaggio() * 0.1;
            case DIFENSORE_CENTRALE -> s.getDifesa() * 0.5 + s.getVelocita() * 0.2 + s.getPassaggio() * 0.2 + s.getAttacco() * 0.1;
            case TERZINO -> s.getDifesa() * 0.3 + s.getVelocita() * 0.3 + s.getPassaggio() * 0.2 + s.getTiro() * 0.1 + s.getAttacco() * 0.1;
            case CENTROCAMPISTA_DIFENSIVO -> s.getDifesa() * 0.3 + s.getAttacco() * 0.2 + s.getPassaggio() * 0.2 + s.getVelocita() * 0.2 + s.getTiro() * 0.1;
            case CENTROCAMPISTA_OFFENSIVO -> s.getAttacco() * 0.3 + s.getTiro() * 0.2 + s.getVelocita() * 0.2 + s.getPassaggio() * 0.3;
            case ALA -> s.getAttacco() * 0.3 + s.getVelocita() * 0.3 + s.getTiro() * 0.2 + s.getPassaggio() * 0.1 + s.getDifesa() * 0.1;
            case ATTACCANTE_ESTERNO -> s.getAttacco() * 0.4 + s.getVelocita() * 0.3 + s.getTiro() * 0.2 + s.getPassaggio() * 0.1;
            case SECONDA_PUNTA -> s.getAttacco() * 0.4 + s.getTiro() * 0.3 + s.getVelocita() * 0.2 + s.getPassaggio() * 0.2;
            case BOMBER -> s.getAttacco() * 0.5 + s.getTiro() * 0.3 + s.getVelocita() * 0.2;
        };
    }

    //===========VALORE EFFETTIVO in formazione============

    // Questo metodo calcola il valore effettivo di un giocatore in base al ruolo assegnato in formazione.
    // Se il ruolo è diverso da quello reale, viene applicata una penalità definita in una mappa realistica.
    public static int calcolaValoreEffettivo(Giocatore g, Ruolo ruoloAssegnato) {
        Ruolo ruoloReale = g.getRuolo();
        int base = g.getValoreTecnico();

        // Nessuna penalità se è nel suo ruolo naturale
        if (ruoloReale == ruoloAssegnato) return base;

        // Mappa delle penalità tra ruoli (in percentuale)
        Map<Ruolo, Map<Ruolo, Double>> malusMatrix = Map.of(
                Ruolo.PORTIERE, Map.of(
                        Ruolo.DIFENSORE_CENTRALE, 0.90,
                        Ruolo.TERZINO, 0.90,
                        Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.95,
                        Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.95,
                        Ruolo.ALA, 0.98,
                        Ruolo.ATTACCANTE_ESTERNO, 0.98,
                        Ruolo.BOMBER, 0.98,
                        Ruolo.SECONDA_PUNTA, 0.98
                ),
                Ruolo.DIFENSORE_CENTRALE, Map.of(
                        Ruolo.TERZINO, 0.15,
                        Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.30,
                        Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.60,
                        Ruolo.ALA, 0.80,
                        Ruolo.ATTACCANTE_ESTERNO, 0.85,
                        Ruolo.BOMBER, 0.95,
                        Ruolo.SECONDA_PUNTA, 0.90,
                        Ruolo.PORTIERE, 0.98
                ),
                Ruolo.TERZINO, Map.of(
                        Ruolo.ALA, 0.10,
                        Ruolo.ATTACCANTE_ESTERNO, 0.30,
                        Ruolo.DIFENSORE_CENTRALE, 0.15,
                        Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.30,
                        Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.50,
                        Ruolo.SECONDA_PUNTA, 0.80,
                        Ruolo.BOMBER, 0.90,
                        Ruolo.PORTIERE, 0.98
                ),
                Ruolo.CENTROCAMPISTA_DIFENSIVO, Map.of(
                        Ruolo.DIFENSORE_CENTRALE, 0.20,
                        Ruolo.TERZINO, 0.35,
                        Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.20,
                        Ruolo.ALA, 0.50,
                        Ruolo.ATTACCANTE_ESTERNO, 0.60,
                        Ruolo.SECONDA_PUNTA, 0.70,
                        Ruolo.BOMBER, 0.80,
                        Ruolo.PORTIERE, 0.95
                ),
                Ruolo.CENTROCAMPISTA_OFFENSIVO, Map.of(
                        Ruolo.SECONDA_PUNTA, 0.10,
                        Ruolo.ATTACCANTE_ESTERNO, 0.15,
                        Ruolo.ALA, 0.30,
                        Ruolo.BOMBER, 0.35,
                        Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.15,
                        Ruolo.TERZINO, 0.60,
                        Ruolo.DIFENSORE_CENTRALE, 0.75,
                        Ruolo.PORTIERE, 0.95
                ),
                Ruolo.ALA, Map.of(
                        Ruolo.ATTACCANTE_ESTERNO, 0.15,
                        Ruolo.TERZINO, 0.10,
                        Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.30,
                        Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.40,
                        Ruolo.DIFENSORE_CENTRALE, 0.35,
                        Ruolo.SECONDA_PUNTA, 0.45,
                        Ruolo.BOMBER, 0.50,
                        Ruolo.PORTIERE, 0.98
                ),
                Ruolo.ATTACCANTE_ESTERNO, Map.of(
                        Ruolo.SECONDA_PUNTA, 0.10,
                        Ruolo.ALA, 0.15,
                        Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.20,
                        Ruolo.BOMBER, 0.30,
                        Ruolo.TERZINO, 0.25,
                        Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.50,
                        Ruolo.DIFENSORE_CENTRALE, 0.70,
                        Ruolo.PORTIERE, 0.98
                ),
                Ruolo.BOMBER, Map.of(
                        Ruolo.SECONDA_PUNTA, 0.15,
                        Ruolo.ATTACCANTE_ESTERNO, 0.25,
                        Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.25,
                        Ruolo.ALA, 0.50,
                        Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.60,
                        Ruolo.TERZINO, 0.75,
                        Ruolo.DIFENSORE_CENTRALE, 0.90,
                        Ruolo.PORTIERE, 0.98
                ),
                Ruolo.SECONDA_PUNTA, Map.of(
                        Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.10,
                        Ruolo.BOMBER, 0.20,
                        Ruolo.ATTACCANTE_ESTERNO, 0.15,
                        Ruolo.ALA, 0.25,
                        Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.45,
                        Ruolo.TERZINO, 0.60,
                        Ruolo.DIFENSORE_CENTRALE, 0.80,
                        Ruolo.PORTIERE, 0.98
                )
        );

        Map<Ruolo, Double> malusMap = malusMatrix.get(ruoloReale);

        if (malusMap == null || !malusMap.containsKey(ruoloAssegnato)) {
            throw new IllegalArgumentException("Ruolo assegnato non valido o non mappato per questo ruolo reale: "
                    + ruoloReale + " → " + ruoloAssegnato);
        }

        double malus = malusMap.get(ruoloAssegnato);


        return (int) (base * (1 - malus));
    }
}
