package ext;

import dao.IDao;

public class DaoImpl2 implements IDao {
    @Override
    public double getData() {
        System.out.println("la version capteurs");
        double temp=700;
        return temp;
    }

}
