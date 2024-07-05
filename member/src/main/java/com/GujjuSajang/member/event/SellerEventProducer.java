package com.GujjuSajang.member.event;

import com.GujjuSajang.core.dto.TokenMemberInfo;
import com.GujjuSajang.core.dto.UpdateStockDto;
import com.GujjuSajang.core.exception.ErrorCode;
import com.GujjuSajang.core.exception.MemberException;
import com.GujjuSajang.core.type.MemberRole;
import com.GujjuSajang.member.dto.ProductStockUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerEventProducer {

    private final EventProducer eventProducer;

    // 제품 재고 변경
    @Transactional
    public ProductStockUpdateDto updateProductStock(TokenMemberInfo tokenMemberInfo, Long productId, ProductStockUpdateDto productStockUpdateDto) {

        validateProductId(productId, productStockUpdateDto.getProductId());
        validateMemberRole(tokenMemberInfo.getRole());

        eventProducer.sendEvent("stock-update",
                UpdateStockDto.builder()
                        .productId(productId)
                        .count(productStockUpdateDto.getCount())
                        .build());

        return ProductStockUpdateDto.builder()
                .productId(productId)
                .name(productStockUpdateDto.getName())
                .count(productStockUpdateDto.getCount())
                .build();
    }

    private static void validateMemberRole(MemberRole memberRole) {
        if (!memberRole.equals(MemberRole.SELLER)) {
            throw new MemberException(ErrorCode.ROLE_NOT_ALLOWED);
        }
    }

    private static void validateProductId(Long productId, Long dtoToProductId) {
        if (!productId.equals(dtoToProductId)) {
            throw new MemberException(ErrorCode.MISS_MATCH_PRODUCT);
        }
    }
}
