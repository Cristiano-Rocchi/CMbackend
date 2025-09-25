package pizzamafia.CMbackend.helpers;

import pizzamafia.CMbackend.entities.Giocatore;
import pizzamafia.CMbackend.entities.StatisticheTecnicheGiocatore;
import pizzamafia.CMbackend.enums.Ruolo;

import java.util.Map;

public class ValutazioneGiocatoreHelper {


    //===========VALORETECNICO GIOCATORE================
    // Questo metodo calcola il valore tecnico finale di un giocatore,
    // in base al ruolo assegnato e alle sue statistiche tecniche.
    public static int calcolaValoreTecnico(Ruolo ruolo, StatisticheTecnicheGiocatore stats) {
        // Ottiene i pesi specifici per il ruolo
        Map<String, Double> pesi = getPesiPerRuolo(ruolo);

        double sommaPesi = 0;
        double punteggioGrezzo = 0;

        // Somma i contributi di ogni attributo pesato
        for (Map.Entry<String, Double> entry : pesi.entrySet()) {
            String attributo = entry.getKey();
            double peso = entry.getValue();

            // recupera il valore dal POJO delle statistiche
            int valore = switch (attributo) {
                //Tecniche
                case "tecnica" -> stats.getTecnica();
                case "equilibrio" -> stats.getEquilibrio();
                case "colpoDiTesta" -> stats.getColpoDiTesta();
                case "tiro" -> stats.getTiro();
                case "assist" -> stats.getAssist();
                case "finalizzazione" -> stats.getFinalizzazione();
                case "dribbling" -> stats.getDribbling();
                case "visione" -> stats.getVisione();
                case "calciPiazzati" -> stats.getCalciPiazzati();
                case "cross" -> stats.getCross();
                case "contrasti" -> stats.getContrasti();
                case "marcatura" -> stats.getMarcatura();
                case "intercettazione" -> stats.getIntercettazione();
                //Mentali
                case "carisma" -> stats.getCarisma();
                case "concentrazione" -> stats.getConcentrazione();
                case "coraggio" -> stats.getCoraggio();
                case "leadership" -> stats.getLeadership();
                case "letturaDelGioco" -> stats.getLetturaDelGioco();
                case "giocoDiSquadra" -> stats.getGiocoDiSquadra();
                case "creativita" -> stats.getCreativita();
                case "freddezza" -> stats.getFreddezza();
                case "aggressivita" -> stats.getAggressivita();
                //Fisiche
                case "accelerazione" -> stats.getAccelerazione();
                case "scatto" -> stats.getScatto();
                case "elevazione" -> stats.getElevazione();
                case "forzaFisica" -> stats.getForzaFisica();
                case "resistenza" -> stats.getResistenza();
                //Portiere
                case "tuffo" -> stats.getTuffo();
                case "riflessi" -> stats.getRiflessi();
                case "posizione" -> stats.getPosizione();
                case "uscite" -> stats.getUscite();
                case "presa" -> stats.getPresa();
                default -> 0;
            };

            punteggioGrezzo += valore * peso;
            sommaPesi += peso;
        }

        // Normalizza il punteggio sulla scala 1–99
        double maxPunteggio = 99 * sommaPesi;
        double normalizzato = (punteggioGrezzo / maxPunteggio) * 99;

        // === BONUS Carisma e Leadership ===
        // clAvg = media tra Carisma e Leadership
        double clAvg = (stats.getCarisma() + stats.getLeadership()) / 2.0;

        // Bonus
        double bonus;
        if (clAvg >= 95) {
            bonus = 3.0;
        } else if (clAvg >= 90) {
            bonus = 2.0;
        } else if (clAvg >= 85) {
            bonus = 1.5;
        } else if (clAvg >= 80) {
            bonus = 1.0;
        } else if (clAvg >= 75) {
            bonus = 0.5;
        } else {
            bonus = 0.0;
        }
        // Somma bonus e applica cap a 99
        double finalCapped = Math.min(99.0, normalizzato + bonus);

        // Arrotonda
        return (int) Math.round(finalCapped);

    }


    //MAP DEI RUOLI CON PESI
    private static Map<String, Double> getPesiPerRuolo(Ruolo ruolo) {
        switch (ruolo) {
            case PORTIERE:
                return Map.ofEntries(
                        // === [A] PILASTRO — 75% ===
                        Map.entry("tuffo",             18.0),
                        Map.entry("riflessi",          18.0),
                        Map.entry("posizione",         18.0),
                        Map.entry("uscite",            12.0),
                        Map.entry("presa",             12.0),

                        // === [B] SUPPORTO — 25% ===
                        Map.entry("concentrazione",     5.0),
                        Map.entry("elevazione",         5.0),
                        Map.entry("coraggio",           5.0),
                        Map.entry("equilibrio",         5.0),
                        Map.entry("tecnica",            2.0),

                        // === [D] IRRILEVANTI — 0% ===
                        Map.entry("dribbling",          0.0),
                        Map.entry("tiro",               0.0),
                        Map.entry("colpoDiTesta",       0.0),
                        Map.entry("assist",             0.0),
                        Map.entry("finalizzazione",     0.0),
                        Map.entry("contrasti",          0.0),
                        Map.entry("marcatura",          0.0),
                        Map.entry("intercettazione",    0.0),
                        Map.entry("creativita",         0.0),
                        Map.entry("aggressivita",       0.0),
                        Map.entry("scatto",             0.0),
                        Map.entry("accelerazione",      0.0),
                        Map.entry("calciPiazzati",      0.0),
                        Map.entry("cross",              0.0),
                        Map.entry("carisma",            0.0),
                        Map.entry("leadership",         0.0),
                        Map.entry("giocoDiSquadra",     0.0),
                        Map.entry("visione",            0.0),
                        Map.entry("resistenza",         0.0),
                        Map.entry("freddezza",          0.0)
                );


            case DIFENSORE_CENTRALE:
                return Map.ofEntries(
                        // === [A] PILASTRO — 60% ===
                        Map.entry("marcatura",        14.0),
                        Map.entry("contrasti",        14.0),
                        Map.entry("letturaDelGioco",  14.0),
                        Map.entry("concentrazione",   10.0),
                        Map.entry("aggressivita",      8.0),

                        // === [B] SUPPORTO — 35% ===
                        Map.entry("colpoDiTesta",      5.0),
                        Map.entry("elevazione",        5.0),
                        Map.entry("intercettazione",   5.0),
                        Map.entry("coraggio",          5.0),
                        Map.entry("equilibrio",        4.0),
                        Map.entry("forzaFisica",       4.0),
                        Map.entry("giocoDiSquadra",    4.0),
                        Map.entry("tecnica",           3.0),

                        // === [C] MARGINALI — 5% ===
                        Map.entry("visione",           1.5),
                        Map.entry("scatto",            1.0),
                        Map.entry("accelerazione",     1.0),
                        Map.entry("resistenza",        1.0),
                        Map.entry("calciPiazzati",      0.5),

                        // [D] IRRILEVANTI — 0%
                        Map.entry("tiro",              0.0),
                        Map.entry("assist",            0.0),
                        Map.entry("finalizzazione",    0.0),
                        Map.entry("dribbling",         0.0),
                        Map.entry("cross",             0.0),
                        Map.entry("freddezza",         0.0),
                        Map.entry("creativita",        0.0),
                        Map.entry("carisma",           0.0),
                        Map.entry("leadership",        0.0)



                );

            case TERZINO_DX, TERZINO_SX:
                return Map.ofEntries(
                        // === [A] PILASTRO — 60% ===
                        Map.entry("marcatura",        14.0),
                        Map.entry("cross",            13.0),
                        Map.entry("resistenza",       12.0),
                        Map.entry("letturaDelGioco",  11.0),
                        Map.entry("contrasti",        10.0),

                        // === [B] SUPPORTO — 35% ===
                        Map.entry("concentrazione",    6.0),
                        Map.entry("aggressivita",      5.0),
                        Map.entry("intercettazione",   5.0),
                        Map.entry("giocoDiSquadra",    5.0),
                        Map.entry("scatto",            3.0),
                        Map.entry("accelerazione",     3.0),
                        Map.entry("assist",            4.0),
                        Map.entry("coraggio",          2.0),
                        Map.entry("tecnica",           2.0),

                        // === [C] MARGINALI — 5% ===
                        Map.entry("visione",           1.0),
                        Map.entry("tiro",              1.0),
                        Map.entry("dribbling",         1.0),
                        Map.entry("equilibrio",        1.0),
                        Map.entry("forzaFisica",       1.0),

                        // [D] IRRILEVANTI — 0%
                        Map.entry("colpoDiTesta",      0.0),
                        Map.entry("elevazione",        0.0),
                        Map.entry("finalizzazione",    0.0),
                        Map.entry("calciPiazzati",     0.0),
                        Map.entry("freddezza",         0.0),
                        Map.entry("creativita",        0.0),
                        Map.entry("carisma",           0.0),
                        Map.entry("leadership",        0.0)
                );


            case ALA_DX, ALA_SX:
                return Map.ofEntries(
                        // === [A] PILASTRO — 55% ===
                        Map.entry("cross",            11.0),
                        Map.entry("contrasti",        11.0),
                        Map.entry("letturaDelGioco",  11.0),
                        Map.entry("concentrazione",   11.0),
                        Map.entry("resistenza",       11.0),

                        // === [B] SUPPORTO — 40% ===
                        Map.entry("assist",            6.0),
                        Map.entry("marcatura",         5.0),
                        Map.entry("dribbling",         5.0),
                        Map.entry("giocoDiSquadra",    5.0),
                        Map.entry("scatto",            5.0),
                        Map.entry("accelerazione",     5.0),
                        Map.entry("aggressivita",      3.0),
                        Map.entry("tecnica",           3.0),
                        Map.entry("intercettazione",   3.0),

                        // === [C] MARGINALI — 5% ===
                        Map.entry("tiro",              1.0),
                        Map.entry("equilibrio",        1.0),
                        Map.entry("visione",           1.0),
                        Map.entry("creativita",        1.0),
                        Map.entry("forzaFisica",       1.0),

                        // === [D] IRRILEVANTI — 0% ===
                        Map.entry("colpoDiTesta",      0.0),
                        Map.entry("elevazione",        0.0),
                        Map.entry("calciPiazzati",     0.0),
                        Map.entry("freddezza",         0.0),
                        Map.entry("finalizzazione",    0.0),
                        Map.entry("carisma",           0.0),
                        Map.entry("leadership",        0.0)

                );





            case CENTROCAMPISTA_DIFENSIVO:
                return Map.ofEntries(
                        // === [A] PILASTRO — 55% ===
                        Map.entry("intercettazione",   11.0),
                        Map.entry("letturaDelGioco",   11.0),
                        Map.entry("tecnica",           11.0),
                        Map.entry("resistenza",        11.0),
                        Map.entry("giocoDiSquadra",    11.0),

                        // === [B] SUPPORTO — 40% ===
                        Map.entry("visione",            5.0),
                        Map.entry("forzaFisica",        4.0),
                        Map.entry("concentrazione",     5.0),
                        Map.entry("contrasti",          5.0),
                        Map.entry("equilibrio",         4.0),
                        Map.entry("aggressivita",       4.0),
                        Map.entry("coraggio",           4.0),
                        Map.entry("creativita",         4.0),
                        Map.entry("marcatura",          4.0),

                        // === [C] MARGINALI — 5% ===
                        Map.entry("calciPiazzati",      1.5),
                        Map.entry("tiro",               1.5),
                        Map.entry("assist",             1.0),
                        Map.entry("dribbling",          1.0),

                        // === [D] IRRILEVANTI — 0% ===
                        Map.entry("colpoDiTesta",       0.0),
                        Map.entry("elevazione",         0.0),
                        Map.entry("scatto",             0.0),
                        Map.entry("accelerazione",      0.0),
                        Map.entry("freddezza",          0.0),
                        Map.entry("finalizzazione",     0.0),
                        Map.entry("cross",              0.0),
                        Map.entry("carisma",            0.0),
                        Map.entry("leadership",         0.0)
                );



            case CENTROCAMPISTA_CENTRALE:
                return Map.ofEntries(
                        // === [A] PILASTRO — 55% ===
                        Map.entry("tecnica",           15.0),
                        Map.entry("visione",           14.0),
                        Map.entry("letturaDelGioco",   13.0),
                        Map.entry("giocoDiSquadra",    13.0),

                        // === [B] SUPPORTO — 40% ===
                        Map.entry("concentrazione",     6.0),
                        Map.entry("creativita",         6.0),
                        Map.entry("resistenza",         5.0),
                        Map.entry("equilibrio",         4.0),
                        Map.entry("assist",             4.0),
                        Map.entry("tiro",               4.0),
                        Map.entry("aggressivita",       3.0),
                        Map.entry("contrasti",          3.0),
                        Map.entry("dribbling",          3.0),
                        Map.entry("calciPiazzati",      2.0),

                        // === [C] MARGINALI — 5% ===
                        Map.entry("finalizzazione",     1.0),
                        Map.entry("marcatura",          1.0),
                        Map.entry("colpoDiTesta",       1.0),
                        Map.entry("intercettazione",    1.0),
                        Map.entry("forzaFisica",        1.0),

                        // === [D] IRRILEVANTI — 0% ===
                        Map.entry("accelerazione",      0.0),
                        Map.entry("scatto",             0.0),
                        Map.entry("elevazione",         0.0),
                        Map.entry("freddezza",          0.0),
                        Map.entry("cross",              0.0),
                        Map.entry("carisma",            0.0),
                        Map.entry("leadership",         0.0)
                );


            case CENTROCAMPISTA_OFFENSIVO:
                return Map.ofEntries(
                        // === [A] PILASTRO — 55% ===
                        Map.entry("tecnica",           13.0),
                        Map.entry("visione",           13.0),
                        Map.entry("creativita",        13.0),
                        Map.entry("letturaDelGioco",    8.0),
                        Map.entry("dribbling",          8.0),

                        // === [B] SUPPORTO — 40% ===

                        Map.entry("giocoDiSquadra",     6.0),
                        Map.entry("assist",             6.0),
                        Map.entry("resistenza",         5.0),
                        Map.entry("coraggio",           5.0),
                        Map.entry("tiro",               5.0),
                        Map.entry("equilibrio",         4.0),
                        Map.entry("calciPiazzati",      4.0),
                        Map.entry("concentrazione",     5.0),

                        // === [C] MARGINALI — 5% ===
                        Map.entry("colpoDiTesta",       1.0),
                        Map.entry("contrasti",          1.0),
                        Map.entry("freddezza",          1.0),
                        Map.entry("forzaFisica",        1.0),
                        Map.entry("finalizzazione",     1.0),

                        // === [D] IRRILEVANTI — 0% ===
                        Map.entry("accelerazione",      0.0),
                        Map.entry("scatto",             0.0),
                        Map.entry("elevazione",         0.0),
                        Map.entry("aggressivita",       0.0),
                        Map.entry("marcatura",          0.0),
                        Map.entry("intercettazione",    0.0),
                        Map.entry("cross",              0.0),
                        Map.entry("carisma",            0.0),
                        Map.entry("leadership",         0.0)
                );



            case ATTACCANTE_ESTERNO_DX, ATTACCANTE_ESTERNO_SX:
                return Map.ofEntries(
                        // === [A] PILASTRO — 60% ===
                        Map.entry("scatto",            13.0),
                        Map.entry("accelerazione",     13.0),
                        Map.entry("tecnica",           12.0),
                        Map.entry("letturaDelGioco",    8.0),
                        Map.entry("dribbling",          7.0),
                        Map.entry("tiro",               7.0),

                        // === [B] SUPPORTO — 35% ===
                        Map.entry("finalizzazione",     6.0),
                        Map.entry("assist",             6.0),
                        Map.entry("creativita",         6.0),
                        Map.entry("coraggio",           5.0),
                        Map.entry("resistenza",         4.0),
                        Map.entry("freddezza",          4.0),
                        Map.entry("cross",              4.0),

                        // === [C] MARGINALI — 5% ===
                        Map.entry("equilibrio",         1.0),
                        Map.entry("colpoDiTesta",       1.0),
                        Map.entry("giocoDiSquadra",     1.0),
                        Map.entry("concentrazione",     1.0),
                        Map.entry("forzaFisica",        1.0),

                        // === [D] IRRILEVANTI — 0% ===
                        Map.entry("marcatura",          0.0),
                        Map.entry("contrasti",          0.0),
                        Map.entry("intercettazione",    0.0),
                        Map.entry("calciPiazzati",      0.0),
                        Map.entry("aggressivita",       0.0),
                        Map.entry("elevazione",         0.0),
                        Map.entry("carisma",            0.0),
                        Map.entry("leadership",         0.0)
                );



            case SECONDA_PUNTA:
                return Map.ofEntries(
                        // === [A] PILASTRO — 60% ===
                        Map.entry("tecnica",           12.0),
                        Map.entry("creativita",        12.0),
                        Map.entry("visione",           12.0),
                        Map.entry("letturaDelGioco",   12.0),
                        Map.entry("assist",            12.0),

                        // === [B] SUPPORTO — 35% ===
                        Map.entry("dribbling",          6.0),
                        Map.entry("tiro",               5.0),
                        Map.entry("coraggio",           5.0),
                        Map.entry("finalizzazione",     5.0),
                        Map.entry("giocoDiSquadra",     4.0),
                        Map.entry("calciPiazzati",      4.0),
                        Map.entry("freddezza",          3.0),
                        Map.entry("concentrazione",     3.0),

                        // === [C] MARGINALI — 5% ===
                        Map.entry("equilibrio",         1.0),
                        Map.entry("colpoDiTesta",       1.0),
                        Map.entry("scatto",             1.0),
                        Map.entry("forzaFisica",        1.0),
                        Map.entry("resistenza",         1.0),

                        // === [D] IRRILEVANTI — 0% ===
                        Map.entry("marcatura",          0.0),
                        Map.entry("contrasti",          0.0),
                        Map.entry("aggressivita",       0.0),
                        Map.entry("accelerazione",      0.0),
                        Map.entry("elevazione",         0.0),
                        Map.entry("cross",              0.0),
                        Map.entry("carisma",            0.0),
                        Map.entry("leadership",         0.0)
                );


            case BOMBER:
                return Map.ofEntries(
                        // === [A] PILASTRO — 60% ===
                        Map.entry("finalizzazione",    15.0),
                        Map.entry("freddezza",         15.0),
                        Map.entry("tiro",              12.0),
                        Map.entry("letturaDelGioco",   10.0),
                        Map.entry("forzaFisica",        8.0),

                        // === [B] SUPPORTO — 35% ===
                        Map.entry("tecnica",            5.0),
                        Map.entry("elevazione",         5.0),
                        Map.entry("equilibrio",         5.0),
                        Map.entry("colpoDiTesta",       5.0),
                        Map.entry("concentrazione",     4.0),
                        Map.entry("coraggio",           4.0),
                        Map.entry("giocoDiSquadra",     4.0),
                        Map.entry("assist",             3.0),

                        // === [C] MARGINALI — 5% ===
                        Map.entry("dribbling",          1.0),
                        Map.entry("calciPiazzati",      1.0),
                        Map.entry("visione",            1.0),
                        Map.entry("creativita",         1.0),
                        Map.entry("aggressivita",       1.0),

                        // === [D] IRRILEVANTI — 0% ===
                        Map.entry("scatto",             0.0),
                        Map.entry("accelerazione",      0.0),
                        Map.entry("resistenza",         0.0),
                        Map.entry("marcatura",          0.0),
                        Map.entry("contrasti",          0.0),
                        Map.entry("intercettazione",    0.0),
                        Map.entry("cross",              0.0),
                        Map.entry("carisma",            0.0),
                        Map.entry("leadership",         0.0)
                );












            default:
                throw new IllegalArgumentException("Ruolo non supportato: " + ruolo);
        }
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
        Map<Ruolo, Map<Ruolo, Double>> malusMatrix = Map.ofEntries(
                Map.entry(Ruolo.PORTIERE, Map.ofEntries(
                        Map.entry(Ruolo.DIFENSORE_CENTRALE, 0.90),
                        Map.entry(Ruolo.TERZINO_DX, 0.90),
                        Map.entry(Ruolo.TERZINO_SX, 0.90),
                        Map.entry(Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.95),
                        Map.entry(Ruolo.CENTROCAMPISTA_CENTRALE, 0.95),
                        Map.entry(Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.95),
                        Map.entry(Ruolo.ALA_DX, 0.98),
                        Map.entry(Ruolo.ALA_SX, 0.98),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_DX, 0.98),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_SX, 0.98),
                        Map.entry(Ruolo.BOMBER, 0.98),
                        Map.entry(Ruolo.SECONDA_PUNTA, 0.98)
                )),
                Map.entry(Ruolo.DIFENSORE_CENTRALE, Map.ofEntries(
                        Map.entry(Ruolo.TERZINO_DX, 0.15),
                        Map.entry(Ruolo.TERZINO_SX, 0.15),
                        Map.entry(Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.30),
                        Map.entry(Ruolo.CENTROCAMPISTA_CENTRALE, 0.50),
                        Map.entry(Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.60),
                        Map.entry(Ruolo.ALA_DX, 0.80),
                        Map.entry(Ruolo.ALA_SX, 0.80),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_DX, 0.85),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_SX, 0.85),
                        Map.entry(Ruolo.BOMBER, 0.95),
                        Map.entry(Ruolo.SECONDA_PUNTA, 0.90),
                        Map.entry(Ruolo.PORTIERE, 0.98)
                )),
                Map.entry(Ruolo.TERZINO_DX, Map.ofEntries(
                        Map.entry(Ruolo.TERZINO_SX, 0.05),
                        Map.entry(Ruolo.ALA_DX, 0.10),
                        Map.entry(Ruolo.ALA_SX, 0.20),
                        Map.entry(Ruolo.DIFENSORE_CENTRALE, 0.15),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_DX, 0.30),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_SX, 0.40),
                        Map.entry(Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.30),
                        Map.entry(Ruolo.CENTROCAMPISTA_CENTRALE, 0.40),
                        Map.entry(Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.50),
                        Map.entry(Ruolo.SECONDA_PUNTA, 0.80),
                        Map.entry(Ruolo.BOMBER, 0.90),
                        Map.entry(Ruolo.PORTIERE, 0.98)
                )),
                Map.entry(Ruolo.TERZINO_SX, Map.ofEntries(
                        Map.entry(Ruolo.TERZINO_DX, 0.05),
                        Map.entry(Ruolo.ALA_SX, 0.10),
                        Map.entry(Ruolo.ALA_DX, 0.20),
                        Map.entry(Ruolo.DIFENSORE_CENTRALE, 0.15),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_SX, 0.30),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_DX, 0.40),
                        Map.entry(Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.30),
                        Map.entry(Ruolo.CENTROCAMPISTA_CENTRALE, 0.40),
                        Map.entry(Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.50),
                        Map.entry(Ruolo.SECONDA_PUNTA, 0.80),
                        Map.entry(Ruolo.BOMBER, 0.90),
                        Map.entry(Ruolo.PORTIERE, 0.98)
                )),
                Map.entry(Ruolo.CENTROCAMPISTA_DIFENSIVO, Map.ofEntries(
                        Map.entry(Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.20),
                        Map.entry(Ruolo.CENTROCAMPISTA_CENTRALE, 0.10),
                        Map.entry(Ruolo.DIFENSORE_CENTRALE, 0.20),
                        Map.entry(Ruolo.TERZINO_DX, 0.35),
                        Map.entry(Ruolo.TERZINO_SX, 0.35),
                        Map.entry(Ruolo.ALA_DX, 0.50),
                        Map.entry(Ruolo.ALA_SX, 0.50),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_DX, 0.60),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_SX, 0.60),
                        Map.entry(Ruolo.SECONDA_PUNTA, 0.70),
                        Map.entry(Ruolo.BOMBER, 0.80),
                        Map.entry(Ruolo.PORTIERE, 0.95)
                )),
                Map.entry(Ruolo.CENTROCAMPISTA_CENTRALE, Map.ofEntries(
                        Map.entry(Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.10),
                        Map.entry(Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.10),
                        Map.entry(Ruolo.DIFENSORE_CENTRALE, 0.30),
                        Map.entry(Ruolo.TERZINO_DX, 0.40),
                        Map.entry(Ruolo.TERZINO_SX, 0.40),
                        Map.entry(Ruolo.ALA_DX, 0.50),
                        Map.entry(Ruolo.ALA_SX, 0.50),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_DX, 0.60),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_SX, 0.60),
                        Map.entry(Ruolo.SECONDA_PUNTA, 0.70),
                        Map.entry(Ruolo.BOMBER, 0.80),
                        Map.entry(Ruolo.PORTIERE, 0.95)
                )),

                Map.entry(Ruolo.CENTROCAMPISTA_OFFENSIVO, Map.ofEntries(
                        Map.entry(Ruolo.SECONDA_PUNTA, 0.10),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_DX, 0.15),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_SX, 0.15),
                        Map.entry(Ruolo.ALA_DX, 0.30),
                        Map.entry(Ruolo.ALA_SX, 0.30),
                        Map.entry(Ruolo.BOMBER, 0.35),
                        Map.entry(Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.15),
                        Map.entry(Ruolo.CENTROCAMPISTA_CENTRALE, 0.10),
                        Map.entry(Ruolo.TERZINO_DX, 0.60),
                        Map.entry(Ruolo.TERZINO_SX, 0.60),
                        Map.entry(Ruolo.DIFENSORE_CENTRALE, 0.75),
                        Map.entry(Ruolo.PORTIERE, 0.95)
                )),
                Map.entry(Ruolo.ALA_DX, Map.ofEntries(
                        Map.entry(Ruolo.ALA_SX, 0.05),
                        Map.entry(Ruolo.TERZINO_DX, 0.10),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_DX, 0.15),
                        Map.entry(Ruolo.TERZINO_SX, 0.20),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_SX, 0.25),
                        Map.entry(Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.30),
                        Map.entry(Ruolo.CENTROCAMPISTA_CENTRALE, 0.30),
                        Map.entry(Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.40),
                        Map.entry(Ruolo.DIFENSORE_CENTRALE, 0.35),
                        Map.entry(Ruolo.SECONDA_PUNTA, 0.45),
                        Map.entry(Ruolo.BOMBER, 0.50),
                        Map.entry(Ruolo.PORTIERE, 0.98)
                )),
                Map.entry(Ruolo.ALA_SX, Map.ofEntries(
                        Map.entry(Ruolo.ALA_DX, 0.05),
                        Map.entry(Ruolo.TERZINO_SX, 0.10),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_SX, 0.15),
                        Map.entry(Ruolo.TERZINO_DX, 0.20),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_DX, 0.25),
                        Map.entry(Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.30),
                        Map.entry(Ruolo.CENTROCAMPISTA_CENTRALE, 0.30),
                        Map.entry(Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.40),
                        Map.entry(Ruolo.DIFENSORE_CENTRALE, 0.35),
                        Map.entry(Ruolo.SECONDA_PUNTA, 0.45),
                        Map.entry(Ruolo.BOMBER, 0.50),
                        Map.entry(Ruolo.PORTIERE, 0.98)
                )),
                Map.entry(Ruolo.ATTACCANTE_ESTERNO_DX, Map.ofEntries(
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_SX, 0.05),
                        Map.entry(Ruolo.SECONDA_PUNTA, 0.10),
                        Map.entry(Ruolo.ALA_DX, 0.15),
                        Map.entry(Ruolo.ALA_SX, 0.20),
                        Map.entry(Ruolo.TERZINO_DX, 0.25),
                        Map.entry(Ruolo.TERZINO_SX, 0.30),
                        Map.entry(Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.20),
                        Map.entry(Ruolo.CENTROCAMPISTA_CENTRALE, 0.40),
                        Map.entry(Ruolo.BOMBER, 0.30),
                        Map.entry(Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.50),
                        Map.entry(Ruolo.DIFENSORE_CENTRALE, 0.70),
                        Map.entry(Ruolo.PORTIERE, 0.98)
                )),
                Map.entry(Ruolo.ATTACCANTE_ESTERNO_SX, Map.ofEntries(
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_DX, 0.05),
                        Map.entry(Ruolo.SECONDA_PUNTA, 0.10),
                        Map.entry(Ruolo.ALA_SX, 0.15),
                        Map.entry(Ruolo.ALA_DX, 0.20),
                        Map.entry(Ruolo.TERZINO_SX, 0.25),
                        Map.entry(Ruolo.TERZINO_DX, 0.30),
                        Map.entry(Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.20),
                        Map.entry(Ruolo.CENTROCAMPISTA_CENTRALE, 0.40),
                        Map.entry(Ruolo.BOMBER, 0.30),
                        Map.entry(Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.50),
                        Map.entry(Ruolo.DIFENSORE_CENTRALE, 0.70),
                        Map.entry(Ruolo.PORTIERE, 0.98)
                )),
                Map.entry(Ruolo.SECONDA_PUNTA, Map.ofEntries(
                        Map.entry(Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.10),
                        Map.entry(Ruolo.CENTROCAMPISTA_CENTRALE, 0.30),
                        Map.entry(Ruolo.BOMBER, 0.20),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_DX, 0.15),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_SX, 0.15),
                        Map.entry(Ruolo.ALA_DX, 0.25),
                        Map.entry(Ruolo.ALA_SX, 0.25),
                        Map.entry(Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.45),
                        Map.entry(Ruolo.TERZINO_DX, 0.60),
                        Map.entry(Ruolo.TERZINO_SX, 0.60),
                        Map.entry(Ruolo.DIFENSORE_CENTRALE, 0.80),
                        Map.entry(Ruolo.PORTIERE, 0.98)
                )),
                Map.entry(Ruolo.BOMBER, Map.ofEntries(
                        Map.entry(Ruolo.SECONDA_PUNTA, 0.20),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_DX, 0.30),
                        Map.entry(Ruolo.ATTACCANTE_ESTERNO_SX, 0.30),
                        Map.entry(Ruolo.CENTROCAMPISTA_OFFENSIVO, 0.40),
                        Map.entry(Ruolo.CENTROCAMPISTA_CENTRALE, 0.50),
                        Map.entry(Ruolo.ALA_DX, 0.50),
                        Map.entry(Ruolo.ALA_SX, 0.50),
                        Map.entry(Ruolo.CENTROCAMPISTA_DIFENSIVO, 0.60),
                        Map.entry(Ruolo.TERZINO_DX, 0.75),
                        Map.entry(Ruolo.TERZINO_SX, 0.75),
                        Map.entry(Ruolo.DIFENSORE_CENTRALE, 0.90),
                        Map.entry(Ruolo.PORTIERE, 0.98)
                ))




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
