package team.j2e8.findcateserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.j2e8.findcateserver.models.City;
import team.j2e8.findcateserver.models.QCity;
import team.j2e8.findcateserver.repositories.CityRepository;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    private QCity qCity = QCity.city;

    public void addCity(String cityTitle, String cityPicture) throws Exception{
        City city = new City();
        city.setCityTitle(cityTitle);
        city.setCityPicture(cityPicture);
        cityRepository.save(city);
    }

    public Iterable<City> getCityPicture(){
        Iterable<City> citiesPicture = cityRepository.findAll();
        return citiesPicture;
    }
}
