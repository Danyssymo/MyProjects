package uno.dos.tres.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import uno.dos.tres.beans.Country;
import uno.dos.tres.beans.Island;
import uno.dos.tres.beans.IslandImage;
import uno.dos.tres.dao.DaoException;
import uno.dos.tres.dao.IslandDao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class IslandDaoImpl implements IslandDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Set<Country> fingAllCountries() throws DaoException {
        Session session = sessionFactory.getCurrentSession();
        Query<Country> query = session.createQuery("from Country", Country.class);
        List<Country> countryList = query.getResultList();
        return new HashSet<>(countryList);
    }

    @Override
    public void addIsland(Island island) throws DaoException {
        Session session = sessionFactory.getCurrentSession();
        session.merge(island);
    }

    @Override
    public List<Island> findAllIslands() throws DaoException {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Island";
        Query<Island> query = session.createQuery(hql, Island.class);
        return query.getResultList();
    }

    @Override
    public List<Island> findIslandsByUserId(int id) throws DaoException {
        Session session = sessionFactory.getCurrentSession();
        Query<Island> query = session.createQuery("FROM Island i WHERE i.user.id = :userId", Island.class);
        query.setParameter("userId", id);
        return query.getResultList();
    }

    @Override
    public Island findIslandById(int id) throws DaoException {
        Session session = sessionFactory.getCurrentSession();
        Island island = session.get(Island.class, id);
        return island;
    }

    @Override
    public void updateIsland(Island island) throws DaoException {
        Session session = sessionFactory.getCurrentSession();
        session.update(island);
    }

    @Override
    public void saveIslandImg(IslandImage image) throws DaoException {
        Session session = sessionFactory.getCurrentSession();
        session.merge(image);
    }
}
