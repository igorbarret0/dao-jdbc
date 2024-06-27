package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.beans.DefaultPersistenceDelegate;
import java.util.Date;
import java.util.List;

public class Program2 {
    public static void main(String[] args) {

        DepartmentDao depDao = DaoFactory.createDepartmentDao();

        System.out.println("=== insert DEPARTMENT === ");
        Department newDepartment = new Department(null, "Groceries");
        depDao.insert(newDepartment);


        System.out.println("=== update DEPARTMENT === ");
        Department updatedDepartment = new Department(2, "Food");
        depDao.update(updatedDepartment);

        System.out.println("=== deleteById DEPARTMENT === ");
        depDao.deleteById(6);

        System.out.println("=== findById DEPARTMENT === ");
        System.out.println(depDao.findById(1));


    }
}