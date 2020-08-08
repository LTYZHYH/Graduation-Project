package team.j2e8.findcateserver.repositories;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import team.j2e8.findcateserver.models.TravelStrategy;

public interface TravelStrategyRepository extends PagingAndSortingRepository<TravelStrategy,Integer>, QuerydslPredicateExecutor<TravelStrategy> {
}
