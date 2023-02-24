package vttp2022.csf.assessment.server;

import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.LatLng;
import vttp2022.csf.assessment.server.models.Restaurant;

public class Utils {
    
    public static JsonArray createCusinesArray(List<String> cuisines){
        JsonArrayBuilder jab = Json.createArrayBuilder();
        cuisines.forEach(c -> jab.add(c));
        return jab.build();

    }

    public static JsonObject createResponse(String key, JsonArray value){
        return Json.createObjectBuilder().add(key, value).build();
    }

    public static JsonObject createJsonResponse(String key, String message){
        return Json.createObjectBuilder().add(key, message).build();
    }

    public static JsonArray createRestaurantsArray(List<Document> docs){
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (Document d : docs){
            JsonObject jo =Json.createObjectBuilder().add("name", d.getString("name")).add("id", d.getString("restaurant_id")).build();
            jab.add(jo);
        }
        JsonArray jsonArray  = jab.build();
        // System.out.println(jsonArray);
        return jsonArray;
    }

    //Document{{cuisine=Japanese, name=Haru Sake Bar, restaurant_id=40849119, 
    //coordinates=[-73.958527, 40.772126], address=1327,3 Avenue,10021,Manhattan}
    public static Restaurant createRestaurant(Document d){
        Restaurant r = new Restaurant();
        r.setAddress(d.getString("address"));
        r.setCuisine(d.getString("cuisine"));
        r.setName(d.getString("name"));
        r.setRestaurantId(d.getString("restaurant_id"));
        System.out.println(d.get("coordinates"));
        //TO DO : Refactor this lousy way of getting coords
        String[] latLngs = d.get("coordinates").toString().split(",");
        Float longz = Float.parseFloat(latLngs[0].substring(1));
        Float latz =Float.parseFloat(latLngs[1].replace("]", "").trim());
        LatLng latLng = new LatLng();
        latLng.setLongitude(longz);
        latLng.setLatitude(latz);
        r.setCoordinates(latLng);
        System.out.println("Restaurant > " + r);
        return r;
    }

    /*
     * export interface Restaurant {
            restaurantId: string
            //changed "namd" to "name" as i assume its a typo
            name: string
            cusisine: string
            address: string
            coordinates: number[]
}
     */
    public static JsonObject fromRestarauntToJsonObject(Restaurant r){
        LatLng latLng = r.getCoordinates();
        JsonArray latLngArray = latLngArray(latLng);
        return Json.createObjectBuilder().add("name", r.getName())
                                        .add("restaurantId", r.getRestaurantId())
                                        .add("cuisine", r.getCuisine())
                                        .add("address", r.getAddress())
                                        .add("coordinates", latLngArray)
                                        .add("image", r.getMapURL())
                                        .build();
    }

    public static JsonArray latLngArray(LatLng latLng){
        return Json.createArrayBuilder().add(latLng.getLongitude()).add(latLng.getLatitude()).build();
    }

    public static Document toDocument(Comment c){
        Document doc = new Document();
        //name='gdgdgd', rating='1', restaurantId='40860020', text='hahah'
        doc.put("name", c.getName());
        doc.put("rating", c.getRating());
        doc.put("restaurantId", c.getRestaurantId());
        doc.put("text", c.getText());
        System.out.println("COMMENT DOC > " + doc);
        return doc;
    }

}
