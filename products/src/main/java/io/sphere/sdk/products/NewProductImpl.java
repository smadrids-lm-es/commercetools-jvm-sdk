package io.sphere.sdk.products;

import java.util.Optional;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.LocalizedStrings;
import io.sphere.sdk.models.MetaAttributes;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.producttypes.ProductType;

import java.util.List;

class NewProductImpl extends Base implements NewProduct {
    private final Reference<ProductType> productType;
    private final LocalizedStrings name;
    private final LocalizedStrings slug;
    private final Optional<LocalizedStrings> description;
    private final List<Reference<Category>> categories;
    private final Optional<LocalizedStrings> metaTitle;
    private final Optional<LocalizedStrings> metaDescription;
    private final Optional<LocalizedStrings> metaKeywords;
    private final NewProductVariant masterVariant;
    private final List<NewProductVariant> variants;

    public NewProductImpl(final Reference<ProductType> productType, final LocalizedStrings name, final LocalizedStrings slug,
                          final Optional<LocalizedStrings> description, final List<Reference<Category>> categories,
                          final MetaAttributes metaAttributes, final NewProductVariant masterVariant,
                          final List<NewProductVariant> variants) {
        this.name = name;
        this.productType = productType;
        this.slug = slug;
        this.description = description;
        this.categories = categories;
        this.metaTitle = metaAttributes.getMetaTitle();
        this.metaDescription = metaAttributes.getMetaTitle();
        this.metaKeywords = metaAttributes.getMetaKeywords();
        this.masterVariant = masterVariant;
        this.variants = variants;
    }

    @Override
    public Reference<ProductType> getProductType() {
        return productType;
    }

    @Override
    public LocalizedStrings getName() {
        return name;
    }

    @Override
    public LocalizedStrings getSlug() {
        return slug;
    }

    @Override
    public Optional<LocalizedStrings> getDescription() {
        return description;
    }

    @Override
    public List<Reference<Category>> getCategories() {
        return categories;
    }

    @Override
    public Optional<LocalizedStrings> getMetaTitle() {
        return metaTitle;
    }

    @Override
    public Optional<LocalizedStrings> getMetaDescription() {
        return metaDescription;
    }

    @Override
    public Optional<LocalizedStrings> getMetaKeywords() {
        return metaKeywords;
    }

    @Override
    public NewProductVariant getMasterVariant() {
        return masterVariant;
    }

    @Override
    public List<NewProductVariant> getVariants() {
        return variants;
    }
}
