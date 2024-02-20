package presentation;
import ext.DaoImpl2;
import metier.MetierImpl;

// instanciation statique par l'utilisation de new couplage forte

public class presentation2 {

    public static void main(String[] args) {
        DaoImpl2 dao=new DaoImpl2();
        MetierImpl metier=new MetierImpl(dao);
        //metier.setDao(dao);
        System.out.println("le resultat est :"+metier.Calcul());
    }

}
