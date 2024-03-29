package security.spring.dto;

import lombok.*;
import security.spring.entity.member.Grade;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberDto {

    @NotEmpty(message = "이름을 입력 해주세요")
    private String username;

    @NotEmpty(message = "아이디를 입력하세요")
    private String loginId;

    @NotEmpty(message = "비밀번호를 입력하세요")
    private String password;
    
    @NotEmpty(message = "비밀번호를 다시 입력해주세요")
    private String passwordRe;

    @NotEmpty(message = "이메일을 입력하세요")
    private String email;
    
    @NotEmpty(message = "전화번호를 입력하세요")
    private String phoneNumber;

    @Min(1968)
    @Max(2020)
    private int year;

    @Min(1)
    @Max(12)
    private int month;

    @Min(1)
    @Max(31)
    private int day;

    private Grade grade;

    private String zipcode;
    private String address;
    private String detailAddr;
    private String subAddr;

    private boolean verified;
    private boolean locked;
    private boolean accountCredentialsExpired;
    private int count;
}
