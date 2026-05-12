package com.woojin.winfairy.core.ui.preview

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

// 1. 작은 폰 (4~5인치대 구형 기기 기준)
@Preview(name = "1. Small Phone", device = Devices.NEXUS_5, showBackground = true, apiLevel = 34, locale = "ko")
// 2. 중간 폰 (가장 대중적인 6인치대 스마트폰 기준)
@Preview(name = "2. Medium Phone", device = Devices.PIXEL_5, showBackground = true, apiLevel = 34, locale = "ko")
// 3. 큰 폰 (Pro Max, Ultra 등 대화면 스마트폰 기준)
@Preview(name = "3. Large Phone", device = Devices.PIXEL_6_PRO, showBackground = true, apiLevel = 34, locale = "ko")
// 4. 폴더블 닫힌 상태 (폭이 좁고 긴 커버 스크린 비율)
@Preview(name = "4. Foldable (Closed)", device = "spec:width=320dp,height=900dp,dpi=420", showBackground = true, apiLevel = 34, locale = "ko")
// 5. 폴더블 열린 상태 (내부 메인 스크린)
@Preview(name = "5. Foldable (Open)", device = Devices.FOLDABLE, showBackground = true, apiLevel = 34, locale = "ko")
annotation class CustomDevicePreviews