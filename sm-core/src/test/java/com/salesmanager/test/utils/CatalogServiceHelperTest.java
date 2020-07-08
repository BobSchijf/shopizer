package com.salesmanager.test.utils;

import com.salesmanager.core.business.utils.CatalogServiceHelper;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.availability.ProductAvailability;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CatalogServiceHelperTest extends com.salesmanager.test.common.AbstractSalesManagerCoreTestCase{

    Locale localeUS;
    Locale localeNL;
    Product product = new Product();

    Set<ProductAvailability> availabilitySet = new HashSet<>();
    ProductAvailability availabilityUS;
    ProductAvailability availabilityCAN;
    ProductAvailability availabilityGER;
    ProductAvailability availabilityAll;

    @BeforeEach
    void setUp() {
        //create availabilities
        availabilityUS = new ProductAvailability();
        availabilityUS.setRegion("US");
        availabilityCAN = new ProductAvailability();
        availabilityCAN.setRegion("CAN");
        availabilityGER = new ProductAvailability();
        availabilityGER.setRegion("GER");

        availabilityAll = new ProductAvailability();
        availabilityAll.setRegion("*");

        availabilitySet.add(availabilityUS);
        availabilitySet.add(availabilityCAN);
        availabilitySet.add(availabilityGER);



        localeUS = Mockito.mock(Locale.class);
        localeNL = Mockito.mock(Locale.class);

        product.setAvailabilities(availabilitySet);

    }

    @Test
    void setToAvailability() {
        Mockito.when(localeUS.getCountry()).thenReturn("US");
        Mockito.when(localeNL.getCountry()).thenReturn("NL");

        //create expected results
        Set<ProductAvailability> expectedResult1 = new HashSet<>();
        expectedResult1.add(availabilityUS);
        Set<ProductAvailability> expectedResult2 = new HashSet<>();
        expectedResult2.add(availabilityUS);
        expectedResult2.add(availabilityCAN);
        expectedResult2.add(availabilityGER);
        Set<ProductAvailability> expectedResult3 = new HashSet<>();
        expectedResult3.add(availabilityAll);
        Set<ProductAvailability> expectedResult4 = new HashSet<>();
        expectedResult4.add(availabilityUS);
        expectedResult4.add(availabilityAll);

        //test 1
        CatalogServiceHelper.setToAvailability(product, localeUS);
        Assert.assertEquals(expectedResult1, product.getAvailabilities());

        //test 2
        product.setAvailabilities(availabilitySet);
        CatalogServiceHelper.setToAvailability(product, localeNL);
        Assert.assertEquals(expectedResult2, product.getAvailabilities());

        //test 3
        Set<ProductAvailability> set = new HashSet<>();
        set.add(availabilityUS);
        set.add(availabilityCAN);
        set.add(availabilityGER);
        set.add(availabilityAll);
        product.setAvailabilities(set);

        CatalogServiceHelper.setToAvailability(product, localeNL);
        Assert.assertEquals(expectedResult3, product.getAvailabilities());

        //test 4
        Set<ProductAvailability> set2 = new HashSet<>();
        set2.add(availabilityUS);
        set2.add(availabilityAll);
        product.setAvailabilities(set2);

        CatalogServiceHelper.setToAvailability(product, localeUS);
        Assert.assertEquals(expectedResult4, product.getAvailabilities());

    }
}