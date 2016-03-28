package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import pl.edu.agh.weaiib.is.odis.proxy.helpers.DateHelper;

import java.util.function.Predicate;

/**
 * Predicate to filter out plugins that should not be
 * used - current time is outside filter time
 */
public class FilterPredicate implements Predicate<Filter> {

    /**
     * Test if current time is between filter time
     * @param filter    Filter object
     * @return          Whether this filter can be applied
     */
    @Override
    public boolean test(Filter filter) {
        return filter != null && DateHelper.currentTimeIsBetween(filter.getFilterFrom(), filter.getFilterTo());
    }
}
