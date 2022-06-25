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
        @Value("\${db.driver-class-name}") driverClassName: String,
        @Value("\${db.jdbc.url}") jdbcUrl: String,
        @Value("\${db.username}") username: String,
        @Value("\${db.password}") password: String
    ): DataSource {
        return HikariDataSource().apply {
            this.driverClassName = driverClassName.trim()
            this.jdbcUrl = jdbcUrl.trim()
            this.password = password.trim()
            this.username = username.trim()
        }
    }

}