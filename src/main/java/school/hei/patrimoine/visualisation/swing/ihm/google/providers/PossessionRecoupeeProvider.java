package school.hei.patrimoine.visualisation.swing.ihm.google.providers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.RecoupementStatus;
import school.hei.patrimoine.modele.recouppement.RecoupeurDePossessions;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.model.Pagination;

import java.util.List;

public class PossessionRecoupeeProvider {
    @Builder(toBuilder = true)
    public record Filter(String nom, RecoupementStatus status, Pagination pagination) {
        public Filter nom(String nom){
           return this.toBuilder().nom(nom) .build();
        }
    }

    @Builder(toBuilder = true)
    public record Meta(Cas planned, Cas done){}

    public List<PossessionRecoupee> getList(Meta meta, Filter filter){
        var recoupeurDePossession = RecoupeurDePossessions.of(meta.planned().getFinSimulation(), meta.done().patrimoine(), meta.done().patrimoine());
        var possessionsRecoupees = recoupeurDePossession.getPossessionsRecoupees();

        //TODO: filter possessionsRecoupees
        return List.of();
    }
}
