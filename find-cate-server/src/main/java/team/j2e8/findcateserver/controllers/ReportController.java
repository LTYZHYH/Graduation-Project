package team.j2e8.findcateserver.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.j2e8.findcateserver.infrastructure.ObjectSelector;
import team.j2e8.findcateserver.models.Commity;
import team.j2e8.findcateserver.models.Reply;
import team.j2e8.findcateserver.models.TravelStrategy;
import team.j2e8.findcateserver.services.ReportService;

@Controller
@RequestMapping(value = "/report")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/reportStrategy")
    public ResponseEntity<?> reportStrategy(@RequestBody JsonNode jsonNode){
        Integer sid = jsonNode.path("strategy_id").intValue();
        String reason = jsonNode.path("strategy_reason").textValue();
        reportService.reportStrategy(sid,reason);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/reportCommit")
    public ResponseEntity<?> reportCommit(@RequestBody JsonNode jsonNode){
        Integer cid = jsonNode.path("id").intValue();
        reportService.reportCommmit(cid);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/reportReply")
    public ResponseEntity<?> reportReply(@RequestBody JsonNode jsonNode){
        Integer rid = jsonNode.path("id").intValue();
        reportService.reportReply(rid);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET,value = "/getReportStrategy")
    public ResponseEntity<?> getReportStrategy(@RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                               @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                               @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "strategyId,desc")String sort ) {
        Page<TravelStrategy> travelStrategyPage = reportService.getAllReportStrategy(sort,pageNum,pageSize);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(travelStrategyPage,"(strategyId,theme,user(userName),reportReason)"));
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET,value = "/getReportCommit")
    public ResponseEntity<?> getReportCommit(@RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                               @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                               @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "commityId")String sort ) {
        Page<Commity> commityPage = reportService.getAllReportCommit(sort,pageNum,pageSize);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(commityPage,"(commityId,user(userName),commityContent)"));
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET,value = "/getReportReply")
    public ResponseEntity<?> getReportReply(@RequestParam(value = "${spring.data.rest.page-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-number}") Integer pageNum,
                                               @RequestParam(value = "${spring.data.rest.limit-param-name}", required = false, defaultValue = "${spring.data.rest.default-page-size}") Integer pageSize,
                                               @RequestParam(value = "${spring.data.rest.sort-param-name}", required = false, defaultValue = "replyId")String sort ) {
        Page<Reply> replyPage = reportService.getAllReportReply(sort,pageNum,pageSize);
        return ResponseEntity.ok(new ObjectSelector().mapPagedObjects(replyPage,"(replyId,replyContent,user(userName))"));
    }
}
