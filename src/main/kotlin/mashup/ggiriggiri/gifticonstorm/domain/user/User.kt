package mashup.ggiriggiri.gifticonstorm.domain.user

import mashup.ggiriggiri.gifticonstorm.domain.BaseEntity
import mashup.ggiriggiri.gifticonstorm.domain.entry.Entry
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class User (
        @Column(unique = true)
        val inherenceId: String,

        @OneToMany(mappedBy = "user")
        val entries: MutableList<Entry> = mutableListOf()
) : BaseEntity()