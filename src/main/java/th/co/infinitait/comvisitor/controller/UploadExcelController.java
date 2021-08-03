package th.co.infinitait.comvisitor.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import th.co.infinitait.comvisitor.exception.NotFoundException;
import th.co.infinitait.comvisitor.model.response.CardRegisterResponse;
import th.co.infinitait.comvisitor.service.ExcelHelperService;
import th.co.infinitait.comvisitor.service.ExcelService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UploadExcelController {

    private final ExcelService fileService;
    private final ExcelHelperService excelHelperService;

    @PostMapping(value = "/excel/upload")
    public ResponseEntity<List<CardRegisterResponse>> uploadFile(@RequestParam("file") MultipartFile file,
                                                                 @RequestParam("card_visitor_template_id") Long cardVisitorTemplateId,
                                                                 @RequestParam("sheet_name") String sheetName) {
        String message = "";
        if (excelHelperService.hasExcelFormat(file)) { // card_visitor_template_id
            try {
                return ResponseEntity.ok(fileService.load(file,cardVisitorTemplateId,sheetName));
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                throw new NotFoundException(message);
            }
        }
        message = "Please upload an excel file!";
        throw new NotFoundException(message);
    }

}
