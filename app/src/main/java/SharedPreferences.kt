import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesSingleton {
    private lateinit var sPref: SharedPreferences

    private const val PREFS_NAME = "MyPref"

    fun init(context: Context) {
        sPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun read(key: String, value: String): String? {
        return sPref.getString(key, value)
    }

    fun read(key: String, value: Long): Long? {
        return sPref.getLong(key, value)
    }

    fun write(key: String, value: String) {
        val prefsEditor: SharedPreferences.Editor = sPref.edit()
        with(prefsEditor) {
            putString(key, value)
            commit()
        }
    }

    fun write(key: String, value: Long) {
        val prefsEditor: SharedPreferences.Editor = sPref.edit()
        with(prefsEditor) {
            putLong(key, value)
            commit()
        }
    }
}