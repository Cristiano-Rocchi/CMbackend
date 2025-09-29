package pizzamafia.CMbackend.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.MentalitaTattica;
import pizzamafia.CMbackend.enums.Modulo;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.SelezioneCpuHelper;
import pizzamafia.CMbackend.payloads.partita.*;
import pizzamafia.CMbackend.repositories.FormazioneRepository;
import pizzamafia.CMbackend.repositories.GiocatoreRepository;
import pizzamafia.CMbackend.repositories.PartitaRepository;
import pizzamafia.CMbackend.repositories.SquadraRepository;
import pizzamafia.CMbackend.services.FormazioneService;
import pizzamafia.CMbackend.helpers.ValutazioneGiocatoreHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormazioneServiceImpl implements FormazioneService {

    private final FormazioneRepository formazioneRepository;
    private final PartitaRepository partitaRepository;
    private final SquadraRepository squadraRepository;
    private final GiocatoreRepository giocatoreRepository;

    // =================== CREATE ===================
    @Override
    public FormazioneRespDTO create(NewFormazioneDTO dto) {
        Partita partita = partitaRepository.findById(dto.partitaId())
                .orElseThrow(() -> new RuntimeException("Partita non trovata"));

        Squadra squadra = squadraRepository.findById(dto.squadraId())
                .orElseThrow(() -> new RuntimeException("Squadra non trovata"));
        //validazione rosa
        Set<UUID> idsRosa = squadra.getGiocatori().stream()
                .map(Giocatore::getId)
                .collect(Collectors.toSet());


        //1.===CREA FORMAZIONE E MENTALITA===

        Formazione formazione = Formazione.builder()
                .partita(partita)
                .squadra(squadra)
                .modulo(dto.modulo())
                .mentalita(dto.mentalita() != null ? dto.mentalita() : MentalitaTattica.BILANCIATA)
                .build();

        List<Titolari> titolari = dto.titolari().stream()
                .map(titolareDTO -> {
                    UUID gid = titolareDTO.giocatoreId();
                    if (!idsRosa.contains(gid)) {
                        throw new RuntimeException("Titolari: il giocatore non appartiene alla squadra: " + gid);
                    }

                    Giocatore g = giocatoreRepository.findById(gid)
                            .orElseThrow(() -> new RuntimeException("Giocatore non trovato"));

                    int valoreEffettivo = ValutazioneGiocatoreHelper.calcolaValoreEffettivo(g, titolareDTO.ruolo());
                    int malus = g.getValoreTecnico() - valoreEffettivo;

                    return Titolari.builder()
                            .formazione(formazione)
                            .giocatore(g)
                            .ruolo(titolareDTO.ruolo())
                            .malus(malus)
                            .valoreEffettivo(valoreEffettivo)
                            .build();
                })
                .toList();


        // 3.==PANCHINA=== 0-8 giocatori, nessun vincolo di ruolo
        Set<Giocatore> panchina = new HashSet<>();

        if (dto.giocatoriPanchina() != null && !dto.giocatoriPanchina().isEmpty()) {
            if (dto.giocatoriPanchina().size() > 8) {
                throw new RuntimeException("La panchina può avere al massimo 8 giocatori");
            }

            // evita che un giocatore sia sia titolare che in panchina + validazione rosa
            Set<UUID> titolariIds = titolari.stream()
                    .map(t -> t.getGiocatore().getId())
                    .collect(Collectors.toSet());

            for (UUID gid : dto.giocatoriPanchina()) {
                if (titolariIds.contains(gid)) {
                    throw new RuntimeException("Un giocatore non può essere sia titolare che in panchina: " + gid);
                }
                if (!idsRosa.contains(gid)) {
                    throw new RuntimeException("Panchina: il giocatore non appartiene alla squadra: " + gid);
                }
                Giocatore g = giocatoreRepository.findById(gid)
                        .orElseThrow(() -> new RuntimeException("Giocatore non trovato: " + gid));
                panchina.add(g);
            }
        }

        formazione.setPanchina(panchina);

        formazione.setTitolari(titolari);

        // 4. === CALCI PIAZZATI ====

        java.util.function.Function<List<UUID>, List<UUID>> sanitize = list -> (list == null ? List.of() : list);
        // 1) RIGORISTI (0-5)
        List<UUID> rigoristi = sanitize.apply(dto.rigoristi());
        if (rigoristi.size() > 5) throw new RuntimeException("Rigoristi: massimo 5 giocatori");
        if (rigoristi.size() != rigoristi.stream().distinct().count()) throw new RuntimeException("Rigoristi: niente duplicati");
        for (UUID gid : rigoristi) {
            if (!idsRosa.contains(gid)) {
                throw new RuntimeException("Rigoristi: il giocatore non appartiene alla squadra: " + gid);
            }
        }
        formazione.setRigoristi(rigoristi);
        // 2) PUNIZIONE RAVVICINATA (0-3 ciascuna)
        //centrale
        List<UUID> prCentrale = sanitize.apply(dto.punizioneRavvicinataCentrale());
        if (prCentrale.size() > 3) throw new RuntimeException("Punizione ravvicinata (centrale): massimo 3 giocatori");
        if (prCentrale.size() != prCentrale.stream().distinct().count()) throw new RuntimeException("Punizione ravvicinata (centrale): niente duplicati");
        for (UUID gid : prCentrale) {
            if (!idsRosa.contains(gid)) {
                throw new RuntimeException("Punizione ravvicinata (centrale): il giocatore non appartiene alla squadra: " + gid);
            }
        }
        formazione.setPunizioneRavvicinataCentrale(prCentrale);
        //destra
        List<UUID> prDestra = sanitize.apply(dto.punizioneRavvicinataDestra());
        if (prDestra.size() > 3) throw new RuntimeException("Punizione ravvicinata (destra): massimo 3 giocatori");
        if (prDestra.size() != prDestra.stream().distinct().count()) throw new RuntimeException("Punizione ravvicinata (destra): niente duplicati");
        for (UUID gid : prDestra) {
            if (!idsRosa.contains(gid)) {
                throw new RuntimeException("Punizione ravvicinata (destra): il giocatore non appartiene alla squadra: " + gid);
            }
        }
        formazione.setPunizioneRavvicinataDestra(prDestra);
        //sinistra
        List<UUID> prSinistra = sanitize.apply(dto.punizioneRavvicinataSinistra());
        if (prSinistra.size() > 3) throw new RuntimeException("Punizione ravvicinata (sinistra): massimo 3 giocatori");
        if (prSinistra.size() != prSinistra.stream().distinct().count()) throw new RuntimeException("Punizione ravvicinata (sinistra): niente duplicati");
        for (UUID gid : prSinistra) {
            if (!idsRosa.contains(gid)) {
                throw new RuntimeException("Punizione ravvicinata (sinistra): il giocatore non appartiene alla squadra: " + gid);
            }
        }
        formazione.setPunizioneRavvicinataSinistra(prSinistra);
        // 3) PUNIZIONE LONTANA (0-3)
        List<UUID> pLontana = sanitize.apply(dto.punizioneLontana());
        if (pLontana.size() > 3) throw new RuntimeException("Punizione lontana: massimo 3 giocatori");
        if (pLontana.size() != pLontana.stream().distinct().count()) throw new RuntimeException("Punizione lontana: niente duplicati");
        for (UUID gid : pLontana) {
            if (!idsRosa.contains(gid)) {
                throw new RuntimeException("Punizione lontana: il giocatore non appartiene alla squadra: " + gid);
            }
        }
        formazione.setPunizioneLontana(pLontana);


        return toRespDTO(formazioneRepository.save(formazione));
    }

    // =================== FIND BY ID ===================
    @Override
    public FormazioneRespDTO findById(UUID id) {
        Formazione formazione = formazioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formazione non trovata"));
        return toRespDTO(formazione);
    }

    // =================== FIND ALL ===================
    @Override
    public List<FormazioneRespDTO> findAll() {
        return formazioneRepository.findAll().stream()
                .map(this::toRespDTO)
                .toList();
    }

    // =================== DELETE ===================
    @Override
    public void deleteById(UUID id) {
        formazioneRepository.deleteById(id);
    }

    // Trova l'altra squadra
    public UUID trovaAltraSquadra(UUID idPartita, UUID idSquadraUtente) {
        Partita partita = partitaRepository.findById(idPartita)
                .orElseThrow(() -> new RuntimeException("Partita non trovata"));

        UUID idCasa = partita.getSquadraCasa().getId();
        UUID idTrasferta = partita.getSquadraTrasferta().getId();

        if (idCasa.equals(idSquadraUtente)) return idTrasferta;
        if (idTrasferta.equals(idSquadraUtente)) return idCasa;

        throw new RuntimeException("La squadra indicata non partecipa a questa partita.");
    }

    // Genera formazione automatica CPU
    public void generaFormazioneAutomaticaCpu(UUID idPartita, UUID idSquadra) {
        Partita partita = partitaRepository.findById(idPartita)
                .orElseThrow(() -> new RuntimeException("Partita non trovata"));

        Squadra squadra = squadraRepository.findById(idSquadra)
                .orElseThrow(() -> new RuntimeException("Squadra non trovata"));

        List<Giocatore> rosa = squadra.getGiocatori();

        // ⚠️ modulo di default per la CPU, ad esempio 4-4-2 con ali
        Modulo moduloCpu = Modulo._4_4_2_1;

        // 1. Ottieni i titolari migliori per quel modulo
        List<Titolari> titolari = SelezioneCpuHelper.generaTitolariDalModulo(rosa, moduloCpu);

        // 2. Crea la formazione
        Formazione formazione = Formazione.builder()
                .partita(partita)
                .squadra(squadra)
                .modulo(moduloCpu)
                .build();

        // 3. Applica malus e valore effettivo ai titolari
        for (Titolari t : titolari) {
            Giocatore g = t.getGiocatore();
            Ruolo ruolo = t.getRuolo();

            int valoreEffettivo = ValutazioneGiocatoreHelper.calcolaValoreEffettivo(g, ruolo);
            int malus = g.getValoreTecnico() - valoreEffettivo;

            t.setValoreEffettivo(valoreEffettivo);
            t.setMalus(malus);
            t.setFormazione(formazione);
        }

        // 4. Applica Mentalita
        formazione.setMentalita(MentalitaTattica.BILANCIATA);
        formazione.setPanchina(new HashSet<>());

        // 5. Salva la formazione
        formazione.setTitolari(titolari);
        formazioneRepository.save(formazione);
    }

    // =================== MAPPING ===================
    private FormazioneRespDTO toRespDTO(Formazione f) {
        List<TitolareRespDTO> titolari = f.getTitolari().stream()
                .map(t -> new TitolareRespDTO(
                        t.getGiocatore().getId(),
                        t.getGiocatore().getNome(),
                        t.getGiocatore().getCognome(),
                        t.getRuolo(),
                        t.getValoreEffettivo()
                ))
                .toList();

        List<PanchinaRespDTO> panchina = (f.getPanchina() == null ? List.<Giocatore>of() : f.getPanchina().stream().toList())
                .stream()
                .map(g -> new PanchinaRespDTO(
                        g.getId(),
                        g.getNome(),
                        g.getCognome()
                ))
                .toList();

        return new FormazioneRespDTO(
                f.getId(),
                f.getPartita().getId(),
                f.getSquadra().getId(),
                f.getModulo(),
                f.getMentalita(),
                titolari,
                panchina,
                f.getRigoristi(),
                f.getPunizioneRavvicinataCentrale(),
                f.getPunizioneRavvicinataDestra(),
                f.getPunizioneRavvicinataSinistra(),
                f.getPunizioneLontana()
        );
    }}

