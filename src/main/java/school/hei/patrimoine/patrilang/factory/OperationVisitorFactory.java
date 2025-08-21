package school.hei.patrimoine.patrilang.factory;

import school.hei.patrimoine.patrilang.visitors.*;
import school.hei.patrimoine.patrilang.visitors.possession.*;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class OperationVisitorFactory {
  public static OperationVisitor make(VariableVisitor variableVisitor, IdVisitor idVisitor) {
    return OperationVisitor.builder()
        .variableVisitor(variableVisitor)
        .remboursementDetteVisitor(new RemboursementDetteVisitor(variableVisitor, idVisitor))
        .groupPossessionVisitor(new GroupPossessionVisitor(variableVisitor))
        .achatMaterielVisitor(new AchatMaterielVisitor(variableVisitor))
        .correctionVisitor(new CorrectionVisitor(variableVisitor, idVisitor))
        .materielVisitor(new MaterielVisitor(variableVisitor))
        .objectifVisitor(new ObjectifVisitor(variableVisitor))
        .transferArgentVisitor(new TransferArgentVisitor(variableVisitor, idVisitor))
        .fluxArgentVisitor(new FluxArgentVisitor(variableVisitor, idVisitor))
        .operationTemplateCallVisitor(new OperationTemplateCallVisitor(variableVisitor))
        .valeurMarcheeVisitor(new ValeurMarcheeVisitor(variableVisitor))
        .venteVisitor(new VenteVisitor(variableVisitor))
        .immobilierVisitor(new ImmobilierVisitor(variableVisitor))
        .build();
  }

  public static OperationVisitor make(VariableVisitor variableVisitor) {
    return make(variableVisitor, new IdVisitor(variableVisitor));
  }
}
