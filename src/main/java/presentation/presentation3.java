package presentation;
import dao.IDao;
import metier.IMetier;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class presentation3 {

    //instanciation dynamique par l'utilisation d'un fichier config.txt

    public static void main(String[] args) throws Exception{
        //charger le fichier config.txt en utilise la classe Scanner

        Scanner scanner = new Scanner(new File("config.txt"));
        String daoClassName=scanner.nextLine();
        Class cDao=Class.forName(daoClassName);
        IDao dao=(IDao)cDao.newInstance();

        String metierClassName=scanner.nextLine();
        Class cMetier=Class.forName(metierClassName);
        IMetier metier=(IMetier)cMetier.newInstance();

        Method method=cMetier.getMethod("setDao",IDao.class);
        method.invoke(metier,dao);
        System.out.println("le resultat est "+metier.Calcul());
    }
}
