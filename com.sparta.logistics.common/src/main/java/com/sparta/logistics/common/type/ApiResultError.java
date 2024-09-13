package com.sparta.logistics.common.type;

public enum ApiResultError {

    NO_ERROR("0000", "OK"),

    NO_AUTH_ERROR("401", "권한이 없습니다."),

    LOGIN_ERR_NOT_MATCH_PASSWD("1401", "비밀번호가 일치하지 않습니다."),
    LOGIN_ERR_NOT_FOUND_ID("1402", "존재하지 않는 아이디입니다."),
    LOGIN_ERR_NOT_FOUND_USER("1404", "사용자 아이디 혹은 비밀번호가 틀렸습니다."),
    LOGIN_ERR_NOT_USED_USER("1405", "사용 할 수 없는 계정 입니다."),
    LOGIN_ERR_REQUIRED("1500", "로그인이 필요합니다"),

    USER_WITHDRAW_NO_AUTH("2000", "게정을 탈퇴할 권한이 없습니다."),
    USER_UPDATE_NO_AUTH("2001", "정보를 수정할 권한이 없습니다."),
    USER_UPDATE_NOT_ALLOWED("2002", "정보를 수정할 수 없는 계정입니다."),
    USER_NO_EXIST("2003", "유저가 존재하지 않습니다."),
    USER_INFO_ACCESS_DENIED("2100", "회원정보를 조회할 권한이 없습니다."),
    USER_ID_EXIST("2990", "이미 존재하는 ID 입니다."),
    DELIVERY_MANAGER_NO_EXIST("2200", "배솜당당자가 존재하지 않습니다."),


    ERROR_AI_API("3000", "AI 오류가 발생했습니다"),
    ERROR_AI_API_NO_PRODUCT("3001", "해당 상품이 존재하지 않습니다."),
    ERROR_AI_API_EXCEED_LENGTH("3002", "답변 글자수가 너무 많습니다."),


    ORDER_NO_EXIST("4000", "주문이 존재하지 않습니다."),
    ORDER_PRODUCT_NO_EXIST("4100", "주문 상품이 존재하지 않습니다."),
    ORDER_PRODUCT_INSUFFICIENT("4101", "주문 상품의 재고가 부족합니다."),
    DELIVERY_NO_EXIST("4300","배송 정보가 존재하지 않습니다."),
    DELIVERY_PATH_NO_EXIST("4400","배송 경로 정보가 존재하지 않습니다."),

    MANAGING_HUB_NO_EXIST("5000", "관리 허브가 존재하지 않습니다."),
    HUB_NO_EXIST("5001", "허브가 존재하지 않습니다."),
    HUB_SEARCH_NO_RESULT("5002", ""),


    COMPANY_NO_EXIST("6000", "업체가 존재하지 않습니다."),
    INVALID_COMPANY_TYPE("6001", "유효하지 않은 업체 유형입니다."),

    PRODUCT_NO_EXIST("7001", "상품이 존재하지 않습니다."),

    MESSAGE_NO_EXIST("8001", "메시지가 존재하지 않습니다."),

    ERROR_INVALID_LOGIN("9101", "로그인정보가 유효하지 않습니다."),

    ERROR_TOKEN_EXPIRED("9200", "만료된 토큰입니다. 다시 로그인해 주세요."),

    BAD_REQUEST("9400", "잘못된 요청입니다."),
    ERROR_ACCESS_DENIED("9403", "실행 권한이 없습니다."),
    NO_AUTH_PERMISSION_DENIED("9403", "사용 권한이 없습니다."),

    NO_AUTH_NOT_FOUND_USER("9404", "유효하지 않은 사용자 정보입니다."),

    ERROR_SERVER_ERROR("9500", "서버 오류가 발생하였습니다."),
    ERROR_CALL_API("9801", "API 호출오류."),

    ERROR_INVALID_ARGUMENT("9601", "잘못된 인자가 전달되었습니다."),  // IllegalArgumentException에 대한 에러 코드
    ERROR_INVALID_STATE("9602", "객체의 상태가 유효하지 않습니다."), // IllegalStateException에 대한 에러 코드
    ERROR_EMPTY_DATA("9603", "데이터가 존재하지 않습니다"),
    SEARCH_NO_RESULT("9604", "검색 결과가 없습니다."),

    ERROR_PARAMETERS("9901", "파라미터 오류."),
    ERROR_NOT_SUPPORTED_METHOD("9902", "지원하지 않는 Method 입니다."),
    ERROR_INTERNAL_API_PARAMETERS("9903", "내부 API 파라미터 오류."),
    ERROR_TIMEOUT("9904", "시간 초과"),
    ERROR_DEFAULT("9999", "오류가 발생하였습니다.");


    private String code;
    private String message;

    private ApiResultError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static boolean isOk(String code) {
        return ApiResultError.NO_ERROR.code.equals(code);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}

