package mashup.ggiriggiri.gifticonstorm.domain.coupon

import mashup.ggiriggiri.gifticonstorm.domain.BaseEntity
import mashup.ggiriggiri.gifticonstorm.domain.entry.Entry
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.OneToMany

@Entity
class Coupon (
        val brandName: String,
        val merchandiseName: String,
        val expiredAt: LocalDateTime,
        val bburigiAt: LocalDateTime,
        val imageUrl: String,

        @Enumerated(EnumType.STRING)
        val category: Category,

        @OneToMany(mappedBy = "coupon")
        val entries: MutableList<Entry> = mutableListOf()
): BaseEntity()