package pelkidev.com.mx.flappybird.data

import android.content.Context

class HighScoreRepository(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun load(): Int = prefs.getInt(KEY, 0)

    fun save(score: Int) {
        val current = load()
        if (score > current) {
            prefs.edit().putInt(KEY, score).apply()
        }
    }

    companion object {
        private const val PREFS = "flappybird_scores"
        private const val KEY = "highscore"
    }
}
