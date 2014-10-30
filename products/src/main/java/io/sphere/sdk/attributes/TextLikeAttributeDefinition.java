package io.sphere.sdk.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.sphere.sdk.models.LocalizedStrings;

abstract class TextLikeAttributeDefinition<T extends AttributeType> extends AttributeDefinitionBase<T> {
    private final TextInputHint textInputHint;

    TextLikeAttributeDefinition(final T attributeType, final String name, final LocalizedStrings label,
                                final boolean isRequired, final AttributeConstraint attributeConstraint,
                                final boolean isSearchable, final TextInputHint textInputHint) {
        super(attributeType, name, label, isRequired, attributeConstraint, isSearchable);
        this.textInputHint = textInputHint;
    }


    @JsonProperty("inputHint")
    public TextInputHint getTextInputHint() {
        return textInputHint;
    }
}
