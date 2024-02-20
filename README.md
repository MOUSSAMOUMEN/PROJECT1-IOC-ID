# Inversion de contrôle et Injection des dépendances
## Partie 1 :
1.	Créer l'interface IDao avec une méthode getData :
    
   ```java
package dao;
public interface IDao {
      double getData();
} 
```

2.	Créer une implémentation de cette interface :

  ```java
 package dao;
public class DaoImpl implements IDao{
    @Override
    public double getData() {
        System.out.println("la version base de donnees ");
        double temp=Math.random()*40;
        return temp;  }
}
```

   voici une autre implémentation (extension) de cette interface  pour le test :

```java
 package ext;
import dao.IDao;
public class DaoImpl2 implements IDao {
    @Override
    public double getData() {
        System.out.println("la version capteurs");
        double temp=700;
        return temp; }
}
```
3.	Créer l'interface IMetier avec une méthode calcul :

   ```java
package metier;
public interface IMetier {
    double Calcul();
}
```
4.	Créer une implémentation de cette interface en utilisant le couplage faible :

   ```java
package metier;
import dao.IDao;
public class MetierImpl implements IMetier {
    private IDao dao;     // c est de couplage faible
    @Override
    public double Calcul() {
        double temp= dao.getData(); //récupérer la valeur de la température
        double resutat=temp*12;
        return resutat;
    }
  // en utilise le setter pour modifier ou pour initialiser la variable dao
    // injecter a la variable dao un objet d'une classe qui implémente IDao
    public void setDao(IDao dao) {
        this.dao = dao;}
}
```

5.	Faire l'injection des dépendances :

      a. Par instanciation statique  --> couplage forte :

    la variable dao dans ce cas est de type DaoImpl ( classe qui implements l'interface IDAO version base de donnees )
   donc la version executee pour cette application est la version base de donnees 

   ```java

package pres;
import dao.DaoImpl;
import metier.MetierImpl;

// instanciation statique par l'utilisation de new couplage forte

public class presentation {
   public static void main(String[] args) {
        DaoImpl dao=new DaoImpl();
        MetierImpl metier=new MetierImpl();
        metier.setDao(dao);
        System.out.println("le resultat est :"+metier.Calcul()); }
}
```

au 2eme cas la variable dao est de type DaoImp2 (calasse qui implements l'interface IDAO version capteurs )

```java
package pres;
import dao.DaoImpl;
import ext.DaoImpl2;
import metier.MetierImpl;

// instanciation statique par l'utilisation de new couplage forte 

public class presentation {
  public static void main(String[] args) {
        DaoImpl2 dao=new DaoImpl2();
        MetierImpl metier=new MetierImpl();
        metier.setDao(dao);
        System.out.println("le resultat est :"+metier.Calcul());
    }
}
```

b. Par instanciation dynamique 

il va faloir cree un fichier extension .txt  dans le projet (config.txt) dans lequelle en mettre deux lignes de configuration package.classe

```plaintext
dao.DaoImpl
metier.MetierImpl
```
l'execution de l'application version base de donnees 

```java
package presentation;
import dao.IDao;
import metier.IMetier;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class presentation {
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
```
pour executee la version capteurs il faut juste changee le premier ligne du fichier config.txt 

```plaintext
ext.DaoImpl2
metier.MetierImpl
```

c. En utilisant le Framework Spring
       - Version XML
       
 apres la creation d'un projet maven et apres la configuration de spring 

 d'abord il faut cree un fichier XML dans le dossier resources applicationContext.xml

 version base de donnees 

 ```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
   <bean id="dao"  class="dao.DaoImpl"></bean>
    <bean id="metier" class="metier.MetierImpl">
        <constructor-arg ref="dao"></constructor-arg>
    </bean>
</beans>
 ```
 ```java
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
```
pour exeucter l'application version capteurs il faut juste changer l'attribut Class de bean dans le fichier XML par ext.DaoImpl2 puis reexecuter l'application

 ```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
   <bean id="dao"  class="ext.DaoImpl2"></bean>
    <bean id="metier" class="metier.MetierImpl">
        <constructor-arg ref="dao"></constructor-arg>
    </bean>
</beans>
 ```

- Version annotations
```java
package dao;
import org.springframework.stereotype.Component;

@Component("dao")
public class DaoImpl implements IDao {
        @Override
        public double getData() {
            System.out.println("la version base de donnees ");
            double temp=Math.random()*40;
            return temp;
        }
}
```
```java
package metier;
import dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("metier")
public class MetierImpl implements IMetier {

    private IDao dao;     // c est de couplage faible
    
    //en utilise cette fois ci le constructeur avec parametre a la place de l'annotation @autowired c est mieux 
    public MetierImpl(IDao dao) {
        this.dao = dao;
    }

    @Override
    public double Calcul() {
        double temp= dao.getData(); //récupérer la valeur de la température
        double resutat=temp*12;
        return resutat;
    }
    // en utilise le setter pour modifier ou pour initialiser la variable dao
    // injecter a la variable dao un objet d'une classe qui implémente IDao
    public void setDao(IDao dao) {
        this.dao = dao;
    }

}
```
en fin en execute l'aaplication 

```java
package presentation;
import metier.IMetier;
import org.springframework.cache.annotation.SpringCacheAnnotationParser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class presentationSpringAnnotation {

    public static void main(String[] args) {

        ApplicationContext context=new AnnotationConfigApplicationContext("dao","metier");
        IMetier metier=context.getBean(IMetier.class);
        System.out.println("le resultat est = > "+metier.Calcul());
    }
```










   



   


