package com.salesmanager.test.catalog;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.catalog.product.ProductRepositoryImpl;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.catalog.product.ProductServiceImpl;
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
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProductRepositoryImplTest extends com.salesmanager.test.common.AbstractSalesManagerCoreTestCase{

    private static final java.sql.Date date = new Date(System.currentTimeMillis());
    String productCode = "TB123465";
    Product product = new Product();

    @Before
    public void setUp() throws Exception {
        Language en = languageService.getByCode("en");
        Language fr = languageService.getByCode("fr");

        MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
        ProductType generalType = productTypeService.getProductType(ProductType.GENERAL_TYPE);

        Category shirt = new Category();
        shirt.setMerchantStore(store);
        shirt.setCode("shirt");

        CategoryDescription shirtEnglishDescription = new CategoryDescription();
        shirtEnglishDescription.setName("Shirt");
        shirtEnglishDescription.setCategory(shirt);
        shirtEnglishDescription.setLanguage(en);

        CategoryDescription shirtFrenchDescription = new CategoryDescription();
        shirtFrenchDescription.setName("Shirt");
        shirtFrenchDescription.setCategory(shirt);
        shirtFrenchDescription.setLanguage(fr);

        Set<CategoryDescription> descriptions = new HashSet<CategoryDescription>();
        descriptions.add(shirtEnglishDescription);
        descriptions.add(shirtFrenchDescription);

        shirt.setDescriptions(descriptions);

        categoryService.create(shirt);

        Manufacturer stone = new Manufacturer();
        stone.setMerchantStore(store);
        stone.setCode("Stone");

        ManufacturerDescription stoned = new ManufacturerDescription();
        stoned.setLanguage(en);
        stoned.setName("Stone");
        stoned.setManufacturer(stone);
        stone.getDescriptions().add(stoned);

        manufacturerService.create(stone);

        // PRODUCT 1
        product.setProductHeight(new BigDecimal(4));
        product.setProductLength(new BigDecimal(3));
        product.setProductWidth(new BigDecimal(1));
        product.setSku(productCode);
        product.setManufacturer(stone);
        product.setType(generalType);
        product.setMerchantStore(store);

        // Product description
        ProductDescription description = new ProductDescription();
        description.setName("Stone Island Shirt");
        description.setLanguage(en);
        description.setProduct(product);

        product.getDescriptions().add(description);

        //add category
        product.getCategories().add(shirt);



        // Availability
        ProductAvailability availability = new ProductAvailability();
        availability.setProductDateAvailable(date);
        availability.setProductQuantity(100);
        availability.setRegion("*");
        availability.setProduct(product);// associate with product

        //productAvailabilityService.create(availability);
        product.getAvailabilities().add(availability);

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




    }

    @Test
    public void getByCode() throws ServiceException {
        productService.create(product);
        Language en = languageService.getByCode("en");

        Product result = productService.getByCode(productCode, en);
        assertEquals(product, result);

    }
}