import com.ddd.attendance.setNamespace

plugins {
    id("attendance.android.library")
    id("attendance.android.hilt")
    id("attendance.android.datastore")
}

setNamespace("data.datastore")
