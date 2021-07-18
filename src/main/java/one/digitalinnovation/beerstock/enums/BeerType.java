package one.digitalinnovation.beerstock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BeerType {
    LAGER(description: "Lager"),
    MALZBIER(description: "Malzbier"),
    WITBIER(description: "Witbier"),
    WEISS(description: "Weiss"),
    ALE(description: "Ale"),
    IPA(description: "IPA"),
    STOUT(description: "Stout");

    private static String description;
    private final String description;


}
