package pizzamafia.CMbackend.helpers;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Modulo;
import pizzamafia.CMbackend.enums.IdeaDiGioco;
import pizzamafia.CMbackend.helpers.azioni.tikitaka.TikiTakaHelper;
import pizzamafia.CMbackend.helpers.azioni.pallalunga.PallaLungaHelper;
import pizzamafia.CMbackend.helpers.azioni.giocofasce.GiocoSulleFasceHelper;
import pizzamafia.CMbackend.helpers.azioni.tikitaka.ModuliTikiTakaMap;
import pizzamafia.CMbackend.helpers.azioni.pallalunga.ModuliPallaLungaMap;
import pizzamafia.CMbackend.helpers.azioni.giocofasce.ModuliGiocoSulleFasceMap;
import pizzamafia.CMbackend.enums.Ruolo;


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

        // =================== 1. Forza media effettiva ===================
        double forzaCasa = titolariCasa.stream().mapToDouble(Titolari::getValoreEffettivo).average().orElse(0);
        double forzaTrasferta = titolariTrasferta.stream().mapToDouble(Titolari::getValoreEffettivo).average().orElse(0);
        forzaCasa += 2.0; // bonus casa

        // =================== 2. Chi attacca (probabilità pesata) ===================
        double totale = Math.max(1e-6, forzaCasa + forzaTrasferta);
        double probabilitaAttaccoCasa = forzaCasa / totale;

        // =================== 3. Numero dinamico di azioni ===================
        int numeroAzioni;
        double dominance = Math.abs(forzaCasa - forzaTrasferta);
        if (dominance > 20) {
            numeroAzioni = random.nextDouble() < 0.7 ? 2 : 1;
        } else if (dominance > 10) {
            numeroAzioni = random.nextDouble() < 0.6 ? 1 : 0;
        } else {
            numeroAzioni = random.nextDouble() < 0.4 ? 1 : 0;
        }

        // =================== 4. Genera le azioni ===================
        int secondoCorrente = 0; // 0..300 nel blocco

        for (int i = 0; i < numeroAzioni; i++) {
            if (secondoCorrente >= 300) break;

            boolean attaccaCasa = random.nextDouble() < probabilitaAttaccoCasa;

            Squadra squadraAttaccante = attaccaCasa ? squadraCasa : squadraTrasferta;
            Squadra squadraDifendente = attaccaCasa ? squadraTrasferta : squadraCasa;

            List<Titolari> titolariAttacco = attaccaCasa ? titolariCasa : titolariTrasferta;
            List<Titolari> titolariDifesa  = attaccaCasa ? titolariTrasferta : titolariCasa;

            // Pesi azione in base a idea di gioco, minuto e dominance
            Map<TipoAzione, Double> pesi = calcolaPesiAzione(squadraAttaccante, dominance, minutoInizio);
            TipoAzione tipo = scegliAzione(pesi, random);

            // Modulo della squadra attaccante
            Modulo moduloAttaccante = recuperaModuloDaTitolari(titolariAttacco);

            // ---- minuto di PARTENZA dell’azione dentro al blocco ----
            int minutoAzione = minutoInizio + (secondoCorrente / 60);

            // Genera l’azione partendo da minutoAzione
            List<EventoPartita> azione = eseguiAzione(
                    tipo, moduloAttaccante,
                    minutoAzione, partita,
                    squadraAttaccante, squadraDifendente,
                    titolariAttacco, titolariDifesa
            );

            if (azione == null || azione.isEmpty()) continue;

            // Append eventi
            eventi.addAll(azione);

            // Avanza il cursore temporale del blocco usando la DURATA REALE
            int durata = durataRealeAzione(azione, minutoAzione);
            secondoCorrente += Math.min(durata, 300 - secondoCorrente); // non superare i 5'
            if (secondoCorrente >= 300) break;
        }


        return eventi;
    }

    // =================== Utility interne ===================

    private enum TipoAzione { TIKI_TAKA, PALLA_LUNGA, GIOCO_FASCE }

    private static Map<TipoAzione, Double> calcolaPesiAzione(Squadra squadraAttaccante,
                                                             double dominance,
                                                             int minutoCorrente) {
        double tiki = 0.40, lunga = 0.30, fasce = 0.30;

        Allenatore allenatore = squadraAttaccante.getAllenatore();
        if (allenatore != null && allenatore.getIdeaDiGioco() != null) {
            IdeaDiGioco idea = allenatore.getIdeaDiGioco();
            switch (idea) {
                case TIKITAKA -> {
                    tiki *= 1.6;  lunga *= 0.85; fasce *= 0.9;
                }
                case PALLA_LUNGA -> {
                    lunga *= 1.6; tiki  *= 0.85; fasce *= 0.95;
                }
                case GIOCO_FASCE -> {
                    fasce *= 1.6; tiki  *= 0.9;  lunga *= 0.95;
                }
            }
        }

        // Finale partita: cresce verticalità
        if (minutoCorrente >= 70) {
            lunga *= 1.15;
            fasce *= 1.10;
            tiki  *= 0.90;
        }

        // Se domini, tiki leggermente più probabile (gestione)
        if (dominance >= 15) {
            tiki *= 1.10;
        }

        double sum = tiki + lunga + fasce;
        Map<TipoAzione, Double> out = new EnumMap<>(TipoAzione.class);
        out.put(TipoAzione.TIKI_TAKA, tiki / sum);
        out.put(TipoAzione.PALLA_LUNGA, lunga / sum);
        out.put(TipoAzione.GIOCO_FASCE, fasce / sum);
        return out;
    }

    private static TipoAzione scegliAzione(Map<TipoAzione, Double> pesi, Random rnd) {
        double x = rnd.nextDouble(), acc = 0;
        for (var e : pesi.entrySet()) {
            acc += e.getValue();
            if (x <= acc) return e.getKey();
        }
        return TipoAzione.TIKI_TAKA;
    }

    private static Modulo recuperaModuloDaTitolari(List<Titolari> titolari) {
        return titolari.stream()
                .map(Titolari::getFormazione)
                .filter(Objects::nonNull)
                .map(Formazione::getModulo)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(Modulo._4_4_2_1); // fallback di sicurezza
    }


    private static List<EventoPartita> eseguiAzione(
            TipoAzione tipo,
            Modulo moduloAttaccante,
            int minutoInizio,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        switch (tipo) {
            case TIKI_TAKA -> {
                int variante = ModuliTikiTakaMap.scegliVariante(moduloAttaccante);
                List<Ruolo> ruoli = ModuliTikiTakaMap.ruoliPer(moduloAttaccante, variante);

                return switch (variante) {
                    case 1 -> TikiTakaHelper.eseguiTikiTaka1(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                    case 2 -> TikiTakaHelper.eseguiTikiTaka2(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                    case 3 -> TikiTakaHelper.eseguiTikiTaka3(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                    case 4 -> TikiTakaHelper.eseguiTikiTaka4(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                    case 5 -> TikiTakaHelper.eseguiTikiTaka5(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                    default -> TikiTakaHelper.eseguiTikiTaka1(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                };
            }

            case PALLA_LUNGA -> {
                int variante = ModuliPallaLungaMap.scegliVariante(moduloAttaccante);
                List<Ruolo> ruoli = ModuliPallaLungaMap.ruoliPer(moduloAttaccante, variante);

                return switch (variante) {
                    case 1 -> PallaLungaHelper.eseguiPallaLunga1(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                    case 2 -> PallaLungaHelper.eseguiPallaLunga2(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                    case 3 -> PallaLungaHelper.eseguiPallaLunga3(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                    case 4 -> PallaLungaHelper.eseguiPallaLunga4(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                    default -> PallaLungaHelper.eseguiPallaLunga1(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                };
            }

            case GIOCO_FASCE -> {
                int variante = ModuliGiocoSulleFasceMap.scegliVariante(moduloAttaccante);
                List<Ruolo> ruoli = ModuliGiocoSulleFasceMap.ruoliPer(moduloAttaccante, variante);

                return switch (variante) {
                    case 1 -> GiocoSulleFasceHelper.eseguiFascia1(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                    case 2 -> GiocoSulleFasceHelper.eseguiFascia2(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                    case 3 -> GiocoSulleFasceHelper.eseguiFascia3(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                    case 4 -> GiocoSulleFasceHelper.eseguiFascia4(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                    case 5 -> GiocoSulleFasceHelper.eseguiFascia5(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                    default -> GiocoSulleFasceHelper.eseguiFascia1(
                            minutoInizio, partita, squadraAttaccante, squadraDifendente,
                            titolariAttacco, titolariDifesa, ruoli);
                };
            }

        }
        return Collections.emptyList();
    }


    private static int durataRealeAzione(List<EventoPartita> ev, int minutoStart) {
        if (ev == null || ev.isEmpty()) return 30; // fallback minimo
        EventoPartita last = ev.get(ev.size() - 1);
        int deltaMin = last.getMinuto() - minutoStart;
        int deltaSec = deltaMin * 60 + last.getSecondo();
        // clamp tra 30s e 300s
        return Math.max(30, Math.min(300, deltaSec));
    }




}
