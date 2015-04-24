package io.sphere.sdk.models;

/**
 * Represents an object itself or a {@link io.sphere.sdk.models.Reference} to it.
 * It is not necessarily the case that the reference is filled.
 *
 * @param <T> the type of the referenced object.
 */
public interface Referenceable<T> {
    /**
     * Creates a reference which is not necessarily filled.
     * @return reference
     */
    Reference<T> toReference();

    default boolean hasSameIdAs(final Referenceable<T> other) {
        return toReference().getId().equals(other.toReference().getId());
    }
}
