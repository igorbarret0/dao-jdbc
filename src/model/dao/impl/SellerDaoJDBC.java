package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = conn.prepareStatement(
                    "INSERT INTO seller " +
                            "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                            "VALUES " +
                            "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, seller.getName());
            ps.setString(2, seller.getEmail());
            ps.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            ps.setDouble(4, seller.getBaseSalary());
            ps.setInt(5, seller.getDepartment().getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    seller.setId(id);
                }
            } else {
                throw new DbException("Unexpected error! No rows affected");
            }

        } catch (SQLException exception) {
            throw new DbException(exception.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closePreparedStatement(ps);
        }

    }

    @Override
    public void update(Seller seller) {}

    @Override
    public void deleteById(Integer id) {

        PreparedStatement ps = null;

        try {

            ps = conn.prepareStatement(
                    "DELETE FROM seller " +
                            "WHERE Id = ?"
            );

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException exception) {
            throw new DbException(exception.getMessage());
        } finally {
            DB.closePreparedStatement(ps);
        }

    }

    @Override
    public Seller findById(Integer id) {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE seller.Id = ?"
            );
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {

                Department dep = instantiateDepartment(rs);
                Seller sel = instantiateSeller(rs, dep);

                return sel;
            }

            return null;
        } catch (SQLException exception) {
            throw new DbException(exception.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closePreparedStatement(ps);
        }

    }

    @Override
    public List<Seller> findAll() {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "ORDER BY Name"
            );

            rs = ps.executeQuery();

            List<Seller> listSellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller sel = instantiateSeller(rs, dep);

                listSellers.add(sel);
            }

            return listSellers;

        } catch (SQLException exception) {
            throw new DbException(exception.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closePreparedStatement(ps);
        }

    }

    @Override
    public List<Seller> findByDepartment(Department department) {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE Department.Id = ? " +
                            "ORDER BY Name"
            );

            ps.setInt(1, department.getId());
            rs = ps.executeQuery();

            List<Seller> listSellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller sel = instantiateSeller(rs, dep);

                listSellers.add(sel);
            }

            return listSellers;

        } catch (SQLException exception) {
            throw new DbException(exception.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closePreparedStatement(ps);
        }

    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {

        Department dep = new Department();

        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));

        return dep;
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {

        Seller sel = new Seller();

        sel.setId(rs.getInt("Id"));
        sel.setName(rs.getString("Name"));
        sel.setEmail(rs.getString("Email"));
        sel.setBirthDate(rs.getDate("BirthDate"));
        sel.setBaseSalary(rs.getDouble("BaseSalary"));
        sel.setDepartment(dep);

        return sel;

    }

}
