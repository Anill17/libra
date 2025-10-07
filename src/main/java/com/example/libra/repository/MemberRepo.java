package com.example.libra.repository;

import com.example.libra.dto.MemberResponse;
import com.example.libra.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepo extends JpaRepository<Member, Long> {


    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);



}
