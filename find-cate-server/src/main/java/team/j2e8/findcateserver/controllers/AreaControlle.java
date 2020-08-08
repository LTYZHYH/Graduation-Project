package team.j2e8.findcateserver.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.j2e8.findcateserver.infrastructure.ObjectSelector;
import team.j2e8.findcateserver.infrastructure.usercontext.IdentityContext;
import team.j2e8.findcateserver.models.Area;
import team.j2e8.findcateserver.services.AreaService;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/area")
public class AreaControlle {
    @Resource(name = "tokenIdentityContext")
    private IdentityContext identityContext;

    @Autowired
    private AreaService areaService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET,value = "/getAreaByCityId")
    public ResponseEntity<?> getAreaByCityId(@RequestParam(value = "city_id",required = false) Integer cityId) throws Exception{
        Iterable<Area> areaPage = areaService.getAreaByCityId(cityId);
        return ResponseEntity.ok(new ObjectSelector().mapObjects(areaPage,"(areaId,areaName)"));
    }
}
