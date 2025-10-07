package com.example.libra.service;
import com.example.libra.dto.MemberResponse;
import com.example.libra.dto.MemberRequest;
import com.example.libra.repository.MemberRepo;
import com.example.libra.model.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepo memberRepo;

    private MemberResponse toResponse(Member m) {
        MemberResponse res = new MemberResponse();
        res.setId(m.getId());
        res.setName(m.getName());
        res.setEmail(m.getEmail());
        res.setPhone(m.getPhone());
        res.setActive(m.isActive());
        return res;
    }

    public MemberResponse register(MemberRequest res) {
        String email = res.getEmail().toLowerCase().trim();
        if (email.isEmpty()) {
            throw new IllegalArgumentException("email is empty");
        } else if (memberRepo.existsByEmail(email)) {
            throw new RuntimeException("Email address already in use");
        } else {
            Member m = new Member();
            m.setEmail(email);
            m.setName(res.getName());
            m.setPhone(res.getPhone());
            m.setActive(true);
            m.setMemberShipDate(LocalDate.now());
            Member saved = memberRepo.save(m);
            return toResponse(m);


        }


    }


    public boolean checkMemberStatus(String email) {
        Optional<Member> m = memberRepo.findByEmail(email);

        if(m.isEmpty()){
            return false;
        }
        else{
            return m.get().isActive();
        }


    }
    public MemberResponse changeStatus(String email,boolean status) {
        Member m = memberRepo.findByEmail(email).orElseThrow( () -> new RuntimeException("member not found"));
        m.setActive(status);
        memberRepo.save(m);
        return toResponse(m);
    }

    public List<Member> findAll() {
        return memberRepo.findAll();
    }
}