package com.mercadolibre.dambetan01.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadolibre.dambetan01.model.Category;
import com.mercadolibre.dambetan01.model.user.Seller;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.internal.build.AllowPrintStacktrace;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Validated
@AllowPrintStacktrace
@NoArgsConstructor
public class ProductDTO {
    @JsonProperty(value = "produciId")
    @NotNull(message = "Id do produto não pode ser nulo.")
    private Long id;

    @JsonProperty(value = "name")
    @Size(min = 3, max = 20, message = "O nome do produto deve ter entre 3 e 20 caracteres.")
    private String name;

    @JsonProperty("sellerId")
    @NotNull(message = "Id do seller não pode ser nulo.")
    private Long sellerId;

    @JsonProperty("categoryId")
    @NotNull(message = "Id da categoria não pode ser nulo.")
    private Long categoryId;

    @JsonProperty("price")
    @NotNull(message = "O valor do produto não pode ser nulo.")
    private BigDecimal price;
}
