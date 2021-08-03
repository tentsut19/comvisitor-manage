package th.co.infinitait.comvisitor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import th.co.infinitait.comvisitor.component.CabsatPayload;
import th.co.infinitait.comvisitor.entity.*;
import th.co.infinitait.comvisitor.model.request.CardRegisterRequest;
import th.co.infinitait.comvisitor.model.response.CardRegisterResponse;
import th.co.infinitait.comvisitor.repository.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExcelService {

    private final ExcelHelperService excelHelperService;
    private final CardRegisterRepository cardRegisterRepository;
    private final CompanyContractRepository companyContractRepository;
    private final DepartmentRepository departmentRepository;
    private final CardTypeRepository cardTypeRepository;
    private final CardVisitorTemplateRepository cardVisitorTemplateRepository;
    private final CustomerRepository customerRepository;
    private final FileManagerRepository fileManagerRepository;
    private final CabsatPayload cabsatPayload;

    public List<CardRegisterResponse> load(MultipartFile file, Long cardVisitorTemplateId, String sheetName) {
        try {
            String formattedDateRun = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String organizationUser = "migrate-"+formattedDateRun;
            List<CardRegisterRequest> cardRegisterRequestList = excelHelperService.excelToMap(file.getInputStream(), sheetName);
            log.info("cardRegisterRequestList : {}",cardRegisterRequestList.size());
            return create(cardRegisterRequestList,organizationUser,cardVisitorTemplateId);
        } catch (Exception e) {
            throw new RuntimeException("Fail to store excel data: " + e.getMessage());
        }
    }

    public List<CardRegisterResponse> create(List<CardRegisterRequest> cardRegisterRequestList, String organizationUser, Long cardVisitorTemplateId){
        List<CardRegisterResponse> cardRegisterResponseList = new ArrayList<>();
        log.info("cabsatPayload : {}",cabsatPayload.getUserId());
        if(StringUtils.isEmpty(cabsatPayload.getUserId())){
            throw new RuntimeException("cabsatPayload null");
        }
        Long customerId = Long.valueOf(cabsatPayload.getUserId());
        if(!CollectionUtils.isEmpty(cardRegisterRequestList)){
            for(CardRegisterRequest cardRegisterRequest:cardRegisterRequestList){
                try {
                    CardRegisterEntity cardRegisterEntity = new CardRegisterEntity();

                    Optional<CardRegisterEntity> optionalCardRegister = cardRegisterRepository.findByFirstNameAndLastNameAndCustomerId(cardRegisterRequest.getFirstName(),cardRegisterRequest.getLastName(),customerId);
                    if(optionalCardRegister.isPresent()){
                        cardRegisterEntity = optionalCardRegister.get();
                        cardRegisterEntity.setUpdatedAt(new Date());
                        cardRegisterEntity.setUpdatedBy(organizationUser);

                        Optional<CustomerEntity> optionalCustomer = customerRepository.findById(customerId);
                        optionalCustomer.ifPresent(cardRegisterEntity::setCustomer);
                    }else{
                        List<CardRegisterEntity> cardRegisterEntities = cardRegisterRepository.findByCodeAndCustomerId(cardRegisterRequest.getCode(),customerId);
                        if(!CollectionUtils.isEmpty(cardRegisterEntities)){
                            for(CardRegisterEntity cardRegister:cardRegisterEntities) {
                                CardRegisterResponse cardRegisterResponse = new CardRegisterResponse();
                                cardRegisterResponse.setErrorMessage(cardRegisterRequest.getCode() + " รหัสบัตรนี้ซ้ำกับข้อมูลของคุณ " +
                                        cardRegister.getFirstName() + " " + cardRegister.getLastName());
                                cardRegisterResponse.setCode(cardRegisterRequest.getCode());
                                cardRegisterResponse.setCarNo(cardRegisterRequest.getCarNo());
                                cardRegisterResponse.setFirstName(cardRegisterRequest.getFirstName());
                                cardRegisterResponse.setLastName(cardRegisterRequest.getLastName());
                                cardRegisterResponseList.add(cardRegisterResponse);
                            }
                            continue;
                        }
                        cardRegisterEntity.setCreatedAt(new Date());
                        cardRegisterEntity.setCreatedBy(organizationUser);

                        Optional<CustomerEntity> optionalCustomer = customerRepository.findById(customerId);
                        optionalCustomer.ifPresent(cardRegisterEntity::setCustomer);

                        FileManagerEntity fileManagerEntity = new FileManagerEntity();
                        fileManagerEntity.setNameDisplay("default.jpeg");
                        fileManagerEntity.setNameFile("default.jpeg");
                        fileManagerEntity.setPath("images/register_card/default/0.jpg");
                        fileManagerEntity.setSeq(0);
                        fileManagerEntity.setType("register_card");
                        fileManagerEntity.setUrl("https://comvisitor-uat-bucket.s3.ap-southeast-1.amazonaws.com/images/default/0.jpeg");
                        optionalCustomer.ifPresent(fileManagerEntity::setCustomer);
                        fileManagerEntity.setCreatedAt(new Date());
                        fileManagerEntity.setCreatedBy(organizationUser);
                        fileManagerRepository.save(fileManagerEntity);

                        cardRegisterEntity.setFileManager(fileManagerEntity);
                    }

                    cardRegisterEntity.setCode(cardRegisterRequest.getCode());
                    cardRegisterEntity.setCarNo(cardRegisterRequest.getCarNo());
                    cardRegisterEntity.setFirstName(cardRegisterRequest.getFirstName());
                    cardRegisterEntity.setLastName(cardRegisterRequest.getLastName());
                    cardRegisterEntity.setIssueDate(cardRegisterRequest.getIssueDate());
                    cardRegisterEntity.setExpiredDate(cardRegisterRequest.getExpiredDate());

                    Optional<CompanyContractEntity> optionalCompanyContract = companyContractRepository.findByNameAndCustomerId(cardRegisterRequest.getCompanyContract(),customerId);
                    optionalCompanyContract.ifPresent(cardRegisterEntity::setCompanyContract);

                    Optional<DepartmentEntity> optionalDepartment = departmentRepository.findByNameAndCustomerId(cardRegisterRequest.getDepartment(),customerId);
                    optionalDepartment.ifPresent(cardRegisterEntity::setDepartment);

                    Optional<CardTypeEntity> optionalCardType = cardTypeRepository.findByNameAndCustomerId(cardRegisterRequest.getCardType(),customerId);
                    optionalCardType.ifPresent(cardRegisterEntity::setCardType);

                    Optional<CardVisitorTemplateEntity> optionalCardVisitorTemplate = cardVisitorTemplateRepository.findByIdAndCustomerId(cardVisitorTemplateId,customerId);
                    optionalCardVisitorTemplate.ifPresent(cardRegisterEntity::setCardVisitorTemplate);

                    cardRegisterRepository.save(cardRegisterEntity);
                }catch (Exception e){
                    CardRegisterResponse cardRegisterResponse = new CardRegisterResponse();
                    cardRegisterResponse.setErrorMessage(e.getMessage());
                    cardRegisterResponse.setCode(cardRegisterRequest.getCode());
                    cardRegisterResponse.setCarNo(cardRegisterRequest.getCarNo());
                    cardRegisterResponse.setFirstName(cardRegisterRequest.getFirstName());
                    cardRegisterResponse.setLastName(cardRegisterRequest.getLastName());
                    cardRegisterResponseList.add(cardRegisterResponse);
                }
            }
        }
        log.info("fail size : {}",cardRegisterResponseList.size());
        return cardRegisterResponseList;
    }

}
