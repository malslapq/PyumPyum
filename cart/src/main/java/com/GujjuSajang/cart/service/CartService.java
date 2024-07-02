package com.GujjuSajang.cart.service;

import com.GujjuSajang.cart.dto.CartDto;
import com.GujjuSajang.cart.dto.CartProductsDto;
import com.GujjuSajang.cart.dto.UpdateCartProductDto;
import com.GujjuSajang.cart.repository.CartRedisRepository;
import com.GujjuSajang.core.dto.CreateMemberEventDto;
import com.GujjuSajang.core.exception.CartException;
import com.GujjuSajang.core.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    private final CartRedisRepository cartRedisRepository;

    @KafkaListener(topics = {"create-member"}, groupId = "createCart")
    public void createCart(CreateMemberEventDto createMemberEventDto) {
        logger.info("Received CreateMemberEventDto: {}", createMemberEventDto);
        cartRedisRepository.save(createMemberEventDto.getId(), new CartDto());
    }

    public CartDto addCartProduct(Long memberId, Long tokenMemberId, CartProductsDto cartProductsDto) {
        CartDto cartDto = getCart(memberId, tokenMemberId);
        cartDto.getCartProductsDtos().add(cartProductsDto);
        cartRedisRepository.save(memberId, cartDto);
        return cartDto;
    }

    public CartDto getCart(Long id, Long tokenId) {
        validateMemberId(id, tokenId);
        return cartRedisRepository.get(id).orElseThrow(() -> new CartException(ErrorCode.CART_NOT_FOUND));
    }

    public CartDto updateCart(Long memberId, Long productId, Long tokenMemberId, UpdateCartProductDto updateCartProductDto) {

        CartDto getCart = getCart(memberId, tokenMemberId);

        getCart.getCartProductsDtos().stream()
                .filter(cartProductsDto -> cartProductsDto.getProductID().equals(productId))
                .findFirst()
                .ifPresent(cartProductsDto -> cartProductsDto.setCount(updateCartProductDto.getCount()));

        cartRedisRepository.save(memberId, getCart);
        return getCart;
    }

    public CartDto deleteCartProduct(Long memberId, Long productId, Long tokenId) {
        CartDto cart = getCart(memberId, tokenId);
        cart.getCartProductsDtos().removeIf(cartProductsDto -> cartProductsDto.getProductID().equals(productId));
        cartRedisRepository.save(memberId, cart);
        return cart;
    }

    public void deleteCart(Long memberId, Long tokenId) {
        validateMemberId(memberId, tokenId);
        cartRedisRepository.delete(memberId);
    }

    private static void validateMemberId(Long memberId, Long tokenId) {
        if (!memberId.equals(tokenId)) {
            throw new CartException(ErrorCode.MISS_MATCH_MEMBER);
        }
    }

}
