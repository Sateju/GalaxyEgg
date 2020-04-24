package lincete.galaxyegg.utils

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, message, duration).show()