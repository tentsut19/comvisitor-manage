package th.co.infinitait.comvisitor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import th.co.infinitait.comvisitor.model.request.CardRegisterRequest;
import th.co.infinitait.comvisitor.util.DoubleUtil;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExcelHelperService {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public List<CardRegisterRequest> excelToMap(InputStream is, String sheetName) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(sheetName);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            Iterator<Row> rows = sheet.iterator();

            List<CardRegisterRequest> cardRegisterRequestList = new ArrayList<>();

            boolean isHeader = true;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                CardRegisterRequest cardRegisterRequest = new CardRegisterRequest();

                CellReference cr = new CellReference("A"); // รหัสบัตร
                Cell cell = currentRow.getCell(cr.getCol());
                CellValue cellValue = evaluator.evaluate(cell);
                String code = getValueString(cellValue);
                cardRegisterRequest.setCode(code);

                if(StringUtils.isEmpty(code)){
                    continue;
                }

                cr = new CellReference("B"); // ทะเบียนรถ
                cell = currentRow.getCell(cr.getCol());
                cellValue = evaluator.evaluate(cell);
                String carNo = getValueString(cellValue);
                cardRegisterRequest.setCarNo(carNo);

                cr = new CellReference("C"); // ชื่อ
                cell = currentRow.getCell(cr.getCol());
                cellValue = evaluator.evaluate(cell);
                String firstName = getValueString(cellValue);
                cardRegisterRequest.setFirstName(firstName);

                cr = new CellReference("D"); // นามสกุล
                cell = currentRow.getCell(cr.getCol());
                cellValue = evaluator.evaluate(cell);
                String lastName = getValueString(cellValue);
                cardRegisterRequest.setLastName(lastName);

                cr = new CellReference("E"); // วันที่ออกบัตร
                cell = currentRow.getCell(cr.getCol());
                cardRegisterRequest.setIssueDate(cell.getDateCellValue());

                cr = new CellReference("F"); // วันหมดอายุ
                cell = currentRow.getCell(cr.getCol());
                cardRegisterRequest.setExpiredDate(cell.getDateCellValue());

                cr = new CellReference("G"); // จากบริษัท
                cell = currentRow.getCell(cr.getCol());
                cellValue = evaluator.evaluate(cell);
                String companyContract = getValueString(cellValue);
                cardRegisterRequest.setCompanyContract(companyContract);

                cr = new CellReference("H"); // แผนกที่ติดต่อ
                cell = currentRow.getCell(cr.getCol());
                cellValue = evaluator.evaluate(cell);
                String department = getValueString(cellValue);
                cardRegisterRequest.setDepartment(department);

                cr = new CellReference("I"); // ประเภทบัตร
                cell = currentRow.getCell(cr.getCol());
                cellValue = evaluator.evaluate(cell);
                String cardType = getValueString(cellValue);
                cardRegisterRequest.setCardType(cardType);

                cardRegisterRequestList.add(cardRegisterRequest);
            }

            workbook.close();
            return cardRegisterRequestList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }


    public static String getValueString(CellValue cell){
        String result = "";
        if(cell == null){
            return result;
        }
        CellType type = cell.getCellType();
        switch (type) {
            case STRING:
                result = cell.getStringValue();
                break;
            case NUMERIC:
                result = DoubleUtil.toString1DecimalFormat(cell.getNumberValue());
                break;
            case BOOLEAN:
                result = cell.getBooleanValue()+"";
                break;
            case BLANK:
            case ERROR:
            case FORMULA:
            case _NONE:
                break;
        }
        return result;
    }

    public static BigDecimal getValueBigDecimal(CellValue cell){
        BigDecimal result = BigDecimal.ZERO;
        if(cell == null){
            return result;
        }
        CellType type = cell.getCellType();
        switch (type) {
            case STRING:
                try {
                    result = new BigDecimal(cell.getStringValue());
                    result = result.setScale(2, BigDecimal.ROUND_HALF_UP);
                }catch (Exception e){
                    result = new BigDecimal("0.00");
                }
                break;
            case NUMERIC:
                result = BigDecimal.valueOf(cell.getNumberValue());
                result = result.setScale(2, BigDecimal.ROUND_HALF_UP);
                break;
            case BOOLEAN:
            case BLANK:
            case ERROR:
            case FORMULA:
            case _NONE:
                break;
        }
        return result;
    }

    public static long getValueLong(CellValue cell){
        long result = 0L;
        if(cell == null){
            return result;
        }
        CellType type = cell.getCellType();
        switch (type) {
            case STRING:
                result = Long.parseLong(cell.getStringValue());
                break;
            case NUMERIC:
                result = (long) cell.getNumberValue();
                break;
            case BOOLEAN:
            case BLANK:
            case ERROR:
            case FORMULA:
            case _NONE:
                break;
        }
        return result;
    }
}