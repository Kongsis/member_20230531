package com.example.member.service;

import com.example.member.dto.MemberDTO;
import com.example.member.entity.MemberEntity;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Long save(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toSaveEntity(memberDTO);
        return memberRepository.save(memberEntity).getId();
    }

    public MemberDTO findByMemberEmail(String loginEmail) {
        // 조회를 하면서 없으면 예외처리, 있으면 MemberEntity 리턴
        MemberEntity memberEntity = memberRepository.findByMemberEmail(loginEmail)
                .orElseThrow(() -> new NoSuchElementException());
        // 있는 경우 DTO로 변환하여 컨트롤러로 리턴
        return MemberDTO.toDTO(memberEntity);
    }

    public boolean login(MemberDTO memberDTO) {
        Optional<MemberEntity> memberEntity =
                memberRepository.findByMemberEmailAndMemberPassword(memberDTO.getMemberEmail(),memberDTO.getMemberPassword());
        if(memberEntity.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public void loginAxios(MemberDTO memberDTO) {
        // chaining method (체이닝 메서드)
        memberRepository.findByMemberEmailAndMemberPassword(memberDTO.getMemberEmail(),memberDTO.getMemberPassword())
                        .orElseThrow(() -> new NoSuchElementException("이메일 또는 비밀번호가 틀립니다."));
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for(MemberEntity memberEntity : memberEntityList) {
            memberDTOList.add(MemberDTO.toDTO(memberEntity));
        }
        return memberDTOList;
    }

//    public MemberDTO findById(Long id) {
//        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
//        if(optionalMemberEntity.isPresent()) {
//            return MemberDTO.toDTO(optionalMemberEntity.get());
//        } else {
//            return null;
//        }
//    }

    public MemberDTO findById(Long id) {
        MemberEntity memberEntity = memberRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        return MemberDTO.toDTO(memberEntity);
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    public void update(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toUpdateEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public boolean emailCheck(String memberEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberEmail);
        if (optionalMemberEntity.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
