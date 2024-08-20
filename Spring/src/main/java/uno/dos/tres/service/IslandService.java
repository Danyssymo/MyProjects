package uno.dos.tres.service;

import uno.dos.tres.beans.Country;
import uno.dos.tres.beans.Island;
import uno.dos.tres.beans.IslandImage;

import java.util.List;
import java.util.Set;

public interface IslandService {

    Set<Country> findAllCountries() throws ServiceException;

    void addIsland(Island island) throws ServiceException;
    List<Island> findAllIslands() throws ServiceException;
    List<Island> findIslandsByUserId(int id) throws ServiceException;
    Island findIslandById(int id) throws ServiceException;
    void updateIsland(Island island) throws ServiceException;
    void saveIslandImg(IslandImage image) throws ServiceException;
}
