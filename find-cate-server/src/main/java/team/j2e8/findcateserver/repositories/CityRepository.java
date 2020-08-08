package team.j2e8.findcateserver.repositories;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import team.j2e8.findcateserver.models.City;

public interface CityRepository extends PagingAndSortingRepository<City,Integer>, QuerydslPredicateExecutor<City> {
}
