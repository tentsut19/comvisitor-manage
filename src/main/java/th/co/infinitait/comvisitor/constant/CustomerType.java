package th.co.infinitait.comvisitor.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CustomerType {
    O("เจ้าของอาคาร"),
    R("ผู้อยู่อาศัย");

    private final String description;
}
