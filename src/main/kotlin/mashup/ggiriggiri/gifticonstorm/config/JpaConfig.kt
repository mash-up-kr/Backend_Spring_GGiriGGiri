package mashup.ggiriggiri.gifticonstorm.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class JpaConfig {

    @Bean
    @Primary
    fun dataSource(
        @Value("\${mysql.driver-class-name}") driverClassName: String,
        @Value("\${mysql.jdbc.url}") jdbcUrl: String,
        @Value("\${mysql.username}") username: String,
        @Value("\${mysql.password}") password: String
    ): DataSource {
        return HikariDataSource().apply {
            this.driverClassName = driverClassName
            this.jdbcUrl = jdbcUrl
            this.password = password
            this.username = username
            this.maximumPoolSize = 100
        }
    }

}