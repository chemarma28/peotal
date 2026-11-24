package categories;

import annotations.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import support.ValidationGroups.Create;
import transactions.TransactionType;

@Valid
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Cloneable {

    @Null(groups = Create.class)
    @Nullable
    public Long id;

    @NotBlank
    @Size(max = 64)
    public String name;

    @NotNull
    public TransactionType operation;

    @NotNull
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$")
    public String color;

    @Pattern(regexp = "^[a-z0-9-]{1,64}$")
    @Nullable
    public String icon;

    @PositiveOrZero
    public Integer ordinal;
}
