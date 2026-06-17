package com.woojin.winfairy.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.woojin.winfairy.R
import com.woojin.winfairy.core.domain.usecase.AnalyzeAllVariablesUseCase
import com.woojin.winfairy.core.domain.usecase.GetAllRecordsWithVariablesUseCase
import com.woojin.winfairy.core.domain.usecase.GetAllUpComingGameUseCase
import com.woojin.winfairy.core.model.VariableWinRate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.util.Locale

@HiltWorker
class GameReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val getAllUpComingGameUseCase: GetAllUpComingGameUseCase,
    private val getAllRecordsWithVariablesUseCase: GetAllRecordsWithVariablesUseCase,
    private val analyzeAllVariablesUseCase: AnalyzeAllVariablesUseCase,
) : CoroutineWorker(context, params) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val tomorrow = LocalDate.now().plusDays(1).toString()
        val games = getAllUpComingGameUseCase().first()
        val tomorrowGame = games.firstOrNull { it.date == tomorrow } ?: return Result.success()

        // 분석 기반 팁 생성
        val isKorean = Locale.getDefault().language == "ko"
        val records = getAllRecordsWithVariablesUseCase().first()
        val analysis = analyzeAllVariablesUseCase(records, isKorean)
        val tip = generateTip(analysis)

        sendNotification(tomorrowGame.opponentTeam, tip)
        return Result.success()
    }

    private fun generateTip(analysisResult: List<VariableWinRate>): String {
        val categoryOpponent = if (Locale.getDefault().language == "ko") "상대팀" else "Opponent"
        val categoryStadium = if (Locale.getDefault().language == "ko") "구장" else "Stadium"

        val valid = analysisResult.filter {
            it.category != categoryOpponent && it.category != categoryStadium
        }

        if (valid.isEmpty()) {
            return listOf(
                "내일 직관 가시는군요! 오늘 컨디션 좋은 유니폼 챙기세요 ⚾",
                "내일 경기 응원 준비됐나요? 승리 기운 가득 받아오세요! 🔥",
                "직관은 역시 직접 가야 제맛이죠! 내일 화이팅이에요 💪"
            ).random()
        }

        val best = valid.first()
        val worst = valid.last()

        val tips = listOf(
            "이번엔 ${best.value} 어때요? ${best.winRate.toInt()}% 확률로 이겨요! 🏆",
            "${josa(worst.value, "은", "는")} 조심하세요. 승률이 ${worst.winRate.toInt()}%밖에 안 돼요 😢",
            "${josa(best.value, "과", "와")} 함께라면 승률 ${best.winRate.toInt()}%! 행운의 부적이네요 🍀",
            "데이터가 말해줘요. ${josa(best.value, "이", "가")} 당신의 승리 공식이에요 📊",
            "혹시 ${josa(worst.value, "을", "를")} 또...? 그땐 승률이 ${worst.winRate.toInt()}%였어요 👀"
        )
        return tips.random()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(opponent: String, tip: String) {
        val context = applicationContext
        val channelId = "game_reminder"
        val manager = context.getSystemService(NotificationManager::class.java)

        val channel = NotificationChannel(
            channelId,
            "직관 예정 알림",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("내일 vs $opponent 직관이 있어요! ⚾")
            .setContentText(tip)
            .setStyle(NotificationCompat.BigTextStyle().bigText(tip))
            .setAutoCancel(true)
            .build()

        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(1001, notification)
        }
    }
}

// 받침 있으면 true
private fun hasFinalConsonant(word: String): Boolean {
    if (word.isEmpty()) return false
    val lastChar = word.last()
    if (lastChar !in '가'..'힣') return false  // 한글 아니면 false
    return (lastChar.code - 0xAC00) % 28 != 0
}

// 받침에 따라 조사 선택
private fun josa(word: String, withFinal: String, withoutFinal: String): String {
    return word + if (hasFinalConsonant(word)) withFinal else withoutFinal
}