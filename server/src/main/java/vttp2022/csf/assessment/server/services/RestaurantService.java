package vttp2022.csf.assessment.server.services;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.LatLng;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.repositories.MapCache;
import vttp2022.csf.assessment.server.repositories.RestaurantRepository;
import vttp2022.csf.assessment.server.repositories.S3Repository;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepo;

	@Autowired
	private MapCache mapCache;

	@Autowired
	private S3Repository s3Repo;

	// TODO Task 2 
	// Use the following method to get a list of cuisines 
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME

	//print out the cuisine names and replace any / with _
	public List<String> getCuisines() {
		//replace / with _
		List<String> cuisines = restaurantRepo.getCuisines();
		List<String> formattedCuisines = cuisines.stream().map(cuisine -> cuisine.replace("/", "_")).toList();
		return formattedCuisines;
	}

	// TODO Task 3 
	// Use the following method to get a list of restaurants by cuisine
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME

	//ASCENDING ORDER!
	public List<Document> getRestaurantsByCuisine(String cuisine) {
		//replace _ with /
		String formattedCuisineName = cuisine.replace("_", "/");
		List<Document> docs = restaurantRepo.getRestaurantsByCuisine(formattedCuisineName);
		return docs;
	}

	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public Optional<Restaurant> getRestaurant(String id) {
		Optional<Restaurant> optRest = restaurantRepo.getRestaurant(id);
		Restaurant r = optRest.get();

		boolean imageExists = mapCache.imageExists(id);

		//image does not exist in Spaces
		if (!imageExists){
			//get coords so that we can call Chuk's API
			LatLng latLng = r.getCoordinates();
			byte[] imageBytes = getMap(latLng);

			//store image in Spaces
			String imageUrl = s3Repo.storeImageInS3(imageBytes, r.getRestaurantId());

			//add imageUrl to the response
			r.setMapURL(imageUrl);

			return Optional.of(r);
			}
		else{
			//image is in Spaces. Retrieve it.
			System.out.println("Image %s exist. Retrieving image url from Spaces".formatted(id));
			String imageUrl = "https://gd-bucket-top-secret.sgp1.digitaloceanspaces.com/%s".formatted(id);
			r.setMapURL(imageUrl);
			return Optional.of(r);
		}
		
	}

	// TODO Task 5
	// Use this method to insert a comment into the restaurant database
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public void addComment(Comment comment) {
		restaurantRepo.addComment(comment);
	}
	//
	// You may add other methods to this class


	public byte[] getMap(LatLng latLng){
		String baseUrl = "http://map.chuklee.com/map";
		String fullUrl = UriComponentsBuilder.fromUriString(baseUrl)
							.queryParam("lat", latLng.getLatitude())
							.queryParam("lng", latLng.getLongitude())
							.toUriString();
	
		RestTemplate restTemp = new RestTemplate();
		ResponseEntity<byte[]> rs= restTemp.exchange(RequestEntity.get(fullUrl).accept(MediaType.IMAGE_PNG).build(), byte[].class);
		return rs.getBody();
	}
}
