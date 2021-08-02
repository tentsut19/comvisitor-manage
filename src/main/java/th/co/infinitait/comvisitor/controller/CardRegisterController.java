package th.co.infinitait.comvisitor.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import th.co.infinitait.comvisitor.component.CabsatPayload;
import th.co.infinitait.comvisitor.model.request.activity.ActivityRequest;
import th.co.infinitait.comvisitor.model.response.cardregister.CardRegisterResponse;
import th.co.infinitait.comvisitor.service.CardRegisterService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CardRegisterController {
    private final CardRegisterService cardRegisterService;
    private final CabsatPayload cabsatPayload;

    @PostMapping(value = "/card-register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardRegisterResponse> createActivity(@Validated @RequestBody ActivityRequest request) {
        log.info("ActivityRequest : {}", request);
        log.info("cabsatPayload : {}", cabsatPayload);
        CardRegisterResponse response = cardRegisterService.createActivity(request,cabsatPayload.getUserId());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/card-register/{activityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardRegisterResponse> getActivity(@PathVariable Long activityId) {
        return ResponseEntity.ok(cardRegisterService.getActivityById(activityId));
    }

    @GetMapping(value = "/card-register/all", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<CardRegisterResponse>> getAll() throws Exception
    {
        return ResponseEntity.ok(cardRegisterService.getAll());
    }

}
