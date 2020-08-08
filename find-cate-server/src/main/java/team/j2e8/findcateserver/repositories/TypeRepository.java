package team.j2e8.findcateserver.repositories;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import team.j2e8.findcateserver.models.Type;
@Repository
public interface TypeRepository extends PagingAndSortingRepository<Type, Integer>, QuerydslPredicateExecutor<Type> {
}
