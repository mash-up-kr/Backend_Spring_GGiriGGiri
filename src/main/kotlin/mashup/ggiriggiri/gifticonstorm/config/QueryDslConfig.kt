package mashup.ggiriggiri.gifticonstorm.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager

@Configuration
class QueryDslConfig(private val entityManager: EntityManager) {

    @Bean
    fun jpaQueryFactory() : JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }

}