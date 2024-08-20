package uno.dos.tres.dao;

import uno.dos.tres.beans.Country;
import uno.dos.tres.beans.Island;
import uno.dos.tres.beans.IslandImage;

import java.util.List;
import java.util.Set;

public interface IslandDao {

    Set<Country> fingAllCountries()throws DaoException;
    void addIsland(Island island)throws DaoException;
    List<Island> findAllIslands()throws DaoException;
    List<Island> findIslandsByUserId(int id)throws DaoException;
    Island findIslandById(int id)throws DaoException;
    void updateIsland(Island island)throws DaoException;
    void saveIslandImg(IslandImage image)throws DaoException;

}
