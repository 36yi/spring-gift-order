package gift.member.service;

import gift.global.exception.MemberNotFoundException;
import gift.member.dto.LoginResponse;
import gift.member.dto.MemberRequest;
import gift.member.dto.MemberResponse;
import gift.member.entity.Member;
import gift.global.exception.LoginFailedException;
import gift.global.exception.MemberAlreadyExistsException;
import gift.member.repository.MemberRepository;
import gift.global.util.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public LoginResponse register(MemberRequest request) {
        validateDuplicateEmail(request.email());

        String encodedPassword = encodePassword(request.password());
        Member member = new Member(request.email(), encodedPassword, request.role());
        memberRepository.save(member);

        String token = jwtUtil.generateToken(member);
        return new LoginResponse(token);
    }

    public LoginResponse login(MemberRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new MemberNotFoundException(request.email()));

        if (!BCrypt.checkpw(request.password(), member.getPassword())) {
            throw new LoginFailedException();
        }
        String token = jwtUtil.generateToken(member);
        return new LoginResponse(token);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));

        if (!member.getEmail().equals(request.email())) {
            validateDuplicateEmail(request.email());
        }

        String updatedPassword = request.password().isBlank()
                ? member.getPassword()
                : encodePassword(request.password());

        member.updateEmail(request.email());
        member.updatePassword(updatedPassword);
    }

    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
        memberRepository.delete(member);
    }

    public List<MemberResponse> findAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    public MemberResponse findMemberById(Long id) {
        return MemberResponse.from(memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id)));
    }

    private void validateDuplicateEmail(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(m -> {
                    throw new MemberAlreadyExistsException(email);
                });
    }

    private String encodePassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }
}