package com.example.sneakerboss.userproductfetching

import com.example.sneakerboss.commons.productfetching.PriceCalculator
import com.example.sneakerboss.commons.productfetching.currencyconverting.PriceConverter
import com.example.sneakerboss.commons.productfetching.productmarkerdatafetching.ProductMarketDataFetcher
import com.example.sneakerboss.commons.productfetching.productmarkerdatafetching.ShoeVariantParser
import com.example.sneakerboss.commonsfortesting.ProductMarketDataProvider.Companion.getProductDetails
import com.example.sneakerboss.userproductfetching.dto.UserProductParser
import com.example.sneakerboss.userproductfetching.dto.UserSettingParser
import com.example.sneakerboss.userproductfetching.entity.User
import com.example.sneakerboss.userproductfetching.entity.UserProduct
import com.example.sneakerboss.userproductfetching.repository.InMemoryUserProductRepository
import com.example.sneakerboss.userproductfetching.repository.InMemoryUserRepository
import com.example.sneakerboss.userproductfetching.repository.UserProductRepository
import com.example.sneakerboss.userproductfetching.repository.UserRepository
import com.example.sneakerboss.usersettings.UserSettingService
import com.example.sneakerboss.usersettings.repository.InMemoryUserSettingRepository
import com.example.sneakerboss.usersettings.repository.UserSettingRepository
import com.example.sneakerboss.uservalidating.EmailFormatValidator
import com.example.sneakerboss.uservalidating.UserValidator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

class UserProductServiceCompTest {

    private lateinit var userProductService: UserProductService
    private lateinit var userProductRepository: UserProductRepository
    private lateinit var userRepository: UserRepository
    private lateinit var userSettingRepository: UserSettingRepository
    private lateinit var userValidator: UserValidator
    private lateinit var productMarketDataFetcher: ProductMarketDataFetcher
    private lateinit var priceConverter: PriceConverter
    private lateinit var priceCalculator: PriceCalculator
    private lateinit var emailFormatValidator: EmailFormatValidator

    private val PRODUCT_DETAILS = getProductDetails()

    @BeforeEach
    fun setup() {
        userProductRepository = InMemoryUserProductRepository()
        userSettingRepository = InMemoryUserSettingRepository()
        userRepository = InMemoryUserRepository()
        productMarketDataFetcher = mock(ProductMarketDataFetcher::class.java)
        emailFormatValidator = EmailFormatValidator()
        userValidator = UserValidator(userRepository, userSettingRepository, emailFormatValidator)
        priceConverter = mock(PriceConverter::class.java)
        priceCalculator = PriceCalculator()
        userProductService = UserProductService(
            userProductRepository = userProductRepository,
            productMarketDataFetcher = productMarketDataFetcher,
            userProductParser = UserProductParser(
                priceConverter, priceCalculator, ShoeVariantParser(priceConverter, priceCalculator)
            ),
            userSettingService = UserSettingService(userSettingRepository, UserSettingParser(), userValidator),
            userValidator = userValidator
        )
    }

    @Test
    fun `adds user product to repository`() {
        //given
        val (existingUser, addingUserProduct) = givenUserAndUserProduct()

        //when
        userProductService.addUserProduct(
            addingUserProduct.parentProductUuid,
            addingUserProduct.shoeVariantUuid,
            existingUser.email
        )
        val actualUserProducts = userProductRepository.findAllByUserId(existingUser.id)

        //then
        assertThat(actualUserProducts.size).isEqualTo(1)
        assertThatUserProductsEqual(actualUserProducts.first(), addingUserProduct)
    }

    @Test
    fun `deletes user product from repository`() {
        //given
        val (existingUser, deletingUserProduct) = givenUserAndUserProduct()
        userProductRepository.save(deletingUserProduct)

        //when
        userProductService.deleteUserProduct(deletingUserProduct.id)
        val actualUserProducts = userProductRepository.findAllByUserId(existingUser.id)

        //then
        assertThat(actualUserProducts).isEmpty()
    }

    private fun givenUserAndUserProduct(): Pair<User, UserProduct> {
        val existingUserEmail = "existingUserEmail@email.com"
        val existingUser = userRepository.save(User.create(existingUserEmail))
        val expectedUserProduct = UserProduct.create(
            PRODUCT_DETAILS.uuid,
            PRODUCT_DETAILS.shoeVariants.first().uuid,
            existingUser.id
        )

        return existingUser to expectedUserProduct
    }

    private fun assertThatUserProductsEqual(actualUserProduct: UserProduct, expectedUserProduct: UserProduct) {
        assertThat(actualUserProduct.parentProductUuid).isEqualTo(expectedUserProduct.parentProductUuid)
        assertThat(actualUserProduct.shoeVariantUuid).isEqualTo(expectedUserProduct.shoeVariantUuid)
        assertThat(actualUserProduct.userId).isEqualTo(expectedUserProduct.userId)
    }
}