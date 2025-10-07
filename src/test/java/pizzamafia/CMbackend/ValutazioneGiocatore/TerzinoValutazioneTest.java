package pizzamafia.CMbackend.ValutazioneGiocatore;

import org.junit.jupiter.api.Test;
import pizzamafia.CMbackend.entities.StatisticheTecnicheGiocatore;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.valutazioni.ValutazioneGiocatoreHelper;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerzinoValutazioneTest {

    @Test
    void calcolaValoreTecnico_TerzinoDx_breakdown() {

        StatisticheTecnicheGiocatore s = new StatisticheTecnicheGiocatore();
        // Core ruolo
        s.setMarcatura(84);
        s.setCross(86);
        s.setResistenza(90);
        s.setLetturaDelGioco(82);
        s.setContrasti(83);
        // Supporto
        s.setConcentrazione(78);
        s.setAggressivita(76);
        s.setIntercettazione(79);
        s.setGiocoDiSquadra(80);
        s.setScatto(88);
        s.setAccelerazione(89);
        s.setAssist(72);
        s.setCoraggio(75);
        s.setTecnica(77);
        // Marginali
        s.setVisione(70);
        s.setTiro(60);
        s.setDribbling(74);
        s.setEquilibrio(76);
        s.setForzaFisica(78);
        // Bonus leadership/carisma (per il bonus finale)
        s.setCarisma(81);
        s.setLeadership(83);
        // Altri irrilevanti per ruolo
        s.setColpoDiTesta(65);
        s.setElevazione(66);
        s.setFinalizzazione(55);
        s.setCalciPiazzati(50);
        s.setCreativita(68);
        s.setFreddezza(72);
        // Portiere (irrilevanti)
        s.setTuffo(10); s.setRiflessi(10); s.setPosizione(10); s.setUscite(10); s.setPresa(10);

        // ===== Pesi TERZINO_DX =====
        Map<String, Double> pesi = new LinkedHashMap<>();
        // [A] PILASTRO — 60%
        pesi.put("marcatura",        14.0);
        pesi.put("cross",            13.0);
        pesi.put("resistenza",       12.0);
        pesi.put("letturaDelGioco",  11.0);
        pesi.put("contrasti",        10.0);
        // [B] SUPPORTO — 35%
        pesi.put("concentrazione",    6.0);
        pesi.put("aggressivita",      5.0);
        pesi.put("intercettazione",   5.0);
        pesi.put("giocoDiSquadra",    5.0);
        pesi.put("scatto",            3.0);
        pesi.put("accelerazione",     3.0);
        pesi.put("assist",            4.0);
        pesi.put("coraggio",          2.0);
        pesi.put("tecnica",           2.0);
        // [C] MARGINALI — 5%
        pesi.put("visione",           1.0);
        pesi.put("tiro",              1.0);
        pesi.put("dribbling",         1.0);
        pesi.put("equilibrio",        1.0);
        pesi.put("forzaFisica",       1.0);
        // [D] IRRILEVANTI — 0% (non in mappa)

        // ===== Breakdown manuale =====
        double sommaPesi = 0.0;
        double punteggioGrezzo = 0.0;

        System.out.println("=== TERZINO DX: Breakdown contributi ===");
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

        // Bonus Carisma/Leadership (stessa tabella del tuo helper)
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
        int calcolato = ValutazioneGiocatoreHelper.calcolaValoreTecnico(Ruolo.TERZINO_DX, s);
        System.out.println("Helper result                  = " + calcolato);

        assertEquals(atteso, calcolato);
    }

    // util per leggere l’attributo per nome
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
