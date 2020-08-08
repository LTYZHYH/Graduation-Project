package team.j2e8.findcateserver.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import team.j2e8.findcateserver.models.Shop;

import java.util.List;

@Repository
public interface ShopRepository extends PagingAndSortingRepository<Shop, Integer>, QuerydslPredicateExecutor<Shop> {
    @Query(value = "select * " +
            "from shop " +
            "where shop_lat > ?2-1 " +
            "and shop_lat < ?2+1 " +
            "and shop_lng > ?1-1 " +
            "and shop_lng < ?1+1 " +
            "and shop_active = 1 " +
            "order by ACOS(SIN((?2 * 3.1415) / 180 ) *SIN((shop_lat * 3.1415) / 180 ) +COS((?2 * 3.1415) / 180 ) * COS((shop_lat * 3.1415) / 180 ) *COS((?1* 3.1415) / 180 - (shop_lng * 3.1415) / 180 ) ) * 6380 asc " +
            "limit ?3,?4", nativeQuery = true)
    List<Shop> findByLngAndLat(Double lng, Double lat, int pageNum, int pageSize);

    @Query(value = "select count(*) " +
            "from shop " +
            "where shop_lat > ?2-1 " +
            "and shop_lat < ?2+1 " +
            "and shop_lng > ?1-1 " +
            "and shop_lng < ?1+1 " +
            "and shop_active = 1 " +
            "order by ACOS(SIN((?2 * 3.1415) / 180 ) *SIN((shop_lat * 3.1415) / 180 ) +COS((?2 * 3.1415) / 180 ) * COS((shop_lat * 3.1415) / 180 ) *COS((?1* 3.1415) / 180 - (shop_lng * 3.1415) / 180 ) ) * 6380 asc ", nativeQuery = true)
    Integer findByLngAndLatTest(Double lng, Double lat);

    @Query(value = "select * " +
            "from shop " +
            "where shop_lat > ?2-1 " +
            "and shop_lat < ?2+1 " +
            "and shop_lng > ?1-1 " +
            "and shop_lng < ?1+1 " +
            "and shop_active = 1 " +
            "and shop_name like %?5% " +
            "order by ACOS(SIN((?2 * 3.1415) / 180 ) *SIN((shop_lat * 3.1415) / 180 ) +COS((?2 * 3.1415) / 180 ) * COS((shop_lat * 3.1415) / 180 ) *COS((?1* 3.1415) / 180 - (shop_lng * 3.1415) / 180 ) ) * 6380 asc " +
            "limit ?3,?4", nativeQuery = true)
    List<Shop> findByLngAndLatAndShopName(Double lng, Double lat, int pageNum, int pageSize, String name);


    @Query(value = "select * " +
            "from shop " +
            "limit ?3,?4", nativeQuery = true)
    List<Shop> findSome(int limit, int offset);

    @Query(value = "select count(*) " +
            "from shop " +
            "where shop_lat > ?2-1 " +
            "and shop_lat < ?2+1 " +
            "and shop_lng > ?1-1 " +
            "and shop_lng < ?1+1 " +
            "and shop_active = 1 " +
            "and shop_name like %?3% " +
            "order by ACOS(SIN((?2 * 3.1415) / 180 ) *SIN((shop_lat * 3.1415) / 180 ) +COS((?2 * 3.1415) / 180 ) * COS((shop_lat * 3.1415) / 180 ) *COS((?1* 3.1415) / 180 - (shop_lng * 3.1415) / 180 ) ) * 6380 asc ", nativeQuery = true)
    Integer findByLngAndLatAndShopNameTest(Double lng, Double lat, String name);

//    @Query(value = "select $1 "+" from shop " , nativeQuery = true)
//    Iterable<Shop> findAll(String photo);
}
