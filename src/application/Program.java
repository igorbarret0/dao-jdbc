package application;

import db.DB;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;

public class Program {
    public static void main(String[] args) {

        Department department = new Department(1, "Books");

        System.out.println(department);

        Seller seller = new Seller(1, "Luke", "luke@email.com", new Date(), 3000.0, department);
        System.out.println(seller);
    }
}