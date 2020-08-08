package team.j2e8.findcateserver.services;

import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.j2e8.findcateserver.models.Area;
import team.j2e8.findcateserver.models.QArea;
import team.j2e8.findcateserver.repositories.AreaRepository;

@Service
public class AreaService {
    @Autowired
    private AreaRepository areaRepository;

    private QArea qArea = QArea.area;

    public Iterable<Area> getAreaByCityId(Integer cityId) throws Exception{
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qArea.city.cityId.eq(cityId));
        return areaRepository.findAll(booleanBuilder);
    }
}
