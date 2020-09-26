package app.leno.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

data class ModelData(

    var id: String? = null,
    var date: String? = null,
    var title: String? = null,
    var text: String? = null,
    var imageUrl: String? = null,
    var created: Timestamp? = null,
    var type: Long? = 0

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readValue(Long::class.java.classLoader) as? Long
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(date)
        parcel.writeString(title)
        parcel.writeString(text)
        parcel.writeString(imageUrl)
        parcel.writeParcelable(created, flags)
        parcel.writeValue(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelData> {
        override fun createFromParcel(parcel: Parcel): ModelData {
            return ModelData(parcel)
        }

        override fun newArray(size: Int): Array<ModelData?> {
            return arrayOfNulls(size)
        }
    }
}