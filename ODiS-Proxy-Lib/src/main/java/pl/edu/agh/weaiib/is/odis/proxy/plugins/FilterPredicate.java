package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import pl.edu.agh.weaiib.is.odis.proxy.helpers.DateHelper;

import java.util.function.Predicate;

public class FilterPredicate implements Predicate<Filter> {
    @Override
    public boolean test(Filter filter) {
        return filter != null && DateHelper.currentTimeIsBetween(filter.getFilterFrom(), filter.getFilterTo());
    }
}
