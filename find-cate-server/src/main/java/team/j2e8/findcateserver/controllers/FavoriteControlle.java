package team.j2e8.findcateserver.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.j2e8.findcateserver.infrastructure.ObjectSelector;
import team.j2e8.findcateserver.models.Favorite;
import team.j2e8.findcateserver.services.FavoriteService;

@Controller
@RequestMapping(value = "/favorite")
public class FavoriteControlle {
    @Autowired
    private FavoriteService favoriteService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/getMyfavoriteStrategy")
    private ResponseEntity<?> getMyfavoriteStrategy(@RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                                    @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                                    @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "favoriteId")String sort) throws Exception{
        Page<Favorite> favoritePage = favoriteService.getMyFavoriteStrategy(sort,pageNum,pageSize);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(favoritePage,"(favoriteId,travelStrategy(strategyId,theme,strategyPicture1))"));
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/addFavorite")
    private ResponseEntity<?> addFavorite(@RequestBody JsonNode jsonNode)throws Exception {
        Integer fid = jsonNode.path("strategy_id").intValue();
        int key = favoriteService.addFavorite(fid);
        if (key == 1) {
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).body(null);
        }
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/cancelFavorite")
    private ResponseEntity<?> cancelFavorite(@RequestBody JsonNode jsonNode) throws Exception{
        Integer strategyId = jsonNode.path("strategy_id").intValue();
        favoriteService.cancelFavoriteAgain(strategyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
