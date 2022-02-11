import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Alcohol {
    Водка(1), Пиво(2),Виски(3),Коньяк(4);
    private final int id;
}