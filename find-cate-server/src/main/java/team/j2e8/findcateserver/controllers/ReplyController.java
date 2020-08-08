package team.j2e8.findcateserver.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.j2e8.findcateserver.infrastructure.ObjectSelector;
import team.j2e8.findcateserver.models.Reply;
import team.j2e8.findcateserver.services.ReplyService;

/**
 * @auther Heyanhu
 * @date 2018/12/11 21:55
 */
@Controller
@RequestMapping(value = "/reply")
public class ReplyController {
    @Autowired
    private ReplyService replyService;
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/replys")
    public ResponseEntity<?> reply(@RequestBody JsonNode jsonNode) throws Exception {
        String replyContent = jsonNode.path("reply_content").textValue();
        int cid = jsonNode.path("commity_id").intValue();
        replyService.insertReply(cid,replyContent);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/getReply")
    public ResponseEntity<?> getReplyByCommityId(@RequestParam Integer commityId,
                                                 @RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                                 @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                                 @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "replyId") String sort
    ) throws Exception {

        Page<Reply> commityPage = replyService.getReplyByCommityId(commityId, sort, pageNum, pageSize);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(commityPage, "(replyId, replyContent, replyTime,user(id,userName,userPhoto),commity(commityId))"));
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/deleteReply")
    public ResponseEntity<?> deleteCommity(@RequestBody JsonNode jsonNode){
        Integer replyId = Integer.valueOf(jsonNode.path("replyId").textValue());
        replyService.deleteReply(replyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
