package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: seller findById ===\n");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("=== TEST 2: seller findByDepartment ===\n");
        Department dep = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(dep);

        for (Seller sel : list) {
            System.out.println(sel);
        }

        System.out.println("=== TEST 3: seller findAll ===\n");
        List<Seller> sellerFindAll = sellerDao.findAll();

        for (Seller sel : sellerFindAll) {
            System.out.println(sel);
        }

        System.out.println("=== TEST 4: seller insert");
        Seller newSeller = new Seller(null, "Igor", "igor@email.com", new Date(),
                1000.0, dep);

        sellerDao.insert(newSeller);

        System.out.println("=== TEST 5: seller update");
        newSeller.setEmail("igor0000@email.com");
        newSeller.setId(15);
        sellerDao.update(newSeller);

    }
}