package vttp2022.csf.assessment.server.controllers;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import vttp2022.csf.assessment.server.Utils;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping(path="/api")
public class RestaurantController {
    
    @Autowired
    private RestaurantService restaurantSvc;

    @GetMapping(path="/cuisines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCuisines(){
        List<String> cuisines = restaurantSvc.getCuisines();   
        JsonArray cuisinesArray = Utils.createCusinesArray(cuisines);
        String response = Utils.createResponse("cuisines", cuisinesArray).toString();
        return ResponseEntity.ok(response);
    }

    @GetMapping(path="/{cuisine}/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRestaurantsByCuisine(@PathVariable String cuisine){
        List<Document> restaurantsDocs = restaurantSvc.getRestaurantsByCuisine(cuisine);
        JsonArray restaurantsArray = Utils.createRestaurantsArray(restaurantsDocs);
        String response = Utils.createResponse("restaurants", restaurantsArray).toString();
        return ResponseEntity.ok(response);
    }

    @GetMapping(path="/restaurant", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRestaurantByID(@RequestParam String id){
        Optional<Restaurant> optRestaurant = restaurantSvc.getRestaurant(id);
        if (optRestaurant.isEmpty()){
            String errorResponse = Utils.createJsonResponse("error", "unable to find restaurant with id > ".formatted(id)).toString();
            return ResponseEntity.status(404).body(errorResponse);
        }
        JsonObject restaurantJsonObj = Utils.fromRestarauntToJsonObject(optRestaurant.get());
        return ResponseEntity.ok(restaurantJsonObj.toString());
    }

    @PostMapping(path="/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveComment(@RequestBody String comment){
        ObjectMapper objMapper = new ObjectMapper();
        try {
            Comment commentObj = objMapper.readValue(comment, Comment.class);
            restaurantSvc.addComment(commentObj);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String response = Utils.createJsonResponse("message", "Comment posted").toString();
        return ResponseEntity.status(201).body(response);
    }
}
