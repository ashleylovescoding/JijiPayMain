import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bill")
data class Bill(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tenantEmail: String,
    val title: String,
    val amount: Double,
    val dueDate: String,
    val isPaid: Boolean,
    val firebase_id: String? = null
)

