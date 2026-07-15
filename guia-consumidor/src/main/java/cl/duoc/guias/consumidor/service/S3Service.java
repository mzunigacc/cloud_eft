package cl.duoc.guias.consumidor.service;

import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Service
public class S3Service {
    private final S3Client client;
    private final String bucket;
    public S3Service(@Value("${aws.region}") String region, @Value("${aws.s3.bucket}") String bucket) {
        this.bucket = bucket;
        this.client = S3Client.builder().region(Region.of(region)).credentialsProvider(DefaultCredentialsProvider.create()).build();
    }
    public String subir(String key, String contenido) {
        client.putObject(PutObjectRequest.builder().bucket(bucket).key(key).contentType("text/plain; charset=utf-8").build(),
                RequestBody.fromString(contenido, StandardCharsets.UTF_8));
        return key;
    }
    public String descargar(String key) {
        ResponseBytes<GetObjectResponse> bytes = client.getObjectAsBytes(GetObjectRequest.builder().bucket(bucket).key(key).build());
        return bytes.asString(StandardCharsets.UTF_8);
    }
    public void eliminar(String key) { client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build()); }
}
