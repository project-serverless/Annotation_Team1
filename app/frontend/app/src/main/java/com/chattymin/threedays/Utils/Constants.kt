package com.dongminpark.threedays.Utils

object Constants {
    const val TAG: String = "로그"
}

enum class RESPONSE_STATE {
    OKAY,
    FAIL
}

object API{
    const val BASE_URL = "https://kj1ikkvxbf.execute-api.ap-northeast-2.amazonaws.com/dev/"

    // SingUp
    const val SIGNUP = "/signup"
    const val CONFIRM = "/confirm"
    const val LOGIN = "/login"

}

object MESSAGE{
    const val ERROR = "오류가 발생했습니다. 다시 시도해주세요"
    const val SAVE = "저장이 완료되었습니다!"
    const val DELETE = "삭제가 완료되었습니다!"
}