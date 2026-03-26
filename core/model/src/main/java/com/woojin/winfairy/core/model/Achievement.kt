package com.woojin.winfairy.core.model

enum class Achievement(
    val code: String,
    val nameKo: String,
    val nameEn: String,
    val descriptionKo: String,
    val descriptionEn: String,
    val category: AchievementCategory,
    val maxProgress: Int,
    val emoji: String
) {
    GAME_1("game_1", "첫 직관", "First Game", "첫 번째 직관 기록 등록", "Record your first game", AchievementCategory.ATTENDANCE, 1, "⚾"),
    GAME_3("game_3", "직관 새내기", "Rookie Fan", "직관 3회 달성", "Attend 3 games", AchievementCategory.ATTENDANCE, 3, "🎫"),
    GAME_5("game_5", "직관 입문자", "Getting Started", "직관 5회 달성", "Attend 5 games", AchievementCategory.ATTENDANCE, 5, "🎟️"),
    GAME_10("game_10", "직관 마니아", "Game Maniac", "직관 10회 달성", "Attend 10 games", AchievementCategory.ATTENDANCE, 10, "💪"),
    GAME_20("game_20", "직관 중독자", "Game Addict", "직관 20회 달성", "Attend 20 games", AchievementCategory.ATTENDANCE, 20, "🔥"),
    GAME_50("game_50", "기록왕", "Record King", "직관 50회 달성", "Attend 50 games", AchievementCategory.ATTENDANCE, 50, "👑"),

    WIN_STREAK_3("win_streak_3", "3연승", "3 Win Streak", "3연승 달성", "Achieve a 3-game win streak", AchievementCategory.STREAK, 3, "✨"),
    WIN_STREAK_5("win_streak_5", "5연승", "5 Win Streak", "5연승 달성", "Achieve a 5-game win streak", AchievementCategory.STREAK, 5, "🌟"),
    WIN_STREAK_7("win_streak_7", "7연승", "7 Win Streak", "7연승 달성", "Achieve a 7-game win streak", AchievementCategory.STREAK, 7, "💫"),
    WIN_STREAK_10("win_streak_10", "10연승 전설", "10 Win Legend", "10연승 달성", "Achieve a 10-game win streak", AchievementCategory.STREAK, 10, "🏆"),

    TIER_FAIRY_CANDIDATE("tier_fairy", "요정 지망생", "Fairy Candidate", "10경기 이상 & 요정 지망생 티어 달성", "Play 10+ games & reach Fairy Candidate tier", AchievementCategory.TIER, 1, "🌱"),
    TIER_WIN_FAIRY("tier_win_fairy", "승리요정 등극", "Victory Fairy", "10경기 이상 & 승리요정 티어 달성", "Play 10+ games & reach Victory Fairy tier", AchievementCategory.TIER, 1, "🧚"),
    TIER_WIN_GUARANTEE("tier_guarantee", "보증수표", "Win Guarantee", "10경기 이상 & 보증수표 티어 달성", "Play 10+ games & reach Win Guarantee tier", AchievementCategory.TIER, 1, "💎"),
    TIER_BASEBALL_GOD("tier_god", "야구의 신", "God of Baseball", "10경기 이상 & 야구의 신 티어 달성", "Play 10+ games & reach God of Baseball tier", AchievementCategory.TIER, 1, "⚡"),

    STADIUM_3("stadium_3", "구장 탐험가", "Stadium Explorer", "3개 구장 직관", "Visit 3 different stadiums", AchievementCategory.SPECIAL, 3, "🗺️"),
    STADIUM_5("stadium_5", "구장 순례자", "Stadium Pilgrim", "5개 구장 직관", "Visit 5 different stadiums", AchievementCategory.SPECIAL, 5, "🏟️"),
    STADIUM_ALL("stadium_all", "전국 일주", "Grand Tour", "전 구장 직관 완료", "Visit all stadiums", AchievementCategory.SPECIAL, 10, "🌏"),

    COMEBACK("comeback", "패배 극복기", "Comeback Story", "3연패 이상 후 승리", "Win after 3+ consecutive losses", AchievementCategory.SPECIAL, 1, "💥"),
    ALL_ENEMY_WIN("all_enemy_win", "전팀 킬러", "Team Killer", "모든 상대팀에게 승리", "Beat every opponent team", AchievementCategory.SPECIAL, 9, "⚔️"),
    PERFECT_MONTH("perfect_month", "이달의 요정", "Fairy of the Month", "한 달 3경기 이상 직관 후 전승", "Win all games (3+) in a month", AchievementCategory.SPECIAL, 1, "🗓️");
}