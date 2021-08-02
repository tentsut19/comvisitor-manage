package th.co.infinitait.comvisitor.exception;

import lombok.Data;

@Data
public class ErrorDetail  {
  private String code;
  private String message;
  private String severityLevel;
}
