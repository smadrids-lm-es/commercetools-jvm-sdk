package io.sphere.sdk.attributes;

import io.sphere.sdk.models.LocalizedStrings;

public class LocalizedTextAttributeDefinitionBuilder extends BaseBuilder<LocalizedTextAttributeDefinition, LocalizedTextAttributeDefinitionBuilder> {

    private final TextInputHint textInputHint;

    LocalizedTextAttributeDefinitionBuilder(final String name, final LocalizedStrings label, final TextInputHint textInputHint) {
        super(name, label);
        this.textInputHint = textInputHint;
    }

    @Override
    protected LocalizedTextAttributeDefinitionBuilder getThis() {
        return this;
    }

    @Override
    public LocalizedTextAttributeDefinition build() {
        return new LocalizedTextAttributeDefinition(new LocalizedTextType(), getName(), getLabel(), isRequired(), getAttributeConstraint(), isSearchable(), textInputHint);
    }

    public static LocalizedTextAttributeDefinitionBuilder of(final String name, final LocalizedStrings label, final TextInputHint textInputHint) {
        return new LocalizedTextAttributeDefinitionBuilder(name, label, textInputHint);
    }
}
