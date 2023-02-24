package vttp2022.csf.assessment.server.repositories;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Repository
public class S3Repository {

    @Autowired
    private AmazonS3 s3Client;

    public String storeImageInS3(byte[] imageBytes, String key){
        ObjectMetadata objMetaData = new ObjectMetadata();
        objMetaData.setContentType("image/png");
        String imageUrl = "";
        InputStream imageIS = new ByteArrayInputStream(imageBytes);
        try {
            PutObjectRequest putObjReq = new PutObjectRequest("gd-bucket-top-secret", key, imageIS, objMetaData );
            putObjReq.withCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(putObjReq);
            imageUrl = "https://gd-bucket-top-secret.sgp1.digitaloceanspaces.com/%s".formatted(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageUrl;
    } 
}
