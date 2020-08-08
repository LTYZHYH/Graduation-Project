package team.j2e8.findcateserver.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import team.j2e8.findcateserver.exceptions.ArgumentErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class HttpResponseDataUtil {
    public static Pageable sortAndPaging(String sort, int pageNum, int pageSize) {
        Sort customSort = Sort.by(getSort(sort));
        return PageRequest.of(pageNum, pageSize, customSort);
    }

    public static Pageable sortAndPagingByLatAndLng(Double lng, Double lat, int pageNum, int pageSize){
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC,
                "ACOS(SIN(("+lat+" * 3.1415) / 180 ) *SIN((shop_lat * 3.1415) / 180 ) + " +
                        "COS(("+lat+" * 3.1415) / 180 ) * COS((shop_lat * 3.1415) / 180 ) * COS(("+lng+"* 3.1415) / 180 - " +
                        "(shop_lng * 3.1415) / 180 ) ) * 6380"));
        return PageRequest.of(pageNum, pageSize, sort);
    }

    private static List<Sort.Order> getSort(String sort) {
        // 兼容以前的接口
        if (sort.equals("id,desc")) {
            sort = "[{\"property\":\"id\",\"direction\":\"desc\",\"nullHandlingHint\":\"last\"}]";
        }
        if (sort.equals("foodId,desc")) {
            sort = "[{\"property\":\"foodId\",\"direction\":\"desc\",\"nullHandlingHint\":\"last\"}]";
        }
        if (sort.equals("travelDays,asc")){
            sort = "[{\"property\":\"travelDays\",\"direction\":\"asc\",\"nullHandlingHint\":\"last\"}]";
        }
        if (sort.equals("scenicNumber,asc")){
            sort = "[{\"property\":\"scenicNumber\",\"direction\":\"asc\",\"nullHandlingHint\":\"last\"}]";
        }
        if (sort.equals("strategyId,desc")){
            sort = "[{\"property\":\"strategyId\",\"direction\":\"desc\",\"nullHandlingHint\":\"last\"}]";
        }
        if (sort.equals("favoriteId")){
            sort = "[{\"property\":\"favoriteId\",\"direction\":\"desc\",\"nullHandlingHint\":\"last\"}]";
        }
        //下面我把这个写死
        if (sort.length() <10)
            sort = "[{\"property\":\"" + sort + "\",\"direction\":\"desc\",\"nullHandlingHint\":\"last\"}]";
        List<Map<String, String>> list;
        try {
            list = new ObjectMapper().readValue(sort, new TypeReference<ArrayList<Map<String, String>>>() {
            });
        } catch (IOException e) {
            throw new ArgumentErrorException("排序方式错误，格式应为sort=[{\"property\":\"id\",\"direction\":\"asc\",\"nullHandlingHint\":\"last\"}," +
                    "{\"property\":\"name\",\"direction\":\"desc\",\"nullHandlingHint\":\"first\"},......]");
        }
        List<Sort.Order> orders = new ArrayList<>();
        list.stream().map((Map<String, String> map) -> new Sort.Order(map.get("direction").equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                map.get("property"), map.get("nullHandlingHint").equals("first") ? Sort.NullHandling.NULLS_FIRST : Sort.NullHandling.NULLS_LAST))
                .forEach(orders::add);
        return orders;
    }

    public static Pageable paging(int pageNum, int pageSize) {
        return  PageRequest.of(pageNum, pageSize);
    }
}
