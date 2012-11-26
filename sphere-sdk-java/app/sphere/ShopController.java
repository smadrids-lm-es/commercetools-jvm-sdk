package sphere;

import de.commercetools.sphere.client.facets.Facet;
import de.commercetools.sphere.client.facets.FacetParser;
import de.commercetools.sphere.client.facets.expressions.FacetExpression;
import de.commercetools.sphere.client.filters.Filter;
import de.commercetools.sphere.client.filters.FilterParser;
import de.commercetools.sphere.client.SearchRequest;
import play.mvc.Controller;

import de.commercetools.sphere.client.filters.expressions.FilterExpression;
import play.mvc.Http;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/** Base controller for controllers using the Sphere backend.
 *  Provides a thread-safe instance of the {@link SphereClient}. */
public class ShopController extends Controller {
    /** Singleton thread-safe instance of the Sphere client. */
    protected static final SphereClient sphere = Sphere.getClient();

    private static Http.Request currentRequest() {
        return Http.Context.current().request();
    }

    /** Creates filter expressions based on query string of the current request,
     *  ready to be passed to {@link SearchRequest#filtered}. */
    protected static List<FilterExpression> filtersForRequest(Filter filter) {
        return FilterParser.parse(currentRequest().queryString(), Collections.singletonList(filter));
    }
    /** Creates filter expressions based on query string of the current request,
     *  ready to be passed to {@link SearchRequest#filtered}. */
    protected static List<FilterExpression> filtersForRequest(Collection<Filter> filters) {
        return FilterParser.parse(currentRequest().queryString(), filters);
    }

    /** Creates facet expressions based on query string of the current request,
     *  ready to be passed to {@link SearchRequest#faceted}. */
    protected static List<FacetExpression> facetsForRequest(Facet facet) {
        return FacetParser.parse(currentRequest().queryString(), Collections.singletonList(facet));
    }
    /** Creates facet expressions based on query string of the current request,
     *  ready to be passed to {@link SearchRequest#faceted}. */
    protected static List<FacetExpression> facetsForRequest(Collection<Facet> facets) {
        return FacetParser.parse(currentRequest().queryString(), facets);
    }
}
