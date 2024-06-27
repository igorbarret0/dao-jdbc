package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.List;

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
        return null;
    }

    @Override
    public List<Department> findAll() {
        return List.of();
    }
}
