package team.j2e8.findcateserver.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.j2e8.findcateserver.infrastructure.ObjectSelector;
import team.j2e8.findcateserver.models.Commity;
import team.j2e8.findcateserver.services.CommityService;

import java.util.Optional;

/**
 * @auther Heyanhu
 * @date 2018/12/11 20:22
 */
@Controller
@RequestMapping(value = "/commity")
public class CommityController {
    @Autowired
    private CommityService commityService;
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/comment")
    public ResponseEntity<?> comment(@RequestBody JsonNode jsonNode) throws Exception {
        String comments = jsonNode.path("comment").textValue();
        int sid = jsonNode.path("strategy_id").intValue();
        commityService.insertComments(sid,comments);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/getComment")
    public ResponseEntity<?> getCommentByStrategyId(@RequestParam Integer strategyId,
                                                    @RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                                    @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                                    @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "commityId") String sort
                                                    )  throws Exception{
        Page<Commity> commityPage = commityService.getCommentsByTravelStrategyId(strategyId,sort,pageNum,pageSize);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(commityPage,"(commityId, commityContent, commityTime,user(id,userName,userPhoto))"));
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/getOneComment")
    public ResponseEntity<?> getComment(@RequestParam Integer commentId) throws Exception{
        Optional<Commity> commityOptional = commityService.getComment(commentId);
        return ResponseEntity.ok(new ObjectSelector().mapObject(commityOptional.get(),"(commityId,commityTime,commityContent,user(id,userName,userPhoto))"));
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/deleteCommity")
    public ResponseEntity<?> deleteCommity(@RequestBody JsonNode jsonNode){
        Integer commityId = Integer.valueOf(jsonNode.path("commityId").textValue());
        commityService.deleteCommity(commityId);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
