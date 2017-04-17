package com.xinguang.tubobo.impl.aliyunoss;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.hzmux.hzcms.common.config.Global;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.Encodes;

/**
 * This sample demonstrates how to upload/download an object to/from 
 * Aliyun OSS using the OSS SDK for Java.
 */
public class AliyunOss {
    
	protected static Logger logger = LoggerFactory.getLogger(AliyunOss.class);
	
    private static final String endpoint;
    private static final String accessKeyId;
    private static final String accessKeySecret;
    private static final String bucketName;
    private static Map<String, String> headers = new LinkedHashMap<String, String>();
    
    private static OSSClient ossClient;
    
    static{

        endpoint = Global.getConfig("aliyun.oss.endpoint");
        accessKeyId = Global.getConfig("aliyun.oss.accessKeyId");
        accessKeySecret = Global.getConfig("aliyun.oss.accessKeySecret");
        bucketName = Global.getConfig("aliyun.oss.bucket.name");

    	ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
     	if (!ossClient.doesBucketExist(bucketName)) {
            CreateBucketRequest createBucketRequest= new CreateBucketRequest(bucketName);
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            ossClient.createBucket(createBucketRequest);
            logger.info("Creating bucket " + bucketName + "\n");
        }
     	headers.put("Content-Type", "image/png");
     	headers.put("Expires", String.valueOf(DateUtils.addYears(new Date(), 100).getTime()));
    }
    
    /**
     * 上传图片到aliyun oss
     */
    public static String uploadPhoto(String key,String picBase64){
    	String photoUrl = "";
    	byte[] data = Encodes.decodeBase64(picBase64);
    	key = key + "_" + DateFormatUtils.format(new Date(),"yyyyMMddHHmmssss");
		try {
			PutObjectRequest request = new PutObjectRequest(bucketName, key, new ByteArrayInputStream(data));
			request.setHeaders(headers);
			ossClient.putObject(request);
			photoUrl = bucketName+"."+endpoint+"/"+key;
			logger.info("upload a photo to aliyun OSS, photoUrl: {}",photoUrl);
		} catch (OSSException oe) {
            logger.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            logger.error("Error Message: " + oe.getErrorCode());
            logger.error("Error Code:       " + oe.getErrorCode());
            logger.error("Request ID:      " + oe.getRequestId());
            logger.error("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            logger.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            logger.error("Error Message: " + ce.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
    	return photoUrl;
    }
    
    public static void main(String[] args) throws IOException {
    	File file = new File("D:\\oU_Young\\icon.png");
    	
    	byte[] buffer = null;  
        try {  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        
    	String picBase64 = Encodes.encodeBase64(buffer);
//    	System.out.println(picBase64);
    	String url = uploadPhoto("young", picBase64);
    	System.out.println("url:" + url);
            /*
             * List the buckets in your account
             */
//            System.out.println("Listing buckets");
            
//            ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
//            listBucketsRequest.setMaxKeys(500);
//            
//            for (Bucket bucket : ossClient.listBuckets()) {
//                System.out.println(" - " + bucket.getName());
//            }
//            System.out.println();
            
            /*
             * Upload an object to your bucket
             */
//            System.out.println("Uploading a new object to OSS from a file\n");
//            ossClient.putObject(new PutObjectRequest(bucketName, "20161118_young", createSampleFile()));
            
            /*
             * Determine whether an object residents in your bucket
             */
//            boolean exists = ossClient.doesObjectExist(bucketName, "20161118_young");
//            System.out.println("Does object " + bucketName + " exist? " + exists + "\n");
            
//            ossClient.setObjectAcl(bucketName, "20161118_young", CannedAccessControlList.PublicRead);
//            ossClient.setObjectAcl(bucketName, "20161118_young", CannedAccessControlList.Default);
            
//            ObjectAcl objectAcl = ossClient.getObjectAcl(bucketName, "20161118_young");
//            System.out.println("ACL:" + objectAcl.getPermission().toString());
            
            /*
             * Download an object from your bucket
             */
//            System.out.println("Downloading an object");
//            OSSObject object = ossClient.getObject(bucketName, "20161118_young");
//            System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
//            displayTextInputStream(object.getObjectContent());

            /*
             * List objects in your bucket by prefix
             */
//            System.out.println("Listing objects");
//            ObjectListing objectListing = ossClient.listObjects(bucketName, "2016");
//            for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
//                System.out.println(" - " + objectSummary.getKey() + "  " +
//                                   "(size = " + objectSummary.getSize() + ")");
//            }
//            System.out.println();

            /*
             * Delete an object
             */
//            System.out.println("Deleting an object\n");
//            ossClient.deleteObject(bucketName, "20161118_young");
//    	  try {
    	
//        } catch (OSSException oe) {
//            System.out.println("Caught an OSSException, which means your request made it to OSS, "
//                    + "but was rejected with an error response for some reason.");
//            System.out.println("Error Message: " + oe.getErrorCode());
//            System.out.println("Error Code:       " + oe.getErrorCode());
//            System.out.println("Request ID:      " + oe.getRequestId());
//            System.out.println("Host ID:           " + oe.getHostId());
//        } catch (ClientException ce) {
//            System.out.println("Caught an ClientException, which means the client encountered "
//                    + "a serious internal problem while trying to communicate with OSS, "
//                    + "such as not being able to access the network.");
//            System.out.println("Error Message: " + ce.getMessage());
//        } finally {
//            /*
//             * Do not forget to shut down the client finally to release all allocated resources.
//             */
//            ossClient.shutdown();
//        }
    }
    
}
