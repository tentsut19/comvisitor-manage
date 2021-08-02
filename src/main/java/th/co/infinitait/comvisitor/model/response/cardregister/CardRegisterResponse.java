package th.co.infinitait.comvisitor.model.response.cardregister;

import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CardRegisterResponse {
    private Long id;
    private String code;
    private String pathQr;
    private String urlQr;
    private String firstName;
    private String lastName;
    private String carNo;
    private Date issueDate;
    private Date expiredDate;
    private Date deletedAt;
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;
}
