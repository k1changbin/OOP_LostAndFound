package com.kw.lostandfound;

import java.text.SimpleDateFormat;
import java.util.Date;

// 시간 문자열을 다루는 유틸리티 클래스
// SimpleDateFormat 기반의 형식 검증과 현재 시각 문자열화를 담당
public class TimeUtil {

    // 시간 형식 (앱 전체에서 이 포맷만 사용, 등록 폼/저장/검증 모두 통일)
    // "yyyy-MM-dd HH:mm" (예: 2026-05-14 12:27)
    // public static final 로 선언  어디서든 TimeUtil.FORMAT 으로 접근 + 변경 불가(상수)
    public static final String FORMAT = "yyyy-MM-dd HH:mm";

    // 현재 시각을 위 FORMAT에 맞춰 문자열로 반환
    // Item 생성자의 registeredAt 기본값, Main.readCommonInput에서 시간 입력 기본값으로 사용
    public static String now() {
        SimpleDateFormat fmt = new SimpleDateFormat(FORMAT); // FORMAT 패턴으로 생성
        return fmt.format(new Date());                       // new Date() = 현재 시각(밀리초)
                                                             // fmt.format(...) = Date 객체를 위 패턴의 문자열로 변환
    }

    // 시간 문자열의 형식이 맞는지 검사 (잘못된 형식이면 false)
    // 사용 예: Main.readCommonInput에서 사용자가 입력한 시간 문자열 검증
    public static boolean isValid(String t) {
        if (t == null) return false;                    // null 들어오면 그냥 실패 처리 (NullPointer 방지)
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(FORMAT);
            fmt.setLenient(false);                      // "2026-13-99" 같은 잘못된 날짜 방지
            fmt.parse(t.trim());                        // trim()으로 앞뒤 공백 제거 후 파싱 시도 / 형식이 안 맞으면 ParseException 발생 catch 블록으로 점프

            return true;                                // 예외 없이 끝나면 올바른 형식
        } catch (Exception e) {
            return false;                               // 어떤 예외든 일단 잘못된 형식으로 간주
        }
    }
}