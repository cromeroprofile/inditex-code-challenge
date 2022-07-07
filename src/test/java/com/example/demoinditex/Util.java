package com.example.demoinditex;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.example.demoinditex.dto.Price;
import com.example.demoinditex.repository.domain.BranchEntity;
import com.example.demoinditex.repository.domain.PriceEntity;
import lombok.val;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;

/** The type Easy random util. */
public class Util {
    public static final long PRODUCT_ID = 35455L;

    /**
     * Generate easy random easy random.
     *
     * @return the easy random
     */
    public static EasyRandom generateEasyRandom() {
        val parameters =
                new EasyRandomParameters()
                        .seed(7893L)
                        .objectPoolSize(200)
                        .randomizationDepth(5)
                        .charset(UTF_8)
                        .overrideDefaultInitialization(true)
                        .stringLengthRange(20, 25)
                        .collectionSizeRange(1, 3)
                        .excludeField(FieldPredicates.named("currency").or(FieldPredicates.named("id").or(FieldPredicates.named("priceList"))))
                        .scanClasspathForConcreteTypes(true);
        return new EasyRandom(parameters);
    }

    /**
     * Generate easy random easy random.
     *
     * @param randomDepth the random depth
     * @return the easy random
     */
    public static EasyRandom generateEasyRandom(int randomDepth) {
        val parameters =
                new EasyRandomParameters()
                        .seed(7893L)
                        .objectPoolSize(200)
                        .randomizationDepth(randomDepth)
                        .charset(UTF_8)
                        .overrideDefaultInitialization(true)
                        .stringLengthRange(20, 25)
                        .collectionSizeRange(1, 3)
                        .scanClasspathForConcreteTypes(true);
        return new EasyRandom(parameters);
    }

    /**
     * Generate easy random easy random.
     *
     * @param randomDepth the random depth
     * @param collectionSize the collection size
     * @return the easy random
     */
    public static EasyRandom generateEasyRandom(int randomDepth, int collectionSize) {
        val parameters =
                new EasyRandomParameters()
                        .seed(7893L)
                        .objectPoolSize(200)
                        .randomizationDepth(randomDepth)
                        .charset(UTF_8)
                        .overrideDefaultInitialization(true)
                        .stringLengthRange(20, 25)
                        .collectionSizeRange(collectionSize, collectionSize)
                        .scanClasspathForConcreteTypes(true);
        return new EasyRandom(parameters);
    }


    public static BranchEntity generateBranchEntity(Long id)  {

        BranchEntity branchEntity = generateEasyRandom().nextObject(BranchEntity.class);
        branchEntity.setId(id);
        return branchEntity;
    }

    public static PriceEntity generatePriceEntity(Long id, BranchEntity branchEntity, Integer priority )  {

        PriceEntity priceEntity = generateEasyRandom().nextObject(PriceEntity.class);
        priceEntity.setPriceList(id);
        priceEntity.setBranchEntity(branchEntity);
        priceEntity.setProductId(PRODUCT_ID);
        priceEntity.setBranchEntity(branchEntity);
        priceEntity.setPriority(priority);
        return priceEntity;
    }

    public static Price generatePrice( )  {
        return generateEasyRandom().nextObject(Price.class);
    }

}
