package fr.ign.cogit.simplu3d.util;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.sig3d.equation.ApproximatedPlanEquation;

public class AngleFromSurface {

  /**
   * 
   * Renvoie l'angle en degré formé par un polygone et le plan horizontal
   * @param o
   * @return
   */
  public static double calculate(IOrientableSurface o) {
    ApproximatedPlanEquation ep = new ApproximatedPlanEquation(o);

    Vecteur v = ep.getNormale();
    v.normalise();

    
    Vecteur vHo = new  Vecteur(v.getX(),v.getY(), 0);

    

    double angleTemp = 0;
    if (vHo.norme() != 0) {
      vHo.normalise();
      angleTemp = Math.PI/2 - Math.acos(v.prodScalaire(vHo));
    }
    
    angleTemp = angleTemp * 180/Math.PI;


    return angleTemp;
  }

}
