package vttp2022.csf.assessment.server.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.StringOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2022.csf.assessment.server.Utils;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.Restaurant;

@Repository
public class RestaurantRepository {
	@Autowired
	private MongoTemplate mongoTemplate;

	private static final String COLLECTION_RESTAURANTS = "restaurants";
	private static final String COLLECTION_COMMENTS = "comments";

	// TODO Task 2
	// Use this method to retrive a list of cuisines from the restaurant collection
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	
	//  db.restaurants.distinct('cuisine')
	public List<String> getCuisines() {
		List<String> rs = mongoTemplate.findDistinct(new Query(), "cuisine", COLLECTION_RESTAURANTS, String.class);
		return rs;
	}

	// TODO Task 3
	// Use this method to retrive a all restaurants for a particular cuisine
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method

	//  db.restaurants.find({cuisine:'Asian'}, {_id:0, name:1, restaurant_id:1}).sort({name:1})
	public List<Document> getRestaurantsByCuisine(String cuisine) {
		Criteria criteria = Criteria.where("cuisine").is(cuisine);
		Query query = new Query(criteria);
		query.fields().exclude("_id").include("name", "restaurant_id");
		query.with(Sort.by("ASC", "name"));
		List<Document> docs = mongoTemplate.find(query, Document.class, COLLECTION_RESTAURANTS);
		// docs.forEach(d -> System.out.println(d));
		return docs;
	}

	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method

	/*
	 * db.restaurants.aggregate([
		{
			$match: {"restaurant_id":"30191841"}
		},
		{
			$project:{
			name:1, "restaurant_id":1, cuisine:1, "address.coord":1, 
			address: {
				$concat: ["$address.building", "(",", "$address.street")"]
			}
			}
		}
  ])
	 */

	//!!!  TODO : write the aggregation query with $concat !!!
	public Optional<Restaurant> getRestaurant(String id) {
		MatchOperation matchOp = Aggregation.match(Criteria.where("restaurant_id").is(id));
		ProjectionOperation projectOp = Aggregation.project("name", "restaurant_id","cuisine").andExclude("_id")
					.and("address.coord").as("coordinates")
					//building , street, zipcode, borough
					.and(StringOperators.Concat.valueOf("address.building")
					.concat(",").concatValueOf("address.street")
					.concat(",").concatValueOf("address.zipcode")
					.concat(",").concatValueOf("borough"))
					.as("address");
		Aggregation agg = Aggregation.newAggregation(matchOp, projectOp);
		System.out.println("RUNNING AGGREGATE QUERY IN MONGO");
		AggregationResults<Document> aggResults = mongoTemplate.aggregate(agg, COLLECTION_RESTAURANTS, Document.class);
		if (null == aggResults.getMappedResults().get(0)){
			return Optional.empty();
		}
		Document restaurantDetail = aggResults.getMappedResults().get(0);
		System.out.println(restaurantDetail);
		Restaurant r = Utils.createRestaurant(restaurantDetail);
		return Optional.of(r);
	}

	// TODO Task 5
	// Use this method to insert a comment into the restaurant database
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method
	//  db.comments.insertOne({name:'?', rating:?, restaurantId:"?", text:"?"})
	public void addComment(Comment comment) {
		Document doc = Utils.toDocument(comment);
		mongoTemplate.insert(doc, COLLECTION_COMMENTS);
		
	}
	
	// You may add other methods to this class

}
