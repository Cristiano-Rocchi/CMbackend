package pizzamafia.CMbackend.ValutazioneGiocatore;

import org.junit.jupiter.api.Test;
import pizzamafia.CMbackend.entities.StatisticheTecnicheGiocatore;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.valutazioni.ValutazioneGiocatoreHelper;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BomberValutazioneTest {

    @Test
    void calcolaValoreTecnico_Bomber_breakdown() {
        StatisticheTecnicheGiocatore s = new StatisticheTecnicheGiocatore();
        // Tecniche
        s.setTecnica(79);
        s.setEquilibrio(88);
        s.setColpoDiTesta(86);
        s.setTiro(81);
        s.setAssist(70);
        s.setFinalizzazione(83);
        s.setDribbling(70);
        s.setVisione(79);
        s.setCalciPiazzati(65);
        // Difensive
        s.setContrasti(35);
        s.setMarcatura(30);
        s.setIntercettazione(28);
        // Mentali
        s.setCarisma(84);
        s.setConcentrazione(81);
        s.setCoraggio(79);
        s.setLeadership(83);
        s.setLetturaDelGioco(81);
        s.setGiocoDiSquadra(75);
        s.setCreativita(73);
        s.setFreddezza(86);
        s.setAggressivita(68);
        // Fisiche
        s.setAccelerazione(70);
        s.setScatto(75);
        s.setElevazione(80);
        s.setForzaFisica(85);
        s.setResistenza(77);
        // Portiere (irrilevanti per BOMBER)
        s.setTuffo(10);
        s.setRiflessi(12);
        s.setPosizione(15);
        s.setUscite(9);
        s.setPresa(11);

        // ===== Pesi BOMBER (copiati 1:1 dal tuo helper) =====
        Map<String, Double> pesi = new LinkedHashMap<>();
        // [A] Pilastro — 60%
        pesi.put("finalizzazione",    15.0);
        pesi.put("freddezza",         15.0);
        pesi.put("tiro",              12.0);
        pesi.put("letturaDelGioco",   10.0);
        pesi.put("forzaFisica",        8.0);
        // [B] Supporto — 35%
        pesi.put("tecnica",            5.0);
        pesi.put("elevazione",         5.0);
        pesi.put("equilibrio",         5.0);
        pesi.put("colpoDiTesta",       5.0);
        pesi.put("concentrazione",     4.0);
        pesi.put("coraggio",           4.0);
        pesi.put("giocoDiSquadra",     4.0);
        pesi.put("assist",             3.0);
        // [C] Marginali — 5%
        pesi.put("dribbling",          1.0);
        pesi.put("calciPiazzati",      1.0);
        pesi.put("visione",            1.0);
        pesi.put("creativita",         1.0);
        pesi.put("aggressivita",       1.0);

        // ===== Breakdown manuale =====
        double sommaPesi = 0.0;
        double punteggioGrezzo = 0.0;

        System.out.println("=== BOMBER: Breakdown contributi ===");
        for (Map.Entry<String, Double> e : pesi.entrySet()) {
            String attr = e.getKey();
            double peso = e.getValue();
            double val = getStatByName(s, attr);
            double contrib = val * peso;
            sommaPesi += peso;
            punteggioGrezzo += contrib;
            System.out.printf("%-17s = %5.1f * %5.2f = %7.2f%n", attr, val, peso, contrib);
        }
        System.out.printf("Somma pesi = %.2f%n", sommaPesi);
        System.out.printf("Punteggio grezzo = %.2f%n", punteggioGrezzo);

        double maxPunteggio = 99.0 * sommaPesi;
        double normalizzato = (punteggioGrezzo / maxPunteggio) * 99.0;

        // Bonus Carisma/Leadership (stessa logica del tuo helper)
        double clAvg = (s.getCarisma() + s.getLeadership()) / 2.0;
        double bonus = (clAvg >= 95) ? 3.0 :
                (clAvg >= 90) ? 2.0 :
                        (clAvg >= 85) ? 1.5 :
                                (clAvg >= 80) ? 1.0 :
                                        (clAvg >= 75) ? 0.5 : 0.0;

        double finalCapped = Math.min(99.0, normalizzato + bonus);
        int atteso = (int) Math.round(finalCapped);

        System.out.printf("MaxPunteggio (99 * sommaPesi) = %.2f%n", maxPunteggio);
        System.out.printf("Normalizzato                    = %.2f%n", normalizzato);
        System.out.printf("Bonus(Carisma/Leadership)      = %.2f (CL avg=%.2f)%n", bonus, clAvg);
        System.out.printf("Finale (cap 99)                = %.2f -> arrotondato = %d%n", finalCapped, atteso);

        // ===== Chiamata helper e asserzione =====
        int calcolato = ValutazioneGiocatoreHelper.calcolaValoreTecnico(Ruolo.BOMBER, s);
        System.out.println("Valutazione Finale                  = " + calcolato);

        assertEquals(atteso, calcolato);
    }

    // --- util per leggere l’attributo per nome (coerente col tuo switch nell’helper) ---
    private double getStatByName(StatisticheTecnicheGiocatore s, String name) {
        return switch (name) {
            case "tecnica" -> s.getTecnica();
            case "equilibrio" -> s.getEquilibrio();
            case "colpoDiTesta" -> s.getColpoDiTesta();
            case "tiro" -> s.getTiro();
            case "assist" -> s.getAssist();
            case "finalizzazione" -> s.getFinalizzazione();
            case "dribbling" -> s.getDribbling();
            case "visione" -> s.getVisione();
            case "calciPiazzati" -> s.getCalciPiazzati();
            case "cross" -> s.getCross();
            case "contrasti" -> s.getContrasti();
            case "marcatura" -> s.getMarcatura();
            case "intercettazione" -> s.getIntercettazione();
            case "carisma" -> s.getCarisma();
            case "concentrazione" -> s.getConcentrazione();
            case "coraggio" -> s.getCoraggio();
            case "leadership" -> s.getLeadership();
            case "letturaDelGioco" -> s.getLetturaDelGioco();
            case "giocoDiSquadra" -> s.getGiocoDiSquadra();
            case "creativita" -> s.getCreativita();
            case "freddezza" -> s.getFreddezza();
            case "aggressivita" -> s.getAggressivita();
            case "accelerazione" -> s.getAccelerazione();
            case "scatto" -> s.getScatto();
            case "elevazione" -> s.getElevazione();
            case "forzaFisica" -> s.getForzaFisica();
            case "resistenza" -> s.getResistenza();
            case "tuffo" -> s.getTuffo();
            case "riflessi" -> s.getRiflessi();
            case "posizione" -> s.getPosizione();
            case "uscite" -> s.getUscite();
            case "presa" -> s.getPresa();
            default -> 0.0;
        };
    }
}

