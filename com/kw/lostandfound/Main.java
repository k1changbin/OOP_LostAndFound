package com.kw.lostandfound;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

// 프로그램의 시작점 (main 메서드를 가진 클래스)
// CLI 메뉴 루프를 돌면서 사용자 입력을 받아 동작 분배
public class Main {

    // 프로그램 시작 진입점
    public static void main(String[] args) {

        String stdinEnc = System.getProperty("stdin.encoding", Charset.defaultCharset().name()); //한글 인코딩 깨짐방지
        Scanner sc = new Scanner(System.in, Charset.forName(stdinEnc));

        System.out.println("===========================================");
        System.out.println("  광운대 분실물 찾기 시스템에 오신 것을 환영합니다!  ");
        System.out.println("===========================================");

        // 메인 루프 — 사용자가 0(종료) 누를 때까지 LOOP
        while (true) {
            printMenu();                    		// 선택메뉴 출력
            int choice = readInt(sc, "선택 > "); 	// 숫자 입력 (잘못된 입력은 -1 반환)

            switch (choice) {
	            case 1: registerLost(sc);   break;	//분실물 등록
	            case 2: registerFound(sc);  break;	//습득물 등록
	            case 3: showList();         break;	//등록현황 보기 (입력X)
	            case 4: showDetail(sc);     break;	//등록물 상세보기
	            case 5: runQuiz(sc);        break;	//퀴즈풀기(습득물 전용)
	            case 6: deleteItem(sc);     break;	//등록물 삭제(습득/분실)
	            case 0:
	                System.out.println("종료합니다. 안녕히 가세요!"); //프로그램 종료
	                return;
	            default:
	                System.out.println("[안내] 0~6 중에서 선택해주세요."); //오입력 방지
	        }
            
        }
    }

    // 메뉴 출력 
    private static void printMenu() {
        System.out.println();
        System.out.println("\033[0;1m――――――――――[ 메뉴 ]――――――――\033[0;0m");
        System.out.println("  1. 분실물 등록 (잃어버림)            ");
        System.out.println("  2. 습득물 등록 (찾음)               ");
        System.out.println("  3. 등록물 목록보기                  ");
        System.out.println("  4. 등록물 상세정보 (ID로 조회)        ");
        System.out.println("  5. 퀴즈 풀기 (습득물 인증)            ");
        System.out.println("  6. 항목 삭제 (비밀번호 인증)           ");
        System.out.println();
        System.out.println("  0. 종료 (자동 저장)                  ");
        System.out.println("―――――――――――――――――――――――");
    }

    
    
    /*=== Menu1 : 분실물 등록 ===*/
    //공통 입력받기 > LostItem객체 생성
    private static void registerLost(Scanner sc) {
        System.out.println("\n[분실물 등록]");

        // 공통 입력사항 받기
        CommonInput in = readCommonInput(sc);
        if (in == null) return; // 입력 도중 검증 실패 시 등록 중단

        // LostItem 객체생성 — type은 생성자 내 "LOST"로 설정 (super 호출)
        LostItem item = new LostItem(
        		in.description, 
        		in.location, 
        		in.time, 
        		in.name, 
        		in.contact);
        
        // 비밀번호 설정 (setter이용 / 삭제용)
        item.setPassword(in.password);  
        
        //리스트에 등록
        ItemList.add(item);             
        System.out.println("[완료] 분실 신고가 등록되었습니다. ID: " + item.getId());
    }

    

    /*=== Menu2 : 습득물 등록 ===*/
    // 공통 입력받기 + 분실자 인증용 퀴즈 설정 > FoundItem객체 생성
    private static void registerFound(Scanner sc) {
        System.out.println("\n[습득물 등록]");
        
        //공통 입력사항 받기
        CommonInput in = readCommonInput(sc);
        if (in == null) return;

     // LostItem 객체생성 — type은 생성자 내 "FOUND"로 설정 (super 호출)
        FoundItem item = new FoundItem(
        		in.description, 
        		in.location, 
        		in.time, 
        		in.name, 
        		in.contact);
        
        // 비밀번호 설정 (setter이용 / 삭제용)
        item.setPassword(in.password);

        // 퀴즈 입력받기 (습득물의 주인 인증용, 객관식 1문제)
        System.out.println("\n[퀴즈 입력] 물건 주인만 알 만한 질문을 만들어주세요.");
        String question = readLine(sc, "질문: ");
        
        // 퀴즈 보기옵션 4개
        String[] options = new String[4];
        for (int i = 0; i < 4; i++) {
            options[i] = readLine(sc, "보기 " + (i + 1) + ": "); //idx+1 하여 보기는 1번부터 시작
        }
        
        int correct;
        while (true) {
            correct = readInt(sc, "정답 번호(1~4): ");
            if (correct >= 1 && correct <= 4) break;
            System.out.println("1~4 사이의 숫자만 입력 가능합니다.");
        }
        
        //퀴즈등록
        item.setQuiz(question, options, correct - 1);  // -1 해서 0~3 인덱스로 저장
        ItemList.add(item);
        
        System.out.println("[완료] 습득 등록 완료. ID: " + item.getId());
    }
    

    /*=== Menu3 : 등록물 목록보기 ===*/
    // OOP 다형성 활용
    private static void showList() {
    	
    	//모든 등록물(Item) 가져오기
        ArrayList<Item> all = ItemList.getAll();
        
        //등록된 항목이 없을시 (배열길이 0일시)
        if (all.size() == 0) {
            System.out.println("\n[목록] 등록된 항목이 없습니다.");
            return;
        }
        
        System.out.println("\n[전체 목록] 총 " + all.size() + "건");
        for (Item it : all) {
        	
            // 요약하여 출력
            String typeLabel = it.getType().equals("LOST") ? "[분실]" : "[습득]";
            System.out.println("  [#" + it.getId() + "] " + typeLabel
                    + " | " + it.getDescription()
                    + " | " + it.getTime()
                    + " | " + it.getStatusLabel());
        }
    }

    /*=== Menu4 : 등록물 상세정보 ===*/
    // 사용자가 ID를 입력하면 해당 아이템의 printDetail()을 호출 
    private static void showDetail(Scanner sc) {
        Item it = pickItemById(sc, "\n[상세 보기] 조회할 ID: ");
        if (it == null) return;

        System.out.println();
        System.out.println();
        
        // Item을 Override, 분실이면 LostItem.printDetail(), 습득이면 FoundItem.printDetail()
        it.printDetail();
    }

    
    /*=== Menu5 : 퀴즈 풀기 ===*/
    private static void runQuiz(Scanner sc) {
        Item it = pickItemById(sc, "\n[퀴즈 풀기] 습득물 ID: ");
        if (it == null) return;

        // 분실 아이템이면 퀴즈 자체가 없음 — 타입 체크로 막음
        if (!it.getType().equals("FOUND")) {
            System.out.println("[안내] 분실 신고에는 퀴즈가 없습니다.");
            return;
        }
        if (it.getStatus().equals("CONTACTING")) {
            System.out.println("[안내] 이미 인증된 항목입니다. 연락처: " + it.getContactDisplay());
            return;
        }

        // 퀴즈 출력
        System.out.println("\n--- 퀴즈 ---");
        System.out.println("Q. " + it.getQuizQuestion());
        String[] opts = it.getQuizOptions();
        for (int i = 0; i < 4; i++) {
            System.out.println("  " + (i + 1) + ") " + opts[i]);
        }

        // 사용자가 정답 선택 (1~4)
        int sel;
        while (true) {
            sel = readInt(sc, "정답(1~4): ");
            if (sel >= 1 && sel <= 4) break;
            System.out.println("1~4 사이의 숫자만 입력 가능합니다.");
        }

        // 정답 비교 (내부 인덱스는 0~3 이므로 -1)
        if (it.checkQuizAnswer(sel - 1)) {
            it.setStatus("CONTACTING");      // 인증 완료 표시 > 다음부터 getContactDisplay가 공개로 동작
            System.out.println("[정답!] 등록자 연락처: " + it.getContactDisplay());
        } else {
            System.out.println("[오답] 다시 시도해보세요.");
        }
    }

    
    /*=== Menu6 : Item 삭제 ===*/
    private static void deleteItem(Scanner sc) {
        Item it = pickItemById(sc, "\n[삭제] 삭제할 ID: ");
        if (it == null) return;

        String pw = readLine(sc, "비밀번호(4자리): ");
        
        // 비교 로직 Item.checkPassword에 캡슐화 — 외부에서 직접 password 비교 X (True 혹은 False 반환)
        // 비밀번호 체크 후 !하여 틀릴 시 실패문구 출력
        if (!it.checkPassword(pw)) {
            System.out.println("[실패] 비밀번호가 일치하지 않습니다.");
            return;
        }
        
        // 비밀번호 일치 시, 아이템 삭제
        ItemList.remove(it);
        System.out.println("[완료] 삭제되었습니다.");
    }

    
    
    // === 공용 메소드들 ===
    // - 분실/습득 등록 모두에서 동일하게 다루는 6개 필드 묶음
    private static class CommonInput {
        String description;
        String location;
        String time;
        String name;
        String contact;
        String password;
    }

    
    // 분실/습득 공통 입력 받기 
    private static CommonInput readCommonInput(Scanner sc) {
        CommonInput in = new CommonInput();
        
        //항목(설명) 입력받기
        in.description = readLine(sc, "설명 (예: 검정 노트북): ");
        if (in.description.equals("")) {
            System.out.println("[실패] 설명은 비울 수 없습니다.");
            return null;
        }

        // 분실/습득장소 입력받기
        in.location = readLine(sc, "장소 (예: 한울관 1층 라운지): ");
        if (in.location.equals("")) {
            System.out.println("[실패] 장소는 비울 수 없습니다.");
            return null;
        }

        // 시간 입력받기
        String tInput = readLine(sc, "시간(yyyy-MM-dd HH:mm, 엔터=지금): ");
        // 빈 문자열(Enter)일 시, 현재시간 등록
        if (tInput.equals("")) 
        	in.time = TimeUtil.now();
        
        // 시간이 입력되었을 시, 시간형식 검증 후 저장
        else if (TimeUtil.isValid(tInput)) 
        	in.time = tInput.trim();
         
        else {
        	//입력시간 형식 검증실패 시
            System.out.println("[실패] 시간 형식이 잘못되었습니다. 예: 2026-05-11 14:30");
            return null;
        }
        
        // 등록자 입력받기
        in.name = readLine(sc, "등록자 이름 (엔터=익명): ");
        if (in.name.equals("")) 
        	in.name = "익명";
        
        // 등록자 연락처 입력받기
        in.contact = readLine(sc, "연락처(예: 010-1234-5678): ");
        if (in.contact.equals("")) {
            System.out.println("[실패] 연락처는 비울 수 없습니다.");
            return null;
        }

        // 비밀번호 — 정확히 4자리 숫자만 허용 (핀 삭제 인증용)
        in.password = readLine(sc, "비밀번호(4자리 숫자): ");
        if (!isFourDigits(in.password)) {
            System.out.println("[실패] 비밀번호는 4자리 숫자여야 합니다.");
            return null;
        }
        return in;
    }

    // ID로 아이템 찾기 — 없으면 null 반환 + 안내 메시지 출력
    // 4,5,6번 메뉴에서 공통으로 사용
    private static Item pickItemById(Scanner sc, String prompt) {
        int id = readInt(sc, prompt);
        for (Item it : ItemList.getAll()) {
            if (it.getId() == id) return it;
        }
        System.out.println("[안내] 해당 ID의 항목이 없습니다.");
        return null;
    }

    // 비밀번호 4자리 숫자 검증 
    private static boolean isFourDigits(String s) {
        if (s == null || s.length() != 4) return false;
        for (int i = 0; i < 4; i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') return false;   // 숫자 아닌 문자 발견하면 즉시 false
        }
        return true;
    }

    // 한 줄 입력받기 (앞뒤 공백 제거)
    private static String readLine(Scanner sc, String prompt) {
        System.out.print(prompt);
        if (sc.hasNextLine()) return sc.nextLine().trim();
        return "";
    }

    // 숫자 입력받기 — 파싱 실패 시 -1 반환 (메뉴 잘못 누른 것과 동일 취급)
    private static int readInt(Scanner sc, String prompt) {
        String s = readLine(sc, prompt);
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
