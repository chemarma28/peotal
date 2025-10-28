package finance.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import finance.objects.ExpenseCard;
import finance.records.Card;
import finance.records.CardFolder;

public class CardFolderFetch {
    public List<CardFolder> retrieve(int year, int month) {
        List<Card> cards = ExpenseCard.where("YEAR(card_date) = ?1 AND MONTH(card_date) = ?2", year, month)
                .stream()
                .map(c -> new Card(
                        c.getLong("id"),
                        StringUtils.isBlank(c.getString("description")) ? c.getString("name")
                                : c.getString("description"),
                        c.getBigDecimal("amount"),
                        c.getLocalDateTime("card_date").toLocalDate()))
                .toList();

        Map<LocalDate, List<Card>> groups = cards.stream()
                .collect(Collectors.groupingBy(Card::cardDate, TreeMap::new, Collectors.toList()));

        List<CardFolder> r = groups.entrySet()
                .stream()
                .map(g -> new CardFolder(g.getKey(), g.getValue(),
                        new BigDecimalSum<>(g.getValue()).resultByProperty(Card::amount)))
                .toList();

        return r;
    }
}
