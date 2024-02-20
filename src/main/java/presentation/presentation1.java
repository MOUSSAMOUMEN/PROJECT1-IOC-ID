package presentation;
import dao.DaoImpl;
import metier.MetierImpl;

// instanciation statique par l'utilisation de new couplage forte
public class presentation1 {

    public static void main(String[] args) {
        DaoImpl dao=new DaoImpl();
        MetierImpl metier=new MetierImpl(dao);
        //metier.setDao(dao);
        System.out.println("le resultat est :"+metier.Calcul());
    }
}
