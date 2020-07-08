package com.salesmanager.test.utils;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.utils.ProductPriceUtils;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.category.CategoryDescription;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.availability.ProductAvailability;
import com.salesmanager.core.model.catalog.product.description.ProductDescription;
import com.salesmanager.core.model.catalog.product.manufacturer.Manufacturer;
import com.salesmanager.core.model.catalog.product.manufacturer.ManufacturerDescription;
import com.salesmanager.core.model.catalog.product.price.ProductPrice;
import com.salesmanager.core.model.catalog.product.price.ProductPriceDescription;
import com.salesmanager.core.model.catalog.product.type.ProductType;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProductPriceUtilsTest extends com.salesmanager.test.common.AbstractSalesManagerCoreTestCase {
    private static final java.sql.Date date = new Date(System.currentTimeMillis());
    Locale locale;
    ProductPriceUtils productPriceUtils = new ProductPriceUtils();
    Product product;
    Set<ProductAvailability> availabilities = new HashSet<>();
    MerchantStore store;

    @Before
    public void setUp() throws Exception {
        product = Mockito.mock(Product.class);
        store = Mockito.mock(MerchantStore.class);
        Language en = languageService.getByCode("en");


        // Availability
        ProductAvailability availability = new ProductAvailability();
        availability.setProductDateAvailable(date);
        availability.setProductQuantity(100);
        availability.setRegion("*");
        availability.setProduct(product);// associate with product

        ProductPrice dprice = new ProductPrice();
        dprice.setDefaultPrice(true);
        dprice.setProductPriceAmount(new BigDecimal(29.99));
        dprice.setProductAvailability(availability);

        ProductPriceDescription dpd = new ProductPriceDescription();
        dpd.setName("Base price");
        dpd.setProductPrice(dprice);
        dpd.setLanguage(en);

        dprice.getDescriptions().add(dpd);
        availability.getPrices().add(dprice);


        availabilities.add(availability);

    }

    @Test
    public void getPriceTest() throws ServiceException {
        Mockito.when(product.getAvailabilities()).thenReturn(availabilities);
        BigDecimal expectedPrice = new BigDecimal(29.99);
        Assert.assertEquals(expectedPrice, productPriceUtils.getPrice(store, product, locale));
    }

    @Test
    public void getAdminFormatedAmountTest() throws Exception {
        store = Mockito.mock(MerchantStore.class);
        BigDecimal input = new BigDecimal("1299.99");
        String expectedResult = "1,299.99";
        Assert.assertEquals(expectedResult, productPriceUtils.getAdminFormatedAmount(store, input));
    }

    @Test
    public void getAmountTest() throws Exception {
        String input = "1,299.99";
        BigDecimal expectedResult = new BigDecimal("1299.99");

        Assert.assertEquals(expectedResult, productPriceUtils.getAmount(input));

    }

}