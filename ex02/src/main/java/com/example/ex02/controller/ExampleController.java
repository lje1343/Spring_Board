package com.example.ex02.controller;

import com.example.ex02.domain.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

@Controller
@RequestMapping("/ex/*")
@Slf4j
public class ExampleController {

    // value에 경로를 작성하고, method에 호출할 서블릿 메소드를 설정한다.
    @RequestMapping(value = "/example", method = {RequestMethod.GET, RequestMethod.POST})
    public void ex01(){
        log.info("ex01.........");
    }

    @GetMapping("")
    public void ex02(){
        log.info("ex02.........");
    }

    @GetMapping("/ex03")
    public String ex03(ExampleVO exampleVO){ // void대신 String을 쓰고 / ExampleVO -> 여러개를 입력받을 수 있다.
        log.info("============================");
        log.info(exampleVO.toString());
        log.info("============================");
        return "ex03"; // 원하는 url을 써준다.
    }

    @GetMapping("/ex04")
    public String ex04(TaskVO taskVO){
        log.info("============================");
        log.info(taskVO.toString());
        log.info("============================");
        return "ex04";
    }

    // 아이디와 비번을 입력받은후 관리자/일반 페이지로 이동
    // 메소드는 리턴타입을 void로 선언한다.
    // localhost/ex/login
    // action : ex/login -> ex/ex/login
    // action : ex/login -> ex/login
    @GetMapping("/login")
    public void login(){}

    @PostMapping("/login")
    public String login(UserVO userVO){
        String res = "";

        if(userVO.getUserId().equals("admin")){
            res = "/admin";
        } else{
            res = "/user";
        }
        return res;
    }

//    실습
//    이름을 입력하고 출근 또는 퇴근 버튼을 클릭한다.
//    출근 시간은 09:00이며, 퇴근 시간 18:00이다.
//    출근 버튼 클릭 시 9시가 넘으면 지각으로 처리하고,
//    퇴근 버튼 클릭 시 18시 전이라면 퇴근이 아닌 업무시간으로 처리한다.
//    자바에서 현재 시간 구하기

//     - SimpleDateFormat 생성자에 전달받은 날짜 형식을 작성한다.
//     - parse() 메소드에 작성한 형식에 맞는 문자열을 전달하면 Date타입으로 변한다.
//     - format() 메소드에 Date 타입을 전달하면 문자열로 변한다.
//    Calendar.getInstance() : 현재 시간
//    시간, 분만 가져오기 - get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE)

//    - 모든 html은 work 경로 안에 생성하며 아래와 같이 분기 별로 html문서 한 개씩 작성한다.
//    - getToWork.html 출근
//    - leaveWork.html 퇴근
//    - late.html 지각
//    - work.html 근무중

    @GetMapping("/work/checkIn")
    public void checkIn(){
    }

    // 1. Calendar.getInstance() 방식
/*    @PostMapping("/work/checkIn")
    public String checkIn(WorkVO workVO){

        Calendar cal = Calendar.getInstance(); // Calendar 메서드 선언(정수형)
        int h = cal.get(Calendar.HOUR_OF_DAY); // 현재 시간
        int m = cal.get(Calendar.MINUTE); // 현재 분
        String m2 = ""; // Calendar ->  0~9분일경우 앞에 0이 생략되므로(정수라서) 0을 추가해줄 변수 선언

        if(m<10){ // 0~9분일경우
            m2 = "0";
        }

        String check = workVO.getInfoTime(); // form으로 받아온 값(출근/퇴근)
        String res = ""; // 보내줄 html을 담아줄 변수 선언

        // 1. 문자열+정수 = 문자열
        // 2. 문자열을 다시 정수형으로
        int time = Integer.parseInt("" + h + m2 + m);

        if(check.equals("출근")){
            if(time>900){
                res = "/ex/work/late";
            } else{
                res = "/ex/work/getToWork";
            }
        } else if(check.equals("퇴근")){
            if(time>1800){
                res = "/ex/work/leaveWork";
            } else{
                res = "/ex/work/work";
            }
        }
        return res;
    }*/

    // 2. SimpleDateFormat 방식
    @PostMapping("/work/checkIn")
    public String checkIn(WorkVO workVO){

        // 입력받을 날짜형식을 정해줘서 변수에 넣어준다.
        String type = "HHmm";
        // SimpleDateFormat에 type 삽입
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        // 현재 날짜 및 시간을 모두 뽑아서 변수에 넣어준다.
        Date nowDate = new Date();
        // 정해진 sdf으로 nowDate 타입을 Date -> String으로 변환시켜준다.
        String nowtime = sdf.format(nowDate);
        log.info(nowtime);

        // html경로 조건문을 위해서 String -> int로 변환시켜준다.
        int time = Integer.parseInt(nowtime);
        log.info(String.valueOf(time));

        String check = workVO.getInfoTime(); // form으로 받아온 값(출근/퇴근)
        String res = ""; // 보내줄 html을 담아줄 변수 선언

        if(check.equals("출근")){
            if(time>900){
                res = "/ex/work/late";
            } else{
                res = "/ex/work/getToWork";
            }

        } else if(check.equals("퇴근")){
            if(time>1800){
                res = "/ex/work/leaveWork";
            } else{
                res = "/ex/work/work";
            }
        }
        return res;
    }

//    실습
//    무기를 강화하기 위해서 아래에 있는 강화 주문서를 사용할 수 있다.

//    10% 공격력 주문서 : 공격력 + 80
//    60% 공격력 주문서 : 공격력 + 40
//    100% 공격력 주문서 : 공격력 + 10

//    한 번만 강화할 수 있으며, 10% 확률로 대성공을 한다.
//    대성공 시 해당 주문서 공격력의 5배가 증가한다.

//    강화하기 버튼을 눌렀을 때 알맞는 결과를 출력한다.

//    ScrollVO 클래스 선언
//    10%, 60%, 100% 주문서의 공격력 수치를 저장한다.
//    기본 생성자를 호출 했을 떄에는 위에 작성된 공격력 수치를 기본 값으로 설정하고,
//    만약 새로운 값을 받게 되면 해당 공격력 수치로 변경되도록 생성자를 오버로딩한다.

    @GetMapping("/upgrade")
    // upgrade/form으로 이동
    public String upgrade(){
        return "upgrade/form";
    }

    @PostMapping("/upgrade")
    public String upgrade(String choice, Model model){
        log.info("*************** choice :::" + choice);
        ScrollVO scrollVO = new ScrollVO(); /* 기본생성자 불러옴 */

        int strength = 0; /* 공격력 */
        boolean check = false; /* flag -> 구분자(true/fasle) */
        switch(Integer.parseInt(choice)){
            case 0: //10%
                check = getChance(10);
                strength = scrollVO.getScroll10();
                break;
            case 1: //60%
                check = getChance(60);
                strength = scrollVO.getScroll60();
                break;
            case 2: //100%
                check = getChance(100);
                strength = scrollVO.getScroll100();
                break;
        }
        if(!check){ return "upgrade/fail";} /* check 결과가 fasle면 강화 실패 */
        if(getChance(10)){
            strength *= 5;
            model.addAttribute("strength", strength); /* model -> 해쉬맵같은 구조(키/값) */
            return "upgrade/superSuccess";
        }
        model.addAttribute("strength", strength);
        return "upgrade/success";
    }

    public boolean getChance(int rating){ /* 성공 실패 확률 정해줌 */
        Random r = new Random();
        int[] numbers = new int[10];
        int index = r.nextInt(numbers.length); /* 0~9 까지의 랜덤한 정수를 뽑아준다. */
        for (int i=0; i<rating/10; i++){
            numbers[i] = 1; /* 10 -> 당첨은 1개만 */
        }

        return numbers[index] == 1;
    }

//    실습
//    사용자가 입력한 바코드 번호에 알맞는 상품명을 전달한다.

    @GetMapping("/market")
    public String goMarket(){return "product/market";}

    @PostMapping("/check")
    public String check(String barcode, Model model){
        String productName = null;
        switch (barcode){
            case "4383927":
                productName = "오징어 땅콩";
                break;
            case "0832147":
                productName = "초코 우유";
                break;
            case "9841631":
                productName = "벌꿀 피자";
                break;
            case "5587578":
                productName = "샌드 위치";
                break;
        }
        model.addAttribute("productName", productName);
        return "product/cashier";
    }


//    실습

//    아이디 : apple
//    비밀번호 : banana
//    로그인 성공 시 apple님 환영합니다.
//    로그인 실패 시 로그인 실패

    @GetMapping("/userLogin")
    public String userLogin(){return "login/login";}

    @PostMapping("/userLogin")
    public String userLogin(UserVO userVO){
        if(userVO.getUserId().equals("apple")){
            if(userVO.getUserPw().equals("banana")){
                return "login/success";
            }
        }
        return "login/fail";
    }

//    노래방 기계 제작
//    사용자의 점수에 따른 알맞는 메세지 출력
    @GetMapping("/song")
    public String goSong(){
        return "song/songbox";
    }

    @PostMapping("/song")
    public String sendMessage(String score, Model model){
        String msg = null;
        if(Integer.parseInt(score) > 50){
            msg = "가수의 실력이군요";
        }else {
            msg = "소질이 없어요.";
        }
        model.addAttribute("msg", msg); /* 앞단에서 사용 가능 */
        return "song/result";
    }

    @GetMapping("/info")
    // 1~2개 보낼땐 -> @ModelAttribute가 좋다. 많아질땐 Model model
    // @ModelAttribute("Key") object obj
    // 전달받은 파라미터를 화면쪽으로 보낼 때 쉽고 간편하게 사용할 수 있다.
    // 여러 개의 데이터를 보낼 때에는 Model 데이터 전달자를 사용하고,
    // 2개 이하의 데이터를 보낼때는 @ModelAttribute를 사용하는 것이 좋다.
    public void getInfo(@ModelAttribute("name") String name, @ModelAttribute("age") Integer age){
    }

    @GetMapping("/datas")
    // 외부에서 여러개를 받을경우 배열 또는 ArrayList
    // 동일한 이름의 파라미터가 여러 개 들어올 때에는 배열 또는 List로 매개변수를 설정한다.
    // 이 때 동일한 이름으로 받아야 하기 때문에 @RequestParam("KEY")을 사용해서 전달받을 데이터의 KEY값을 지정해준다.
    // KEY 파라미터명이 전달되면 뒤에 있는 매개변수로 들어간다.

    public void getDatas(@RequestParam("data") ArrayList<Integer> datas){ // 파라미터명을 data로
        log.info(String.valueOf(datas.size()));
        datas.stream().map(String::valueOf).forEach(log::info);
    }

    @GetMapping("/different")
    // 파라미터 명과 매개변수 명이 다르면 직접 지정해준다.
    public void getData(@RequestParam("data") String name){
    }
}
