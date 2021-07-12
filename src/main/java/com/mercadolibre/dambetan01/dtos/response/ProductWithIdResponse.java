package com.mercadolibre.dambetan01.dtos.response;

import com.mercadolibre.dambetan01.dtos.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProductWithIdResponse extends ProductDTO {
    private Long id;
}
