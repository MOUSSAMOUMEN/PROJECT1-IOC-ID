package metier;
import dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("metier")
public class MetierImpl implements IMetier {


    private IDao dao;     // c est de couplage faible

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
