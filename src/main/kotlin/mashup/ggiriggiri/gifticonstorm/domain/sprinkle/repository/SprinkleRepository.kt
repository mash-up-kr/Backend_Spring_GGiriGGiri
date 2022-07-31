package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository

import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.Sprinkle
import org.springframework.data.jpa.repository.JpaRepository

fun SprinkleRepository.findBySprinkleId(sprinkleId: Long): Sprinkle? {
    return this.findById(sprinkleId).orElse(null)
}

interface SprinkleRepository: JpaRepository<Sprinkle, Long>, SprinkleRepositoryCustom