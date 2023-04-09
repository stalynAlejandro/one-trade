package com.pagonxt.onetradefinance.integrations.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Enum class. This class stores the name of product, its productId and its code
 * Include class attributes, constructor and getters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public enum CollectionLoanProduct {

    EXPORT_COLLECTION("CLE", "206601"),
    IMPORT_COLLECTION("CLI", "185601");

    private final String productId;

    private final String code;

    /**
     * constructor method
     * @param productId :a string with the product id of the product
     * @param code      :a string with the code of the product
     */
    CollectionLoanProduct(String productId, String code) {
        this.productId = productId;
        this.code = code;
    }

    /**
     * getter method
     * @return a string with the product id
     */
    public String getProductId() {
        return productId;
    }

    /**
     * getter method
     * @return a string with the product code
     */
    public String getCode() {
        return code;
    }

    /**
     * class method. This method returns de product code by a product id
     * @param productId a string with the product id
     * @return a string with the product code
     */
    public static String getProductCode(String productId) {
        return Objects.requireNonNull(Arrays.stream(CollectionLoanProduct.values())
                        .filter(collectionProduct -> collectionProduct.getProductId().equals(productId))
                        .findFirst()
                        .orElse(null))
                .getCode();
    }
}
