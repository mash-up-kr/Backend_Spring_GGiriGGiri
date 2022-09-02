package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository

import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.Sprinkle
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface SprinkleRepository: JpaRepository<Sprinkle, Long>, SprinkleRepositoryCustom {

    fun findAllBySprinkledFalseAndSprinkleAtLessThanEqual(time: LocalDateTime): List<Sprinkle>
}