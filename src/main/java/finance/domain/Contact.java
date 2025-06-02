package finance.domain;

import java.util.List;

import finance.repository.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Contact implements Entity<Long> {
    public Long id;
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public Office office;
    public List<ExpenseCard> expenseCards;
    boolean isAdmin;

    @Override
    public Long id() {
        return id;
    }
}
