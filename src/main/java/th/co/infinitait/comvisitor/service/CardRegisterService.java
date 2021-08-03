package th.co.infinitait.comvisitor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import th.co.infinitait.comvisitor.entity.CardRegisterEntity;
import th.co.infinitait.comvisitor.exception.NotFoundException;
import th.co.infinitait.comvisitor.model.request.CardRegisterRequest;
import th.co.infinitait.comvisitor.model.response.CardRegisterResponse;
import th.co.infinitait.comvisitor.repository.CardRegisterRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardRegisterService {
    private final CardRegisterRepository cardRegisterRepository;

    public CardRegisterResponse createActivity(CardRegisterRequest request, String organizationUser) {
        Long activityId = create(request,organizationUser);
        return getActivityById(activityId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(CardRegisterRequest request, String organizationUser) {

        CardRegisterEntity cardRegisterEntity = CardRegisterEntity.builder()
                .firstName(request.getFirstName())
                .createdAt(new Date())
                .createdBy(organizationUser)
                .build();
        CardRegisterEntity entity = cardRegisterRepository.save(cardRegisterEntity);
        return entity.getId();
    }

    public List<CardRegisterResponse> getAll(){
        List<CardRegisterResponse> cardRegisterResponseList = new ArrayList<>();
        List<CardRegisterEntity> cardRegisterEntityList = cardRegisterRepository.findAll();
        if(!CollectionUtils.isEmpty(cardRegisterEntityList)){
            cardRegisterEntityList.forEach(cardRegisterEntity -> {
                cardRegisterResponseList.add(toActivityResponse(cardRegisterEntity));
            });
        }
        return cardRegisterResponseList;
    }

    public List<CardRegisterResponse> getByDepartmentId(Long departmentId){
        List<CardRegisterResponse> cardRegisterResponseList = new ArrayList<>();
//        List<CardRegisterEntity> cardRegisterEntityList = cardRegisterRepository.findByDepartmentId(departmentId);
//        if(!CollectionUtils.isEmpty(cardRegisterEntityList)){
//            cardRegisterEntityList.forEach(cardRegisterEntity -> {
//                cardRegisterResponseList.add(toActivityResponse(cardRegisterEntity));
//            });
//        }
        return cardRegisterResponseList;
    }

    public CardRegisterResponse getActivityById(Long cardRegisterId) {
        Optional<CardRegisterEntity> optionalActivityEntity = cardRegisterRepository.findById(cardRegisterId);
        if(!optionalActivityEntity.isPresent()){
            throw new NotFoundException(String.format("CardRegister NotFound By cardRegisterId : %1s",cardRegisterId));
        }
        return optionalActivityEntity.map((Function<CardRegisterEntity, CardRegisterResponse>) this::toActivityResponse).orElse(null);
    }

    public CardRegisterResponse toActivityResponse(CardRegisterEntity entity){

        return CardRegisterResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .createdAt(entity.getCreatedAt())
                .updatedBy(entity.getUpdatedBy())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
