package farkhat.myrzabekov.shabyttan.presentation.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.annotation.StringRes
import java.util.Locale

fun Context.getStringInLocale(@StringRes resId: Int, languageCode: String): String {
    val locale = Locale(languageCode)

    val configuration = Configuration(resources.configuration)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        configuration.setLocales(LocaleList(locale))
    } else {
        configuration.locale = locale
    }

    val localizedContext = createConfigurationContext(configuration)
    val localizedResources = localizedContext.resources

    return localizedResources.getString(resId)
}
