package presentation;

import metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class presentationSpringXml {

    public static void main(String[] args) {

        //En utilisant le Framework Spring
        //  Version XML

        ApplicationContext context=new ClassPathXmlApplicationContext("applicationConetext.xml");
        IMetier metier=(IMetier) context.getBean("metier");
        System.out.print("le resultat est => "+metier.Calcul());
    }
}
