package finance.service.mapper;

import org.modelmapper.ModelMapper;

import com.google.inject.Singleton;

import finance.domain.ExpenseCard;
import finance.service.dto.CardItem;

@Singleton
public class ExpenseCardMapper extends ModelMapper {
    public ExpenseCardMapper() {
        typeMap(ExpenseCard.class, CardItem.class).addMappings(mapper -> {
            mapper.map(ExpenseCard::getAmount, CardItem::setAmount);
            mapper.map(ExpenseCard::getDescription, CardItem::setDescription);
        });

        this.validate();
    }
}
