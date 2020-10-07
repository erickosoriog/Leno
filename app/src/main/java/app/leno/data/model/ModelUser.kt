package app.leno.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

data class ModelUser(
    var id: String? = null,
    var username: String? = null,
    var email: String? = null,
    var registered: Timestamp? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Timestamp::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(username)
        parcel.writeString(email)
        parcel.writeParcelable(registered, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelUser> {
        override fun createFromParcel(parcel: Parcel): ModelUser {
            return ModelUser(parcel)
        }

        override fun newArray(size: Int): Array<ModelUser?> {
            return arrayOfNulls(size)
        }
    }
}