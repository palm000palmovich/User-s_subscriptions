package users.subscriptions.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import users.subscriptions.model.Subscriptions;
import users.subscriptions.services.SubscriptionsService;

import java.util.Map;

@RestController
@RequestMapping(path = "/subscriptions")
public class SubscriptionsController {
    private final SubscriptionsService subscriptionsService;

    public SubscriptionsController(SubscriptionsService subscriptionsService) {
        this.subscriptionsService = subscriptionsService;
    }

    @GetMapping(path = "/top")
    public ResponseEntity<Map<String, Subscriptions>> getTop2Subs(){
        Map<String, Subscriptions> map = subscriptionsService.getTop3Subscriptions();

        if (map == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(map);
    }
}
