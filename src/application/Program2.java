package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program2 {
    public static void main(String[] args) {

        DepartmentDao depDao = DaoFactory.createDepartmentDao();

        System.out.println("=== INSERT DEPARTMENT === ");
        Department newDepartment = new Department(null, "Groceries");
        depDao.insert(newDepartment);



    }
}