package gift.service.member;

import gift.JwtProvider;
import gift.dto.member.MemberRequestDto;
import gift.dto.member.MemberResponseDto;
import gift.entity.Member;
import gift.entity.Token;
import gift.exception.AlreadyRegisterException;
import gift.exception.InvalidPasswordException;
import gift.exception.NotRegisterException;
import gift.exception.notfound.MemberNotFoundException;
import gift.repository.member.MemberJpaRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

  private final MemberJpaRepository repository;
  private final JwtProvider jwtProvider;

  public MemberServiceImpl(MemberJpaRepository repository, JwtProvider jwtProvider) {
    this.repository = repository;
    this.jwtProvider = jwtProvider;
  }

  @Transactional
  public Token register(MemberRequestDto requestDto) {
    if (repository.findByEmail(requestDto.getEmail()).isPresent()) {
      throw new AlreadyRegisterException("이미 가입된 이메일입니다");
    }
    Member member = repository.save(new Member(requestDto.getEmail(), requestDto.getPassword()));
    Token token = jwtProvider.generateToken(member);
    return token;
  }

  public Token login(MemberRequestDto requestDto) {
    Optional<Member> memberOptional = repository.findByEmail(requestDto.getEmail());

    Member memberByEmail = memberOptional.orElseThrow(
        () -> new NotRegisterException("가입되지 않은 이메일"));
    if (memberByEmail.isPasswordNotMatch(requestDto.getPassword())) {
      throw new InvalidPasswordException("비밀번호가 일치하지 않습니다");
    }
    Token token = jwtProvider.generateToken(memberByEmail);
    return token;
  }

  public List<MemberResponseDto> findAllMember() {
    List<Member> allMembers = repository.findAll();
    List<MemberResponseDto> responseDtoList = new ArrayList<>();
    for (Member member : allMembers) {
      MemberResponseDto responseDto = new MemberResponseDto(member.getId(), member.getEmail(),
          member.getPassword());
      responseDtoList.add(responseDto);
    }
    return responseDtoList;
  }

  public MemberResponseDto findMemberById(Long id) {
    return repository.findById(id)
        .map(MemberResponseDto::new)
        .orElseThrow(() -> new MemberNotFoundException("member가 없습니다"));
  }

  @Transactional
  public MemberResponseDto createMember(MemberRequestDto requestDto) {
    Member member = repository.save(
        new Member(requestDto.getEmail(), requestDto.getPassword()));
    return new MemberResponseDto(member);
  }

  @Transactional
  public MemberResponseDto updateMember(Long id, MemberRequestDto requestDto) {
    Member member = repository.findById(id)
        .orElseThrow(() -> new MemberNotFoundException("member가 없습니다"));
    member.update(requestDto.getEmail(), requestDto.getPassword());
//    repository.save(member);
    return new MemberResponseDto(id, member.getEmail(), member.getPassword());
  }

  public void deleteMember(Long id) {
    repository.deleteById(id);
  }
}
