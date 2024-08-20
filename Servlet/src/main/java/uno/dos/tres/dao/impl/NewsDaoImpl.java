package uno.dos.tres.dao.impl;

import uno.dos.tres.bean.News;
import uno.dos.tres.dao.сonnectionpool.ConnectionPool;
import uno.dos.tres.dao.сonnectionpool.ConnectionPoolException;
import uno.dos.tres.dao.DaoException;
import uno.dos.tres.dao.NewsDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewsDaoImpl implements NewsDao {

    private final String COLUMN_NAME_ID_USERS = "idusers";
    private final String COLUMN_NAME_ID_NEWS = "idnews";
    private final String COLUMN_NAME_TITLE = "title";
    private final String COLUMN_NAME_CONTENT = "content";
    private final String COLUMN_NAME_IMG = "img_path";
    private final String COLUMN_NAME_USERNAME = "username";
    private final String COLUMN_NAME_MAIL = "mail";
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
    private final String SQL_FOR_ADD_NEWS_ONE = "INSERT INTO news (title, content, img_path, users_idusers) VALUES (?, ?, ?, ?)";
    private final String SQL_FOR_ADD_NEWS_TWO = "SELECT idusers FROM users WHERE username = ?";

    @Override
    public void addNews(News news, String username) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatementUser = null;
        PreparedStatement preparedStatementNews = null;
        ResultSet rs = null;
        String title = news.getTitle();
        String content = news.getContent();
        String imgPath = news.getImgPath();
        int userId;
        try {
            connection = connectionPool.takeConnection();
            connection.setAutoCommit(false);
            preparedStatementUser = connection.prepareStatement(SQL_FOR_ADD_NEWS_TWO);
            preparedStatementUser.setString(1, username);
            rs = preparedStatementUser.executeQuery();
            userId = -1;
            while (rs.next()) {
                userId = rs.getInt(COLUMN_NAME_ID_USERS);
            }

            if (userId != -1) {
                preparedStatementNews = connection.prepareStatement(SQL_FOR_ADD_NEWS_ONE);
                preparedStatementNews.setString(1, title);
                preparedStatementNews.setString(2, content);
                preparedStatementNews.setString(3, imgPath);
                preparedStatementNews.setInt(4, userId);
                preparedStatementNews.executeUpdate();
            } else {
                connection.rollback();
                throw new DaoException("User Not Found");
            }
            connection.commit();
        } catch (ConnectionPoolException | SQLException e) {
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
                connectionPool.closeConnection(rs, preparedStatementUser, preparedStatementNews, connection);
            } catch (ConnectionPoolException e) {
                throw new DaoException(e);
            }
        }
    }

    private final String SQL_FOR_DELETE_NEWS = "DELETE FROM news WHERE idnews = ?";

    @Override
    public void deleteNews(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SQL_FOR_DELETE_NEWS);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                connectionPool.closeConnection(preparedStatement, connection);
            } catch (ConnectionPoolException e) {
                throw new DaoException(e);
            }
        }
    }

    private final String SQL_FOR_UPDATE_NEWS = "UPDATE news SET title = ?, content = ?, img_path = ? WHERE idnews = ?";

    @Override
    public void updateNews(int id, String title, String content, String imgPath) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SQL_FOR_UPDATE_NEWS);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, content);
            preparedStatement.setString(3, imgPath);
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException(e);
        }
    }

    private final String SQL_FOR_SHOW_NEWS = "SELECT n.idnews, n.title, n.content, n.img_path, u.mail, u.username " +
            "FROM news n " +
            "JOIN users u ON n.users_idusers = u.idusers;";

    @Override
    public List<News> getNews() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<News> list = new ArrayList<>();
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SQL_FOR_SHOW_NEWS);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                News news = new News();
                news.setId(resultSet.getInt(COLUMN_NAME_ID_NEWS));
                news.setTitle(resultSet.getString(COLUMN_NAME_TITLE));
                news.setContent(resultSet.getString(COLUMN_NAME_CONTENT));
                news.setImgPath(resultSet.getString(COLUMN_NAME_IMG));
                news.setAuthorUsername(resultSet.getString(COLUMN_NAME_USERNAME));
                news.setAuthorMail(resultSet.getString(COLUMN_NAME_MAIL));
                list.add(news);
            }
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                connectionPool.closeConnection(resultSet, preparedStatement, connection);
            } catch (ConnectionPoolException e) {
                throw new DaoException(e);
            }
        }
        return list;
    }
}
