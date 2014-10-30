package io.sphere.sdk.categories;

import java.util.Objects;
import java.util.Optional;

import io.sphere.sdk.models.Builder;
import io.sphere.sdk.models.LocalizedStrings;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.models.Referenceable;

/**
 * Creates templates for new categories.
 *
 * {@include.example example.CategoryLifecycleExample#newCategoryConstruction()}
 */
public class NewCategoryBuilder implements Builder<NewCategory> {
    private LocalizedStrings name;
    private LocalizedStrings slug;
    private Optional<LocalizedStrings> description = Optional.empty();
    private Optional<Reference<Category>> parent = Optional.empty();
    private Optional<String> orderHint = Optional.empty();
    private Optional<String> externalId = Optional.empty();

    private NewCategoryBuilder(final LocalizedStrings name, final LocalizedStrings slug) {
        this.name = name;
        this.slug = slug;
    }

    public static NewCategoryBuilder of(final LocalizedStrings name, final LocalizedStrings slug) {
        return new NewCategoryBuilder(name, slug);
    }

    public NewCategoryBuilder description(final Optional<LocalizedStrings> description) {
        this.description = description;
        return this;
    }

    public NewCategoryBuilder description(final LocalizedStrings description) {
        return description(Optional.ofNullable(description));
    }

    public NewCategoryBuilder parent(final Optional<Reference<Category>> parent) {
        this.parent = parent;
        return this;
    }

    public NewCategoryBuilder parent(final Referenceable<Category> parent) {
        return parent(Optional.ofNullable(parent.toReference()));
    }

    public NewCategoryBuilder orderHint(final Optional<String> orderHint) {
        this.orderHint = orderHint;
        return this;
    }

    public NewCategoryBuilder orderHint(final String orderHint) {
        Objects.requireNonNull(orderHint);
        return orderHint(Optional.of(orderHint));
    }
    
    public NewCategoryBuilder externalId(final Optional<String> externalId) {
        this.externalId = externalId;
        return this;
    }

    public NewCategoryBuilder externalId(final String externalId) {
        Objects.requireNonNull(externalId);
        return externalId(Optional.of(externalId));
    }

    public NewCategory build() {
        return new NewCategory(name, slug, description, parent, orderHint, externalId);
    }
}
