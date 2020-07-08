package com.salesmanager.test.shoppingcart;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.category.CategoryDescription;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.attribute.*;
import com.salesmanager.core.model.catalog.product.availability.ProductAvailability;
import com.salesmanager.core.model.catalog.product.description.ProductDescription;
import com.salesmanager.core.model.catalog.product.manufacturer.Manufacturer;
import com.salesmanager.core.model.catalog.product.manufacturer.ManufacturerDescription;
import com.salesmanager.core.model.catalog.product.price.FinalPrice;
import com.salesmanager.core.model.catalog.product.price.ProductPrice;
import com.salesmanager.core.model.catalog.product.price.ProductPriceDescription;
import com.salesmanager.core.model.catalog.product.type.ProductType;
import com.salesmanager.core.model.common.Billing;
import com.salesmanager.core.model.common.Delivery;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.CustomerGender;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.country.Country;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.reference.zone.Zone;
import com.salesmanager.core.model.shoppingcart.ShoppingCart;
import com.salesmanager.core.model.shoppingcart.ShoppingCartAttributeItem;
import com.salesmanager.core.model.shoppingcart.ShoppingCartItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartServiceImplTest extends com.salesmanager.test.common.AbstractSalesManagerCoreTestCase {
    UUID cartCode = UUID.randomUUID();
    Customer customer = new Customer();
    ShoppingCart shoppingCart = new ShoppingCart();

    @Before
    public void setUp() throws Exception {
        MerchantStore store = merchantService.getByCode( MerchantStore.DEFAULT_STORE );

        Language en = languageService.getByCode("en");

        Country country = countryService.getByCode("US");
        Zone zone = zoneService.getByCode("VT");
        //* Create a customer *//
        customer.setMerchantStore(store);
        customer.setDefaultLanguage(en);
        customer.setEmailAddress("email@email.com");
        customer.setPassword("-1999");
        customer.setNick("My New nick");
        customer.setCompany(" Apple");
        customer.setGender(CustomerGender.M);
        customer.setDateOfBirth(new Date());

        Billing billing = new Billing();
        billing.setAddress("Billing address");
        billing.setCity("Billing city");
        billing.setCompany("Billing company");
        billing.setCountry(country);
        billing.setFirstName("John");
        billing.setLastName("Samson");
        billing.setPostalCode("Billing postal code");
        billing.setState("Billing state");
        billing.setZone(zone);

        Delivery delivery = new Delivery();
        delivery.setAddress("Shipping address");
        delivery.setCountry(country);
        delivery.setZone(zone);

        customer.setBilling(billing);
        customer.setDelivery(delivery);

        customerService.create(customer);

        /** CATALOG CREATION **/

        ProductType generalType = productTypeService.getProductType(ProductType.GENERAL_TYPE);

        /**
         * Create the category
         */
        Category watches = new Category();
        watches.setMerchantStore(store);
        watches.setCode("watches");

        CategoryDescription watchesEnglishDescription = new CategoryDescription();
        watchesEnglishDescription.setName("Watches");
        watchesEnglishDescription.setCategory(watches);
        watchesEnglishDescription.setLanguage(en);

        Set<CategoryDescription> descriptions = new HashSet<CategoryDescription>();
        descriptions.add(watchesEnglishDescription);


        watches.setDescriptions(descriptions);
        categoryService.create(watches);


        /**
         * Create a manufacturer
         */
        Manufacturer GShock = new Manufacturer();
        GShock.setMerchantStore(store);
        GShock.setCode("GShock");

        ManufacturerDescription GShockDesc = new ManufacturerDescription();
        GShockDesc.setLanguage(en);
        GShockDesc.setManufacturer(GShock);
        GShockDesc.setName("G-Shock");
        GShock.getDescriptions().add(GShockDesc);

        manufacturerService.create(GShock);

        /**
         * Create an option
         */
        ProductOption option = new ProductOption();
        option.setMerchantStore(store);
        option.setCode("clockwork");
        option.setProductOptionType(ProductOptionType.RADIO.name());

        ProductOptionDescription optionDescription = new ProductOptionDescription();
        optionDescription.setLanguage(en);
        optionDescription.setName("Clockwork");
        optionDescription.setDescription("Item clockwork");
        optionDescription.setProductOption(option);

        option.getDescriptions().add(optionDescription);

        productOptionService.saveOrUpdate(option);


        /** first option value **/
        ProductOptionValue hands = new ProductOptionValue();
        hands.setMerchantStore(store);
        hands.setCode("hands");

        ProductOptionValueDescription handsDescription = new ProductOptionValueDescription();
        handsDescription.setLanguage(en);
        handsDescription.setName("Hands");
        handsDescription.setDescription("Hands as clockwork");
        handsDescription.setProductOptionValue(hands);

        hands.getDescriptions().add(handsDescription);

        productOptionValueService.saveOrUpdate(hands);


        ProductOptionValue digital = new ProductOptionValue();
        digital.setMerchantStore(store);
        digital.setCode("digital");

        /** second option value **/
        ProductOptionValueDescription digitalDesc = new ProductOptionValueDescription();
        digitalDesc.setLanguage(en);
        digitalDesc.setName("Digital");
        digitalDesc.setDescription("Digital clockwork");
        digitalDesc.setProductOptionValue(digital);

        digital.getDescriptions().add(digitalDesc);

        productOptionValueService.saveOrUpdate(digital);


        /**
         * Create a complex product
         */
        Product product = new Product();
        product.setProductHeight(new BigDecimal(4));
        product.setProductLength(new BigDecimal(3));
        product.setProductWidth(new BigDecimal(1));
        product.setSku("TB1234578");
        product.setManufacturer(GShock);
        product.setType(generalType);
        product.setMerchantStore(store);

        // Product description
        ProductDescription description = new ProductDescription();
        description.setName("G-Shock Watch");
        description.setLanguage(en);
        description.setProduct(product);

        product.getDescriptions().add(description);
        product.getCategories().add(watches);


        //availability
        ProductAvailability availability = new ProductAvailability();
        availability.setProductDateAvailable(new Date());
        availability.setProductQuantity(100);
        availability.setRegion("*");
        availability.setProduct(product);// associate with product

        //price
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
        product.getAvailabilities().add(availability);


        //attributes
        //hands
        ProductAttribute handsAttribute = new ProductAttribute();
        handsAttribute.setProduct(product);
        handsAttribute.setProductOption(option);
        handsAttribute.setAttributeDefault(true);
        handsAttribute.setProductAttributePrice(new BigDecimal(0));//no price variation
        handsAttribute.setProductAttributeWeight(new BigDecimal(0));//no weight variation
        handsAttribute.setProductOption(option);
        handsAttribute.setProductOptionValue(hands);

        product.getAttributes().add(handsAttribute);
        //digital
        ProductAttribute digitalAttribute = new ProductAttribute();
        digitalAttribute.setProduct(product);
        digitalAttribute.setProductOption(option);
        digitalAttribute.setProductAttributePrice(new BigDecimal(5));//5 + dollars
        digitalAttribute.setProductAttributeWeight(new BigDecimal(0));//no weight variation
        digitalAttribute.setProductOption(option);
        digitalAttribute.setProductOptionValue(digital);

        product.getAttributes().add(digitalAttribute);

        productService.create(product);

        /** Create Shopping cart **/
        shoppingCart.setCustomerId(customer.getId());
        shoppingCart.setMerchantStore(store);

        shoppingCart.setShoppingCartCode(cartCode.toString());

        ShoppingCartItem item = new ShoppingCartItem(shoppingCart,product);
        item.setShoppingCart(shoppingCart);

        FinalPrice price = pricingService.calculateProductPrice(product);

        item.setItemPrice(price.getFinalPrice());
        item.setQuantity(1);

        /** user selects digital **/
        ShoppingCartAttributeItem attributeItem = new ShoppingCartAttributeItem(item,digitalAttribute);
        item.getAttributes().add(attributeItem);

        shoppingCart.getLineItems().add(item);


        //create cart
    }

    @Test
    public void getShoppingCart() throws ServiceException {
        shoppingCartService.create(shoppingCart);

        ShoppingCart result = shoppingCartService.getShoppingCart(customer);
        Assert.assertEquals(shoppingCart, result);
    }

}