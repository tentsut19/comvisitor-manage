package th.co.infinitait.comvisitor.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_CARD_REGISTER")
public class CardRegisterEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "path_qr")
    private String pathQr;

    @Column(name = "url_qr")
    private String urlQr;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "car_no")
    private String carNo;

    @Column(name = "issue_date")
    private Date issueDate;

    @Column(name = "expired_date")
    private Date expiredDate;

    @Column(name = "DELETED_AT")
    private Date deletedAt;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name="file_manager_id")
    private FileManagerEntity fileManager;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id")
    private CustomerEntity customer;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name="card_type_id")
    private CardTypeEntity cardType;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name="card_visitor_template_id")
    private CardVisitorTemplateEntity cardVisitorTemplate;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name="company_contract_id")
    private CompanyContractEntity companyContract;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name="department_id")
    private DepartmentEntity department;

}
