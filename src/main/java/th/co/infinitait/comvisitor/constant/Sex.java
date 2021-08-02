package th.co.infinitait.comvisitor.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Sex {
    M("MALE"),
    F("FEMALE"),
    U("UNKNOWN");

    private final String description;
}
