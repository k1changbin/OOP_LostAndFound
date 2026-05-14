package com.kw.lostandfound;

// 분실(LOST) 아이템 클래스. Item을 상속받아서 분실 신고 쪽 동작을 담당함.

// [상속]
// - extends Item 으로 공통 필드/메소드를 그대로 받아옴.
// - LOST랑 FOUND가 같이 쓰는 부분은 부모인 Item에 한 번만 써두면 돼서 코드 중복이 줄어듦.

// [다형성]
// - 부모 Item의 메소드 두 개를 @Override 로 새로 써줬음.
//   1) getContactDisplay() -> 등록자 연락처를 그냥 그대로 보여줌 (분실자는 본인 보호 안 해도 됨)
//   2) printDetail()       -> "분실 신고" 양식으로 콘솔에 출력함.


public class LostItem extends Item {

    // 생성자. type을 항상 "LOST"로 고정해서 부모 생성자(super)에 넘김.
    // - 클래스 이름이 이미 "분실"이라는 뜻이라 type을 따로 안 받음.
    // - super(...)는 생성자 첫 줄 !!


    public LostItem(String description,
                    String location, String time,
                    String registrantName, String registrantContact) {
        super("LOST", description, location, time, registrantName, registrantContact);
    }

    // [재정의 1] 연락처 표시 방식
    //
    // 분실 신고한 사람은 따로 가릴 필요 없으니까 등록자 연락처를 그대로 돌려줌.
    // (FoundItem은 같은 메소드를 "퀴즈 통과 전이면 마스킹"으로 다르게 짜둠 -> 이게 다형성)
    //
    // @Override는 부모 메소드를 다시 쓰는 거라고 컴파일러한테 알려주는 표시임.
    //   - 이름이나 시그니처를 잘못 적으면 컴파일 에러로 잡아줘서 실수 막아줌.
    @Override
    public String getContactDisplay() {
        return getRegistrantContact();   // 부모의 getter. 분실은 그냥 공개.
    }

    // [재정의 2] 상세 정보 출력 양식
    @Override
    public void printDetail() {
        System.out.println("──────────────────────────────");
        System.out.println("[#" + getId() + "] 분실 신고");
        System.out.println("──────────────────────────────");
        System.out.println("설명     : " + getDescription());
        System.out.println("발생 시간: " + getTime());
        System.out.println("등록 시각: " + getRegisteredAt());
        System.out.println("장소     : " + getLocation());
        System.out.println("등록자   : " + getRegistrantName());
        // 여기서 호출하는 getContactDisplay()는 내가 위에 새로 쓴게 실행됨 -> 그대로 공개됨.
        System.out.println("연락처   : " + getContactDisplay());
        System.out.println("상태     : " + getStatusLabel());
        System.out.println("──────────────────────────────");
    }
}
