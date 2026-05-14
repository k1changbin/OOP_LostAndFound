package com.kw.lostandfound;

//   모든 아이템의 공통 부모 클래스
// - 자식 클래스: LostItem (분실), FoundItem (습득)
// - getContactDisplay(), printDetail() 은 기본 동작을 부모가 제공하고 자식이 필요하면 @Override 로 재정의 (OOP 다형성)
// - 모든 필드 private + getter/setter 로만 접근 (OOP 캡슐화)
public class Item {

    // 필드 (모두 private — OOP 캡슐화)
    private int id;                   // 고유번호 (1번부터 증가, nextId로 관리)
    private String type;              // "LOST" 또는 "FOUND" — 이 한 글자로 분실/습득 분기
    private String description;       // 설명 (예: "검정 노트북")
    private String location;          // 장소 (예: "새빛관 1층 라운지")
    private String time;              // 분실/습득 발생 시간 ("yyyy-MM-dd HH:mm")
    private String registeredAt;      // 등록한 시각 (등록 버튼 누른 그 순간) — TimeUtil.now()로 자동등록
    private String registrantName;    // 등록자 이름 (비워두면 "익명")
    private String registrantContact; // 등록자 연락처
    private String status;            // "ACTIVE"(활성) 또는 "CONTACTING"(인계 진행 중)
    private String password;          // 4자리 숫자 비밀번호

    // 퀴즈 (FOUND — 객관식 4지선다 1문제)
    private String quizQuestion;      // 질문 문장 (예: "노트북 모서리 색은?")
    private String[] quizOptions;     // 보기 4개 (인덱스 0~3)
    private int quizCorrectIndex;     // 정답 인덱스 (0~3) 0이 1번

    // 다음 ID 값 (static 으로 관리해서 객체가 생성될 때마다 자동 증가) 1부터 시작 새 Item 만들 때마다 생성자에서 +1
    private static int nextId = 1;

    // 생성자
    // 새 Item 만들 때 필수 정보 모두 받음 (퀴즈/비번/상태는 기본값으로 시작)
    // 호출 경로: LostItem/FoundItem 생성자의 super(...)
    public Item(String type, String description,
                String location, String time,
                String registrantName, String registrantContact) {
        this.id = nextId;                          // 현재 nextId 값을 id로 사용
        nextId = nextId + 1;                       // id 번호 갱신
        this.type = type;                          // 매개변수 그대로 저장 ("LOST" / "FOUND")
        this.description = description;
        this.location = location;
        this.time = time;
        this.registeredAt = TimeUtil.now();         // 등록 시각은 "지금"으로 자동 입력
        this.registrantName = registrantName;
        this.registrantContact = registrantContact;
        this.status = "ACTIVE";                     // 처음 생성된 아이템은 무조건 활성 상태
        this.password = "";                         // 비밀번호는 빈 문자열 — 이후 setPassword()로 설정
        this.quizQuestion = "";                     // 퀴즈도 빈 상태 — FOUND면 setQuiz()로 채움
        this.quizOptions = new String[]{"", "", "", ""}; // 4칸짜리 빈 배열 (NullPointer 방지)
        this.quizCorrectIndex = 0;                  // 기본값 0
    }

    // Getter
    public int getId() { return id; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getTime() { return time; }
    public String getRegisteredAt() { return registeredAt; }
    public String getRegistrantName() { return registrantName; }
    public String getRegistrantContact() { return registrantContact; }
    public String getStatus() { return status; }
    public String getQuizQuestion() { return quizQuestion; }
    public String[] getQuizOptions() { return quizOptions; }

    // Setter
    public void setStatus(String status) { this.status = status; }       // 퀴즈 통과 시 CONTACTING으로 상태변경
    public void setPassword(String password) { this.password = password; } // 등록 직후 한 번 세팅

    // 퀴즈는 3개 값을 한 번에 묶어서 설정 (질문/보기/정답 일관성 유지)
    public void setQuiz(String question, String[] options, int correctIndex) {
        this.quizQuestion = question;
        this.quizOptions = options;
        this.quizCorrectIndex = correctIndex;
    }

    // 비밀번호 일치 확인 (Main.deleteItem에서 호출)
    // 검증 로직을 Item 안에 캡슐화 — 비밀번호 값은 외부에 노출하지 않고 비교 결과만 반환
    public boolean checkPassword(String input) {
        if (input == null) return false;             // null 방어
        return password.equals(input.trim());        // trim()으로 앞뒤 공백 무시, password 내용 비교
    }

    // 퀴즈 정답 확인 (FOUND 일 때만)
    // 사용자가 고른 인덱스(0~3)를 받아 정답과 비교 — Main.runQuiz()에서 호출
    public boolean checkQuizAnswer(int selectedIndex) {
        return selectedIndex == quizCorrectIndex;   // 단순 int 비교
    }

    // 연락처 표시 방식 — 기본은 등록자 연락처를 그대로 공개
    //  - LostItem 은 부모의 기본 동작 그대로 사용
    //  - FoundItem 은 status 가 CONTACTING 이 아니면 마스킹 문자열을 반환하도록 @Override
    public String getContactDisplay() {
        return getRegistrantContact();
    }

    public void printDetail() {
        String header = type.equals("LOST") ? "분실 신고" : "습득 등록";
        System.out.println("──────────────────────────────");
        System.out.println("[#" + getId() + "] " + header);
        System.out.println("──────────────────────────────");
        System.out.println("설명     : " + getDescription());
        System.out.println("발생 시간: " + getTime());
        System.out.println("등록 시각: " + getRegisteredAt());
        System.out.println("장소     : " + getLocation());
        System.out.println("등록자   : " + getRegistrantName());
        System.out.println("연락처   : " + getContactDisplay());
        System.out.println("상태     : " + getStatusLabel());
        System.out.println("──────────────────────────────");
    }

    // 상태 한글 표시 (UI 표시용)
    // status는 영문 코드("ACTIVE"/"CONTACTING")로 저장하고, 화면에 보여줄 때만 한글로 변환
    // 데이터 저장 형식과 UI 표시를 분리하는 패턴
    public String getStatusLabel() {
        if (status.equals("ACTIVE")) return "활성";              // 일반 상태
        if (status.equals("CONTACTING")) return "인계 진행 중";   // 퀴즈 통과해서 연락 시작된 상태
        return status;                                            // 알 수 없는 값은 원본 그대로
    }

}
