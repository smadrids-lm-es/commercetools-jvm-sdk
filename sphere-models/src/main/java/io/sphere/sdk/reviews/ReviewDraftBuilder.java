package io.sphere.sdk.reviews;

import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.*;
import io.sphere.sdk.products.ProductIdentifiable;
import io.sphere.sdk.states.State;

import javax.annotation.Nullable;
import java.util.Optional;

public class ReviewDraftBuilder extends Base implements Builder<ReviewDraft> {
    private final String productId;
    private final String customerId;
    @Nullable
    private String authorName;
    @Nullable
    private String title;
    @Nullable
    private String text;
    @Nullable
    private Double score;
    @Nullable
    private Reference<State> state;

    private ReviewDraftBuilder(final String productId, final String customerId) {
        this.productId = productId;
        this.customerId = customerId;
    }

    public static ReviewDraftBuilder of(final ProductIdentifiable product, final Identifiable<Customer> customer) {
        return of(product.getId(), customer.getId());
    }

    public static ReviewDraftBuilder of(final String productId, final String customerId) {
        return new ReviewDraftBuilder(productId, customerId);
    }

    public static ReviewDraftBuilder of(final ReviewDraft template) {
        return new ReviewDraftBuilder(template.getProductId(), template.getCustomerId())
                .authorName(template.getAuthorName()).title(template.getTitle())
                .text(template.getText()).score(template.getScore());
    }

    public ReviewDraftBuilder authorName(@Nullable final String authorName) {
        this.authorName = authorName;
        return this;
    }

    public ReviewDraftBuilder title(@Nullable final String title) {
        this.title = title;
        return this;
    }

    public ReviewDraftBuilder text(@Nullable final String text) {
        this.text = text;
        return this;
    }

    public ReviewDraftBuilder score(@Nullable final Double score) {
        this.score = score;
        return this;
    }

    public ReviewDraftBuilder state(@Nullable final Referenceable<State> state) {
        final Reference<State> stateReference = Optional.ofNullable(state).map(Referenceable::toReference).orElse(null);
        this.state = stateReference;
        return this;
    }

    @Override
    public ReviewDraft build() {
        return new ReviewDraft(productId, customerId, authorName, title, text, score, state);
    }
}