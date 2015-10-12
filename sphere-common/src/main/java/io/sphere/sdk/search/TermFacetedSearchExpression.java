package io.sphere.sdk.search;

import java.util.List;

/**
 * Faceted search expressions, contains both a facet expression and a list of filter expressions.
 * Example: facet of variants.attributes.color and filtering variants.attributes.color:"green","yellow"
 * @param <T> Type of the resource for the faceted search
 */
public interface TermFacetedSearchExpression<T> extends FacetedSearchExpression<T> {

    /**
     * Returns a facet expression.
     * @return facet expression
     */
    TermFacetExpression<T> facetExpression();

    static <T> TermFacetedSearchExpression<T> of(final TermFacetExpression<T> facetExpression, final List<FilterExpression<T>> filterExpressions) {
        return new TermFacetedSearchExpressionImpl<>(facetExpression, filterExpressions);
    }
}
