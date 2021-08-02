package th.co.infinitait.comvisitor.exception;

import lombok.Data;
import java.util.List;

@Data
public class Error  {
    private List<ErrorDetail> errors;
}
