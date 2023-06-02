package com.example.member.controller;

import com.example.member.dto.MemberDTO;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Member;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save")
    public String saveForm() {
        return "memberPages/memberSave";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "memberPages/memberLogin";
    }

    @PostMapping("/dup-check")
    public ResponseEntity emailCheck(@RequestBody MemberDTO memberDTO) {
//        memberService.findByMemberEmail(memberDTO.getMemberEmail());
//        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
        boolean result = memberService.emailCheck(memberDTO.getMemberEmail());
        if(result) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/login")
    public String loginForm(){
        return "memberPages/memberLogin";
    }

    @PostMapping("/login")
    public String memberLogin(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        boolean loginResult = memberService.login(memberDTO);
        if (loginResult) {
            session.setAttribute("loginEmail", memberDTO.getMemberEmail());
            return"memberPages/memberMain";
        } else {
            return "memberPages/memberLogin";
        }
    }

    @PostMapping("/login/axios")
    public ResponseEntity memberLoginAxios(@RequestBody MemberDTO memberDTO, HttpSession session) throws Exception {
        memberService.loginAxios(memberDTO);
        session.setAttribute("loginEmail", memberDTO.getMemberEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/")
    public String findAll(Model model) {
        List<MemberDTO> list = memberService.findAll();
        model.addAttribute("memberList", list);
        return "memberPages/memberList";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "memberPages/memberDetail";
    }

    @GetMapping("/mypage")
    public String myPage() {
        return "memberPages/memberMain";
    }

    @GetMapping("/axios/{id}")
    public ResponseEntity detailAxios(@PathVariable Long id) throws Exception {
        MemberDTO memberDTO = memberService.findById(id);
        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }

//    @GetMapping("/{id}")
//    public String detail(@PathVariable Long id, Model model) {
//        MemberDTO memberDTO = memberService.findById(id);
//        model.addAttribute("member", memberDTO);
//        return "memberPages/memberDetail";
//    }

    @GetMapping("/delete/{id}")
    public String memberDelete(@PathVariable Long id) {
        memberService.delete(id);
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("/update/{id}")
//    public String updateForm(@PathVariable Long id, Model model) {
//        MemberDTO memberDTO = memberService.findById(id);
//        model.addAttribute("member", memberDTO);
//        return "memberPages/memberUpdate";
//    }

//    @PostMapping("/update")
//    public String update(@ModelAttribute MemberDTO memberDTO) {
//        memberService.update(memberDTO);
//        return "redirect:/";
//    }

//    @PutMapping("/update/axios")
//    public ResponseEntity updateAxios(@RequestBody MemberDTO memberDTO) throws Exception {
//        System.out.println("memberDTO = " + memberDTO);
//        memberService.update(memberDTO);
//        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
//    }

    @GetMapping("/update")
    public String updateForm(HttpSession session, Model model) {
        String loginEmail = (String) session.getAttribute("loginEmail");
//        MemberDTO memberDTO = memberService.findByMemberEmail(loginEmail);
//        model.addAttribute("member", memberDTO);
        model.addAttribute("member", memberService.findByMemberEmail(loginEmail));
        return "memberPages/memberUpdate";
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
