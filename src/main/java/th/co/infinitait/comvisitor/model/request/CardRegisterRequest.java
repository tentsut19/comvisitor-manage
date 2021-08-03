package th.co.infinitait.comvisitor.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRegisterRequest {
    private Long id;
    private String code;
    private String carNo;
    private String firstName;
    private String lastName;
    private Date issueDate;
    private Date expiredDate;
    private String companyContract;
    private String department;
    private String cardType;
}
