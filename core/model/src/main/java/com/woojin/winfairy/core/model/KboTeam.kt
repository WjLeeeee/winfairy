package com.woojin.winfairy.core.model

enum class KboTeam(
    val teamName: String,
    val subName: String,
    val teamNameEn: String,
    val subNameEn: String,
    val stadium: String,
    val stadiumEn: String,
) {
    DooSan("두산", "베어스", "Doosan", "Bears", "잠실야구장(두산)", "Jamsil Stadium(Doosan)"),
    Lg("LG", "트윈스", "LG", "Twins", "잠실야구장(LG)", "Jamsil Stadium(LG)"),
    Kiwoom("키움", "히어로즈", "Kiwoom", "Heroes", "고척스카이돔", "Gocheok Sky Dome"),
    Kt("KT", "위즈", "KT", "Wiz", "수원KT위즈파크", "Suwon KT Wiz Park"),
    Ssg("SSG", "랜더스", "SSG", "Landers", "인천SSG랜더스필드", "Incheon SSG Landers Field"),
    Kia("KIA", "타이거즈", "KIA", "Tigers", "광주기아챔피언스필드", "Gwangju KIA Champions Field"),
    HanWha("한화", "이글스", "Hanwha", "Eagles", "한화생명볼파크", "Hanwha Life Ball Park"),
    Nc("NC", "다이노스", "NC", "Dinos", "창원NC파크", "Changwon NC Park"),
    Lotte("롯데", "자이언츠", "Lotte", "Giants", "사직종합운동장", "Sajik Stadium"),
    Samsung("삼성", "라이온즈", "Samsung", "Lions", "대구삼성라이온즈파크", "Daegu Samsung Lions Park"),
}