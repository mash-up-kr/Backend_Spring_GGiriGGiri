package mashup.ggiriggiri.gifticonstorm.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class AWSConfig{

    @Value("\${cloud.aws.credentials.access-key}")
    private val accessKey: String? = null

    @Value("\${cloud.aws.credentials.secret-key}")
    val secretKey: String? = null

    @Value("\${cloud.aws.region.static}")
    private val region: String? = null

    @Primary
    @Bean
    fun amazonS3Client(): AmazonS3Client? {
        val awsCreds = BasicAWSCredentials(accessKey, secretKey)
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(AWSStaticCredentialsProvider(awsCreds))
            .build() as AmazonS3Client
    }

}