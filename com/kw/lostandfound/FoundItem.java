package com.kw.lostandfound;

// 습득(FOUND) 아이템 클래스. Item을 상속받아서 습득 등록 쪽 동작을 담당함.

// [상속]
// - extends Item 으로 공통 필드/메소드를 그대로 받아옴.
// - LOST랑 FOUND가 같이 쓰는 부분은 부모인 Item에 한 번만 써두면 돼서 코드 중복이 줄어듦.

// [다형성]
// - 부모 Item의 메소드 두 개를 @Override 로 새로 써줬음.
//   1) getContactDisplay() -> status가 CONTACTING이 아니면 연락처를 가림 (본인 인증이 이 프로그램의 핵심임)
//   2) printDetail()       -> "습득 등록" 양식 + 아직 인증 전이면 안내 메시지도 같이 출력



public class FoundItem extends Item {

    // 생성자. type을 항상 "FOUND"로 고정해서 부모 생성자(super)에 넘김.
    // - 클래스 이름이 이미 "습득"이라는 뜻이라 type을 따로 안 받음.
    // - super(...)는 생성자 첫 줄 !!
    //   id 자동 부여, status="ACTIVE", password="", 퀴즈는 빈 값 등

    public FoundItem(String description,
                     String location, String time,
                     String registrantName, String registrantContact) {
        super("FOUND", description, location, time, registrantName, registrantContact);
    }

    // [재정의 1] 연락처 표시 방식
    
    // 습득물은 "본인 인증한 사람한테만 공개"가 핵심임. 규칙은 아래처럼 동작함.
    //   - status가 "CONTACTING"이면 (= 누가 퀴즈 통과한 상태)  -> 실제 연락처 공개
    //   - 그 외 ACTIVE면          (= 아직 아무도 인증 안 함)  -> 별표로 가림
    
    // @Override는 부모 메소드를 다시 쓰는 거라고 컴파일러한테 알려주는 표시임.
    //   - 이름이나 시그니처를 잘못 적으면 컴파일 에러로 잡아줘서 실수 막아줌.
    @Override
    public String getContactDisplay() {
        // getStatus()는 부모의 public getter
        if (getStatus().equals("CONTACTING")) {
            return getRegistrantContact();      // 인증 통과 -> 진짜 연락처 반환
        }
        return "***-****-****";                  // 아직 미인증 -> 가림 처리
    }

    // [재정의 2] 상세 정보 출력 양식
    // "습득 등록" 헤더 붙여서 출력하고, 퀴즈 통과 전이면 맨 아래에 안내 메시지도 한 줄 더 붙임.
    // 부모 Item.printDetail()이랑 기본 틀은 같고 헤더랑 꼬리 메시지만 다름 (다형성으로 알아서 골라짐).
    @Override
    public void printDetail() {
        System.out.println("──────────────────────────────");
        System.out.println("[#" + getId() + "] 습득 등록");
        System.out.println("──────────────────────────────");
        System.out.println("설명     : " + getDescription());
        System.out.println("발생 시간: " + getTime());
        System.out.println("등록 시각: " + getRegisteredAt());
        System.out.println("장소     : " + getLocation());
        System.out.println("등록자   : " + getRegistrantName());
        // 여기서 호출하는 getContactDisplay()는 위에 내가 새로 쓴 버전이 실행됨
        // -> 인증 됐는지에 따라 진짜 연락처 / 별표가 골라져서 나옴.
        System.out.println("연락처   : " + getContactDisplay());
        System.out.println("상태     : " + getStatusLabel());
        // 퀴즈 통과 전이면 안내 메시지 한 줄 더 추가
        if (getStatus().equals("ACTIVE")) {
            System.out.println("※ 퀴즈를 풀면 등록자 연락처가 공개됩니다.");
        }
        System.out.println("──────────────────────────────");
    }
}
