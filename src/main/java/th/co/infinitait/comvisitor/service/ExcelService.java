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

import java.io.IOException;
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

    public List<CardRegisterResponse> load(MultipartFile file, Long cardVisitorTemplateId, String sheetName) throws IOException {
        String formattedDateRun = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String organizationUser = "migrate-"+formattedDateRun;
        List<CardRegisterRequest> cardRegisterRequestList = excelHelperService.excelToMap(file.getInputStream(), sheetName);
        log.info("cardRegisterRequestList : {}",cardRegisterRequestList.size());
        return create(cardRegisterRequestList,organizationUser,cardVisitorTemplateId);
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
                    List<CardRegisterEntity> cardRegisterEntityList = cardRegisterRepository.findByFirstNameAndLastNameAndCustomerId(cardRegisterRequest.getFirstName(),cardRegisterRequest.getLastName(),customerId);
                    if(CollectionUtils.isEmpty(cardRegisterEntityList)){
                        CardRegisterEntity cardRegisterEntity = new CardRegisterEntity();
                        cardRegisterEntity.setCreatedBy(organizationUser);
                        cardRegisterEntity.setCreatedAt(new Date());

                        List<CardRegisterEntity> cardRegisterEntities = cardRegisterRepository.findByCodeAndCustomerId(cardRegisterRequest.getCode(), customerId);
                        if (!CollectionUtils.isEmpty(cardRegisterEntities)) {
                            for (CardRegisterEntity cardRegister : cardRegisterEntities) {
                                CardRegisterResponse cardRegisterResponse = new CardRegisterResponse();
                                cardRegisterResponse.setErrorMessage(cardRegisterRequest.getCode() + " รหัสบัตรนี้ซ้ำกับข้อมูลของคุณ " +
                                        cardRegister.getFirstName() + " " + cardRegister.getLastName());
                                cardRegisterResponse.setCode(cardRegisterRequest.getCode());
                                cardRegisterResponse.setCarNo(cardRegisterRequest.getCarNo());
                                cardRegisterResponse.setFirstName(cardRegisterRequest.getFirstName());
                                cardRegisterResponse.setLastName(cardRegisterRequest.getLastName());
                                cardRegisterResponseList.add(cardRegisterResponse);
                            }
                        }

                        CardRegisterResponse cardRegisterResponse = createOrUpdateCardRegister(cardRegisterEntity,cardRegisterRequest,customerId,organizationUser,cardVisitorTemplateId);
                        if(cardRegisterResponse != null) {
                            cardRegisterResponseList.add(cardRegisterResponse);
                        }
                    }else{
                        for(CardRegisterEntity cardRegisterEntity:cardRegisterEntityList){
                            cardRegisterEntity.setUpdatedBy(organizationUser);
                            cardRegisterEntity.setUpdatedAt(new Date());
                            CardRegisterResponse cardRegisterResponse = createOrUpdateCardRegister(cardRegisterEntity,cardRegisterRequest,customerId,organizationUser,cardVisitorTemplateId);
                            if(cardRegisterResponse != null) {
                                cardRegisterResponseList.add(cardRegisterResponse);
                            }
                        }
                    }

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

    public CardRegisterResponse createOrUpdateCardRegister(CardRegisterEntity cardRegisterEntity,
                                                                 CardRegisterRequest cardRegisterRequest,
                                                                 Long customerId,
                                                                 String organizationUser,
                                                                 Long cardVisitorTemplateId) {
        boolean isError = false;
        String errorMessage = "";
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

        cardRegisterEntity.setCode(cardRegisterRequest.getCode());
        cardRegisterEntity.setCarNo(cardRegisterRequest.getCarNo());
        cardRegisterEntity.setFirstName(cardRegisterRequest.getFirstName());
        cardRegisterEntity.setLastName(cardRegisterRequest.getLastName());
        cardRegisterEntity.setIssueDate(cardRegisterRequest.getIssueDate());
        cardRegisterEntity.setExpiredDate(cardRegisterRequest.getExpiredDate());

        Optional<CompanyContractEntity> optionalCompanyContract = companyContractRepository.findByNameAndCustomerId(cardRegisterRequest.getCompanyContract(), customerId);
        if(optionalCompanyContract.isPresent()){
            cardRegisterEntity.setCompanyContract(optionalCompanyContract.get());
        }else{
            isError = true;
            errorMessage += "\nจากบริษัท : " + cardRegisterRequest.getCompanyContract() + " ไม่มีในระบบ";
        }

        Optional<DepartmentEntity> optionalDepartment = departmentRepository.findByNameAndCustomerId(cardRegisterRequest.getDepartment(), customerId);
        if(optionalDepartment.isPresent()){
            cardRegisterEntity.setDepartment(optionalDepartment.get());
        }else{
            isError = true;
            errorMessage += "\nแผนกที่ติดต่อ : " + cardRegisterRequest.getDepartment() + " ไม่มีในระบบ";
        }

        Optional<CardTypeEntity> optionalCardType = cardTypeRepository.findByNameAndCustomerId(cardRegisterRequest.getCardType(), customerId);
        if(optionalCardType.isPresent()){
            cardRegisterEntity.setCardType(optionalCardType.get());
        }else{
            isError = true;
            errorMessage += "\nประเภทบัตร : " + cardRegisterRequest.getCardType() + " ไม่มีในระบบ";
        }

        Optional<CardVisitorTemplateEntity> optionalCardVisitorTemplate = cardVisitorTemplateRepository.findByIdAndCustomerId(cardVisitorTemplateId, customerId);
        if(optionalCardVisitorTemplate.isPresent()){
            cardRegisterEntity.setCardVisitorTemplate(optionalCardVisitorTemplate.get());
        }else{
            isError = true;
            errorMessage += "\nTemplate Register Card : " + cardVisitorTemplateId + " ไม่มีในระบบ";
        }

        if(isError){
            return setErrorMessage(cardRegisterRequest,errorMessage);
        }else{
            cardRegisterRepository.save(cardRegisterEntity);
            return null;
        }
    }

    public CardRegisterResponse setErrorMessage(CardRegisterRequest cardRegisterRequest, String errorMessage){
        CardRegisterResponse cardRegisterResponse = new CardRegisterResponse();
        cardRegisterResponse.setErrorMessage(errorMessage);
        cardRegisterResponse.setCode(cardRegisterRequest.getCode());
        cardRegisterResponse.setCarNo(cardRegisterRequest.getCarNo());
        cardRegisterResponse.setFirstName(cardRegisterRequest.getFirstName());
        cardRegisterResponse.setLastName(cardRegisterRequest.getLastName());

        return cardRegisterResponse;
    }

}
