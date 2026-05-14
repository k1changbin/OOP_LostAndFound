package com.kw.lostandfound;

import java.util.ArrayList;


// 분실/습득 아이템들을 한 곳에 모아두고 관리하는 클래스. 모든 메소드/필드가 static.
// - 부를 때는 ItemList.add(...), ItemList.getAll() 처럼 클래스 이름으로 바로 호출 ㄱㄱ.
public class ItemList {

    
    // 모든 아이템을 담는 내부 리스트.
    // private static  <- 외부에서 직접 못 건드림 - 이것이 바로 캡슐화
    // 외부에서는 아래 add/remove/getAll 메소드를 통해서만 접근 가능함.
    // ArrayList<Item>: 제네릭으로 Item 타입만 들어가는 동적 배열임. 정적 배열은 크기 고정이라 불편해서 ArrayList 씀.
    private static ArrayList<Item> items = new ArrayList<Item>();


    // [아이템 추가]
    // 부르는 위치: Main.registerLost / registerFound -> 새 아이템 만들고 나서 바로 호출됨.
    public static void add(Item item) {
        items.add(item);    // ArrayList의 add를 그대로 호출 (맨 뒤에 붙음).
    }


    // [아이템 제거]
    // 부르는 위치: Main.deleteItem() -> 비밀번호 인증 통과한 다음 삭제할 때.
    // 같은 거인지 비교할 때 .equals()
    public static void remove(Item item) {
        items.remove(item);
    }


    // [저장된 아이템들을 다 가져오기]
    // 주의: 콜바이 레퍼런스? 중임. 수정하면 수정됨.!!!!!! 주의!!
    public static ArrayList<Item> getAll() {
        return items;
    }
}
