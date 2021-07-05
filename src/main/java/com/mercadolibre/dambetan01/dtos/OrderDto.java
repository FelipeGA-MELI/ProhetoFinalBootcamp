package com.mercadolibre.dambetan01.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class OrderDto {
    @JsonProperty("orderNumber")
    private Long orderNumber;

    @JsonProperty("orderDate")
    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "orderDate is required")
    private LocalDate orderDate;

    @JsonProperty("section")
    @NotNull(message = "section is required")
    @Valid
    private SectorDto section;

    @JsonProperty("batchStock")
    @NotNull(message = "batchStock is required")
    @Valid
    private List<ProductStockDto> batchStock;



}
