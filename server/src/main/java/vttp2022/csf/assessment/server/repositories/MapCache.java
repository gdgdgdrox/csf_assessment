package vttp2022.csf.assessment.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;

@Repository
public class MapCache {
	@Autowired
    private AmazonS3 s3Client;
	// TODO Task 4
	// Use this method to retrieve the map
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// public String getMap(String restaurantId) {
	// 	GetObjectRequest getObjReq = new GetObjectRequest("gd-bucket-top-secret", restaurantId);
	// 	S3Object result = s3Client.getObject(getObjReq);
	// 	result.getObjectContent();
	// }

	// You may add other methods to this class
	public boolean imageExists(String restaurantId){
		return s3Client.doesObjectExist("gd-bucket-top-secret", restaurantId);
	}
}
