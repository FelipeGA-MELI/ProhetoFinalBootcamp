package com.mercadolibre.dambetan01.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Validated
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProductDTO {
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
