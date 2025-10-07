package pizzamafia.CMbackend.ValutazioneGiocatore;
import org.junit.jupiter.api.Test;
import pizzamafia.CMbackend.entities.StatisticheTecnicheGiocatore;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.valutazioni.ValutazioneGiocatoreHelper;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CentrocampDifensivoValutazioneTest {

    @Test
    void calcolaValoreTecnico_CentroDif_breakdown() {
        // ===== Statistiche esempio per un mediano/CDM =====
        StatisticheTecnicheGiocatore s = new StatisticheTecnicheGiocatore();
        // Pilastri
        s.setIntercettazione(86);
        s.setLetturaDelGioco(88);
        s.setTecnica(79);
        s.setResistenza(90);
        s.setGiocoDiSquadra(85);
        // Supporto
        s.setVisione(75);
        s.setForzaFisica(82);
        s.setConcentrazione(84);
        s.setContrasti(87);
        s.setEquilibrio(78);
        s.setAggressivita(81);
        s.setCoraggio(80);
        s.setCreativita(70);
        s.setMarcatura(83);
        // Marginali
        s.setCalciPiazzati(55);
        s.setTiro(63);
        s.setAssist(68);
        s.setDribbling(71);
        // Bonus C/L
        s.setCarisma(90);
        s.setLeadership(87);
        // Altri irrilevanti (0% nel ruolo)
        s.setColpoDiTesta(60);
        s.setElevazione(60);
        s.setScatto(74);
        s.setAccelerazione(73);
        s.setFreddezza(72);
        s.setCross(65);
        s.setPresa(10); s.setUscite(10); s.setTuffo(10); s.setRiflessi(10); s.setPosizione(10);

        // ===== Pesi CENTROCAMPISTA_DIFENSIVO =====
        Map<String, Double> pesi = new LinkedHashMap<>();
        // [A] PILASTRO — 55%
        pesi.put("intercettazione",   11.0);
        pesi.put("letturaDelGioco",   11.0);
        pesi.put("tecnica",           11.0);
        pesi.put("resistenza",        11.0);
        pesi.put("giocoDiSquadra",    11.0);
        // [B] SUPPORTO — 40%
        pesi.put("visione",            5.0);
        pesi.put("forzaFisica",        4.0);
        pesi.put("concentrazione",     5.0);
        pesi.put("contrasti",          5.0);
        pesi.put("equilibrio",         4.0);
        pesi.put("aggressivita",       4.0);
        pesi.put("coraggio",           4.0);
        pesi.put("creativita",         4.0);
        pesi.put("marcatura",          4.0);
        // [C] MARGINALI — 5%
        pesi.put("calciPiazzati",      1.5);
        pesi.put("tiro",               1.5);
        pesi.put("assist",             1.0);
        pesi.put("dribbling",          1.0);
        // [D] IRRILEVANTI — 0% (non inseriti)

        // ===== Breakdown manuale =====
        double sommaPesi = 0.0;
        double punteggioGrezzo = 0.0;

        System.out.println("=== CENTROCAMPISTA DIFENSIVO: Breakdown contributi ===");
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

        // Bonus Carisma/Leadership
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

        // ===== Confronto con helper =====
        int calcolato = ValutazioneGiocatoreHelper.calcolaValoreTecnico(Ruolo.CENTROCAMPISTA_DIFENSIVO, s);
        System.out.println("Valutazione Finale                  = " + calcolato);

        assertEquals(atteso, calcolato);
    }


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

