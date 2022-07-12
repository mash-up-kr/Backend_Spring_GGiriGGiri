package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository

import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.Sprinkle
import org.springframework.data.jpa.repository.JpaRepository

interface SprinkleRepository: JpaRepository<Sprinkle, Long>, SprinkleRepositoryCustom