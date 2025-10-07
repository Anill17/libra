package com.example.libra.controller;

import com.example.libra.dto.MemberRequest;
import com.example.libra.dto.MemberResponse;
import com.example.libra.dto.StatusRequestDto;
import com.example.libra.model.Member;
import com.example.libra.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private MemberService memberService;

    @GetMapping("")
    public ResponseEntity<List<Member>> getAll() {
        return ResponseEntity.ok(memberService.findAll());
    }
    @GetMapping("/status")
    public ResponseEntity<Boolean> retrieve(@RequestBody StatusRequestDto body) {
        return ResponseEntity.ok(memberService.checkMemberStatus(body.getEmail()));
    }
    @PatchMapping("/{email}/state")
    public ResponseEntity<MemberResponse> updateStatus(@PathVariable String email, boolean status) {
        return ResponseEntity.ok(memberService.changeStatus(email,status));

    }
    @PostMapping("/create")
    public ResponseEntity<MemberResponse> create(@Valid @RequestBody MemberRequest member) {
        return ResponseEntity.ok(memberService.register(member));
    }
}
