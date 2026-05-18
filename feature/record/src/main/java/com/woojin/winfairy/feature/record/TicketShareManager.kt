package com.woojin.winfairy.feature.record

import android.app.Activity
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.os.Build
import android.util.Log
import android.view.View
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.createBitmap
import com.kakao.sdk.template.model.Button

fun captureComposable(
    view: View,
    coordinates: LayoutCoordinates,
    onCaptured: (Bitmap) -> Unit
) {
    val bounds = coordinates.boundsInWindow()
    val bitmap = createBitmap(bounds.width.toInt(), bounds.height.toInt())
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        android.view.PixelCopy.request(
            (view.context as Activity).window,
            android.graphics.Rect(
                bounds.left.toInt(),
                bounds.top.toInt(),
                bounds.right.toInt(),
                bounds.bottom.toInt()
            ),
            bitmap,
            { copyResult ->
                if (copyResult == android.view.PixelCopy.SUCCESS) {
                    onCaptured(bitmap)
                }
            },
            Handler(Looper.getMainLooper())
        )
    }
}

fun shareTicket(
    view: View,
    coordinates: LayoutCoordinates,
    cacheDir: File,
    packageName: String,
) {
    captureComposable(view, coordinates) { bitmap ->
        val paddedBitmap = addPaddingToBitmap(bitmap, 60)
        val file = File(cacheDir, "ticket_share.png")
        FileOutputStream(file).use { out ->
            paddedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        ShareClient.instance.uploadImage(image = file) { result, error ->
            if (error != null) {
                Log.e("woojinCheck", "업로드 실패: $error")
                return@uploadImage
            }
            val imageUrl = result?.infos?.original?.url ?: return@uploadImage

            val defaultFeed = FeedTemplate(
                content = Content(
                    title = "승요판독기 직관 기록",
                    description = "내 직관 티켓을 확인해봐!",
                    imageUrl = imageUrl,
                    link = Link(
                        mobileWebUrl = "https://play.google.com/store/apps/details?id=$packageName",
                        androidExecutionParams = mapOf("screen" to "home")
                    )
                ),
                buttons = listOf(
                    Button(
                        title = "앱에서 보기",
                        link = Link(
                            mobileWebUrl = "https://play.google.com/store/apps/details?id=$packageName",
                            androidExecutionParams = mapOf("screen" to "home")
                        )
                    )
                )
            )

            val context = view.context
            if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
                ShareClient.instance.shareDefault(context, defaultFeed) { sharingResult, shareError ->
                    if (shareError != null) {
                        Log.e("woojinCheck", "공유 실패: $shareError")
                    } else {
                        context.startActivity(sharingResult!!.intent)
                    }
                }
            }
        }
    }
}

fun addPaddingToBitmap(bitmap: Bitmap, padding: Int): Bitmap {
    val newWidth = bitmap.width + padding * 2
    val newHeight = bitmap.height + padding * 2
    val paddedBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(paddedBitmap)
    canvas.drawColor(0xfff1f0eb.toInt())
    canvas.drawBitmap(bitmap, padding.toFloat(), padding.toFloat(), null)
    return paddedBitmap
}