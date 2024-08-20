package uno.dos.tres.dao.impl;

import uno.dos.tres.bean.AuthInfo;
import uno.dos.tres.bean.RegInfo;
import uno.dos.tres.bean.User;
import uno.dos.tres.bean.UserRoles;
import uno.dos.tres.dao.AuthDao;
import uno.dos.tres.dao.сonnectionpool.ConnectionPool;
import uno.dos.tres.dao.сonnectionpool.ConnectionPoolException;
import uno.dos.tres.dao.DaoException;

import java.sql.*;


public class SQLAuthDao implements AuthDao {

    private final String COLUMN_NAME_MAIL = "mail";
    private final String COLUMN_NAME_USERNAME = "username";
    private final String COLUMN_NAME_TITLE = "title";
    private final String COLUMN_NAME_ID_ROLES = "idroles";
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public User checkToken(String token) throws DaoException {
        return new User("Dany", UserRoles.ADMIN);
    }

    private final String SQL_FOR_SIGH_IN = "SELECT u.idusers, u.username, u.password, u.mail, u.status, r.idroles, r.title " +
            "FROM users u " +
            "JOIN users_has_roles ur ON u.idusers = ur.users_idusers " +
            "JOIN roles r ON ur.roles_idroles = r.idroles " +
            "WHERE u.mail = ? AND u.password = ?";

    @Override
    public User signIn(AuthInfo authInfo) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String mail = authInfo.getLogin();
        String password = authInfo.getPassword();
        try {
            connection = connectionPool.takeConnection();
            statement = connection.prepareStatement(SQL_FOR_SIGH_IN);
            statement.setString(1, mail);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            try {
                if (resultSet.next()) {
                    String userMail = resultSet.getString(COLUMN_NAME_MAIL);
                    String username = resultSet.getString(COLUMN_NAME_USERNAME);
                    UserRoles role = UserRoles.valueOf(resultSet.getString(COLUMN_NAME_TITLE));
                    System.out.println(username + role);
                    return new User(userMail, username, role);
                } else {
                    throw new DaoException("No such user");
                }
            } catch (SQLException e) {
                throw new DaoException(e);
            }

        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("BD Error", e);
        } finally {
            try {
                connectionPool.closeConnection(resultSet, statement, connection);
            } catch (ConnectionPoolException e) {
                throw new DaoException(e);
            }
        }
    }

    private final String SQL_FOR_SIGN_UP = "INSERT INTO users (username, password, mail) VALUES (?, ?, ?)";
    private final String ROLE_QUERY = "SELECT idroles FROM roles WHERE title = ?";
    private final String INSERT_USER_ROLE_SQL = "INSERT INTO users_has_roles (users_idusers, roles_idroles) VALUES (?, ?)";

    @Override
    public void signUp(RegInfo regInfo) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement pstmtUserRole = null;
        ResultSet generatedKeys = null;
        int roleId = -1;
        int userId = -1;
        try {
            connection = connectionPool.takeConnection();
            connection.setAutoCommit(false);
            String username = regInfo.getUserName();
            String password = regInfo.getPassword();
            String mail = regInfo.getEmail();
            UserRoles roleName = regInfo.getRole();
            preparedStatement = connection.prepareStatement(SQL_FOR_SIGN_UP, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, mail);
            preparedStatement.executeUpdate();
            generatedKeys = preparedStatement.getGeneratedKeys();
            while (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            }

            PreparedStatement psRole = connection.prepareStatement(ROLE_QUERY);
            psRole.setString(1, String.valueOf(roleName));
            ResultSet rsRole = psRole.executeQuery();
            while (rsRole.next()) {
                roleId = rsRole.getInt(COLUMN_NAME_ID_ROLES);
            }

            pstmtUserRole = connection.prepareStatement(INSERT_USER_ROLE_SQL);
            pstmtUserRole.setInt(1, userId);
            pstmtUserRole.setInt(2, roleId);
            pstmtUserRole.executeUpdate();

            connection.commit();

        } catch (SQLException | ConnectionPoolException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                throw new DaoException(ex);
            }
            throw new DaoException(e);
        } finally {
            try {
                connectionPool.closeConnection(generatedKeys, preparedStatement, connection);
            } catch (ConnectionPoolException e) {
                throw new DaoException(e);
            }
        }
    }

    private final String CHECK_USER_FOR_USER_MAIL = "SELECT COUNT(*) FROM users WHERE mail = ?";

    @Override
    public boolean checkUserMail(RegInfo regInfo) throws DaoException {
        Connection connection = null;
        PreparedStatement checkUserStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.takeConnection();
            checkUserStatement = connection.prepareStatement(CHECK_USER_FOR_USER_MAIL);
            String mail = regInfo.getEmail();
            checkUserStatement.setString(1, mail);
            resultSet = checkUserStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }

        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e);
        } finally {
            try {
                connectionPool.closeConnection(resultSet, checkUserStatement, connection);
            } catch (ConnectionPoolException e) {
                throw new DaoException(e);
            }
        }
        return false;
    }
}