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
public class Office implements Entity<Long> {
    public Long id;
    public String name;
    public List<Contact> contacts;

    @Override
    public Long id() {
        return id;
    }
}
