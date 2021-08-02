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
@Table(name = "TB_FILE_MANAGER")
public class FileManagerEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "name_display")
    private String nameDisplay;

    @Column(name = "name_file")
    private String nameFile;

    @Column(name = "path")
    private String path;

    @Column(name = "type")
    private String type;

    @Column(name = "url")
    private String url;

    @Column(name = "description")
    private String description;

    @Column(name = "seq")
    private Integer seq;

    @Column(name = "active_at")
    private Date activeAt;

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
    @JoinColumn(name="customer_id")
    private CustomerEntity customer;
}
