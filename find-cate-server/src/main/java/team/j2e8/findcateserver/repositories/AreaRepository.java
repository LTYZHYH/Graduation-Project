package team.j2e8.findcateserver.repositories;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import team.j2e8.findcateserver.models.Area;

public interface AreaRepository extends PagingAndSortingRepository<Area,Integer>, QuerydslPredicateExecutor<Area> {
}
