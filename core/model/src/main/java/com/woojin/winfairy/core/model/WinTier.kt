package com.woojin.winfairy.core.model
enum class WinTier(
    val tierName: String,
    val tierNameEn: String,
    val description: String,
    val descriptionEn: String,
    val minWinRate: Float,
    val maxWinRate: Float
) {
    NO_GAME(
        "직관 기록 없음",
        "No Records",
        "아직 직관 기록이 없어요.\n첫 직관을 기록해보세요!",
        "No records yet. Record your first game!",
        -1f, -1f
    ),
    DEFEAT_TOTEM(
        "패배의 토템",
        "Defeat Totem",
        "내가 오면 지는 게 아니라\n내가 와서 지는 거였어?",
        "They don't lose when I come\nThey lose because I come.",
        0f, 9f
    ),
    ANGER_INDUCER(
        "화병 유발자",
        "Rage Inducer",
        "대체 뭐가 문제인건데?",
        "What on earth is going wrong?",
        10f, 19f
    ),
    HOPE_TORTURER(
        "희망 고문의 달인",
        "Master of False Hope",
        "9회말 투아웃에 역전패하는 걸\n직관하는 확률.",
        "The odds of witnessing a comeback loss\nwith two outs in the 9th.",
        20f, 29f
    ),
    BODHISATTVA(
        "보살의 경지",
        "Zen Master",
        "지더라도 즐거웠으면 됐...\n아니, 안 즐거워요.",
        "As long as we had fun losing...\nnah, it wasn't fun.",
        30f, 39f
    ),
    NORMAL_SUPPORTER(
        "평범한 서포터",
        "Average Supporter",
        "이길 때도 있고 질 때도 있는거지.",
        "You win some, you lose some.",
        40f, 49f
    ),
    HALF_AND_HALF(
        "양념반 후라이드반",
        "Half & Half",
        "팀의 균형 수호자.",
        "Guardian of team balance.",
        50f, 59f
    ),
    FAIRY_CANDIDATE(
        "승리 요정 지망생",
        "Fairy Candidate",
        "이제 슬슬 목소리에\n힘이 실리기 시작합니다.",
        "Starting to cheer\nwith a little more confidence.",
        60f, 69f
    ),
    WIN_COLLECTOR(
        "승리 콜렉터",
        "Win Collector",
        "티켓값이 아깝지 않은\n축복받은 발걸음.",
        "Blessed footsteps worth\nevery penny of the ticket.",
        70f, 79f
    ),
    WIN_FAIRY(
        "승리요정",
        "Victory Fairy",
        "내가 나타나면 상대 팀 응원석은\n침묵에 빠집니다.",
        "When I show up\nthe opposing fans fall silent.",
        80f, 89f
    ),
    WIN_GUARANTEE(
        "승리 보증수표",
        "Win Guarantee",
        "현금보다 확실한 승리의 가치.",
        "More reliable than cash.",
        90f, 99f
    ),
    BASEBALL_GOD(
        "야구의 신",
        "God of Baseball",
        "오늘은 쉬엄쉬엄하세요.\n어차피 제가 왔으니까 이깁니다.",
        "Take it easy today.\nWe're winning anyway since I'm here.",
        100f, 100f
    );
}