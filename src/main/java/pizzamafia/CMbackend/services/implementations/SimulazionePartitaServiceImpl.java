package pizzamafia.CMbackend.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pizzamafia.CMbackend.entities.Formazione;
import pizzamafia.CMbackend.entities.Partita;
import pizzamafia.CMbackend.entities.Titolari;
import pizzamafia.CMbackend.entities.MarcatorePartita;
import pizzamafia.CMbackend.exceptions.NotFoundException;
import pizzamafia.CMbackend.helpers.SimulazionePartitaHelper;
import pizzamafia.CMbackend.repositories.FormazioneRepository;
import pizzamafia.CMbackend.repositories.PartitaRepository;
import pizzamafia.CMbackend.repositories.TitolariRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimulazionePartitaServiceImpl implements pizzamafia.CMbackend.services.SimulazionePartitaService {

    private final PartitaRepository partitaRepository;
    private final FormazioneRepository formazioneRepository;
    private final TitolariRepository titolariRepository;

    // =================== SIMULAZIONE PARTITA ===================
    @Override
    public Partita simula(UUID partitaId) {
        // 1. Recupera la partita
        Partita partita = partitaRepository.findById(partitaId)
                .orElseThrow(() -> new NotFoundException("Partita non trovata con ID: " + partitaId));

        // 2. Recupera le formazioni della partita
        List<Formazione> formazioni = formazioneRepository.findByPartitaId(partitaId);
        if (formazioni.size() != 2) {
            throw new NotFoundException("Devono esserci esattamente due formazioni per simulare la partita.");
        }

        // Distinzione tra casa e trasferta
        Formazione formazioneCasa = formazioni.stream()
                .filter(f -> f.getSquadra().getId().equals(partita.getSquadraCasa().getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Formazione squadra casa non trovata."));

        Formazione formazioneTrasferta = formazioni.stream()
                .filter(f -> f.getSquadra().getId().equals(partita.getSquadraTrasferta().getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Formazione squadra trasferta non trovata."));

        // 3. Recupera i titolari delle due formazioni
        List<Titolari> titolariCasa = titolariRepository.findByFormazioneId(formazioneCasa.getId());
        List<Titolari> titolariTrasferta = titolariRepository.findByFormazioneId(formazioneTrasferta.getId());

        // 4. Calcola il valore delle due formazioni
        double valoreCasa = SimulazionePartitaHelper.calcolaValoreFormazione(titolariCasa);
        double valoreTrasferta = SimulazionePartitaHelper.calcolaValoreFormazione(titolariTrasferta);

        // 5. Applica bonus casa
        valoreCasa = SimulazionePartitaHelper.applicaFattoreCasa(valoreCasa);

        // 6. Simula il risultato
        int[] risultato = SimulazionePartitaHelper.generaRisultato(valoreCasa, valoreTrasferta);
        int goalCasa = risultato[0];
        int goalTrasferta = risultato[1];

        partita.setGoalCasa(goalCasa);
        partita.setGolTrasferta(goalTrasferta);


        // 7. Genera marcatori
        List<MarcatorePartita> marcatori = SimulazionePartitaHelper.generaMarcatori(
                goalCasa, goalTrasferta, titolariCasa, titolariTrasferta, partita
        );


        partita.getMarcatori().clear();
        partita.getMarcatori().addAll(marcatori);


        // 8. Salva e ritorna la partita aggiornata
        return partitaRepository.save(partita);
        // Temporaneo, continueremo nello step successivo
    }

}
