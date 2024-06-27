package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.beans.DefaultPersistenceDelegate;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department department) {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = conn.prepareStatement(
                    "INSERT INTO department " +
                            "(Name) " +
                            "VALUES " +
                            "(?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, department.getName());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    department.setId(id);
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
    public void update(Department department) {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = conn.prepareStatement(
                    "UPDATE department " +
                            "SET Name = ? " +
                            "WHERE Id = ?"
            );

            ps.setString(1, department.getName());
            ps.setInt(2, department.getId());

            ps.executeUpdate();

        } catch (SQLException exception) {
            throw new DbException(exception.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closePreparedStatement(ps);
        }

    }

    @Override
    public void deleteById(Integer id) {

        PreparedStatement ps = null;

        try {

            ps = conn.prepareStatement(
                    "DELETE FROM department " +
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
    public Department findById(Integer id) {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = conn.prepareStatement(
                    "SELECT * FROM department " +
                            "WHERE Id = ?"
            );

            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Department dep = instantiateDepartment(rs);
                return dep;
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
    public List<Department> findAll() {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = conn.prepareStatement(
                    "SELECT * FROM department"
            );

            rs = ps.executeQuery();

            List<Department> listDepartments = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {

                Department dep = map.get(rs.getInt("Id"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("Id"), dep);
                }

                listDepartments.add(dep);
            }

            return listDepartments;


        } catch (SQLException exception) {
            throw new DbException(exception.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closePreparedStatement(ps);
        }

    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {

        Department dep = new Department();

        dep.setId(rs.getInt("Id"));
        dep.setName(rs.getString("Name"));

        return dep;
    }

}
