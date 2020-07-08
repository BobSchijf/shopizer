package com.salesmanager.test.utils;

import com.salesmanager.core.business.utils.ProductImageSizeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class ProductImageSizeUtilsTest extends com.salesmanager.test.common.AbstractSalesManagerCoreTestCase {

    @Test
    public void resizeWithRatioTest() {
        BufferedImage image = new BufferedImage(50,50,1);
        BufferedImage expected = new BufferedImage(120,120,1);

        BufferedImage result = ProductImageSizeUtils.resizeWithRatio(image, 120, 120);

        Assert.assertEquals(expected.getHeight(), result.getHeight());
        Assert.assertEquals(expected.getWidth(), result.getWidth());

        for(int x = 0; x <100; x++) {
            for(int y = 0; y <100; y++) {
                Assert.assertEquals(expected.getRGB(x,y), result.getRGB(x,y));
            }
        }
    }
}