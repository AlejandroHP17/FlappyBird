package pelkidev.com.mx.flappybird.presentation

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import pelkidev.com.mx.flappybird.R

class FlappySounds(context: Context) {
    private val pool = SoundPool.Builder()
        .setMaxStreams(4)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build(),
        )
        .build()

    private val wing = pool.load(context, R.raw.flappybird_sfx_wing, 1)
    private val hit = pool.load(context, R.raw.flappybird_sfx_hit, 1)
    private val die = pool.load(context, R.raw.flappybird_sfx_die, 1)
    private val point = pool.load(context, R.raw.flappybird_sfx_point, 1)

    fun playWing() = pool.play(wing, 1f, 1f, 1, 0, 1f)
    fun playHit() = pool.play(hit, 1f, 1f, 1, 0, 1f)
    fun playDie() = pool.play(die, 1f, 1f, 1, 0, 1f)
    fun playPoint() = pool.play(point, 1f, 1f, 1, 0, 1f)

    fun release() = pool.release()
}
