package categories;

import annotations.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import transactions.TransactionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    public Long id;
    public String name;
    public TransactionType operation;
    public String color;
    @Nullable
    public String icon;
    public Integer ordinal;
}
