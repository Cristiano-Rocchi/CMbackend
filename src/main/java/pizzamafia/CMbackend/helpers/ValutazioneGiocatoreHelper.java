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
                case "accelerazione" -> stats.getAccelerazione();
                case "agilita" -> stats.getAgilita();
                case "elevazione" -> stats.getElevazione();
                case "forza" -> stats.getForza();
                case "resistenza" -> stats.getResistenza();
                case "scatto" -> stats.getScatto();
                case "aggressivita" -> stats.getAggressivita();
                case "carisma" -> stats.getCarisma();
                case "coraggio" -> stats.getCoraggio();
                case "creativita" -> stats.getCreativita();
                case "determinazione" -> stats.getDeterminazione();
                case "giocoDiSquadra" -> stats.getGiocoDiSquadra();
                case "impegno" -> stats.getImpegno();
                case "intuito" -> stats.getIntuito();
                case "posizione" -> stats.getPosizione();
                case "calciPiazzati" -> stats.getCalciPiazzati();
                case "colpoDiTesta" -> stats.getColpoDiTesta();
                case "contrasti" -> stats.getContrasti();
                case "dribbling" -> stats.getDribbling();
                case "finalizzazione" -> stats.getFinalizzazione();
                case "marcatura" -> stats.getMarcatura();
                case "riflessi" -> stats.getRiflessi();
                case "tecnica" -> stats.getTecnica();
                case "assist" -> stats.getAssist();
                case "tiriDaLontano" -> stats.getTiriDaLontano();
                case "inserimento" -> stats.getInserimento();
                default -> 0;
            };

            punteggioGrezzo += valore * peso;
            sommaPesi += peso;
        }

        // Normalizza il punteggio sulla scala 1–99
        double maxPunteggio = 99 * sommaPesi;
        double normalizzato = (punteggioGrezzo / maxPunteggio) * 99;

        return (int) Math.round(normalizzato);
    }


    //MAP DEI RUOLI CON PESI
    private static Map<String, Double> getPesiPerRuolo(Ruolo ruolo) {
        switch (ruolo) {
            case PORTIERE:
                return Map.ofEntries(
                        Map.entry("riflessi", 1.6),
                        Map.entry("posizione", 1.5),
                        Map.entry("intuito", 1.4),
                        Map.entry("agilita", 1.3),
                        Map.entry("elevazione", 1.1),
                        Map.entry("carisma", 1.1),
                        Map.entry("determinazione", 0.9),
                        Map.entry("coraggio", 0.85),
                        Map.entry("forza", 0.7),
                        Map.entry("impegno", 0.7),
                        Map.entry("resistenza", 0.5),
                        Map.entry("giocoDiSquadra", 0.45),
                        Map.entry("scatto", 0.3),
                        Map.entry("tecnica", 0.2),
                        Map.entry("aggressivita", 0.3)
                );


            case DIFENSORE_CENTRALE:
                return Map.ofEntries(
                        Map.entry("marcatura", 1.5),
                        Map.entry("contrasti", 1.3),
                        Map.entry("posizione", 1.4),
                        Map.entry("colpoDiTesta", 1.2),
                        Map.entry("elevazione", 1.0),
                        Map.entry("aggressivita", 0.9),
                        Map.entry("intuito", 0.8),
                        Map.entry("carisma", 1.3),
                        Map.entry("determinazione", 0.8),
                        Map.entry("impegno", 0.7),
                        Map.entry("coraggio", 0.7),
                        Map.entry("resistenza", 0.6),
                        Map.entry("giocoDiSquadra", 0.6),
                        Map.entry("tecnica", 0.4),
                        Map.entry("scatto", 0.4),
                        Map.entry("agilita", 0.3),
                        Map.entry("accelerazione", 0.3),
                        Map.entry("riflessi", 0.25)
                );
            case TERZINO_DX, TERZINO_SX:
                return Map.ofEntries(
                        Map.entry("accelerazione", 1.4),
                        Map.entry("resistenza", 1.4),
                        Map.entry("scatto", 1.3),
                        Map.entry("contrasti", 1.1),
                        Map.entry("posizione", 1.1),
                        Map.entry("marcatura", 1.0),
                        Map.entry("impegno", 1.0),
                        Map.entry("determinazione", 1.0),
                        Map.entry("assist", 1.0),
                        Map.entry("carisma", 1.0),
                        Map.entry("agilita", 0.8),
                        Map.entry("inserimento", 0.8),
                        Map.entry("coraggio", 0.7),
                        Map.entry("aggressivita", 0.6),
                        Map.entry("giocoDiSquadra", 0.6),
                        Map.entry("intuito", 0.4),
                        Map.entry("forza", 0.4),
                        Map.entry("dribbling", 0.4),
                        Map.entry("riflessi", 0.3),
                        Map.entry("tiriDaLontano", 0.3),
                        Map.entry("calciPiazzati", 0.3),
                        Map.entry("creativita", 0.2)
                );
            case CENTROCAMPISTA_DIFENSIVO:
                return Map.ofEntries(
                        Map.entry("contrasti", 1.5),
                        Map.entry("marcatura", 1.35),
                        Map.entry("posizione", 1.35),
                        Map.entry("resistenza", 1.4),
                        Map.entry("forza", 1.2),
                        Map.entry("aggressivita", 1.25),
                        Map.entry("impegno", 1.2),
                        Map.entry("determinazione", 1.1),
                        Map.entry("carisma", 1.1),
                        Map.entry("giocoDiSquadra", 1.0),
                        Map.entry("coraggio", 1.0),
                        Map.entry("tiriDaLontano", 0.6),
                        Map.entry("tecnica", 0.5),
                        Map.entry("intuito", 0.5),
                        Map.entry("scatto", 0.5),
                        Map.entry("agilita", 0.4),
                        Map.entry("colpoDiTesta", 0.35),
                        Map.entry("riflessi", 0.35),
                        Map.entry("dribbling", 0.3),
                        Map.entry("assist", 0.3),
                        Map.entry("accelerazione", 0.3),
                        Map.entry("calciPiazzati", 0.2),
                        Map.entry("creativita", 0.2),
                        Map.entry("elevazione", 0.2),
                        Map.entry("inserimento", 0.2),
                        Map.entry("finalizzazione", 0.1)
                );
            case CENTROCAMPISTA_CENTRALE:
                return Map.ofEntries(
                        Map.entry("resistenza", 1.4),
                        Map.entry("giocoDiSquadra", 1.4),
                        Map.entry("posizione", 1.3),
                        Map.entry("tecnica", 1.25),
                        Map.entry("impegno", 1.25),
                        Map.entry("determinazione", 1.2),
                        Map.entry("intuito", 1.1),
                        Map.entry("creativita", 1.1),
                        Map.entry("carisma", 1.1),
                        Map.entry("inserimento", 1.15),
                        Map.entry("assist", 0.8),
                        Map.entry("scatto", 1.0),
                        Map.entry("forza", 0.9),
                        Map.entry("contrasti", 1.0),
                        Map.entry("marcatura", 0.65),
                        Map.entry("tiriDaLontano", 0.6),
                        Map.entry("coraggio", 0.6),
                        Map.entry("accelerazione", 0.6),
                        Map.entry("dribbling", 0.6),
                        Map.entry("riflessi", 0.5),
                        Map.entry("calciPiazzati", 0.5),
                        Map.entry("colpoDiTesta", 0.45),
                        Map.entry("agilita", 0.7),
                        Map.entry("elevazione", 0.4),
                        Map.entry("aggressivita", 0.4),
                        Map.entry("finalizzazione", 0.4)
                );
            case CENTROCAMPISTA_OFFENSIVO:
                return Map.ofEntries(
                        Map.entry("creativita", 1.35),
                        Map.entry("tecnica", 1.3),
                        Map.entry("intuito", 1.2),
                        Map.entry("dribbling", 1.2),
                        Map.entry("assist", 1.2),
                        Map.entry("giocoDiSquadra", 1.2),
                        Map.entry("tiriDaLontano", 1.1),
                        Map.entry("inserimento", 1.1),
                        Map.entry("carisma", 1.1),
                        Map.entry("agilita", 1.0),
                        Map.entry("resistenza", 0.8),
                        Map.entry("forza", 0.8),
                        Map.entry("calciPiazzati", 0.8),
                        Map.entry("coraggio", 0.85),
                        Map.entry("determinazione", 0.7),
                        Map.entry("impegno", 0.7),
                        Map.entry("scatto", 0.6),
                        Map.entry("accelerazione", 0.6),
                        Map.entry("riflessi", 0.6),
                        Map.entry("finalizzazione", 0.5),
                        Map.entry("contrasti", 0.4),
                        Map.entry("aggressivita", 0.4),
                        Map.entry("elevazione", 0.3),
                        Map.entry("colpoDiTesta", 0.3),
                        Map.entry("marcatura", 0.25)
                );
            case ALA_DX,ALA_SX:
                return Map.ofEntries(
                        Map.entry("accelerazione", 1.4),
                        Map.entry("scatto", 1.35),
                        Map.entry("dribbling", 1.25),
                        Map.entry("assist", 1.2),
                        Map.entry("tecnica", 1.05),
                        Map.entry("impegno", 1.1),
                        Map.entry("inserimento", 1.1),
                        Map.entry("agilita", 1.0),
                        Map.entry("posizione", 1.0),
                        Map.entry("carisma", 0.95),
                        Map.entry("creativita", 0.85),
                        Map.entry("marcatura", 0.8),
                        Map.entry("giocoDiSquadra", 0.8),
                        Map.entry("determinazione", 0.75),
                        Map.entry("coraggio", 0.7),
                        Map.entry("aggressivita", 0.7),
                        Map.entry("tiriDaLontano", 0.65),
                        Map.entry("intuito", 0.6),
                        Map.entry("forza", 0.6),
                        Map.entry("contrasti", 0.55),
                        Map.entry("finalizzazione", 0.45),
                        Map.entry("calciPiazzati", 0.45),
                        Map.entry("riflessi", 0.45),
                        Map.entry("colpoDiTesta", 0.3),
                        Map.entry("elevazione", 0.3)
                );
            case ATTACCANTE_ESTERNO_DX, ATTACCANTE_ESTERNO_SX:
                return Map.ofEntries(
                        Map.entry("accelerazione", 1.3),
                        Map.entry("scatto", 1.3),
                        Map.entry("dribbling", 1.25),
                        Map.entry("assist", 1.2),
                        Map.entry("finalizzazione", 1.1),
                        Map.entry("resistenza", 1.05),
                        Map.entry("tiriDaLontano", 1.0),
                        Map.entry("tecnica", 1.0),
                        Map.entry("carisma", 1.0),
                        Map.entry("agilita", 0.9),
                        Map.entry("creativita", 0.9),
                        Map.entry("inserimento", 0.85),
                        Map.entry("impegno", 0.85),
                        Map.entry("posizione", 0.8),
                        Map.entry("giocoDiSquadra", 0.75),
                        Map.entry("intuito", 0.75),
                        Map.entry("determinazione", 0.7),
                        Map.entry("forza", 0.7),
                        Map.entry("riflessi", 0.6),
                        Map.entry("calciPiazzati", 0.5),
                        Map.entry("elevazione", 0.5),
                        Map.entry("colpoDiTesta", 0.4),
                        Map.entry("aggressivita", 0.4),
                        Map.entry("contrasti", 0.3)
                );
            case SECONDA_PUNTA:
                return Map.ofEntries(
                        Map.entry("assist", 1.4),
                        Map.entry("tecnica", 1.4),
                        Map.entry("dribbling", 1.3),
                        Map.entry("creativita", 1.3),
                        Map.entry("finalizzazione", 1.2),
                        Map.entry("coraggio", 1.1),
                        Map.entry("intuito", 1.1),
                        Map.entry("carisma", 1.1),
                        Map.entry("determinazione", 1.0),
                        Map.entry("agilita", 1.0),
                        Map.entry("posizione", 0.8),
                        Map.entry("calciPiazzati", 0.8),
                        Map.entry("giocoDiSquadra", 0.75),
                        Map.entry("inserimento", 0.7),
                        Map.entry("tiriDaLontano", 0.7),
                        Map.entry("scatto", 0.6),
                        Map.entry("riflessi", 0.55),
                        Map.entry("accelerazione", 0.5),
                        Map.entry("resistenza", 0.4),
                        Map.entry("impegno", 0.4),
                        Map.entry("forza", 0.3),
                        Map.entry("elevazione", 0.2),
                        Map.entry("colpoDiTesta", 0.1)
                );
            case BOMBER:
                return Map.ofEntries(
                        Map.entry("finalizzazione", 1.5),
                        Map.entry("posizione", 1.4),
                        Map.entry("colpoDiTesta", 1.2),
                        Map.entry("carisma", 1.2),
                        Map.entry("intuito", 1.25),
                        Map.entry("elevazione", 1.1),
                        Map.entry("determinazione", 1.1),
                        Map.entry("forza", 1.0),
                        Map.entry("tiriDaLontano", 0.9),
                        Map.entry("impegno", 0.9),
                        Map.entry("tecnica", 0.8),
                        Map.entry("coraggio", 0.8),
                        Map.entry("calciPiazzati", 0.7),
                        Map.entry("aggressivita", 0.6),
                        Map.entry("scatto", 0.65),
                        Map.entry("riflessi", 0.5),
                        Map.entry("accelerazione", 0.45),
                        Map.entry("dribbling", 0.45),
                        Map.entry("giocoDiSquadra", 0.45),
                        Map.entry("agilita", 0.45),
                        Map.entry("creativita", 0.4),
                        Map.entry("assist", 0.4),
                        Map.entry("resistenza", 0.4),
                        Map.entry("inserimento", 0.35)
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
